package io.shulie.amdb.enums;

/**
 * @author pirateskipper
 * @time:2020-8-20 18:12:34
 */

public enum ResponseCodeEnum {

    PARAMETER_ILLEGAL("100","参数错误");

    ResponseCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
