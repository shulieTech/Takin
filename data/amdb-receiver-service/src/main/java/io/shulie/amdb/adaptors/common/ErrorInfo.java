package io.shulie.amdb.adaptors.common;

/**
 * @author vincent
 */
public class ErrorInfo {
    private transient Object[] args;
    private String code;
    private transient String msgTemplate;
    private String msg;

    public ErrorInfo(String code, String msgTemp, Object... args) {
        this.code = code;
        this.msgTemplate = msgTemp;
        this.args = args;
        this.msg = this.formatErrorMsg();
    }

    public static ErrorInfo build(String code, String msgTemp, Object... args) {
        return new ErrorInfo(code, msgTemp, args);
    }

    public Object[] getArgs() {
        return this.args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsgTemplate() {
        return this.msgTemplate;
    }

    public void setMsgTemplate(String msgTemplate) {
        this.msgTemplate = msgTemplate;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String formatErrorMsg() {
        return this.args != null ? String.format(this.msgTemplate, this.args) : this.msgTemplate;
    }
}
