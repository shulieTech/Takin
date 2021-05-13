package io.shulie.tro.exception.entity;

/**
 * @author shiyajian
 * create: 2020-09-25
 */
public class TroException extends RuntimeException {

    private ExceptionReadable ex;

    private Object source;

    public TroException(ExceptionReadable ex, Object source) {
        super();
        this.ex = ex;
        this.source = source;
    }

    public ExceptionReadable getEx() {
        return ex;
    }

    public void setEx(ExceptionReadable ex) {
        this.ex = ex;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }


}
