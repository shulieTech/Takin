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

package io.shulie.tro.web.app.service.agent.impl;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.google.common.collect.Lists;
import com.pamirs.tro.entity.domain.entity.linkmanage.figure.MiddleWareEnum;
import io.shulie.tro.web.app.response.application.AgentPluginSupportResponse;
import io.shulie.tro.web.app.response.linkmanage.MiddleWareResponse;
import io.shulie.tro.web.app.response.linkmanage.SupportStatusResponse;
import io.shulie.tro.web.app.service.agent.AgentPluginSupportService;
import io.shulie.tro.web.data.dao.agent.AgentPluginDAO;
import io.shulie.tro.web.data.dao.agent.AgentPluginLibSupportDAO;
import io.shulie.tro.web.data.result.agent.AgentPluginLibSupportResult;
import io.shulie.tro.web.data.result.agent.AgentPluginResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/10/13 7:38 下午
 * @Description:
 */
@Component
@Slf4j
public class AgentPluginSupportServiceImpl implements AgentPluginSupportService {

    @Resource
    private AgentPluginDAO agentPluginDAO;

    @Resource
    private AgentPluginLibSupportDAO libSupportDAO;

    @Override
    public List<AgentPluginSupportResponse> queryAgentPluginSupportList() {
        List<AgentPluginSupportResponse> pluginSupportResponseList = Lists.newArrayList();
        List<AgentPluginResult> agentPluginResultList = agentPluginDAO.getAgentPluginList();
        List<AgentPluginLibSupportResult> libSupportResultList = libSupportDAO.getAgentPluginLibSupportList();
        if (CollectionUtils.isEmpty(agentPluginResultList) || CollectionUtils.isEmpty(libSupportResultList)) {
            return pluginSupportResponseList;
        }
        for (AgentPluginLibSupportResult agentPluginLibSupportResult : libSupportResultList) {
            Optional<AgentPluginResult> optional = agentPluginResultList
                .stream()
                .filter(
                    agentPluginResult -> agentPluginResult.getId().equals(agentPluginLibSupportResult.getPluginId()))
                .findFirst();
            if (optional.isPresent()) {
                AgentPluginResult agentPluginResult = optional.get();
                AgentPluginSupportResponse supportResponse = new AgentPluginSupportResponse();
                //先封装插件基础信息
                supportResponse.setPluginId(agentPluginResult.getId());
                supportResponse.setPluginType(agentPluginResult.getPluginType());
                supportResponse.setPluginName(agentPluginResult.getPluginName());
                //再封装插件支持的lib包信息
                supportResponse.setIsIgnore(agentPluginLibSupportResult.getIsIgnore());
                supportResponse.setLibName(agentPluginLibSupportResult.getLibName());
                JSONArray regExpArray = JSON.parseArray(agentPluginLibSupportResult.getLibVersionRegexp());
                List<Pattern> patternList = regExpArray.stream().map(r -> {
                    Pattern pattern = Pattern.compile(r.toString());
                    return pattern;
                }).collect(Collectors.toList());
                supportResponse.setRegexpList(patternList);
                pluginSupportResponseList.add(supportResponse);
            } else {
                return pluginSupportResponseList;
            }
        }
        return pluginSupportResponseList;
    }

    @Override
    public Boolean isSupportLib(List<AgentPluginSupportResponse> supportList, String libName) {
        String tempLibName = libName;
        String tempLibVersion = libName;
        tempLibName = tempLibName.substring(0, tempLibName.lastIndexOf("-"));
        tempLibVersion = tempLibVersion.substring(tempLibVersion.lastIndexOf("-") + 1);
        String finalTempLibName = tempLibName;
        Optional<AgentPluginSupportResponse> optional = supportList
            .stream()
            .filter(s -> s.getLibName().equalsIgnoreCase(finalTempLibName)).collect(Collectors.toList()).stream()
            .findFirst();
        if (!optional.isPresent()) {
            return Boolean.FALSE;
        }
        AgentPluginSupportResponse agentPluginSupportResponse = optional.get();
        List<Pattern> patternListAll = Lists.newArrayList();
        patternListAll.addAll(agentPluginSupportResponse.getRegexpList());
        if (CollectionUtils.isEmpty(patternListAll)) {
            return Boolean.FALSE;
        }
        for (Pattern pattern : patternListAll) {
            Matcher matcher = pattern.matcher(tempLibVersion);
            if (matcher.matches()) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public MiddleWareResponse convertLibInfo(List<AgentPluginSupportResponse> supportList, String libName) {
        MiddleWareResponse middleWareResponse = new MiddleWareResponse();
        String tempLibName = libName;
        String tempLibVersion = libName;
        if (!libName.contains("-")) {
            middleWareResponse.setPluginType(MiddleWareEnum.getMiddleWareTypeValue(""));
            middleWareResponse.setPluginName("-");
            middleWareResponse.setLibName(libName);
            middleWareResponse.setStatusResponse(SupportStatusResponse.buildStatus(1));
            return middleWareResponse;
        }
        tempLibName = tempLibName.substring(0, tempLibName.lastIndexOf("-"));
        tempLibVersion = tempLibVersion.substring(tempLibVersion.lastIndexOf("-") + 1);
        try {
            //TODO 此处会报错，这里保留方便排查问题
            tempLibVersion = tempLibVersion.substring(0, tempLibVersion.indexOf(".jar"));
        } catch (Exception e) {
            if (tempLibVersion.contains(".jar")) {
                tempLibVersion = tempLibVersion.substring(0, tempLibVersion.indexOf(".jar"));
            }
            if (tempLibVersion.contains(".war")) {
                tempLibVersion = tempLibVersion.substring(0, tempLibVersion.indexOf(".war"));
            }
            log.error("解析应用版本号出错，数据为：{}", libName);
        }
        String finalTempLibName = tempLibName;
        Optional<AgentPluginSupportResponse> optional = supportList
            .stream()
            .filter(s -> s.getLibName().equalsIgnoreCase(finalTempLibName)).collect(Collectors.toList()).stream()
            .findFirst();
        if (!optional.isPresent()) {
            middleWareResponse.setPluginType(MiddleWareEnum.getMiddleWareTypeValue(""));
            middleWareResponse.setPluginName("-");
            middleWareResponse.setLibName(libName);
            middleWareResponse.setStatusResponse(SupportStatusResponse.buildStatus(1));
        } else {
            AgentPluginSupportResponse agentPluginSupportResponse = optional.get();
            middleWareResponse.setPluginName(agentPluginSupportResponse.getPluginName());
            String typeName = MiddleWareEnum.getMiddleWareTypeValue(agentPluginSupportResponse.getPluginType());
            middleWareResponse.setPluginType(typeName);
            middleWareResponse.setLibName(libName);
            if (agentPluginSupportResponse.getIsIgnore()) {
                middleWareResponse.setStatusResponse(SupportStatusResponse.buildStatus(3));
                return middleWareResponse;
            } else {
                List<Pattern> patternListAll = Lists.newArrayList();
                patternListAll.addAll(agentPluginSupportResponse.getRegexpList());
                if (CollectionUtils.isEmpty(patternListAll)) {
                    middleWareResponse.setStatusResponse(SupportStatusResponse.buildStatus(0));
                    return middleWareResponse;
                }
                for (Pattern pattern : patternListAll) {
                    Matcher matcher = pattern.matcher(tempLibVersion);
                    if (matcher.matches()) {
                        middleWareResponse.setStatusResponse(SupportStatusResponse.buildStatus(2));
                        return middleWareResponse;
                    }
                }
            }
            middleWareResponse.setStatusResponse(SupportStatusResponse.buildStatus(0));
        }
        return middleWareResponse;
    }
}
