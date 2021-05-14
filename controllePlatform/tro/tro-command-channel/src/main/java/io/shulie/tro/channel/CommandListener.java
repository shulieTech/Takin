package io.shulie.tro.channel;

import io.shulie.tro.channel.bean.CommandPacket;

/**
 * @author: Hengyu
 * @className: CommandListener
 * @date: 2020/12/29 10:52 下午
 * @description: 命令处理工具类
 */
public interface CommandListener {

    /**
     * 接收命令进行回调
     * @param packet 命令对象包
     */
    public void call(CommandPacket packet);

}
