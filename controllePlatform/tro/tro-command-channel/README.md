**tro-common-channel**

****ServerChannel use example****

```

        # zk 配置参数对象
        ZkClientConfig config = new ZkClientConfig();
        config.setZkServers("192.168.1.101:2181,192.168.1.102:2181,192.168.1.103:2181");

        # ServerChannel实例化配置
        ServerChannel channel = new DefaultServerChannel()
                .build(config)
                .setChannelProtocol(new JsonChannelProtocol());

        # 发送命令包对象传参示例
        CommandPacket command = new CommandPacket();

        command.setStatus(CommandStatus.COMMAND_SEND);
        CommandSend send = new CommandSend();
        # 设置客户端唯一示例
        send.setAgentId("192.168.100.109-76117");
        Map<String,Object> map = new HashMap<>();
        map.put("file","filepath");
        send.setParam(map);
        send.setCommandId(new UploadFileCommand().getId());
        command.setSend(send);

        # 下发命令
        channel.send(command, new CommandListener() {
            @Override
            public void call(CommandPacket packet) {
                String s = JSON.toJSONString(packet);
                System.out.println(s);
            }
        });

```

****ClientChannel use example****

```

        # zk配置命令参数
        ZkClientConfig config = new ZkClientConfig();
        config.setZkServers("192.168.1.101:2181,192.168.1.102:2181,192.168.1.103:2181");


        # 实例化客户端通道
        ClientChannel channel = new DefaultClientChannel()
                .build(config)
                .setChannelProtocol(new JsonChannelProtocol())
                .registerHandler(new UploadFileCommand(), new UploadFileHandler());

        # 注册监听AgendId 客户端唯一标识
        channel.register("192.168.100.109-76117");
```

****客户端和服务需要统一命令数据序列化协议****

下面给出FastJson序列化实现
```
package io.shulie.tro.channel.router.zk;

import com.alibaba.fastjson.JSON;
import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.channel.protocal.ChannelProtocol;

/**
 * @author: HengYu
 * @className: JSONChannelProtocol
 * @date: 2020/12/30 9:17 上午
 * @description:
 */
public class JsonChannelProtocol implements ChannelProtocol {

    @Override
    public byte[] serialize(CommandPacket packet) {
        String json = JSON.toJSONString(packet);
        byte[] bytes = json.getBytes();
        return bytes;
    }

    @Override
    public CommandPacket deserialize(byte[] data) {
        String jsonStr = new String(data);
        CommandPacket commandPacket = JSON.parseObject(jsonStr, CommandPacket.class);
        return commandPacket;
    }
}
```

****客户端命令处理对象示例****

```
package io.shulie.tro.channel;

import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.channel.bean.CommandResponse;
import io.shulie.tro.channel.handler.CommandHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: HengYu
 * @className: CommandHandler
 * @date: 2020/12/29 11:29 下午
 * @description: 定义命令处理
 */
public class UploadFileHandler implements CommandHandler {

    @Override
    public CommandResponse handle(CommandPacket packet) {
        CommandResponse response = new CommandResponse();
        Map<String, Object> param = new HashMap<>(8);
        param.put("test","test");
        response.setParam(param);
        return response;
    }

}

```