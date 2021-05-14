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

package io.shulie.tro.web.app.controller.pressurecontrol;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import com.pamirs.tro.common.ResponseError;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.constant.PressureOperateEnum;
import com.pamirs.tro.common.constant.ResponseConstant;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.PressureReadyService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 说明: 压测准备模块接口
 *
 * @author shulie
 * @version v1.0
 * @2018年4月13日
 */
@Api(tags = "压测准备模块接口")
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
public class PMCheckController {
    private final Logger LOGGER = LoggerFactory.getLogger(PMCheckController.class);
    @Autowired
    @Qualifier("ScriptThreadPool")
    protected ThreadPoolExecutor runShellTaskExecutor;
    @Autowired
    private PressureReadyService pressureReadyService;

    /**
     * 说明: API.02.02.001 查询压测检测列表接口(包含异常检测)
     *
     * @param paramMap 二级链路名称,基础链路名称,应用名称,负责人工号
     * @return 成功, 则返回检测列表信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_PRESSUREREADY_PMCHECK_QUERY_CHECKLIST_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryChecklist(@RequestBody Map<String, Object> paramMap) {
        try {
            return ResponseOk.create(pressureReadyService.queryChecklist(paramMap));
        } catch (Exception e) {
            LOGGER.error("PMCheckController.queryChecklist 查询压测检测信息异常{}", e);
            return ResponseError.create(TROErrorEnum.PMCHECK_QUERY_CHECKLIST_EXCEPTION.getErrorCode(),
                TROErrorEnum.PMCHECK_QUERY_CHECKLIST_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.02.02.002 影子库整体同步检测 接口(查询构建表)
     *
     * @param applicationId 应用id
     * @return 成功, 则返回影子库列结果, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_PRESSUREREADY_PMCHECK_CHECK_BASICDATA_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryCheckShadowlib(@RequestParam("applicationId") String applicationId) {
        try {
            if (StringUtils.isEmpty(applicationId)) {
                return ResponseError.create(TROErrorEnum.PMCHECK_QUERY_CHECKSHADOWLIB_PARAMLACK.getErrorCode(),
                    TROErrorEnum.PMCHECK_QUERY_CHECKSHADOWLIB_PARAMLACK.getErrorMessage());
            }
            return ResponseOk.create(pressureReadyService.queryCheckShadowlib(applicationId).call());
        } catch (Exception e) {
            LOGGER.error("PMCheckController.queryCheckShadowlib 影子库整体同步检测查询异常{}", e);
            return ResponseError.create(TROErrorEnum.PMCHECK_QUERY_CHECKSHADOWLIB_EXCEPTION.getErrorCode(),
                TROErrorEnum.PMCHECK_QUERY_CHECKSHADOWLIB_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.02.02.003 白名单检测实时接口(包含dubbo和http)
     *
     * @param applicationId 应用id
     * @return 成功, 则返回白名单检测结果, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_PRESSUREREADY_PMCHECK_CHECK_WLIST_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryCheckWList(@RequestParam("applicationId") String applicationId) {
        try {
            return ResponseOk.create(pressureReadyService.queryCheckWList(applicationId).call());
        } catch (Exception e) {
            LOGGER.error("PMCheckController.queryCheckWList 白名单检测异常{}", e);
            return ResponseError.create(TROErrorEnum.PMCHECK_QUERY_CHECKWLIST_EXCEPTION.getErrorCode(),
                TROErrorEnum.PMCHECK_QUERY_CHECKWLIST_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.02.02.004 缓存预热检测
     *
     * @param applicationId 应用id
     * @return 成功, 则返回缓存预热检测结果, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_PRESSUREREADY_CACHE_CHECK_CACHE_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryCheckCache(@RequestParam("applicationId") String applicationId) {
        try {
            return ResponseOk.create(pressureReadyService.queryCheckCache(applicationId).call());
        } catch (Exception e) {
            LOGGER.error("PMCheckController.queryCheckCache 缓存预热检测异常{}", e);
            return ResponseError.create(TROErrorEnum.PMCHECK_QUERY_CHECKCACHE_EXCEPTION.getErrorCode(),
                TROErrorEnum.PMCHECK_QUERY_CHECKCACHE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.02.02.005 批量检测接口
     *
     * @param applicationIds 应用id
     * @return 成功, 则返回检测列表结果, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_PRESSUREREADY_PMCHECK_CHECK_BATCHCHECK_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> batchCheck(@RequestParam("applicationIds") String applicationIds) {
        try {
            return ResponseOk.create(pressureReadyService.batchCheck(applicationIds));
        } catch (TROModuleException e) {
            LOGGER.error("PMCheckController.batchCheck {}" + e.getMessage(), e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("PMCheckController.batchCheck 批量检测失败{}", e);
            return ResponseError.create(TROErrorEnum.PMCHECK_QUERY_BATCHCHECK_EXCEPTION.getErrorCode(),
                TROErrorEnum.PMCHECK_QUERY_BATCHCHECK_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.02.02.006 压测检测调试开关接口
     *
     * @param switchList 应用ids和开关数据
     * @return 成功, 更新所有的应用为成功状态, 则返回成功信息; 失败则返回构建应用失败错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_PRESSUREREADY_PMCHECK_DEBUG_SWITCH_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> debugSwitch(@RequestBody List<Map<String, Object>> switchList) {
        try {
            pressureReadyService.debugSwitch(switchList, PressureOperateEnum.PRESSURE_CHECK_DEBUG_SWITCH);
            return ResponseOk.create(ResponseConstant.DEBUG_SWITCH);
        } catch (Exception e) {
            LOGGER.error("DataBuildController.debugSwitch 调试开关开启失败{}", e);
            return ResponseError.create(TROErrorEnum.BUILDDATA_DEBUG_SWITCH_FAIL.getErrorCode(),
                TROErrorEnum.BUILDDATA_DEBUG_SWITCH_FAIL.getErrorMessage());
        }
    }
}
