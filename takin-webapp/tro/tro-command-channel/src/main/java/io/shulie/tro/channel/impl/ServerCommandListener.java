package io.shulie.tro.channel.impl;

import io.shulie.tro.channel.CommandListener;
import io.shulie.tro.channel.bean.CommandPacket;

/**
 * @author: HengYu
 * @className: ServerCommandListener
 * @date: 2020/12/30 8:44 上午
 * @description:
 */
public class ServerCommandListener  implements CommandListener {
    @Override
    public void call(CommandPacket packet) {
        System.out.println(packet.toString());
    }
}
