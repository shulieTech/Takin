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

package io.shulie.tro.cloud.web.entrypoint.controller.engine;

import com.pamirs.tro.entity.domain.vo.engine.EngineNotifyParam;
import io.shulie.tro.cloud.biz.service.scene.EngineCallbackService;
import io.shulie.tro.cloud.common.constants.APIUrls;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 何仲奇
 * @package io.shulie.tro.cloud.controller.jmeter
 * @date 2020/9/23 2:42 下午
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL + "engine/callback")
public class EngineCallbackController {

    @Autowired
    private EngineCallbackService engineCallbackService;

    @PostMapping()
    @ApiOperation(value = "jmeter回调状态")
    public ResponseResult taskResultNotify(@RequestBody EngineNotifyParam notify) {
        return engineCallbackService.notifyEngineState(notify);
    }
}
