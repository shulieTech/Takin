package io.shulie.tro.channel.bean;


import io.shulie.tro.channel.utils.ThrowableUtils;

/**
 * @author: Hengyu
 * @className: CommandPacket
 * @date: 2020/12/29 10:20 下午
 * @description: 命令传输对象包
 */
public class CommandResponse<T> extends CommandCommon {

    private boolean success;
    private T result;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static <T> CommandResponse<T> success(T result) {
        CommandResponse<T> response = new CommandResponse<T>();
        response.setSuccess(true);
        response.setResult(result);
        return response;
    }

    public static <T> CommandResponse<T> failure(String message) {
        CommandResponse<T> response = new CommandResponse<T>();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    public static <T> CommandResponse<T> failure(Throwable e) {
        CommandResponse<T> response = new CommandResponse<T>();
        response.setSuccess(false);
        response.setMessage(ThrowableUtils.toString(e));
        return response;
    }

    @Override
    public String toString() {
        if (isSuccess()) {
            return result == null ? "" : result.toString();
        } else {
            return message;
        }
    }
}