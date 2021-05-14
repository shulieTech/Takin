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

/* https://github.com/orange1438 */
package com.pamirs.tro.entity.dao.simplify;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.simplify.AppBusinessTableInfo;
import com.pamirs.tro.entity.domain.query.agent.AppBusinessTableQuery;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.tro.entity.dao.simplify
 * @Date 2020-03-25 15:09
 */
public interface TAppBusinessTableInfoMapper {

    int insert(AppBusinessTableInfo record);

    int insertBatch(List<AppBusinessTableInfo> records);

    AppBusinessTableInfo selectByUserIdAndUrl(AppBusinessTableInfo record);

    AppBusinessTableInfo selectByUserIdAndUrl(Long id);

    List<AppBusinessTableInfo> selectList(AppBusinessTableQuery query);

    Long selectCountByUserIdAndUrl(AppBusinessTableInfo record);

    int update(AppBusinessTableInfo record);
}
