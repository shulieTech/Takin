/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
