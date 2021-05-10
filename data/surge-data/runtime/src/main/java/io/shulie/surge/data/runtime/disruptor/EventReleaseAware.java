package io.shulie.surge.data.runtime.disruptor;

public interface EventReleaseAware {
    void setEventReleaser(EventReleaser eventReleaser);
}
