package io.shulie.tro.channel.bean;

/**
 * @author: Hengyu
 * @className: CommandPacket
 * @date: 2020/12/29 10:20 下午
 * @description: 命令传输对象包
 */
public class CommandSend extends CommandCommon {

    /**
     * 指定接收AgentId
     */
    private String agentId;


    /**
     * 模块Id
     */
    private String moduleId;

    /**
     * 命令名称
     */
    private String command;


    public CommandSend() {
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }


    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}