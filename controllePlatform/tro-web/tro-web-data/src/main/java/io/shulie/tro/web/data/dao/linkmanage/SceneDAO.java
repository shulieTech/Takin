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

package io.shulie.tro.web.data.dao.linkmanage;

import java.util.List;

import io.shulie.tro.web.data.param.linkmanage.SceneCreateParam;
import io.shulie.tro.web.data.param.linkmanage.SceneQueryParam;
import io.shulie.tro.web.data.param.linkmanage.SceneUpdateParam;
import io.shulie.tro.web.data.result.linkmange.SceneResult;

/**
 * @Author: fanxx
 * @Date: 2020/11/4 2:56 下午
 * @Description:
 */
public interface SceneDAO {
    int insert(SceneCreateParam param);

    /**
     * 指定责任人-业务流程
     *
     * @param updateParam
     * @return
     */
    int allocationUser(SceneUpdateParam updateParam);

    List<SceneResult> selectList(SceneQueryParam queryParam);
}
