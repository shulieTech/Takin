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

package io.shulie.tro.web.app.controller.confcenter;

import com.pamirs.tro.common.ResponseError;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.entity.domain.entity.TBaseConfig;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.BaseConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TRO基础配置
 */
@Api(tags = "TRO基础配置")
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
public class BaseConfigController {

    private final Logger LOGGER = LoggerFactory.getLogger(BaseConfigController.class);

    @Autowired
    private BaseConfigService baseConfigService;

    /**
     * 新增配置
     * @param tBaseConfig
     * @return
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_GLOBAL_CONFIG_ADD,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "新增配置")
    public ResponseEntity<Object> addBaseConfig(@RequestBody TBaseConfig tBaseConfig) {
        try {
            baseConfigService.addBaseConfig(tBaseConfig);
            return ResponseOk.create("新增成功");
        } catch (Exception e) {
            LOGGER.error("BaseConfigController.updateBaseConfig 更新异常{}", e);
            return ResponseError.create(TROErrorEnum.API_TRO_CONFCENTER_ADD_BASE_CONFIG_EXCEPTION.getErrorCode(),
                TROErrorEnum.API_TRO_CONFCENTER_ADD_BASE_CONFIG_EXCEPTION.getErrorMessage());
        }
    }

    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_GLOBAL_CONFIG_QUERY,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "获取配置")
    public ResponseEntity<Object> queryByConfigCode(@RequestParam("configCode") String configCode) {
        if (StringUtils.isEmpty(String.valueOf(configCode))) {
            return ResponseError.create(1010100301, "参数缺失");
        }
        try {
            return ResponseOk.create(baseConfigService.queryByConfigCode(configCode));
        } catch (Exception e) {
            LOGGER.error("BaseConfigController.queryByConfigCode 查询异常{}", e);
            return ResponseError.create(TROErrorEnum.API_TRO_CONFCENTER_BASE_CONFIG_QUERY_EXCEPTION.getErrorCode(),
                TROErrorEnum.API_TRO_CONFCENTER_BASE_CONFIG_QUERY_EXCEPTION.getErrorMessage());
        }
    }

    @GetMapping(value = "/base/config", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "获取模板配置")
    public Response<String> queryConfigByCode(@RequestParam(value = "configCode") String configCode) {
        TBaseConfig config = baseConfigService.queryByConfigCode(configCode);
        return Response.success(config != null ? config.getConfigValue() : null);
    }

    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_GLOBAL_CONFIG_UPDATE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateBaseConfig(@RequestBody TBaseConfig tBaseConfig) {

        try {
            baseConfigService.updateBaseConfig(tBaseConfig);
            return ResponseOk.create("更新成功");
        } catch (Exception e) {
            LOGGER.error("BaseConfigController.updateBaseConfig 更新异常{}", e);
            return ResponseError.create(TROErrorEnum.API_TRO_CONFCENTER_UPDATE_BASE_CONFIG_EXCEPTION.getErrorCode(),
                TROErrorEnum.API_TRO_CONFCENTER_UPDATE_BASE_CONFIG_EXCEPTION.getErrorMessage());
        }
    }

}
