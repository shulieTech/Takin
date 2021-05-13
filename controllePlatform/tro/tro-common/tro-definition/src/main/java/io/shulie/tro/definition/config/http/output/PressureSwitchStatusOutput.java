package io.shulie.tro.definition.config.http.output;

/**
 * @author shiyajian
 * create: 2020-12-09
 */
public class PressureSwitchStatusOutput {

    /**
     * OPENED("已开启",0),
     * OPENING("开启中",1),
     * OPEN_FAILING("开启异常",2),
     * CLOSED("已关闭",3),
     * CLOSING("关闭中",4),
     * CLOSE_FAILING("关闭异常",5)
     */
    private String switchStatus;

}
