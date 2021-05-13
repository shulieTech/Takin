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

package io.shulie.tro.web.app.controller.agent;

import java.util.List;

import com.pamirs.tro.common.constant.ConfigConstants;
import com.pamirs.tro.entity.domain.dto.ApplicationSwitchStatusDTO;
import com.pamirs.tro.entity.domain.dto.config.WhiteListSwitchDTO;
import com.pamirs.tro.entity.domain.entity.simplify.TShadowJobConfig;
import com.pamirs.tro.entity.domain.vo.dsmanage.DsAgentVO;
import com.pamirs.tro.entity.domain.vo.dsmanage.DsServerVO;
import com.pamirs.tro.entity.domain.vo.guardmanage.LinkGuardVo;
import io.shulie.tro.web.app.agent.vo.ShadowConsumerVO;
import io.shulie.tro.web.app.cache.AgentConfigCacheManager;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.AgentUrls;
import io.shulie.tro.web.app.response.application.ShadowServerConfigurationResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AgentUrls.PREFIX_URL)
@Api(tags = "Agent应用配置")
@Slf4j
public class AgentPullController {

    @Autowired
    private AgentConfigCacheManager agentConfigCacheManager;

    @ApiOperation("拉取影子库表配置")
    @GetMapping(value = AgentUrls.SHADOW_DB_TABLE_URL)
    public Response<List<DsAgentVO>> getConfigs(@RequestParam("appName") String appName) {
        return Response.success(agentConfigCacheManager.getShadowDb(appName));
    }

    @ApiOperation("拉取影子消费者配置")
    @GetMapping(value = AgentUrls.SHADOW_SHADOW_CONSUMER_URL)
    public Response<List<ShadowConsumerVO>> getShadowConsumer(@RequestParam("appName") String appName) {
        return Response.success(agentConfigCacheManager.getShadowConsumer(appName));
    }

    @ApiOperation("拉取Es影子索引配置")
    @GetMapping(value = AgentUrls.SHADOW_ES_SERVER_URL)
    public Response<List<DsServerVO>> getShadowEsServer(@RequestParam("appName") String appName) {
        return Response.success(agentConfigCacheManager.getShadowEsServers(appName));
    }

    @ApiOperation("拉取kafka影子索引配置")
    @GetMapping(value = AgentUrls.SHADOW_KAFKA_CLUSTER_URL)
    public Response<List<DsServerVO>> getShadowKafkaCluster(@RequestParam("appName") String appName) {
        return Response.success(agentConfigCacheManager.getShadowKafkaCluster(appName));
    }

    @ApiOperation("拉取Hbase影子配置")
    @GetMapping(value = AgentUrls.SHADOW_HBASE_SERVER_URL)
    public Response<List<DsServerVO>> getShadowHbaseServer(@RequestParam("appName") String appName) {
        return Response.success(agentConfigCacheManager.getShadowHbase(appName));
    }

    @ApiOperation(value = "拉取Job配置")
    @GetMapping(value = AgentUrls.TRO_SHADOW_JOB_URL, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response<List<TShadowJobConfig>> queryByAppName(
        @RequestParam(value = "appName", defaultValue = "") String appName) {
        return Response.success(agentConfigCacheManager.getShadowJobs(appName));
    }

    @ApiOperation("拉取影子库server配置")
    @GetMapping(value = AgentUrls.SHADOW_SERVER_URL)
    public Response<List<ShadowServerConfigurationResponse>> getShadowServerConfigs(
        @RequestParam("appName") String appName) {
        return Response.success(agentConfigCacheManager.getShadowServer(appName));
    }

    @GetMapping(value = AgentUrls.GUARD_URL)
    @ApiOperation("挡板列表查询接口")
    public Response<List<LinkGuardVo>> getGuardList(
        @ApiParam(name = "applicationName", value = "系统名字")
        @RequestParam(value = "applicationName") String applicationName) {
        return Response.success(agentConfigCacheManager.getGuards(applicationName));
    }

    @GetMapping(value = AgentUrls.APP_WHITE_LIST_SWITCH_STATUS)
    @ApiOperation(value = "查看全局白名单开关")
    public Response<WhiteListSwitchDTO> getWhiteListSwitch() {
        WhiteListSwitchDTO switchDTO = new WhiteListSwitchDTO();
        switchDTO.setConfigCode(ConfigConstants.WHITE_LIST_SWITCH);
        switchDTO.setSwitchFlagFix(agentConfigCacheManager.getAllowListSwitch());
        return Response.success(switchDTO);
    }

    @ApiOperation("获取应用压测开关状态接口")
    @GetMapping(value = AgentUrls.APP_PRESSURE_SWITCH_STATUS)
    public Response<ApplicationSwitchStatusDTO> AgentAppSwitchInfo() {
        return Response.success(agentConfigCacheManager.getPressureSwitch());
    }

}
