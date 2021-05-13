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

package io.shulie.tro.web.data.dao.application;

import com.baomidou.mybatisplus.extension.service.IService;
import io.shulie.tro.web.data.model.mysql.ShadowJobConfigEntity;

import java.util.List;

/**
 * 影子任务配置 dao 层
 *
 * @author liuchuan
 * @date 2021/4/8 10:50 上午
 */
public interface ShadowJobConfigDAO extends IService<ShadowJobConfigEntity> {

    /**
     * 通过应用id, 获得影子任务
     *
     * @param applicationId 应用id
     * @return 任务列表
     */
    List<ShadowJobConfigEntity> listByApplicationId(Long applicationId);

}
