package io.shulie.tro.exception.entity;

/**
 * @author shiyajian
 * create: 2020-09-04
 */
public enum ExceptionCode  implements ExceptionReadable {

    /**
     * 异常信息ExceptionCode
     */
    HTTP_UTIL_URL_EMPTY("tro-util-http-0001", "url is empty or clazz is null"),
    HTTP_REQUEST_ERROR("tro-util-http-0002", "HTTP request is error"),
    HTTP_LOGIN_ERROR("tro-user-login-0001", "Tro user login error");
    private String errorCode;

    private String defaultValue;

    ExceptionCode(String errorCode, String defaultValue) {
        this.errorCode = errorCode;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getDefaultValue() {
        return this.defaultValue;
    }
}
