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

import com.pamirs.tro.entity.domain.vo.TAlarmMonitorVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 说明: 监控告警dao
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/6/28 15:55
 */
@Mapper
public interface TAlarmMonitorDao {

    /**
     * 说明: 根据二级链路id查询告警列表
     *
     * @param secondLinkId 二级链路id
     * @param startTime    告警开始时间
     * @param endTime      告警结束时间
     * @return 二级链路下的告警列表
     * @author shulie
     * @date 2018/6/28 16:37
     */
    List<TAlarmMonitorVo> queryAlarmListBySecondLinkId(@Param("secondLinkId") String secondLinkId,
        @Param("startTime") String startTime,
        @Param("endTime") String endTime);
}
