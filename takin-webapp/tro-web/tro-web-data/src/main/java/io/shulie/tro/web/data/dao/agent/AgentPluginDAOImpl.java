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

package io.shulie.tro.web.data.dao.agent;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.web.data.mapper.mysql.AgentPluginMapper;
import io.shulie.tro.web.data.model.mysql.AgentPluginEntity;
import io.shulie.tro.web.data.result.agent.AgentPluginResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/10/13 10:59 上午
 * @Description:
 */
@Component
public class AgentPluginDAOImpl implements AgentPluginDAO {

    @Autowired
    private AgentPluginMapper agentPluginMapper;

    @Override
    public List<AgentPluginResult> getAgentPluginList() {
        List<AgentPluginResult> agentPluginResultList = Lists.newArrayList();
        LambdaQueryWrapper<AgentPluginEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                AgentPluginEntity::getId,
                AgentPluginEntity::getPluginType,
                AgentPluginEntity::getPluginName
        );
        List<AgentPluginEntity> agentPluginEntityList = agentPluginMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(agentPluginEntityList)) {
            return agentPluginResultList;
        }
        agentPluginResultList = agentPluginEntityList.stream().map(agentPluginEntity -> {
            AgentPluginResult agentPluginResult = new AgentPluginResult();
            agentPluginResult.setId(agentPluginEntity.getId());
            agentPluginResult.setPluginType(agentPluginEntity.getPluginType());
            agentPluginResult.setPluginName(agentPluginEntity.getPluginName());
            return agentPluginResult;
        }).collect(Collectors.toList());
        return agentPluginResultList;
    }
}
