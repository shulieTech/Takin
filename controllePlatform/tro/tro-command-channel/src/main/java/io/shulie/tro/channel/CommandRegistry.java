package io.shulie.tro.channel;

import io.shulie.tro.channel.handler.CommandHandler;
import io.shulie.tro.channel.type.Command;

/**
 * @author: Hengyu
 * @className: CommandRegistry
 * @date: 2020/12/29 10:52 下午
 * @description: 命令注册中心
 */
public interface CommandRegistry {

    /**
     * 发送工具类
     * @param command 命令对象
     * @param handler 监听处理对象
     */
    public void register(Command command, CommandHandler handler);

    /**
     * 获取命令处理对象
     * @param commandId 命令类型Id
     * @return 命令处理对象
     */
    public CommandHandler getHandler(String commandId);

}
