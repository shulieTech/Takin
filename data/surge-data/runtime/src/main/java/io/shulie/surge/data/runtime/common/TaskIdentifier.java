package io.shulie.surge.data.runtime.common;

public interface TaskIdentifier {
    /**
     * 获取当前任务的唯一 ID
     * @return
     */
    String getTaskId();

    /**
     * 获取同类任务分组的名称
     * @return
     */
    String getTaskGroupName();

    /**
     * 获取处理这个任务的工作单位 ID
     * @return
     */
    String getWorkerId();

    /**
     * 获取当前整个计算拓扑的 ID
     * @return
     */
    String getTopologyId();

    /**
     * 获取当前任务所属的进程 ID
     * @return
     */
    int getProcessId();

    /**
     * 获取同类任务分组中，当前任务的顺序号
     * @return
     */
    int getTaskGroupIndex();
}
