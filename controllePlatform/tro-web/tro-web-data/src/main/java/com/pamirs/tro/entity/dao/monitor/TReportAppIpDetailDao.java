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
import com.pamirs.tro.entity.domain.vo.TReportAppIpDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * TReportAppIpDetailDao接口
 *
 * @author shulie
 * @version v1.0
 * @2018年5月21日
 */
@Mapper
public interface TReportAppIpDetailDao extends BaseDao<TReportAppIpDetail> {

    /**
     * 查询机器详情
     *
     * @param id id
     * @return TReportAppIpDetail列表
     * @author shulie
     * @2018年5月21日
     * @version v1.0
     */
    List<TReportAppIpDetail> queryMachineDetail(String id);

    List<TReportAppIpDetail> countMechine(@Param("reportId") String reportId,
        @Param("applicationName") String applicationName, @Param("linkId") String linkId, @Param("ip") String ip);

    /**
     * @param reportId 报告id
     * @param linkId   基础链路id
     * @return 报告列表
     * @description 获取报告中的机器信息列表
     * @author shulie
     * @create 2018/6/27 22:17
     */
    List<TReportAppIpDetail> queryReportAppIpListByReportIdAndLinkId(
        @Param("reportId") String reportId,
        @Param("linkId") String linkId);

}
