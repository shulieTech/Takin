package io.shulie.surge.data.runtime.common.impl;

import com.google.inject.Singleton;
import io.shulie.surge.data.common.utils.CommonUtils;
import io.shulie.surge.data.common.utils.IpAddressUtils;
import io.shulie.surge.data.runtime.common.TaskIdentifier;

/**
 * 任务实时运行环境的唯一标识
 *
 * @author pamirs
 */
@Singleton
public class DefaultTaskIdentifier implements TaskIdentifier {

    private String taskId = "0";
    private String taskGroupName = "Standalone";
    private String workerId = "0";
    private String topologyId = "StandaloneTopology";
    private int taskGroupIndex = 0;
    private static int pid = CommonUtils.getPid();

    @Override
    public String getTaskId() {
        return IpAddressUtils.getLocalAddress() + "_" + taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getTaskGroupName() {
        return taskGroupName;
    }

    public void setTaskGroupName(String taskGroupName) {
        this.taskGroupName = taskGroupName;
    }

    @Override
    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    @Override
    public String getTopologyId() {
        return topologyId;
    }

    public void setTopologyId(String topologyId) {
        this.topologyId = topologyId;
    }

    @Override
    public int getTaskGroupIndex() {
        return taskGroupIndex;
    }

    public void setTaskGroupIndex(int taskGroupIndex) {
        this.taskGroupIndex = taskGroupIndex;
    }

    @Override
    public int getProcessId() {
        return pid;
    }

    @Override
    public String toString() {
        return "[worker-" + workerId + "/task-" + taskId + "/pid-" + pid + "][" +
                taskGroupName + "-" + taskGroupIndex + "]";
    }
}
