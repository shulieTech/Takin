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

package io.shulie.tro.web.app.controller.preventcheat;

import java.util.Map;

import javax.validation.Valid;

import com.pamirs.tro.common.ResponseError;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.entity.domain.vo.TApplicationMntConfigVo;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.ApplicationMntConfigService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 说明: 应用配置接口
 *
 * @author shulie
 * @version v1.0
 * @2018年4月13日
 */
@Api(tags = "应用配置接口")
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
public class ApplicationMntConfigController {

    private final Logger LOGGER = LoggerFactory.getLogger(ApplicationMntConfigController.class);

    @Autowired
    private ApplicationMntConfigService applicationMntConfigService;

    /**
     * 说明: API.06.01.001 查询应用各种开关
     *
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_APPLICATION_CONFIG_PAGE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryApplicationConfigPage(@RequestBody Map<String, Object> paramMap) {
        try {
            return ResponseOk.create(applicationMntConfigService.queryApplicationConfigPage(paramMap));
        } catch (Exception e) {
            LOGGER.error("ApplicationMntConfigController.queryApplicationConfigPage 查询配置分页异常{}", e);
            return ResponseError.create(
                TROErrorEnum.API_TRO_CONFCENTER_APPLICATION_CONFIG_PAGE_EXCEPTION.getErrorCode(),
                TROErrorEnum.API_TRO_CONFCENTER_APPLICATION_CONFIG_PAGE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.06.02.002 查询某个应用的开关与全局开关
     *
     * @return 成功, 则返回应用信息列表, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_APPLICATION_CONFIG_QUERY,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryConfig(@RequestParam("applicationName") String applicationName) {
        try {
            return ResponseOk.create(applicationMntConfigService.queryConfig(applicationName));
        } catch (Exception e) {
            LOGGER.error("ApplicationMntConfigController.updateApplicationConfig 查询异常{}", e);
            return ResponseError.create(
                TROErrorEnum.API_TRO_CONFCENTER_APPLICATION_CONFIG_UPDATE_EXCEPTION.getErrorCode(),
                TROErrorEnum.API_TRO_CONFCENTER_APPLICATION_CONFIG_UPDATE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.06.03.003 批量更新应用各种开关
     *
     * @return 成功, 则返回应用信息列表, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_APPLICATION_CONFIG_BATCH_UPDATE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateApplicationConfigBatch(@RequestBody @Valid TApplicationMntConfigVo vo,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1050100401, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            applicationMntConfigService.updateConfigBatch(vo);
            return ResponseOk.create("更新成功");
        } catch (Exception e) {
            LOGGER.error("ApplicationMntConfigController.updateApplicationConfigBatch 批量更新异常{}", e);
            return ResponseError.create(
                TROErrorEnum.API_TRO_CONFCENTER_APPLICATION_CONFIG_UPDATE_EXCEPTION.getErrorCode(),
                TROErrorEnum.API_TRO_CONFCENTER_APPLICATION_CONFIG_UPDATE_EXCEPTION.getErrorMessage());
        }
    }

}
