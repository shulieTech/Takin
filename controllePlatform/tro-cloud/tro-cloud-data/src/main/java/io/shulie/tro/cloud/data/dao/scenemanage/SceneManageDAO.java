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

package io.shulie.tro.cloud.data.dao.scenemanage;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import io.shulie.tro.cloud.data.model.mysql.SceneManageEntity;
import io.shulie.tro.cloud.data.result.scenemanage.SceneManageListFromUpdateScriptResult;
import io.shulie.tro.cloud.data.result.scenemanage.SceneManageListResult;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.data.dao.scenemanage
 * @date 2020/10/26 4:40 下午
 */
public interface SceneManageDAO extends IService<SceneManageEntity> {

    /**
     * 根据名称查询压测场景
     *
     * @param pressureTestSceneName 压测场景名称
     * @return 列表结果
     */
    SceneManageListResult queryBySceneName(String pressureTestSceneName);

    /**
     * 查询租户id下的所有压测场景
     * 注解, 自动执行数据隔离
     *
     * @return 场景列表
     */
    List<SceneManageListFromUpdateScriptResult> listFromUpdateScript();

}
