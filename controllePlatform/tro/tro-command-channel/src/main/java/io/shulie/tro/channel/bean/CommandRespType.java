package io.shulie.tro.channel.bean;

/**
 * @author: Hengyu
 * @className: CommandRespType
 * @date: 2020/12/29 10:20 下午
 * @description: 命令响应类型
 */
public enum CommandRespType {

    /**
     * 命令异步回调响应
     */
    COMMAND_CALLBACK(0),

    /**
     * 命令响应HTTP推送
     */
    COMMAND_HTTP_PUSH(1);

    private int value;

    CommandRespType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}