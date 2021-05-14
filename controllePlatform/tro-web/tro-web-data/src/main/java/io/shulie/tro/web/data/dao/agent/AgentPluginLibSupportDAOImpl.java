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
import io.shulie.tro.web.data.mapper.mysql.AgentPluginLibSupportMapper;
import io.shulie.tro.web.data.model.mysql.AgentPluginLibSupportEntity;
import io.shulie.tro.web.data.result.agent.AgentPluginLibSupportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @Author: fanxx
 * @Date: 2020/10/13 11:00 上午
 * @Description:
 */
@Component
public class AgentPluginLibSupportDAOImpl implements AgentPluginLibSupportDAO {

    @Autowired
    private AgentPluginLibSupportMapper agentPluginLibSupportMapper;

    @Override
    public List<AgentPluginLibSupportResult> getAgentPluginLibSupportList() {
        List<AgentPluginLibSupportResult> agentPluginLibSupportResultList = Lists.newArrayList();
        LambdaQueryWrapper<AgentPluginLibSupportEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                AgentPluginLibSupportEntity::getId,
                AgentPluginLibSupportEntity::getPluginId,
                AgentPluginLibSupportEntity::getLibName,
                AgentPluginLibSupportEntity::getLibVersionRegexp,
                AgentPluginLibSupportEntity::getIsIgnore
        );
        List<AgentPluginLibSupportEntity> agentPluginLibSupportEntityList = agentPluginLibSupportMapper
            .selectList(wrapper);
        if (CollectionUtils.isEmpty(agentPluginLibSupportEntityList)) {
            return agentPluginLibSupportResultList;
        }
        agentPluginLibSupportResultList = agentPluginLibSupportEntityList.stream().map(
            agentPluginLibSupportEntity -> {
                AgentPluginLibSupportResult agentPluginLibSupportResult = new AgentPluginLibSupportResult();
                agentPluginLibSupportResult.setId(agentPluginLibSupportEntity.getId());
                agentPluginLibSupportResult.setPluginId(agentPluginLibSupportEntity.getPluginId());
                agentPluginLibSupportResult.setLibName(agentPluginLibSupportEntity.getLibName());
                agentPluginLibSupportResult.setLibVersionRegexp(agentPluginLibSupportEntity.getLibVersionRegexp());
                agentPluginLibSupportResult.setIsIgnore(agentPluginLibSupportEntity.getIsIgnore());
                return agentPluginLibSupportResult;
            }).collect(Collectors.toList());
        return agentPluginLibSupportResultList;
    }
}
