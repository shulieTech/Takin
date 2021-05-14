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

package io.shulie.tro.web.app.service;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.TReport;
import com.pamirs.tro.entity.domain.query.Result;
import com.pamirs.tro.entity.domain.query.ResultList;
import com.pamirs.tro.entity.domain.query.TReportQuery;
import com.pamirs.tro.entity.domain.vo.TReportAppIpDetail;
import com.pamirs.tro.entity.domain.vo.TReportDetail;

/**
 * 说明: 压测报告相关服务实接口
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 */
public interface TReportService {

    /**
     * 增加报告
     *
     * @param tReport 报告
     * @return 增加成功返回void，失败返回错误信息
     * @version v1.0
     * @Date:
     */
    Result<Void> add(TReport tReport);

    /**
     * 修改报告
     *
     * @param tReport 报告
     * @return 修改成功返回void，失败返回错误信息
     * @version v1.0
     * @Date:
     */
    Result<Void> modify(TReport tReport);

    /**
     * 删除报告
     *
     * @param id 报告id
     * @return 修改成功返回void，失败返回错误信息
     * @version v1.0
     * @Date:
     */
    Result<Void> deleteById(Long id);

    ResultList<TReport> queryListByQuery(TReportQuery query);

    /**
     * 查询报告详情
     *
     * @param id 报告id
     * @return 修改成功返回报告，失败返回错误信息
     */
    Result<TReport> queryOneById(Long id);

    /**
     * 查询报告
     *
     * @param id     报告id
     * @param status 状态
     * @return 报告详情
     * @version v1.0
     * @Date:
     */
    Result<TReport> queryOneByScenarioId(Long id, Integer status);

    /**
     * 查询所有的压测机器性能情况
     *
     * @param tReport 报告
     * @return TReportAppIpDetail
     * @version v1.0
     * @Date:
     */
    List<TReportAppIpDetail> queryMachineDetail(TReport tReport);

    /**
     * 查询报告
     *
     * @param secondLinkId 二级链路id
     * @param status       状态
     * @return 报告
     * @version v1.0
     * @Date:
     */
    List<TReport> queryBySecondLinkIdAndStatus(String secondLinkId, String status);

    List<TReport> queryStatus(String status);

    TReportDetail queryReportDetail(Long reportId);

    /**
     * @param reportId 报告id
     * @param linkId   基础链路id
     * @return List<TReportAppIpDetail>
     * @description 报告应用详细信息列表
     * @author shulie
     * @create 2018/6/27 22:59
     */
    List<TReportAppIpDetail> queryApplicationIpByIpList(String reportId, String linkId);
}
