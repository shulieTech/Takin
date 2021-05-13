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

package io.shulie.tro.cloud.open.entrypoint.controller.engine;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.shulie.tro.cloud.biz.input.engine.EnginePluginWrapperInput;
import io.shulie.tro.cloud.biz.output.engine.EnginePluginDetailOutput;
import io.shulie.tro.cloud.biz.output.engine.EnginePluginSimpleInfoOutput;
import io.shulie.tro.cloud.biz.service.engine.EnginePluginService;
import io.shulie.tro.cloud.common.constants.APIUrls;
import io.shulie.tro.cloud.common.constants.EnginePluginConstants;
import io.shulie.tro.cloud.common.constants.ResponseResultConstant;
import io.shulie.tro.cloud.open.req.engine.EnginePluginDetailsWrapperReq;
import io.shulie.tro.cloud.open.req.engine.EnginePluginFetchWrapperReq;
import io.shulie.tro.cloud.open.req.engine.EnginePluginStatusWrapperReq;
import io.shulie.tro.cloud.open.req.engine.EnginePluginWrapperReq;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 引擎控制器
 *
 * @author lipeng
 * @date 2021-01-06 2:53 下午
 */
@Slf4j
@RestController
@Api(tags = "压测引擎管理")
@RequestMapping(APIUrls.TRO_OPEN_API_URL + "engine")
public class EnginePluginOpenController {

    @Resource
    private EnginePluginService enginePluginService;

    @ApiOperation(value = "获取引擎支持的插件信息")
    @PostMapping("/fetchAvailableEnginePlugins")
    public ResponseResult<Map<String, List<EnginePluginSimpleInfoOutput>>> fetchAvailableEnginePlugins(@RequestBody EnginePluginFetchWrapperReq request) {
        List<String> pluginTypes = request.getPluginTypes();
        List<String> pluginTypesInput = Lists.newArrayList();
        //插件类型小写存储
        if(CollectionUtils.isNotEmpty(pluginTypes)) {
            pluginTypes.forEach(item -> pluginTypesInput.add(StringUtils.lowerCase(item)));
        } else{
            return ResponseResult.fail(ResponseResultConstant.RESPONSE_RESULT_CODE_ERROR, "参数不能为空。");
        }
        return ResponseResult.success(enginePluginService.findEngineAvailablePluginsByType(pluginTypesInput));
    }

    @ApiOperation(value = "获取引擎插件详情信息")
    @PostMapping("/fetchEnginePluginDetails")
    public ResponseResult<EnginePluginDetailOutput> fetchEnginePluginDetails(@RequestBody EnginePluginDetailsWrapperReq request) {
        Long pluginId = request.getPluginId();
        if(pluginId == null) {
            return ResponseResult.fail(ResponseResultConstant.RESPONSE_RESULT_CODE_ERROR, "pluginId不能为空", "传递参数pluginId");
        }
        return enginePluginService.findEnginePluginDetailss(pluginId);
    }

    @ApiOperation(value = "保存引擎插件")
    @PostMapping("/saveEnginePlugin")
    public ResponseResult saveEnginePlugin(@RequestBody EnginePluginWrapperReq request) {
        EnginePluginWrapperInput input = new EnginePluginWrapperInput();
        BeanUtils.copyProperties(request, input);
        enginePluginService.saveEnginePlugin(input);
        return ResponseResult.success();
    }

    @ApiOperation("启用引擎插件")
    @PostMapping("enableEnginePlugin")
    public ResponseResult enableEnginePlugin(@RequestBody EnginePluginStatusWrapperReq request) {
        Long pluginId = request.getPluginId();
        if(Objects.isNull(pluginId) || pluginId == 0) {
            ResponseResult.fail(ResponseResultConstant.RESPONSE_RESULT_CODE_ERROR, "pluginId不能为空", "传递参数pluginId");
        }
        enginePluginService.changeEnginePluginStatus(pluginId, EnginePluginConstants.ENGINE_PLUGIN_STATUS_ENABLED);
        return ResponseResult.success();
    }

    @ApiOperation("禁用引擎插件")
    @PostMapping("disableEnginePlugin")
    public ResponseResult disableEnginePlugin(@RequestBody EnginePluginStatusWrapperReq request) {
        Long pluginId = request.getPluginId();
        if(Objects.isNull(pluginId) || pluginId == 0) {
            ResponseResult.fail(ResponseResultConstant.RESPONSE_RESULT_CODE_ERROR, "pluginId不能为空", "传递参数pluginId");
        }
        enginePluginService.changeEnginePluginStatus(pluginId, EnginePluginConstants.ENGINE_PLUGIN_STATUS_DISABLED);
        return ResponseResult.success();
    }

}
