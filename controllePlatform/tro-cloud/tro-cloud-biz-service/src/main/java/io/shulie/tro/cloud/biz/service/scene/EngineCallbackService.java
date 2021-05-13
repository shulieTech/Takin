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

import com.pamirs.tro.entity.domain.vo.engine.EngineNotifyParam;
import io.shulie.tro.common.beans.response.ResponseResult;

/**
 * todo 先临时放到这里
 *
 * @author 何仲奇
 * @Package io.shulie.tro.scenemanage.poll
 * @date 2020/9/23 2:55 下午
 */
public interface EngineCallbackService {
    /**
     * jmeter 状态回传
     *
     * @param notify
     */
    ResponseResult notifyEngineState(EngineNotifyParam notify);
}
