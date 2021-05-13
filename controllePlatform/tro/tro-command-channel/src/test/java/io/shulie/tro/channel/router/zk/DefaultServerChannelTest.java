package io.shulie.tro.channel.router.zk;

import com.alibaba.fastjson.JSON;
import io.shulie.tro.channel.ClientChannel;
import io.shulie.tro.channel.CommandListener;
import io.shulie.tro.channel.ServerChannel;
import io.shulie.tro.channel.UploadFileHandler;
import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.channel.bean.CommandSend;
import io.shulie.tro.channel.bean.CommandStatus;
import io.shulie.tro.channel.protocal.JsonChannelProtocol;
import io.shulie.tro.channel.type.UploadFileCommand;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DefaultServerChannelTest {
    public static final String ZK_SERVERS = "192.168.1.101:2181,192.168.1.102:2181,192.168.1.103:2181";
    public static final String AGENT_ID = "192.168.100.109-76117";
    @Test
    public void getCurrentCommand() throws Exception {

        ZkClientConfig config = new ZkClientConfig();
        config.setConnectionTimeoutMillis(60000);
        config.setSessionTimeoutMillis(60000);
        config.setZkServers(ZK_SERVERS);


        ClientChannel channel = new DefaultClientChannel()
                .setChannelProtocol(new JsonChannelProtocol())
                .registerHandler(new UploadFileCommand(), new UploadFileHandler())
                .build(config);

        //-javaagent:/Users/ranghai/Desktop/pradar-package/sf/local/pradar-agent/agent/pradar-core-ext-bootstrap-1.0.0.jar -Dpradar.project.name=pradar-redis-debug-examples
        channel.register(AGENT_ID);












        ServerChannel serverChannel = new DefaultServerChannel()
                .build(config)
                .setChannelProtocol(new JsonChannelProtocol());

        CommandPacket command = new CommandPacket();

        command.setId("1");

        command.setStatus(CommandStatus.COMMAND_SEND);
        CommandSend send = new CommandSend();
        send.setAgentId(AGENT_ID);
        Map<String, Object> map = new HashMap<>();

        map.put("file", "filepath");
        send.setParam(map);

        send.setCommandId(new UploadFileCommand().getId());
        command.setSend(send);

        boolean send1 = serverChannel.send(command, new CommandListener() {
            @Override
            public void call(CommandPacket packet) {
                String s = JSON.toJSONString(packet);
            }
        });
        //获取命令执行状态
        CommandPacket currentCommand = serverChannel.getCurrentCommand(AGENT_ID,"1");

        Assert.assertNotNull(currentCommand);



        System.out.println("执行命令关闭流程");
        channel.close();
        serverChannel.close();

    }
}