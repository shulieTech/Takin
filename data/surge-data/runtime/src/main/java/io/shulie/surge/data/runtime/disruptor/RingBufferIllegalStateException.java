package io.shulie.surge.data.runtime.disruptor;

/**
 * ringbuffer 满时拒绝
 */
public class RingBufferIllegalStateException extends IllegalStateException {
    /**
     * Constructs an IllegalStateException with no detail message.
     * A detail message is a String that describes this particular exception.
     */
    public RingBufferIllegalStateException() {
        super();
    }
    public RingBufferIllegalStateException(String message) {
        super(message);
    }
}
