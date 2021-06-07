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

import java.util.List;

import com.pamirs.tro.entity.domain.entity.LinkGuardEntity;
import com.pamirs.tro.entity.domain.query.LinkGuardQueryParam;
import com.pamirs.tro.entity.domain.vo.guardmanage.LinkGuardVo;
import io.shulie.tro.common.beans.annotation.ModuleDef;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.service.linkManage.LinkGuardService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 慕白
 * @Date: 2020-03-05 09:20
 * @Description: 挡板配置
 */

@RestController
@RequestMapping(APIUrls.TRO_API_URL + "console")
@Api(tags = "挡板配置", value = "挡板配置")
public class LinkGuardController {

    @Autowired
    private LinkGuardService linkGuardService;

    /**
     * 添加挡板
     *
     * @param vo
     * @return
     */
    @ApiOperation("挡板添加接口")
    @PostMapping("/link/guard/guardmanage")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.OUTLET_BAFFLE,
        logMsgKey = BizOpConstants.Message.MESSAGE_OUTLET_BAFFLE_CREATE
    )
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
            needAuth = ActionTypeEnum.CREATE
    )
    public Response storetechLink(@RequestBody LinkGuardVo vo) {
        // 备注字段上限
        if(StringUtils.isNotBlank(vo.getRemark()) && vo.getRemark().length() > 200) {
            throw new TroWebException(ExceptionCode.GUARD_PARAM_ERROR,"备注长度不得超过200字符");
        }
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.CREATE);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.CLASS_METHOD_NAME, vo.getMethodInfo());
        return linkGuardService.addGuard(vo);

    }

    /**
     * 查询挡板
     *
     * @param applicationName
     * @param id
     * @param current
     * @param pageSize
     * @return
     */
    @GetMapping("/link/guard/guardmanage")
    @ApiOperation("挡板列表查询接口")
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
            needAuth = ActionTypeEnum.QUERY
    )
    public Response<List<LinkGuardVo>> gettGuardList(
        @ApiParam(name = "applicationName", value = "系统名字") @RequestParam(value = "applicationName", required = false)
            String applicationName,
        @ApiParam(name = "id", value = "挡板id") @RequestParam(value = "id", required = false) Long id,
        @ApiParam(name = "applicationId", value = "系统id") @RequestParam(value = "applicationId", required = false)
            String applicationId,
        Integer current,
        Integer pageSize
    ) {
        LinkGuardQueryParam param = new LinkGuardQueryParam();
        param.setId(id);
        param.setApplicationName(applicationName);
        param.setApplicationId(applicationId);
        param.setCurrentPage(current);
        param.setPageSize(pageSize);
        return linkGuardService.selectByExample(param);
    }

    @GetMapping("/link/guard/guardmanage/all")
    @ApiOperation("查询所有开启的挡板")
    public Response<List<LinkGuardEntity>> getAllEnableGuard(
    ) {
        return linkGuardService.selectAll();
    }

    @GetMapping("/link/guard/guardmanage/info")
    @ApiOperation("挡板详情接口")
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
            needAuth = ActionTypeEnum.QUERY
    )
    public Response<LinkGuardVo> gettGuardInfo(
        @ApiParam(name = "id", value = "挡板id") @RequestParam(value = "id", required = true) Long id

    ) {
        if (id == null) {
            return Response.fail("0", "id 不能为空", null);
        }
        return linkGuardService.getById(id);
    }

    @ApiOperation("修改挡板接口")
    @PutMapping("/link/guard/guardmanage")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.OUTLET_BAFFLE,
        logMsgKey = BizOpConstants.Message.MESSAGE_OUTLET_BAFFLE_UPDATE
    )
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
            needAuth = ActionTypeEnum.UPDATE
    )
    public Response modifyGuard(@RequestBody @ApiParam(name = "vo", value = "挡板入参对象") LinkGuardVo vo) {
        // 备注字段上限
        if(StringUtils.isNotBlank(vo.getRemark()) && vo.getRemark().length() > 200) {
            throw new TroWebException(ExceptionCode.GUARD_PARAM_ERROR,"备注长度不得超过200字符");
        }
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.UPDATE);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.CLASS_METHOD_NAME, vo.getMethodInfo());
        return linkGuardService.updateGuard(vo);
    }

    @ApiOperation("删除挡板接口")
    @RequestMapping(value = "/link/guard/guardmanage", method = RequestMethod.DELETE)
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.OUTLET_BAFFLE,
        logMsgKey = BizOpConstants.Message.MESSAGE_OUTLET_BAFFLE_DELETE
    )
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
            needAuth = ActionTypeEnum.DELETE
    )
    public Response deleteGuard(@RequestBody LinkGuardVo vo) {
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.DELETE);

        LinkGuardVo linkGuardVo = linkGuardVo(vo.getId());
        if (null == linkGuardVo) {
            return Response.fail("该挡板不存在");
        }
        OperationLogContextHolder.addVars(BizOpConstants.Vars.CLASS_METHOD_NAME, linkGuardVo.getMethodInfo());
        return linkGuardService.deleteById(vo.getId());
    }

    @ApiOperation("开启、关闭挡板接口")
    @PutMapping(value = "/link/guard/guardmanage/switch")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.OUTLET_BAFFLE,
        logMsgKey = BizOpConstants.Message.MESSAGE_OUTLET_BAFFLE_ENABLE_DISABLE
    )
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
            needAuth = ActionTypeEnum.ENABLE_DISABLE
    )
    public Response guard(@RequestBody LinkGuardVo vo) {
        OperationLogContextHolder.operationType(
            vo.getIsEnable() ? BizOpConstants.OpTypes.ENABLE : BizOpConstants.OpTypes.DISABLE);
        LinkGuardVo linkGuardVo = linkGuardVo(vo.getId());
        if (null == linkGuardVo) {
            return Response.fail("该挡板不存在");
        }
        OperationLogContextHolder.addVars(BizOpConstants.Vars.CLASS_METHOD_NAME, linkGuardVo.getMethodInfo());
        return linkGuardService.enableGuard(vo.getId(), vo.getIsEnable());
    }

    private LinkGuardVo linkGuardVo(Long id) {
        Response<LinkGuardVo> linkGuardVo = linkGuardService.getById(id);
        if (null == linkGuardVo) {
            return null;
        }
        LinkGuardVo data = linkGuardVo.getData();
        return data;
    }

}
