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

package io.shulie.tro.cloud.biz.service.scene;

import com.pamirs.tro.entity.domain.dto.report.SceneActionDTO;
import com.pamirs.tro.entity.domain.vo.report.SceneTaskNotifyParam;
import io.shulie.tro.cloud.biz.input.scenemanage.*;
import io.shulie.tro.cloud.biz.output.scenemanage.SceneStartTrialRunOutput;
import io.shulie.tro.cloud.biz.output.scenetask.SceneActionOutput;
import io.shulie.tro.cloud.biz.output.scenetask.SceneTaskQueryTpsOutput;
import io.shulie.tro.cloud.common.bean.task.TaskResult;

import java.util.List;

/**
 * @Author 莫问
 * @Date 2020-04-22
 */
public interface SceneTaskService {

    /**
     * 启动场景测试
     * @param input
     * @return
     */
    SceneActionOutput start(SceneTaskStartInput input);

    /**
     * 停止场景测试
     *
     * @param sceneId
     * @return
     */
    void stop(Long sceneId);

    /**
     * 检查场景压测启动状态
     *
     * @param sceneId
     * @return
     */
    SceneActionOutput checkSceneTaskStatus(Long sceneId);

    /**
     * 处理场景任务事件
     *
     * @param taskResult
     */
    void handleSceneTaskEvent(TaskResult taskResult);

    /**
     * 结束标识，之后并不是pod生命周期结束，而是metric数据传输完毕，将状态回置成压测停止
     *
     * @param param
     * @see CollectorService
     */

    String taskResultNotify(SceneTaskNotifyParam param);

    /**
     * 开始任务试跑
     * @param input
     * @return
     */
    SceneStartTrialRunOutput startTrialRun(SceneStartTrialRunInput input);
    /**
     * 调整压测任务的tps
     * @param input
     */
    void updateSceneTaskTps(SceneTaskUpdateTpsInput input);

    /**
     * 查询当前调整压测任务的tps
     * @param input
     * @return
     */
    SceneTaskQueryTpsOutput queryAdjustTaskTps(SceneTaskQueryTpsInput input);

    /**
     * 启动流量调试，返回报告id
     * @param input
     * @return
     */
    Long startFlowDebugTask(SceneManageWrapperInput input, List<Long> enginePluginIds);
}
