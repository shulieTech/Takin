package io.shulie.surge.data.common.batch;

import java.util.concurrent.TimeUnit;

/**
 * 时间滚动策略
 *
 * @author vincent
 */
public class TimedRotationPolicy implements RotationPolicy {

    /**
     * 滚动间隔
     */
    private long interval;

    public TimedRotationPolicy(int count, TimeUnit units) {
        this.interval = units.toMillis(count);
    }

    protected TimedRotationPolicy(long interval) {
        this.interval = interval;
    }

    /**
     * @param offset current offset of file being written
     * @return true if a rotation should be performed
     */
    @Override
    public boolean mark(long offset) {
        return false;
    }

    /**
     * Called after the rotates.
     */
    @Override
    public void reset() {

    }

    @Override
    public RotationPolicy copy() {
        return new TimedRotationPolicy(this.interval);
    }

    public long getInterval() {
        return this.interval;
    }

}