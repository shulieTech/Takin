package io.shulie.tro.channel;

import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.channel.bean.CommandResponse;
import io.shulie.tro.channel.bean.CommandSend;
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

        CommandSend send = packet.getSend();
        Object index = send.getParam().get("index");
        System.out.println("client receiver index: "+ index);

        return response;
    }

}
