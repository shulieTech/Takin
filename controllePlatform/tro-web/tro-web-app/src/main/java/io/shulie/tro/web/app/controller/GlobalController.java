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

package io.shulie.tro.web.app.controller;

import com.pamirs.tro.common.constant.ConfigConstants;
import com.pamirs.tro.entity.domain.dto.config.WhiteListSwitchDTO;
import io.shulie.tro.common.beans.annotation.ModuleDef;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.cache.AgentConfigCacheManager;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.service.config.ConfigService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "控制台白名单配置")
@Slf4j
public class GlobalController {

    @Autowired
    private ConfigService configService;
    @Autowired
    private AgentConfigCacheManager agentConfigCacheManager;

    @GetMapping("/console/switch/whitelist")
    @ApiOperation(value = "查看全局白名单开关")
    public Response<WhiteListSwitchDTO> getWhiteListSwitch() {
        WhiteListSwitchDTO switchDTO = new WhiteListSwitchDTO();
        switchDTO.setConfigCode(ConfigConstants.WHITE_LIST_SWITCH);
        String key = RestContext.getUser().getCustomerKey();
        switchDTO.setSwitchFlagFix(configService.getAllowListSwitch(key));
        return Response.success(switchDTO);
    }

    @PutMapping("/console/switch/whitelist/open")
    @ApiOperation(value = "打开全局白名单开关")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.CONFIG_CENTER,
        subModuleName = BizOpConstants.SubModules.PRESSURE_WHITELIST_SWITCH,
        logMsgKey = BizOpConstants.Message.MESSAGE_WHITELIST_SWITCH_ACTION
    )
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.PRESSURE_WHITELIST_SWITCH,
            needAuth = ActionTypeEnum.ENABLE_DISABLE
    )
    public Response openWhiteListSwitch() {
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.OPEN);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.ACTION, BizOpConstants.OpTypes.OPEN);
        configService.updateAllowListSwitch(RestContext.getUser().getKey(), true);
        agentConfigCacheManager.evictAllowListSwitch();
        return Response.success();
    }

    @PutMapping("/console/switch/whitelist/close")
    @ApiOperation(value = "关闭全局白名单开关")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.CONFIG_CENTER,
        subModuleName = BizOpConstants.SubModules.PRESSURE_WHITELIST_SWITCH,
        logMsgKey = BizOpConstants.Message.MESSAGE_WHITELIST_SWITCH_ACTION
    )
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.PRESSURE_WHITELIST_SWITCH,
            needAuth = ActionTypeEnum.ENABLE_DISABLE
    )
    public Response closeWhiteListSwitch() {
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.CLOSE);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.ACTION, BizOpConstants.OpTypes.CLOSE);
        configService.updateAllowListSwitch(RestContext.getUser().getKey(), false);
        agentConfigCacheManager.evictAllowListSwitch();
        return Response.success();
    }

}
