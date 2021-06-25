package io.shulie.tro.channel.bean;

/**
 * @author: HengYu
 * @className: CommandPacket
 * @date: 2020/12/29 10:20 下午
 * @description: 命令传输对象包
 */
public class CommandPacket{


    private String id;

    /**
     * 命令状态
     */
    private CommandStatus status;


    /**
     * 命令状态
     */
    private CommandRespType commandRespType = CommandRespType.COMMAND_CALLBACK;

    /**
     * 命令状态
     */
    private String responsePushUrl;


    /**
     * 命令发送对象
     */
    private CommandSend send;

    /**
     * 命令响应
     */
    private CommandResponse response;


    public CommandSend getSend() {
        return send;
    }

    public void setSend(CommandSend send) {
        this.send = send;
    }

    public CommandResponse getResponse() {
        return response;
    }

    public void setResponse(CommandResponse response) {
        this.response = response;
    }

    public CommandStatus getStatus() {
        return status;
    }

    public void setStatus(CommandStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public CommandRespType getCommandRespType() {
        return commandRespType;
    }

    public void setCommandRespType(CommandRespType commandRespType) {
        this.commandRespType = commandRespType;
    }

    public String getResponsePushUrl() {
        return responsePushUrl;
    }

    public void setResponsePushUrl(String responsePushUrl) {
        this.responsePushUrl = responsePushUrl;
    }



}