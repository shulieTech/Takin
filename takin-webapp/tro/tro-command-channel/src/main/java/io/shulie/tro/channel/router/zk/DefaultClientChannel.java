package io.shulie.tro.channel.router.zk;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.recipes.cache.ChildData;
import com.netflix.curator.framework.recipes.cache.PathChildrenCacheEvent;
import com.netflix.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import io.shulie.tro.channel.ClientChannel;
import io.shulie.tro.channel.CommandRegistry;
import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.channel.bean.CommandRespType;
import io.shulie.tro.channel.bean.CommandResponse;
import io.shulie.tro.channel.bean.CommandStatus;
import io.shulie.tro.channel.bean.Constants;
import io.shulie.tro.channel.bean.HeartBeat;
import io.shulie.tro.channel.handler.CommandHandler;
import io.shulie.tro.channel.impl.DefaultCommandRegistry;
import io.shulie.tro.channel.protocal.ChannelProtocol;
import io.shulie.tro.channel.router.zk.bean.CreateMode;
import io.shulie.tro.channel.type.Command;
import io.shulie.tro.channel.utils.HttpUtils;
import io.shulie.tro.channel.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: HengYu
 * @className: ZkServerChannel
 * @date: 2020/12/30 7:50 上午
 * @description: zk server
 */
public class DefaultClientChannel implements ClientChannel {


    public static final int TTL_REMOVE_TIME = 1000 * 60 * 60 * 24;
    private String userAppKey;
    private ZkClientConfig config;
    private ChannelProtocol protocol;
    private CommandRegistry registry;
    private ZkPathHeartbeat heartbeat;
    private ScheduledExecutorService scheduledExecutorService;

    private Logger logger = LoggerFactory.getLogger(DefaultClientChannel.class);
    private ZkPathChildrenCache pathChildrenCache;
    private ZkClient zkClient;

    public DefaultClientChannel() {
        this.registry = new DefaultCommandRegistry();
    }

    @Override
    public ClientChannel build(ZkClientConfig config) throws Exception {
        validateParam();
        this.config = config;
        try {
            this.zkClient = NetflixCuratorZkClientFactory.getInstance().create(config);
        } catch (Exception e) {
            logger.error("CommandChannel 初始化ZK配置异常",e);
            throw e;
        }

        if (this.scheduledExecutorService == null){
            this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1, Executors.defaultThreadFactory());
        }
        this.heartbeat = new ZkPathHeartbeat(this.zkClient,this.scheduledExecutorService);
        return this;
    }

    private void validateParam() {
//        if (StringUtils.isBlank(this.userAppKey)){
//            throw  new IllegalArgumentException("CommandChannel userAppKey not set!");
//        }
        if (this.protocol == null){
            throw  new IllegalArgumentException("CommandChannel channelProtocol not set!");
        }
    }

    @Override
    public DefaultClientChannel registerUserAppKey(String userAppKey) {
        this.userAppKey = userAppKey;
        return this;
    }

    @Override
    public ClientChannel registerHandler(Command command, CommandHandler handler) {
        this.registry.register(command, handler);
        return this;
    }

    @Override
    public ClientChannel setChannelProtocol(ChannelProtocol protocol) {
        this.protocol = protocol;
        return this;
    }

    @Override
    public ClientChannel setScheduledExecutorService(ScheduledExecutorService executorService) {
        this.scheduledExecutorService = executorService;
        return this;
    }


    @Override
    public void close() {
        try {
            this.pathChildrenCache.stop();
            this.heartbeat.stop();
            this.zkClient.stop();
            logger.info("ChannelCommand client command channel close!");
        } catch (Exception e) {
            logger.error("ChannelCommand zk close exception", e);
        }
    }


    private String getCommandPath(String agentId) {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.COMMAND_PATH_PREFIX);
        builder.append("/" + agentId);
        return builder.toString();
    }



    @Override
    public void register(String agentId) throws Exception {

        validate(agentId);

        String commandPath = getCommandPath(agentId);

        //清理旧的注册对象信息
        clearOldRegisterInfo(agentId);

        ZkNodeStat stat = zkClient.getStat(commandPath);
        if (stat == null) {
            zkClient.ensureParentExists(commandPath);
            byte[] serialize = genInitBeat();
            zkClient.createNode(commandPath, serialize, CreateMode.PERSISTENT);
            logger.info("ChannelCommand client create path :{}",commandPath);
        }else{
            logger.info("ChannelCommand client path existed :{}",commandPath);
        }

        heartbeat.start(commandPath);

        pathChildrenCache = zkClient.createPathChildrenCache(commandPath, new ZkChildListener() {
            @Override
            public void call(CuratorFramework client, PathChildrenCacheEvent event) {
                ChildData data = event.getData();
                String path = data.getPath();

                Type type = event.getType();

                if (type == Type.CHILD_ADDED){
                    receiverCommand(path, data.getData());
                }else if (type == Type.CHILD_UPDATED){
                    updateCommand(path, data.getData());
                }else if (type == Type.CHILD_REMOVED){
                    removeCommand(path, data.getData());
                }
            }
        });

        pathChildrenCache.start();
    }

    private byte[] genInitBeat() throws UnsupportedEncodingException {
        HeartBeat beat = heartbeat.generateHeartBeat();
        byte[] serialize = heartbeat.serialize(beat);
        return serialize;
    }

    private void clearOldRegisterInfo(String agentId) {
        try {
            String currIp = getPathIp(agentId);
            List<String> strings = zkClient.listChildren(Constants.COMMAND_PATH_PREFIX);
            for (String currAgentId: strings ){
                String oldRegisterId = getPathIp(currAgentId);
                if (oldRegisterId.equals(currIp)){
                    byte[] data = zkClient.getData(Constants.COMMAND_PATH_PREFIX + "/" + currAgentId);
                    HeartBeat heartBeat = this.heartbeat.deserialize(data);
                    if (heartBeat == null){
                        continue;
                    }
                    long timestamp = heartBeat.getTtl();
                    if (System.currentTimeMillis() - timestamp > TTL_REMOVE_TIME){
                        logger.info("ChannelCommand delete old register path : [{}]",currAgentId);
                        zkClient.delete(getCommandPath(currAgentId),true);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("ChannelCommand zk clear old command register info error", e);
        }
    }

    public String getPathIp(String currAgentId) {
        return currAgentId.split("-")[0];
    }

    void receiverCommand(String commandPath, byte[] data) {

        //com.netflix.curator 客户端时间为空，需要重新获取
        //apache高版本不存在这个问题
        if (data == null || data.length == 0){
            try {
                data = zkClient.getData(commandPath);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("ChannelCommand receiverCommand get data exception ",e);
            }
            if (data == null || data.length == 0){
                logger.error("ChannelCommand receiverCommand data is null");
                return;
            }
        }


        CommandPacket commandPacket = protocol.deserialize(data);
        if (commandPacket == null) {
            return;
        }
        if (commandPacket.getStatus() != CommandStatus.COMMAND_SEND) {
            return;
        }

        logger.info("ChannelCommand receiverCommand path: {} commandId: {} data: {}", commandPath, commandPacket.getId(),this.protocol.serializeJson(commandPacket));


        //更新命令为执行中
        commandPacket.setStatus(CommandStatus.COMMAND_RUNNING);
        updateCommandPacket(commandPacket, commandPath);

        try {
            //更新命令响应
            CommandHandler handler = registry.getHandler(commandPacket.getSend().getCommandId());
            CommandResponse response = handler.handle(commandPacket);
            commandPacket.setStatus(CommandStatus.COMMAND_COMPLETED_SUCCESS);

            CommandRespType commandRespType = commandPacket.getCommandRespType();
            //提送服务器地址为空，走zk数据回传，否则走http接口推送数据
            if (commandRespType.getValue() == CommandRespType.COMMAND_CALLBACK.getValue()){
                commandPacket.setResponse(response);
            }else{
                //将响应对象序列化为JSON对象进行数据推送
                boolean result = pushResponseData(commandPacket.getResponsePushUrl(), commandPacket, response);
                if (!result){
                    commandPacket.setStatus(CommandStatus.COMMAND_COMPLETED_FAIL);
                }
            }
            updateCommandPacket(commandPacket, commandPath);
        } catch (Throwable ex) {
            logger.error("ChannelCommand exec command handler exception!", ex);
            //更新命令为执行中
            commandPacket.setStatus(CommandStatus.COMMAND_COMPLETED_FAIL);
            updateCommandPacket(commandPacket, commandPath);
        }

    }

    private boolean pushResponseData(String pushUrl, CommandPacket commandPacket, CommandResponse response) {
        if (StringUtils.isBlank(pushUrl)){
            logger.error("ChannelCommand push url is empty，stop push！");
            return false;
        }
        CommandPacket packet = deepCopyPacket(commandPacket, response);
        String json = this.protocol.serializeJson(packet);
        String result = HttpUtils.doPost(this.userAppKey, pushUrl, json);
        if (StringUtils.isBlank(result)){
            return false;
        }else{
            return true;
        }
    }

    private CommandPacket deepCopyPacket(CommandPacket commandPacket, CommandResponse response) {
        CommandPacket packet = new CommandPacket();
        packet.setId(commandPacket.getId());
        packet.setResponse(response);
        packet.setStatus(commandPacket.getStatus());
        packet.setSend(commandPacket.getSend());
        packet.setCommandRespType(commandPacket.getCommandRespType());
        packet.setResponsePushUrl(commandPacket.getResponsePushUrl());
        return packet;
    }

    void updateCommand(String commandPath, byte[] data) {
        logger.info("ChannelCommand updateCommand  path: {}",commandPath);
    }

    void removeCommand(String commandPath, byte[] data) {
        logger.info("ChannelCommand removeCommand  path: {}", commandPath);
    }

    private void updateCommand(CommandPacket commandPacket, String commandPath, CommandStatus status) {
        commandPacket.setStatus(status);
        updateCommandPacket(commandPacket, commandPath);
    }

    private void updateCommandPacket(CommandPacket commandPacket, String commandPath) {
        byte[] bytes = protocol.serialize(commandPacket);
        try {
            zkClient.updateData(commandPath, bytes);
        } catch (Exception e) {
            logger.error("ChannelCommand client updateData exception", e);
        }
    }


    private void validate(String agentId) {
        if (StringUtils.isBlank(agentId)) {
            throw new IllegalArgumentException("ChannelCommand agentId param cannot be empty ");
        }
    }
}
