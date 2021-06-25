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

package io.shulie.tro.cloud.biz.service.engine.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.shulie.tro.cloud.biz.service.engine.EnginePluginSupportedService;
import io.shulie.tro.cloud.data.mapper.mysql.EnginePluginSupportedMapper;
import io.shulie.tro.cloud.data.model.mysql.EnginePluginFilesRef;
import io.shulie.tro.cloud.data.model.mysql.EnginePluginSupportedVersionEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 引擎插件支持接口实现
 *
 * @author lipeng
 * @date 2021-01-12 4:42 下午
 */
@Slf4j
@Service
public class EnginePluginSupportedServiceImpl extends ServiceImpl<EnginePluginSupportedMapper, EnginePluginSupportedVersionEntity> implements EnginePluginSupportedService {

    @Resource
    private EnginePluginSupportedMapper enginePluginSupportedMapper;

    /**
     * 根据插件id移除支持的版本
     *
     * @param pluginId 插件id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSupportedVersionsByPluginId(Long pluginId) {
        enginePluginSupportedMapper.deleteSupportedVersionsByPluginId(pluginId);
    }

    /**
     * 批量保存支持的插件版本
     *
     * @param supportedVersions 支持的插件版本信息
     * @param pluginId          插件id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveSupportedVersions(List<String> supportedVersions, Long pluginId) {
        if(Objects.isNull(pluginId)) {
            log.warn("pluginId is null");
            return;
        }
        if(CollectionUtils.isNotEmpty(supportedVersions)) {
            List<EnginePluginSupportedVersionEntity> supportedInfos = Lists.newArrayList();
            supportedVersions.forEach(item -> {
                EnginePluginSupportedVersionEntity info = new EnginePluginSupportedVersionEntity();
                info.setPluginId(pluginId);
                info.setSupportedVersion(item);
                supportedInfos.add(info);
            });
            this.saveBatch(supportedInfos);
        }
    }

    /**
     * 根据插件id获取支持的版本号
     *
     * @param pluginId
     * @return
     */
    @Override
    public List<String> findSupportedVersionsByPluginId(Long pluginId) {
        if(Objects.isNull(pluginId)) {
            log.warn("pluginId is empty");
            return Lists.newArrayList();
        }
        QueryWrapper<EnginePluginSupportedVersionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plugin_id", pluginId);
        List<EnginePluginSupportedVersionEntity> infos = enginePluginSupportedMapper.selectList(queryWrapper);
        return infos.size() > 0 ? infos.stream()
                .map(EnginePluginSupportedVersionEntity::getSupportedVersion)
                .collect(Collectors.toList()) : Lists.newArrayList();
    }

}
