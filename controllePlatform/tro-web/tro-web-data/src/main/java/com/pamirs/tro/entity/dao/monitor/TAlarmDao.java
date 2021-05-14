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

package com.pamirs.tro.entity.dao.monitor;

import java.util.List;

import com.pamirs.tro.entity.dao.common.BaseDao;
import com.pamirs.tro.entity.domain.entity.TAlarm;
import com.pamirs.tro.entity.domain.query.TAlarmQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报警查询dao
 */
@Mapper
public interface TAlarmDao extends BaseDao<TAlarm> {

    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<TAlarm> selectList(TAlarmQuery query);

    /**
     * 查询列表数
     *
     * @param query
     * @return
     */
    long selectListCount(TAlarmQuery query);

}
