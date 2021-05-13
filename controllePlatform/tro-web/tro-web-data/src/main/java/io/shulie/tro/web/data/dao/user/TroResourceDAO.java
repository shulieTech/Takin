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

package io.shulie.tro.web.data.dao.user;

import java.util.List;

import io.shulie.tro.web.data.param.user.ResourceMenuQueryParam;
import io.shulie.tro.web.data.result.user.ResourceMenuResult;

/**
 * @Author: fanxx
 * @Date: 2020/11/3 7:31 下午
 * @Description:
 */
public interface TroResourceDAO {

    /**
     * 查询可配置权限的（有页面的）菜单列表
     *
     * @return
     */
    List<ResourceMenuResult> selectAuthConfigMenu();

    /**
     * 查询全量菜单列表
     *
     * @return
     */
    List<ResourceMenuResult> selectMenu(ResourceMenuQueryParam queryParam);

}
