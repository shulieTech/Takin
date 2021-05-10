package io.shulie.surge.data.deploy.pradar.common;

/**
 * @Author: xingchen
 * @ClassName: TraceFlagEnum
 * @Package: io.shulie.surge.data.deploy.pradar.common
 * @Date: 2021/3/3114:08
 * @Description:
 */
public enum TraceFlagEnum {
    LOG_OK("LOG_OK"),
    LOG_ERROR("LOG_ERROR");

    public String code;

    TraceFlagEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
