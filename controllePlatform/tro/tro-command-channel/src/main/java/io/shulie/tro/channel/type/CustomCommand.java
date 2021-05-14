package io.shulie.tro.channel.type;

/**
 * @author: Hengyu
 * @className: CustomCommand
 * @date: 2020/12/29 10:20 下午
 * @description: 自定发送命令
 */
public class CustomCommand extends Command {

    public CustomCommand(String commandId) {
        this.setId(commandId);
        this.setDesc("custom command");
    }
    
}