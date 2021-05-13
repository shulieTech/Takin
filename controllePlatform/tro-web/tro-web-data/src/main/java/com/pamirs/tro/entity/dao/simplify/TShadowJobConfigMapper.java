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

import com.pamirs.tro.entity.annocation.DataAuth;
import com.pamirs.tro.entity.domain.entity.simplify.TShadowJobConfig;
import com.pamirs.tro.entity.domain.query.ShadowJobConfigQuery;
import org.apache.ibatis.annotations.Param;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.tro.entity.dao.simplify
 * @Date 2020-03-17 15:40
 */
public interface TShadowJobConfigMapper {
    int delete(Long id);

    int insert(TShadowJobConfig record);

    TShadowJobConfig selectOneById(Long id);

    @DataAuth
    List<TShadowJobConfig> selectList(ShadowJobConfigQuery query);

    int update(TShadowJobConfig record);

    int updateByPrimaryKeyWithBLOBs(TShadowJobConfig record);

    int updateByPrimaryKey(TShadowJobConfig record);

    List<TShadowJobConfig> getAllEnableShadowJobs(@Param("applicationId") long applicationId);
}
