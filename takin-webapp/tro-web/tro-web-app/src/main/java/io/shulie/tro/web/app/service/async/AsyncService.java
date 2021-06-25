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

package io.shulie.tro.web.app.service.async;

import java.util.List;

import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageWrapperResp.SceneSlaRefResp;
import io.shulie.tro.web.data.param.perfomanceanaly.PerformanceBaseDataParam;

/**
 * @ClassName AsyncService
 * @Description 异步数据
 * @Author qianshui
 * @Date 2020/11/9 下午8:59
 */
public interface AsyncService {

    void savePerformanceBaseData(PerformanceBaseDataParam param);

    void monitorCpuMemory(Long sceneId, Long reportId, List<String> appNames, List<SceneSlaRefResp> stopSla, List<SceneSlaRefResp> warnSla);
}
