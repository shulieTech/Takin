package io.shulie.amdb.exception;

/**
 * @Author: xingchen
 * @ClassName: AmdbExcetion
 * @Package: io.shulie.amdb.exception
 * @Date: 2020/10/1914:26
 * @Description:
 */
public class AmdbExcetion extends RuntimeException{
    public AmdbExcetion() {
    }

    public AmdbExcetion(String message) {
        super(message);
    }

    public AmdbExcetion(String message, Throwable cause) {
        super(message, cause);
    }

    public AmdbExcetion(Throwable cause) {
        super(cause);
    }

    public AmdbExcetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
