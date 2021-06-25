package io.shulie.tro.channel;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.channel.bean.CommandSend;
import io.shulie.tro.channel.bean.CommandStatus;
import io.shulie.tro.channel.protocal.JsonChannelProtocol;
import io.shulie.tro.channel.router.zk.DefaultClientChannel;
import io.shulie.tro.channel.router.zk.DefaultServerChannel;
import io.shulie.tro.channel.router.zk.ZkClientConfig;
import io.shulie.tro.channel.type.UploadFileCommand;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;

public class SingleChannelTest {

    public static final String ZK_SERVERS = "127.0.0.1:2181";
    public static final String AGENT_ID = "192.168.100.110-76117";
    public static final Logger logger = LoggerFactory.getLogger(SingleChannelTest.class);

    public void test() {
        try {
            Thread thread = Thread.currentThread();
            Thread clientThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        clientStart();
                    } catch (Exception e) {
                        logger.error("clientStart ",e);
                        Assert.fail("please check zookeeper config address，The test command channel case relies on zookeeper！");
                    }
                }
            });
            clientThread.start();


            Thread.sleep(2000L);
            Thread serverThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        serverStart();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Assert.fail("please check zookeeper config address，The test command channel case relies on zookeeper！");
                    }
                }
            });

            serverThread.start();
            clientThread.join();
            serverThread.join();
            System.out.println("调试流程终止...");
        } catch (Exception e) {
            Assert.fail("The test command channel is  error！");
            e.printStackTrace();
        }
    }

    public void clientStart() throws Exception {
        ZkClientConfig config = new ZkClientConfig();
        config.setConnectionTimeoutMillis(60000);
        config.setSessionTimeoutMillis(60000);
        config.setZkServers(ZK_SERVERS);

        ClientChannel channel = new DefaultClientChannel()
                .registerUserAppKey("AA")
                .setChannelProtocol(new JsonChannelProtocol())
                .registerHandler(new UploadFileCommand(), new UploadFileHandler())
                .build(config);

        //-javaagent:/Users/ranghai/Desktop/pradar-package/sf/local/pradar-agent/agent/pradar-core-ext-bootstrap-1.0.0.jar -Dpradar.project.name=pradar-redis-debug-examples
        channel.register(AGENT_ID);

        Thread.sleep(10000L);
        System.out.println("执行命令关闭流程");
        channel.close();
    }

    public void serverStart() throws Exception {

        ZkClientConfig config = new ZkClientConfig();
        config.setConnectionTimeoutMillis(60000);
        config.setSessionTimeoutMillis(60000);
        config.setZkServers(ZK_SERVERS);

        ServerChannel channel = new DefaultServerChannel()
                .build(config)
                .setChannelProtocol(new JsonChannelProtocol());

        CommandPacket command = new CommandPacket();

        command.setStatus(CommandStatus.COMMAND_SEND);
        CommandSend send = new CommandSend();
        send.setAgentId(AGENT_ID);
        Map<String, Object> map = new HashMap<>();

        map.put("file", "filepath");
        send.setParam(map);

        send.setCommandId(new UploadFileCommand().getId());
        command.setSend(send);

        int i = 1;
        while (i <= 1) {

            map.put("file", "filepath");
            map.put("index", i);
            send.setParam(map);

            command.setId(i+"");
            boolean send1 = channel.send(command, new CommandListener() {
                @Override
                public void call(CommandPacket packet) {
                    String s = JSON.toJSONString(packet);
                    System.out.println(s);
                    Assert.assertNotNull(s);

                    CommandPacket packet1 = JSON.parseObject(s, CommandPacket.class);
                    Assert.assertNotNull(packet1);
                    CommandStatus status = packet1.getStatus();
                    Assert.assertThat(status.getStatus(), anyOf(is(CommandStatus.COMMAND_COMPLETED_SUCCESS.getStatus()), is(CommandStatus.COMMAND_COMPLETED_FAIL.getStatus())));
                }
            });

            if (send1 == false) {
                System.out.println("command index " + i + " send fail");
            }
            System.out.println("发送命令：" + i);
            i++;
        }
        //获取命令执行状态
//        CommandPacket currentCommand = channel.getCurrentCommand(AGENT_ID);
//        Assert.assertNull(currentCommand);
        Thread.sleep(20000L);
        System.out.println("执行命令关闭流程");
        channel.close();
    }
}