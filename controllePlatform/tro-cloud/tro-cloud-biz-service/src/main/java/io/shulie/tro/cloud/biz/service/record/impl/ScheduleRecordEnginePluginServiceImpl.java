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

package io.shulie.tro.cloud.biz.service.record.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import io.shulie.tro.cloud.biz.service.record.ScheduleRecordEnginePluginService;
import io.shulie.tro.cloud.data.mapper.mysql.ScheduleRecordEnginePluginMapper;
import io.shulie.tro.cloud.data.model.mysql.ScheduleRecordEnginePluginRefEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 调度记录引擎插件实现
 *
 * @author lipeng
 * @date 2021-01-13 11:31 上午
 */
@Slf4j
@Service
public class ScheduleRecordEnginePluginServiceImpl extends ServiceImpl<ScheduleRecordEnginePluginMapper, ScheduleRecordEnginePluginRefEntity> implements ScheduleRecordEnginePluginService {
    /**
     * 保存调度记录引擎插件信息
     *
     * @param recordId
     * @param enginePluginFilePath
     */
    @Override
    public void saveScheduleRecordEnginePlugins(Long recordId, List<String> enginePluginFilePath) {
        if(recordId == null) {
            log.warn("recordId不能为空。");
            return;
        }

        //没有额外引擎插件加载
        if(enginePluginFilePath == null || enginePluginFilePath.size() == 0) {
            return;
        }

        //组装数据保存
        List<ScheduleRecordEnginePluginRefEntity> infos = Lists.newArrayList();
        enginePluginFilePath.forEach(item -> {
            ScheduleRecordEnginePluginRefEntity info = new ScheduleRecordEnginePluginRefEntity();
            info.setScheduleRecordId(recordId);
            info.setEnginePluginFilePath(item);
            infos.add(info);
        });
        this.saveBatch(infos);

    }
}
