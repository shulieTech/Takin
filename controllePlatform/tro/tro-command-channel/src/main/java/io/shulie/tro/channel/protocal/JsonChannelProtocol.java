package io.shulie.tro.channel.protocal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.shulie.tro.channel.bean.CommandPacket;

/**
 * @author: Hengyu
 * @className: JSONChannelProtocol
 * @date: 2020/12/30 9:17 上午
 * @description:
 */
public class JsonChannelProtocol implements ChannelProtocol {

    @Override
    public byte[] serialize(CommandPacket packet) {
        String json = JSON.toJSONString(packet, SerializerFeature.DisableCircularReferenceDetect);
        byte[] bytes = json.getBytes();
        return bytes;
    }

    @Override
    public String serializeJson(CommandPacket packet) {
        String json = JSON.toJSONString(packet, SerializerFeature.DisableCircularReferenceDetect);
        return json;
    }

    @Override
    public CommandPacket deserialize(byte[] data) {
        String jsonStr = new String(data);
        CommandPacket commandPacket = JSON.parseObject(jsonStr, CommandPacket.class);
        return commandPacket;
    }


}
