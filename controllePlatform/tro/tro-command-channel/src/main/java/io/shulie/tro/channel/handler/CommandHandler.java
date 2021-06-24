package io.shulie.tro.channel.handler;

import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.channel.bean.CommandResponse;

/**
 * @author: HengYu
 * @className: CommandHandler
 * @date: 2020/12/29 11:29 下午
 * @description: 定义命令处理
 */
public interface CommandHandler {

    /**
     * 命令处理对象
     * @param packet 命令传输信息对象
     * @return 命令处理响应
     */
    public CommandResponse handle(CommandPacket packet);

}
