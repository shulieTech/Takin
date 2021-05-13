package io.shulie.tro.channel.protocal;

import io.shulie.tro.channel.bean.CommandPacket;

/**
 * @author: Hengyu
 * @className: ChannelProtocal
 * @date: 2020/12/30 9:14 上午
 * @description:
 */
public interface ChannelProtocol {


    /**
     * 序列化
     * @param packet 数据包对象
     * @return 二进制数组
     */
    public byte[] serialize(CommandPacket packet);



    /**
     * JSON序列化
     * @param packet 数据包对象
     * @return 二进制数组
     */
    public String serializeJson(CommandPacket packet);

    /**
     * 反序列化方法
     * @param data 二进制数组
     * @return 数据包对象
     */
    public CommandPacket deserialize(byte[] data);
}
