package io.shulie.amdb.enums;

/**
 * @Author: xingchen
 * @ClassName: LinkTypeEnum
 * @Package: io.shulie.amdb.enums
 * @Date: 2020/10/1914:33
 * @Description:
 */
public enum LinkTypeEnum {
    CUSTOM("CUSTOM", "自定义链路"),
    INTERFACE("INTERFACE", "接口链路"),
    APP("APP", "应用链路");
    private String type;
    private String name;

    LinkTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
