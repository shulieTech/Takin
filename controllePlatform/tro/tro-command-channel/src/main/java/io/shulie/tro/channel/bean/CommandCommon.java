package io.shulie.tro.channel.bean;

import java.util.Map;

/**
 * @author: Hengyu
 * @className: CommandCommon
 * @date: 2020/12/30 7:59 上午
 * @description:
 */
public class CommandCommon {
    /**
     * 命令Id
     */
    private String commandId;

    /**
     * 命令传递参数
     */
    private Map<String,Object> param;

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }
}
