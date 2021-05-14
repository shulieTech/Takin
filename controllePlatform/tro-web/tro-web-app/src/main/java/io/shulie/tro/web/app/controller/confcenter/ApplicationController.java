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

import com.github.pagehelper.util.StringUtil;
import com.pamirs.tro.entity.domain.query.ApplicationQueryParam;
import com.pamirs.tro.entity.domain.vo.ApplicationVo;
import io.shulie.tro.common.beans.annotation.ModuleDef;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.service.ApplicationService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: mubai<chengjiacai @ shulie.io>
 * @author: liuchuan
 *
 * @Date: 2020-03-16 14:55
 * @Description:
 */

@RestController("confApplicationController")
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "接口: 应用管理中心", value = "应用管理中心")
public class ApplicationController {

    private static String FALSE_CODE = "0";
    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/application/center/list")
    @ApiOperation("应用列表查询接口")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.QUERY
    )
    public Response<List<ApplicationVo>> getApplicationListWithAuth(
        @ApiParam(name = "applicationName", value = "系统名字") String applicationName,
        Integer current,
        Integer pageSize,
        @ApiParam(name = "accessStatus", value = "接入状态") Integer accessStatus
    ) {
        current = current + 1;
        ApplicationQueryParam param = new ApplicationQueryParam();
        param.setCurrentPage(current);
        param.setPageSize(pageSize);
        param.setApplicationName(applicationName);
        return applicationService.getApplicationList(param, accessStatus);
    }

    @GetMapping("/application/center/list/dictionary")
    @ApiOperation("应用列表查询接口")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.QUERY
    )
    public Response<List<ApplicationVo>> getApplicationListNoAuth(
    ) {
        return applicationService.getApplicationList();
    }

    @GetMapping("/console/application/center/app/info")
    @ApiOperation("应用详情查询接口")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.QUERY
    )
    public Response<ApplicationVo> getApplicationInfoWithAuth(
        @ApiParam(name = "id", value = "系统id") String id
    ) {
        return applicationService.getApplicationInfo(id);
    }

    @GetMapping("/application/center/app/info/dictionary")
    @ApiOperation("应用详情查询接口")
    public Response<ApplicationVo> getApplicationInfoNoAuth(
        @ApiParam(name = "id", value = "系统id") String id
    ) {
        return applicationService.getApplicationInfo(id);
    }

    @PostMapping("/console/application/center/app/info")
    @ApiOperation("新增应用接口")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.BASIC_INFO,
        logMsgKey = BizOpConstants.Message.MESSAGE_BASIC_INFO_CREATE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.CREATE
    )
    public Response addApplication(@RequestBody ApplicationVo vo) {
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.CREATE);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.APPLICATION, vo.getApplicationName());
        return applicationService.addApplication(vo);
    }

    @PutMapping("/console/application/center/app/info")
    @ApiOperation("编辑应用")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.BASIC_INFO,
        logMsgKey = BizOpConstants.Message.MESSAGE_BASIC_INFO_UPDATE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.UPDATE
    )
    public Response modifyApplication(@RequestBody ApplicationVo vo) {
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.UPDATE);
        return applicationService.modifyApplication(vo);
    }

    @ApiOperation("删除应用接口")
    @DeleteMapping("/console/application/center/app/info")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.BASIC_INFO,
        logMsgKey = BizOpConstants.Message.MESSAGE_BASIC_INFO_DELETE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.DELETE
    )
    public Response deleteApplication(
        @RequestBody ApplicationVo vo) {
        if (vo == null || StringUtil.isEmpty(vo.getId())) {
            return Response.fail(FALSE_CODE, "id 不能为空");
        }
        Response<ApplicationVo> applicationVo = applicationService.getApplicationInfo(vo.getId());
        if (null == applicationVo.getData().getId()) {
            return Response.fail("该应用不存在");
        }
        ApplicationVo data = applicationVo.getData();
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.DELETE);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.APPLICATION, data.getApplicationName());
        return applicationService.deleteApplication(vo.getId());
    }

    @ApiOperation("压测全局开关接口")
    @PutMapping("/application/center/app/switch")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.CONFIG_CENTER,
        subModuleName = BizOpConstants.SubModules.PRESSURE_TEST_SWITCH,
        logMsgKey = BizOpConstants.Message.MESSAGE_PRESSURE_TEST_SWITCH_ACTION
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.PRESSURE_TEST_SWITCH,
        needAuth = ActionTypeEnum.ENABLE_DISABLE
    )
    public Response AppSwitch(
        @RequestBody ApplicationVo vo) {
        if (vo == null || vo.getPressureEnable() == null) {
            return Response.fail(FALSE_CODE, "pressureEnable 不能为空");
        }
        OperationLogContextHolder.operationType(
            vo.getPressureEnable() ? BizOpConstants.OpTypes.OPEN : BizOpConstants.OpTypes.CLOSE);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.ACTION,
            vo.getPressureEnable() ? BizOpConstants.OpTypes.OPEN : BizOpConstants.OpTypes.CLOSE);
        return applicationService.userAppPressureSwitch(null, vo.getPressureEnable());
    }

    @ApiOperation("压测开关状态重新计算接口")
    @GetMapping("/application/center/app/switch/calculate")
    public Response AppSwitchForce(@RequestParam(value = "uid", required = false) Long uid) {
        return applicationService.calculateUserSwitch(uid);
    }

    @ApiOperation("获取应用压测开关状态接口")
    @GetMapping("/application/center/app/switch")
    public Response AppSwitchInfo() {
        return applicationService.userAppSwitchInfo();
    }

    @ApiOperation("获取下载导出配置地址")
    @GetMapping("/application/center/app/config/url")
    public Response getExportDownloadConfigUrl(@ApiParam(name = "id", value = "系统id") @NotNull String id,
        HttpServletRequest request) {
        return applicationService.buildExportDownLoadConfigUrl(id, request);
    }

    @ApiOperation("|_ 应用配置导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "应用id",
                    paramType = "query", dataType = "int")
    })
    @GetMapping("/application/center/app/config/export")
    public void export(@RequestParam("id") Long applicationId, HttpServletResponse response) {
        applicationService.exportApplicationConfig(response, applicationId);
    }

    @ApiOperation("|_ 应用配置导入")
    @PostMapping("/application/center/app/config/import")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "应用id",
                    paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "file", value = "导入的 excel",
                    paramType = "form", dataType = "file")
    })
    public Response importApplicationConfig(@RequestParam MultipartFile file, @RequestParam Long id) {
        return applicationService.importApplicationConfig(file, id);
    }

    @ApiOperation("应用数据源配置是否为新版本")
    @GetMapping("/application/center/app/config/ds/isnew")
    public Response<Boolean> appDsConfigIsNewVersion(){
        return applicationService.appDsConfigIsNewVersion();
    }

}
