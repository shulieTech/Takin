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

package io.shulie.tro.web.app.cache;

import java.util.List;

import com.pamirs.tro.entity.domain.dto.ApplicationSwitchStatusDTO;
import com.pamirs.tro.entity.domain.entity.simplify.TShadowJobConfig;
import com.pamirs.tro.entity.domain.vo.dsmanage.DsAgentVO;
import com.pamirs.tro.entity.domain.vo.dsmanage.DsServerVO;
import com.pamirs.tro.entity.domain.vo.guardmanage.LinkGuardVo;
import io.shulie.tro.web.app.agent.vo.ShadowConsumerVO;
import io.shulie.tro.web.app.cache.agentimpl.AllowListSwitchConfigAgentCache;
import io.shulie.tro.web.app.cache.agentimpl.GuardConfigAgentCache;
import io.shulie.tro.web.app.cache.agentimpl.PressureSwitchConfigAgentCache;
import io.shulie.tro.web.app.cache.agentimpl.ShadowConsumerConfigAgentCache;
import io.shulie.tro.web.app.cache.agentimpl.ShadowDbConfigAgentCache;
import io.shulie.tro.web.app.cache.agentimpl.ShadowEsServerConfigAgentCache;
import io.shulie.tro.web.app.cache.agentimpl.ShadowHbaseConfigAgentCache;
import io.shulie.tro.web.app.cache.agentimpl.ShadowJobConfigAgentCache;
import io.shulie.tro.web.app.cache.agentimpl.ShadowKafkaClusterConfigAgentCache;
import io.shulie.tro.web.app.cache.agentimpl.ShadowServerConfigAgentCache;
import io.shulie.tro.web.app.response.application.ShadowServerConfigurationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-12-17
 */
@Component
public class AgentConfigCacheManager {

    @Autowired
    private AllowListSwitchConfigAgentCache allowListSwitchConfigCache;

    @Autowired
    private ShadowServerConfigAgentCache shadowServerConfigCache;

    @Autowired
    private ShadowDbConfigAgentCache shadowDbConfigCache;

    @Autowired
    private ShadowJobConfigAgentCache shadowJobConfigCache;

    @Autowired
    private GuardConfigAgentCache guardConfigCache;

    @Autowired
    private PressureSwitchConfigAgentCache pressureSwitchConfigCache;

    @Autowired
    private ShadowConsumerConfigAgentCache shadowConsumerConfigAgentCache;


    @Autowired
    private ShadowEsServerConfigAgentCache shadowEsServerConfigAgentCache;

    @Autowired
    private ShadowKafkaClusterConfigAgentCache shadowKafkaClusterConfigAgentCache;



    @Autowired
    private ShadowHbaseConfigAgentCache shadowHbaseConfigAgentCache;

    /**
     * 获得白名单开关的缓存结果
     */
    public boolean getAllowListSwitch() {
        return allowListSwitchConfigCache.get(null);
    }

    /**
     * 开关缓存清空，下次查询时候重新加载
     */
    public void evictAllowListSwitch() {
        allowListSwitchConfigCache.evict(null);
    }

    public List<TShadowJobConfig> getShadowJobs(String appName) {
        return shadowJobConfigCache.get(appName);
    }

    public void evictShadowJobs(String appName) {
        shadowJobConfigCache.evict(appName);
    }

    public List<ShadowServerConfigurationResponse> getShadowServer(String appName) {
        return shadowServerConfigCache.get(appName);
    }

    public void evictShadowServer(String appName) {
        shadowServerConfigCache.evict(appName);
    }

    public List<DsAgentVO> getShadowDb(String appName) {
        return shadowDbConfigCache.get(appName);
    }

    public void evictShadowDb(String appName) {
        shadowDbConfigCache.evict(appName);
    }

    public List<LinkGuardVo> getGuards(String appName) {
        return guardConfigCache.get(appName);
    }

    public void evictGuards(String appName) {
        guardConfigCache.evict(appName);
    }

    public ApplicationSwitchStatusDTO getPressureSwitch() {
        return pressureSwitchConfigCache.get(null);
    }

    public void evictPressureSwitch() {
        pressureSwitchConfigCache.evict(null);
    }

    public List<ShadowConsumerVO> getShadowConsumer(String appName) {
        return shadowConsumerConfigAgentCache.get(appName);
    }

    public void evictShadowConsumer(String appName) {
        shadowConsumerConfigAgentCache.evict(appName);
    }

    /**
     * 获取影子消费者配置业务逻辑
     * @param appName 应用名称
     * @return
     */
    public List<DsServerVO> getShadowEsServers(String appName) {
        return shadowEsServerConfigAgentCache.get(appName);
    }

    /**
     * 清空缓存，从新加载
     * @param appName 应用名称
     */
    public void evictShadowEsServers(String appName) {
        shadowEsServerConfigAgentCache.evict(appName);
    }

    /**
     * 清空缓存，从新加载
     * @param appName 应用名称
     */
    public void evictShadowKafkaCluster(String appName) {
        shadowKafkaClusterConfigAgentCache.evict(appName);
    }

    /**
     * 获取kafka影子配置
     * @param appName
     * @return
     */
    public List<DsServerVO> getShadowKafkaCluster(String appName) {
        return shadowKafkaClusterConfigAgentCache.get(appName);
    }
    /**
     * 清空影子消费者配置业务逻辑
     * @param applicationName 应用名称
     * @return
     */
    public void evictShadowHbase(String applicationName){
        shadowHbaseConfigAgentCache.evict(applicationName);
    }

    /**
     * 获取影子消费者配置业务逻辑
     * @param applicationName 应用名称
     * @return
     */
    public List<DsServerVO> getShadowHbase(String applicationName){
        return shadowHbaseConfigAgentCache.get(applicationName);
    }


}

