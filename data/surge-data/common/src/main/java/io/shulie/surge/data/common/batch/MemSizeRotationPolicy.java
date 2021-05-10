package io.shulie.surge.data.common.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 内存占用滚动策略
 *
 * @author vincent
 */
public class MemSizeRotationPolicy implements RotationPolicy {
    private static final Logger LOG = LoggerFactory.getLogger(MemSizeRotationPolicy.class);
    private long maxBytes;
    private long lastOffset = 0;
    private long currentBytesWritten = 0;

    public MemSizeRotationPolicy(float count, Units units) {
        this.maxBytes = (long) (count * units.getByteCount());
    }

    protected MemSizeRotationPolicy(long maxBytes) {
        this.maxBytes = maxBytes;
    }

    @Override
    public boolean mark(long offset) {
        long diff = offset - this.lastOffset;
        this.currentBytesWritten += diff;
        this.lastOffset = offset;
        return this.currentBytesWritten >= this.maxBytes;
    }

    @Override
    public void reset() {
        this.currentBytesWritten = 0;
        this.lastOffset = 0;
    }

    @Override
    public RotationPolicy copy() {
        return new MemSizeRotationPolicy(this.maxBytes);
    }

    public static enum Units {

        KB((long) Math.pow(2, 10)),
        MB((long) Math.pow(2, 20)),
        GB((long) Math.pow(2, 30)),
        TB((long) Math.pow(2, 40));

        private long byteCount;

        private Units(long byteCount) {
            this.byteCount = byteCount;
        }

        public long getByteCount() {
            return byteCount;
        }
    }
}
