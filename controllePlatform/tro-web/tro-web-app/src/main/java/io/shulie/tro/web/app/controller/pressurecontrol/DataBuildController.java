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

import com.pamirs.tro.common.ResponseError;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.constant.PressureOperateEnum;
import com.pamirs.tro.common.constant.ResponseConstant;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.PressureReadyService;
import io.swagger.annotations.Api;
import org.apache.commons.collections4.MapUtils;
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
 * 说明: 数据构建接口
 *
 * @author shulie
 * @version v1.0
 * @2018年4月13日
 */
@Api(tags = "数据构建接口")
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
public class DataBuildController {

    private final Logger LOGGER = LoggerFactory.getLogger(DataBuildController.class);

    @Autowired
    private PressureReadyService pressureReadyService;

    /**
     * 说明: API.02.01.001 根据条件查询构建信息列表
     *
     * @param paramMap 二级链路名称,基础链路名称,应用名称,负责人工号
     * @return 成功, 则返回构建列表信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_PRESSUREREADY_BUILDDATA_QUERY_BUILDINFO_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryBuildinfo(@RequestBody Map<String, Object> paramMap) {
        try {
            return ResponseOk.create(pressureReadyService.queryBuildinfo(paramMap));
        } catch (Exception e) {
            LOGGER.error("DataBuildController.queryBuildinfo 查询构建信息异常{}", e);
            return ResponseError.create(TROErrorEnum.BUILDDATA_QUERY_BUILDINFO_EXCEPTION.getErrorCode(),
                TROErrorEnum.BUILDDATA_QUERY_BUILDINFO_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.02.01.002 更新增脚本执行状态接口(脚本回调接口)
     *
     * @param map 接收参数(包含应用id,脚本执行状态,脚本类型)
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_PRESSUREREADY_BUILDDATA_UPDATE_SCRIPTSTATUS_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateScriptExcuteStatus(@RequestBody Map<String, Object> map) {
        if (map == null || map.isEmpty() || StringUtils.isEmpty(MapUtils.getString(map, "applicationId"))) {
            return ResponseError.create(TROErrorEnum.BUILDDATA_UPDATE_SCRIPTSTATUS_PARAMLACK.getErrorCode(),
                TROErrorEnum.BUILDDATA_UPDATE_SCRIPTSTATUS_PARAMLACK.getErrorMessage());
        }

        try {
            pressureReadyService.updateScriptExcuteStatus(map);
            return ResponseOk.create("succeed");
        } catch (Exception e) {
            LOGGER.error("DataBuildController.updateScriptExcuteStatus 新增脚本执行状态异常{}", e);
            return ResponseError.create(TROErrorEnum.BUILDDATA_UPDATE_SCRIPTSTATUS_EXCEPTION.getErrorCode(),
                TROErrorEnum.BUILDDATA_UPDATE_SCRIPTSTATUS_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.02.01.003 根据脚本类型和应用id查询脚本构建状态
     *
     * @param applicationId 应用id
     * @param scriptType    脚本类型
     * @return 成功, 则返回脚本构建状态信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_PRESSUREREADY_BUILDDATA_QUERY_SCRIPTBUILDSTATUS_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryScriptExcuteStatus(@RequestParam("applicationId") String applicationId,
        @RequestParam("scriptType") String scriptType
    ) {
        try {
            if (StringUtils.isEmpty(scriptType) || StringUtils.isEmpty(applicationId)) {
                return ResponseError.create(TROErrorEnum.BUILDDATA_QUERY_SCRIPTSTATUS_PARAMLACK.getErrorCode(),
                    TROErrorEnum.BUILDDATA_QUERY_SCRIPTSTATUS_PARAMLACK.getErrorMessage());
            }
            return ResponseOk.create(pressureReadyService.queryScriptExcuteStatus(applicationId, scriptType));
        } catch (Exception e) {
            LOGGER.error("DataBuildController.queryScriptExcuteStatus 查询脚本执行状态异常{}", e);
            return ResponseError.create(TROErrorEnum.BUILDDATA_QUERY_SCRIPTSTATUS_EXCEPTION.getErrorCode(),
                TROErrorEnum.BUILDDATA_QUERY_SCRIPTSTATUS_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.02.01.004 批量清理脚本接口
     *
     * @param applicationIds 应用ids
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_PRESSUREREADY_BUILDDATA_QUERY_BATCHCLEAN_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> batchClean(@RequestParam("applicationIds") String applicationIds) {
        try {
            pressureReadyService.batchClean(applicationIds);
            return ResponseOk.create("批量清理正在执行,请等待...");
        } catch (Exception e) {
            LOGGER.error("DataBuildController.batchClean 批量清理数据异常{}", e);
            return ResponseError.create(TROErrorEnum.BUILDDATA_QUERY_BATCHCLEAN_EXCEPTION.getErrorCode(),
                TROErrorEnum.BUILDDATA_QUERY_BATCHCLEAN_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.02.01.005 构建执行脚本接口
     *
     * @param paraMap 应用id和脚本类型
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_PRESSUREREADY_BUILDDATA_EXECUTE_SCRIPT_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> executeScript(@RequestBody Map<String, Object> paraMap) {
        try {
            pressureReadyService.executeScriptPreCheck(paraMap);
            return ResponseOk.create("脚本正在执行,请等待......");
        } catch (TROModuleException e) {
            LOGGER.error("DataBuildController.executeScript 该应用没有缓存不用执行缓存脚本{}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("DataBuildController.executeScript 执行脚本异常{}", e);
            return ResponseError.create(TROErrorEnum.BUILDDATA_EXECUTE_SCRIPT_EXCEPTION.getErrorCode(),
                TROErrorEnum.BUILDDATA_EXECUTE_SCRIPT_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.02.01.006 数据构建调试开关接口
     *
     * @param switchList 应用ids和开关数据
     * @return 成功, 更新所有的应用为成功状态, 则返回成功信息; 失败则返回构建应用失败错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_PRESSUREREADY_BUILDDATA_DEBUG_SWITCH_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> debugSwitch(@RequestBody List<Map<String, Object>> switchList) {
        try {
            pressureReadyService.debugSwitch(switchList, PressureOperateEnum.DATA_BUILD_DEBUG_SWITCH);
            return ResponseOk.create(ResponseConstant.DEBUG_SWITCH);
        } catch (Exception e) {
            LOGGER.error("DataBuildController.debugSwitch 调试开关开启失败{}", e);
            return ResponseError.create(TROErrorEnum.BUILDDATA_DEBUG_SWITCH_FAIL.getErrorCode(),
                TROErrorEnum.BUILDDATA_DEBUG_SWITCH_FAIL.getErrorMessage());
        }
    }
}
