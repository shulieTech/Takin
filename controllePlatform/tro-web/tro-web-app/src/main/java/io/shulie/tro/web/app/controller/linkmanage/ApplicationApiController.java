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

package io.shulie.tro.web.app.controller.linkmanage;

import java.util.Objects;

import com.pamirs.tro.entity.domain.vo.entracemanage.ApiCreateVo;
import com.pamirs.tro.entity.domain.vo.entracemanage.ApiDeleteVo;
import com.pamirs.tro.entity.domain.vo.entracemanage.ApiUpdateVo;
import com.pamirs.tro.entity.domain.vo.entracemanage.EntranceApiVo;
import io.shulie.tro.common.beans.annotation.ModuleDef;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.service.linkManage.ApplicationApiService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.shulie.tro.web.common.vo.application.ApplicationApiManageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: vernon
 * @Date: 2020/4/2 13:09
 * @Description:
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "applicationApi", value = "应用api")
public class ApplicationApiController {

    @Autowired
    private ApplicationApiService apiService;


    @ApiOperation("storm拉取api")
    @GetMapping(value = "/api/pull")
    public Response pull(@RequestParam(value = "appName",required = false) String appName) {
        try {
            return apiService.pullApi(appName);

        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    @ApiOperation("查询")
    @GetMapping(value = "/api/get")
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.ENTRYRULE,
            needAuth = ActionTypeEnum.QUERY
    )
    public Response query(@ApiParam(name = "applicationName", value = "应用名") String applicationName,
                          @ApiParam(name = "api", value = "入口名") String api,
                          Integer current,
                          Integer pageSize) {
        EntranceApiVo vo = new EntranceApiVo();
        vo.setApplicationName(applicationName);
        vo.setApi(api);
        vo.setPageSize(pageSize);
        vo.setCurrentPage(current);
        try {
            return apiService.query(vo);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    @ApiOperation("删除")
    @DeleteMapping(value = "/api/delete")
    @ModuleDef(
            moduleName = BizOpConstants.Modules.CONFIG_CENTER,
            subModuleName = BizOpConstants.SubModules.ENTRYRULE,
            logMsgKey = BizOpConstants.Message.MESSAGE_ENTRYRULE_DELETE
    )
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.ENTRYRULE,
            needAuth = ActionTypeEnum.DELETE
    )
    public Response delete(@RequestBody ApiDeleteVo vo) {
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.DELETE);
        Response<ApplicationApiManageVO> response = apiService.queryDetail(vo.getId());
        ApplicationApiManageVO apiManageDto = response.getData();
        if (Objects.isNull(apiManageDto)) {
            return Response.fail("入口规则不存在");
        }
        OperationLogContextHolder.addVars(BizOpConstants.Vars.APPLICATION_NAME, apiManageDto.getApplicationName());
        OperationLogContextHolder.addVars(BizOpConstants.Vars.ENTRY_API, apiManageDto.getApi());
        try {
            return apiService.delete(vo.getId());
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    @ApiOperation("编辑")
    @PutMapping(value = "/api/update")
    @ModuleDef(
            moduleName = BizOpConstants.Modules.CONFIG_CENTER,
            subModuleName = BizOpConstants.SubModules.ENTRYRULE,
            logMsgKey = BizOpConstants.Message.MESSAGE_ENTRYRULE_UPDATE
    )
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.ENTRYRULE,
            needAuth = ActionTypeEnum.UPDATE
    )
    public Response update(@RequestBody ApiUpdateVo vo) {
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.UPDATE);
        try {
            return apiService.update(vo);
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate")) {
                return Response.fail("已经存在相同入口");
            }
            return Response.fail(e.getMessage());
        }
    }

    @ApiOperation("新增")
    @PostMapping(value = "/api/add")
    @ModuleDef(
            moduleName = BizOpConstants.Modules.CONFIG_CENTER,
            subModuleName = BizOpConstants.SubModules.ENTRYRULE,
            logMsgKey = BizOpConstants.Message.MESSAGE_ENTRYRULE_CREATE
    )
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.ENTRYRULE,
            needAuth = ActionTypeEnum.CREATE
    )
    public Response add(@RequestBody ApiCreateVo vo) {
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.CREATE);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.APPLICATION_NAME, vo.getApplicationName());
        OperationLogContextHolder.addVars(BizOpConstants.Vars.ENTRY_API, vo.getApi());
        // TODO: 2020/6/15
        try {
            return apiService.create(vo);
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate")) {
                return Response.fail("不能重复添加");
            }
            return Response.fail(e.getMessage());
        }
    }

    @ApiOperation("详情")
    @GetMapping(value = "/api/getDetail")
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.ENTRYRULE,
            needAuth = ActionTypeEnum.QUERY
    )
    public Response queryDetail(@RequestParam("id") String id) {
        try {
            return apiService.queryDetail(id);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }
}
