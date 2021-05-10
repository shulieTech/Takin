package io.shulie.surge.data.runtime.processor;

import io.shulie.surge.data.common.factory.GenericFactorySpec;
import io.shulie.surge.data.runtime.digest.DataDigester;

public abstract class ProcessorConfigSpec<T extends DefaultProcessor> implements GenericFactorySpec<T> {
    //处理器
    private DataDigester[] digesters;
    //执行器数量
    private int executeSize = Runtime.getRuntime().availableProcessors() * 2;
    //ringbuffer长度
    private int ringBufferSize = 32768;
    //ringbuffer保留率
    private float ringBufferRemainRate = 0.2f;
    //执行器名称
    private String name;

    /**
     * @return 被创建的对象的 interface
     */
    @Override
    public abstract Class<T> productClass();

    public DataDigester[] getDigesters() {
        return digesters;
    }

    public void setDigesters(DataDigester[] digesters) {
        this.digesters = digesters;
    }

    public int getExecuteSize() {
        return executeSize;
    }

    public void setExecuteSize(int executeSize) {
        this.executeSize = executeSize;
    }

    public int getRingBufferSize() {
        return ringBufferSize;
    }

    public void setRingBufferSize(int ringBufferSize) {
        this.ringBufferSize = ringBufferSize;
    }

    public float getRingBufferRemainRate() {
        return ringBufferRemainRate;
    }

    public void setRingBufferRemainRate(float ringBufferRemainRate) {
        this.ringBufferRemainRate = ringBufferRemainRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
