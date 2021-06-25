package io.shulie.tro.channel.exception;

/**
 * @author: HengYu
 * @className: ChannelException
 * @date: 2020/12/29 10:17 下午
 * @description:
 */
public class ChannelException extends Exception  {
    public ChannelException() {
    }

    public ChannelException(String message) {
        super(message);
    }

    public ChannelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelException(Throwable cause) {
        super(cause);
    }
}
