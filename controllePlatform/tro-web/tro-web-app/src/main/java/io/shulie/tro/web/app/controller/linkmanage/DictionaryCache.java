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
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.entity.dao.dict.TDictionaryDataMapper;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.EnumResult;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.HttpTypeEnumMapping;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.LinkChangeEnumMapping;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.LinkChangeTypeEnumMapping;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.LinkLevelEnumMapping;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.LinkTypeEnumMapping;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.fastdebug.DebugHttpTypeEnumMapping;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.fastdebug.DebugRequestTypeEnumMapping;
import com.pamirs.tro.entity.domain.vo.TDictionaryVo;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.data.dao.user.TroResourceDAO;
import io.shulie.tro.web.data.result.user.ResourceMenuResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: vernon
 * @Date: 2019/12/2 16:51
 * @Description: 常量字典枚举类
 */
@Component
public class DictionaryCache {

    private static Map<String, List<EnumResult>> dicMap = Maps.newHashMap();
    @Resource
    private TDictionaryDataMapper tDictionaryDataMapper;

    @Autowired
    private TroResourceDAO troResourceDAO;

    public static EnumResult getObjectByParam(String key, Integer valueCode) {
        return getObjectByParam(key, String.valueOf(valueCode));
    }

    public static EnumResult getObjectByParamByLabel(String key, String label) {
        if (key == null || label == null) {
            return null;
        }
        List<EnumResult> dataList = dicMap.get(key);
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        return dataList.stream().filter(data -> data.getLabel().equals(label)).findFirst().orElse(
            null);

    }

    public static EnumResult getObjectByParam(String key, String valueCode) {
        if (key == null || valueCode == null) {
            return null;
        }
        List<EnumResult> dataList = dicMap.get(key);
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        EnumResult result = dataList.stream().filter(data -> data.getValue().equals(valueCode)).findFirst().orElse(
            null);
        if (result == null) {
        }
        return result;
    }

    @PostConstruct
    public void initDictionary() {
        dicMap.put("link_level", LinkLevelEnumMapping.neededEnumResults());
        dicMap.put("isCore", LinkTypeEnumMapping.neededEnumResults());
        dicMap.put("isChange", LinkChangeEnumMapping.neededEnumResults());
        dicMap.put("changeType", LinkChangeTypeEnumMapping.neededEnumResults());
        dicMap.put("http_type", HttpTypeEnumMapping.neededEnumResults());
        dicMap.put("DEBUG_REQUEST_TYPE", DebugHttpTypeEnumMapping.neededEnumResults());
        dicMap.put("DEBUG_HTTP_TYPE", DebugRequestTypeEnumMapping.neededEnumResults());
        //数据字段
        fillDictFromDB();
    }

    @PostConstruct
    public void initMenu() {
        List<ResourceMenuResult> resourceMenuResultList = troResourceDAO.selectAuthConfigMenu();
        if (CollectionUtils.isNotEmpty(resourceMenuResultList)) {
            resourceMenuResultList.forEach(menuResult -> {
                BizOpConstants.modelNameMap.put(menuResult.getCode(), menuResult.getName());
            });
        }
    }

    private void fillDictFromDB() {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("valueActive", "Y");
        List<TDictionaryVo> voList = tDictionaryDataMapper.queryDictionaryList(paramMap);
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }
        //分组
        Map<String, List<TDictionaryVo>> groupMap = voList.stream().collect(
            Collectors.groupingBy(TDictionaryVo::getTypeAlias));
        //组合数据
        groupMap.forEach((key, value) -> {
            List<EnumResult> resultList = Lists.newArrayList();
            value.forEach(data -> {
                EnumResult result = new EnumResult();
                try {
                    result.setNum(Integer.parseInt(data.getValueOrder()));
                } catch (Exception e) {
                }
                result.setLabel(data.getValueName());
                result.setValue(data.getValueCode());
                result.setDisable("N".equalsIgnoreCase(data.getValueActive()));
                resultList.add(result);
            });
            dicMap.put(key, resultList);
        });
    }

    public Map<String, List<EnumResult>> getDicMap() {
        return dicMap;
    }
}
