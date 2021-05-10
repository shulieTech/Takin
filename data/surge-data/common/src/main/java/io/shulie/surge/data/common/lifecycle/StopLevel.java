package io.shulie.surge.data.common.lifecycle;


/**
 * {@link com.pamirs.pradar.log.runtime.LogRuntime} 停止时，触发回调的 {@link Stoppable} 停止级别
 *
 * @author pamirs
 */
public enum StopLevel {

	/*
	 * 务必按照停止的先后顺序定义
	 */
    /**
     * 输入层，一般提供流计算中的数据接入
     */
    INPUT(false),
    /**
     * 逻辑处理层，一般提供流计算中的计算逻辑的实现
     */
    ACTION(false),
    /**
     * 数据处理层，一般提供数据 IO 操作实现
     */
    SUPPORT(true),
    /**
     * 基础层，一般提供底层数据注册，如 Zookeeper
     */
    RESOURCE(true),
    /**
     * 资源层，提供统一管理加载的资源
     */
    BASIC(true), ;

    private final boolean processLevel;

    private StopLevel(boolean processLevel) {
        this.processLevel = processLevel;
    }

    /**
     * 是否在进程最后退出时才触发
     * @return
     */
    public boolean isProcessLevel() {
        return processLevel;
    }
}
