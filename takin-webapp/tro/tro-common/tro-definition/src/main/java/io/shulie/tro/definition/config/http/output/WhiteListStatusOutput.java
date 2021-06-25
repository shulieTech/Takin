package io.shulie.tro.definition.config.http.output;

import java.io.Serializable;

/**
 * @author shiyajian
 * create: 2020-12-09
 */
public class WhiteListStatusOutput implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置编码
     */
    private String configCode;

    /**
     * 开关状态
     * 0-关闭
     * 1-开启
     */
    private Integer switchFlag = 1;

    /**
     * OPENED("已开启",0),
     * OPENING("开启中",1),
     * OPEN_FAILING("开启异常",2),
     * CLOSED("已关闭",3),
     * CLOSING("关闭中",4),
     * CLOSE_FAILING("关闭异常",5)
     */
    private String switchStatus;

    @Deprecated
    public void setSwitchFlag(Integer switchFlag) {
        this.switchFlag = switchFlag;
        if (switchFlag != null && switchFlag == 1) {
            this.setSwitchStatus("OPENED");
        } else {
            this.setSwitchStatus("CLOSED");
        }
    }

    public void setSwitchFlagFix(Boolean value) {
        this.switchFlag = value ? 1 : 0;
        this.setSwitchStatus(value ? "OPENED" : "CLOSED");
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public Integer getSwitchFlag() {
        return switchFlag;
    }

    public String getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(String switchStatus) {
        this.switchStatus = switchStatus;
    }
}
