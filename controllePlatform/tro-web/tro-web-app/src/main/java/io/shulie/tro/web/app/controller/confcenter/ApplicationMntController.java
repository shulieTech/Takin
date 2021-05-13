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

import java.util.Map;

import javax.validation.Valid;

import com.pamirs.tro.common.ResponseError;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.ConfCenterService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
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
 * 说明: 应用管理接口
 *
 * @author shulie
 * @version v1.0
 * @2018年4月13日
 */
@Api(tags = "应用管理接口")
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
public class ApplicationMntController {

    private final Logger LOGGER = LoggerFactory.getLogger(ApplicationMntController.class);

    @Autowired
    private ConfCenterService confCenterService;

    /**
     * 说明: API.01.01.001 添加应用接口
     *
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_ADD_APPLICATION_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> saveApplication(@RequestBody @Valid TApplicationMnt tApplicationMnt,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseError.create(1010100101, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            confCenterService.saveApplication(tApplicationMnt);
            //不想service嵌套service 特地 controller层做 以后可能facade层
            return ResponseOk.create("succeed");
        } catch (TROModuleException e) {
            LOGGER.error("ConfCenterController.queryWList 应用已经存在,请勿重新添加 {}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("ApplicationMntController.saveApplication 新增应用异常{}", e);
            return ResponseError.create(TROErrorEnum.CONFCENTER_ADD_APPLICATION_EXCEPTION.getErrorCode(),
                TROErrorEnum.CONFCENTER_ADD_APPLICATION_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.01.01.002 查询应用列表信息接口
     *
     * @return 成功, 则返回应用信息列表, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_APPLICATIONINFO_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryApplicationinfo(@RequestBody Map<String, Object> paramMap) {
        try {
            return ResponseOk.create(confCenterService.queryApplicationList(paramMap));
        } catch (Exception e) {
            LOGGER.error("ApplicationMntController.queryApplicationinfo 查询异常{}", e);
            return ResponseError.create(1010100102, "查询应用信息异常");
        }
    }

    /**
     * 说明: API.01.01.003 根据应用id查询应用信息详情接口
     *
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_MODIFY_APPLICATIONINFO_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryApplicationinfoById(@RequestParam("applicationId") long applicationId) {
        if (StringUtils.isEmpty(String.valueOf(applicationId))) {
            return ResponseError.create(1010100301, "参数缺失");
        }
        try {
            return ResponseOk.create(confCenterService.queryApplicationinfoById(applicationId));
        } catch (Exception e) {
            LOGGER.error("ApplicationMntController.queryApplicationinfoById 查询异常{}", e);
            return ResponseError.create(1010100302, "根据应用id查询异常");
        }
    }

    /**
     * 说明: API.01.01.004 批量删除应用信息接口
     *
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_DELETE_APPLICATIONINFO_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deleteApplicationinfoByIds(@RequestParam("applicationIds") String applicationIds) {

        try {
            String diableDeleteApplication = confCenterService.deleteApplicationinfoByIds(applicationIds);
            if (StringUtils.isNotEmpty(diableDeleteApplication)) {
                return ResponseError.create("该应用{ " + diableDeleteApplication + " }在基础链路中使用,不允许删除");
            } else {
                return ResponseOk.create("succeed");
            }
        } catch (Exception e) {
            LOGGER.error("ApplicationMntController.deleteApplicationinfoById 删除应用异常{}", e);
            return ResponseError.create(1010100102, "批量删除应用异常");
        }
    }

    /**
     * 说明: API.01.01.005 查询应用下拉框数据接口
     *
     * @return 成功, 则返回应用列表下拉框数据, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_APPLICATIONDATA_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryApplicationdata() {

        try {
            return ResponseOk.create(confCenterService.queryApplicationdata());
        } catch (Exception e) {
            LOGGER.error("ConfCenterController.queryWList 查询应用下拉框数据异常{}", e);
            return ResponseError.create(1010100102, "查询应用下拉框数据异常");
        }
    }

    /**
     * 说明: API.01.01.006 根据应用id更新应用信息
     *
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_UPDATE_APPLICATIONINFO_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateApplicationinfo(@RequestBody @Valid TApplicationMnt tApplicationMnt,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1010100601, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            confCenterService.updateApplicationinfo(tApplicationMnt);
            return ResponseOk.create("succeed");
        } catch (TROModuleException e) {
            LOGGER.error("ConfCenterController.updateApplicationinfo 创建应用脚本存放路径不存在{}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("ConfCenterController.updateApplicationinfo 根据应用id更新应用信息异常{}", e);
            return ResponseError.create(1010100602, "根据应用id更新应用信息异常");
        }
    }

    /**
     * 说明: API.01.01.007  查询应用信息列表(从pradar获取数据)
     *
     * @return 成功, 则返回应用信息列表, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @GetMapping(value = APIUrls.API_TRO_CONFCENTER_QUERY_APPNAMELIST_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryAppNameByPradar() {
        try {
            String result = "{\"success\":\"true\",\"code\":200,\"data\":null}";
            return ResponseOk.create(result);
        } catch (Exception e) {
            LOGGER.error("ApplicationMntController.queryAppNameByPradar 查询异常{}", e);
            return ResponseError.create(1010100702, "查询应用信息异常");
        }
    }

    /**
     * 更新 应用agentVersion版本
     *
     * @param appName
     * @param agentVersion
     * @return
     */
    //@GetMapping(value = APIUrls.API_TRO_UPDATE_APP_AGENT_VERSION_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //public ResponseEntity<Object> appAgentVersionUpdate(@RequestParam("appName") String appName,
    //    @RequestParam(value = "agentVersion", required = false) String agentVersion,
    //    @RequestParam(value = "pradarVersion", required = false) String pradarVersion) {
    //    try {
    //        confCenterService.updateAppAgentVersion(appName, agentVersion, pradarVersion);
    //        return ResponseOk.create("succeed");
    //    } catch (TROModuleException e) {
    //        LOGGER.error("ApplicationMntController.appAgentVersionUpdate 更新应用版本异常 {}", e);
    //        return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
    //    } catch (Exception e) {
    //        LOGGER.error("ApplicationMntController.appAgentVersionUpdate 更新应用版本异常 {}", e);
    //        return ResponseError.create(1010100102, "更新应用版本异常");
    //    }
    //}

}
