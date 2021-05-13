package io.shulie.tro.channel.router.zk;

import io.shulie.tro.channel.CommandListener;
import io.shulie.tro.channel.ServerChannel;
import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.channel.bean.CommandRespType;
import io.shulie.tro.channel.bean.CommandStatus;
import io.shulie.tro.channel.bean.Constants;
import io.shulie.tro.channel.protocal.ChannelProtocol;
import io.shulie.tro.channel.router.zk.bean.CreateMode;
import io.shulie.tro.channel.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Hengyu
 * @className: ZkServerChannel
 * @date: 2020/12/30 7:50 上午
 * @description: zk server
 */
public class DefaultServerChannel implements ServerChannel {

    private ZkClientConfig config;
    private ZkClient zkClient;
    private CommandListener listener;
    private ChannelProtocol protocol;
    private Logger logger = LoggerFactory.getLogger(DefaultServerChannel.class);

    public DefaultServerChannel() {
    }

    public ServerChannel build(ZkClientConfig config) throws Exception {
        DefaultServerChannel channel = new DefaultServerChannel();
        channel.config = config;
        channel.zkClient = NetflixCuratorZkClientFactory.getInstance().create(config);
        return channel;
    }

    @Override
    public ServerChannel setChannelProtocol(ChannelProtocol protocol) {
        this.protocol = protocol;
        return this;
    }

    @Override
    public CommandPacket getCurrentCommand(String agentId, String commandId) throws Exception {
        if (zkClient == null) {
            logger.info("ChannelCommand Zookeeper Connection not  initialize");
            return null;
        }
        String path = getCommandParentPath(agentId);
        path = getCommandPath(path, commandId);
        boolean result = checkPathExist(path);
        if (!result) {
            return null;
        }
        return getCommandPacketByPath(path);
    }


    private CommandPacket getCommandPacketByPath(String path) throws Exception {
        byte[] data = zkClient.getData(path);
        CommandPacket packet = this.protocol.deserialize(data);
        return packet;
    }

    @Override
    public void close() {
        try {
            logger.info("ChannelCommand server command channel close!");
            this.zkClient.stop();
        } catch (Exception e) {
            logger.error("ChannelCommand zk close exception", e);
        }
    }

    @Override
    public boolean send(CommandPacket command, CommandListener listener) throws Exception {

        validate(command, listener);

        String agentId = command.getSend().getAgentId();
        String parentPath = getCommandParentPath(agentId);
        String commandPath = getCommandPath(parentPath,command.getId());
        boolean result = checkAndSetData(parentPath,commandPath, command);
        if (!result) {
            return false;
        }

        ZkNodeCache zkNodeCache = zkClient.createZkNodeCache(commandPath, false);
        zkNodeCache.setUpdateListener(new Runnable() {
            @Override
            public void run() {
                try {
                    processUpdate(zkNodeCache, listener);
                } catch (Exception ex) {
                    logger.error("ChannelCommand exec command callback method exception!", ex);
                }
            }
        });
        zkNodeCache.start();
        return true;
    }


    private void processUpdate(ZkNodeCache zkNodeCache, CommandListener listener) throws Exception {

        byte[] data = zkNodeCache.getData();
        if (data == null || data.length == 0){
            return;
        }
        CommandPacket commandPacket = this.protocol.deserialize(data);

        if (commandPacket == null) {
            return;
        }
        if (commandPacket.getStatus() == CommandStatus.COMMAND_COMPLETED_FAIL
                || commandPacket.getStatus() == CommandStatus.COMMAND_COMPLETED_SUCCESS
        ) {
            listener.call(commandPacket);
            zkNodeCache.delete();
            zkNodeCache.stop();
        }
    }


    private boolean checkAndSetData(String path, String commandPath, CommandPacket packet) throws Exception {
        if (!checkPathExist(path)) {
            logger.info("ChannelCommand Agent not register command path ，send command fail!");
            return false;
        }

        try {
            //创建命令临时节点
            byte[] serialize = protocol.serialize(packet);
            zkClient.createNode(commandPath,serialize, CreateMode.EPHEMERAL);
        }catch (Exception ex){
            logger.error("ChannelCommand zookeeper create node exception",ex);
            return false;
        }
        logger.info("ChannelCommand zookeeper send command id:{}",packet.getId());
        return true;
    }

    private boolean checkPathExist(String path) throws Exception {
        //检测路径是否存在
        if (zkClient.exists(path)) {
            return true;
        }
        return false;
    }


    private String getCommandPath(String path, String commandId) {
        StringBuilder builder = new StringBuilder();
        builder.append(path);
        builder.append("/" + commandId);
        return builder.toString();
    }

    private String getCommandParentPath(String agentId) {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.COMMAND_PATH_PREFIX);
        builder.append("/" + agentId);
        return builder.toString();
    }

    private void validate(CommandPacket command, CommandListener listener) {
        if (command == null || listener == null) {
            throw new IllegalArgumentException("ChannelCommand command and listener param cannot be empty ");
        }
        if (command.getId() == null) {
            throw new IllegalArgumentException("ChannelCommand command send id cannot be empty ");
        }
        if (command.getSend() == null) {
            throw new IllegalArgumentException("ChannelCommand command send param cannot be empty ");
        }
        if (StringUtils.isBlank(command.getSend().getAgentId())) {
            throw new IllegalArgumentException("ChannelCommand agentId param cannot be empty ");
        }
        if (this.protocol == null) {
            throw new IllegalArgumentException("ChannelCommand channel protocol cannot be null ");
        }

        if (command.getCommandRespType() == CommandRespType.COMMAND_HTTP_PUSH && StringUtils.isBlank(command.getResponsePushUrl())) {
            throw new IllegalArgumentException("ChannelCommand 命令接受响应类型是HTTP，CommandPacket 推送地址不能为空！ ");
        }
    }
}
