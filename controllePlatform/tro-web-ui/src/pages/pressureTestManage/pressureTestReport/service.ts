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

import { httpGet, httpPost, httpPut } from 'src/utils/request';

const PressureTestReportService = {
  /**
   * @name 获取警告详情列表
   */
  async queryWaringDetailList(data = {}) {
    const url = '/report/listWarn';
    return httpGet(url, data);
  },
  /**
   * @name 获取报告详情
   */
  async queryReportDetail(data = {}) {
    const url = '/report/getReportByReportId';
    return httpGet(url, data);
  },
  /**
   * @name 获取警告列表
   */
  async queryWarningList(data = {}) {
    const url = '/report/listWarn';
    return httpGet(url, data);
  },
  /**
   * @name 获取报告业务活动列表
   */
  async queryReportBusinessActivity(data = {}) {
    const url = '/report/queryReportActivityByReportId';
    return httpGet(url, data);
  },
  /**
   * @name 获取实况业务活动列表
   */
  async queryLiveBusinessActivity(data = {}) {
    const url = '/report/queryReportActivityBySceneId';
    return httpGet(url, data);
  },
  /**
   * @name 获取报告链路趋势图信息
   */
  async queryLinkChartsInfo(data = {}) {
    const url = '/report/queryReportTrend';
    return httpGet(url, data);
  },
  /**
   * @name 获取实况详情
   */
  async queryLiveDetail(data = {}) {
    const url = '/report/tempReportDetail';
    return httpGet(url, data);
  },
  /**
   * @name 获取实况链路趋势图信息
   */
  async queryLiveLinkChartsInfo(data = {}) {
    const url = '/report/queryTempReportTrend';
    return httpGet(url, data);
  },
  /**
   * @name 停止压测
   */
  async stopPressureTest(data = {}) {
    const url = '/scene/task/stop';
    return httpPost(url, data);
  },
  /**
   * @name 获取瓶颈接口
   */
  async queryBottleneckAPIList(data = {}) {
    const url = '/report/bottleneckInterface/list';
    return httpGet(url, data);
  },
  /**
   * @name 获取风险机器左侧应用列表
   */
  async queryRiskMachineAppList(data = {}) {
    const url = '/report/risk/application';
    return httpGet(url, data);
  },
  /**
   * @name 获取风险机器列表
   */
  async queryRiskMachineList(data = {}) {
    const url = '/report/risk/machine/list';
    return httpGet(url, data);
  },
  /**
   * @name 获取压测明细列表
   */
  async queryPressureTestDetailList(data = {}) {
    const url = '/report/businessActivity/summary/list';
    return httpGet(url, data);
  },
  /**
   * @name 获取容量水位应用列表
   */
  async queryWaterLevelList(data = {}) {
    const url = '/report/application/list';
    return httpGet(url, data);
  },
  /**
   * @name 获取风险机器性能详情
   */
  async queryMachinePerformanceDetail(data = {}) {
    const url = '/report/machine/detail';
    return httpGet(url, data);
  },
  /**
   * @name 获取容量水位应用机器列表
   */
  async queryWaterLeveAppMachineList(data = {}) {
    const url = '/report/machine/list';
    return httpGet(url, data);
  },
  /**
   * @name 获取报告汇总数据
   */
  async queryReportCount(data = {}) {
    const url = '/report/count';
    return httpGet(url, data);
  },
  /**
   * @name 获取业务活动压测明细
   */
  async queryBusinessActivityPressureTestDetail(data = {}) {
    const url = '/report/link/list';
    return httpGet(url, data);
  },
  /**
   * @name 获取请求明细列表
   */
  async queryRequestDetail(data = {}) {
    const url = '/report/link/detail';
    return httpGet(url, data);
  },
  /**
   * @name 获取请求流量列表
   */
  async queryRequestList(data = {}) {
    const url = '/report/realtime/link/list';
    return httpGet(url, data);
  },
  /**
   * @name 获取请求流量失败数据
   */
  async queryRequestCount(data = {}) {
    const url = '/report/application/trace/failedCount';
    return httpGet(url, data);
  },
  /**
   * @name 获取漏数验证数据
   */
  async queryMissingDataList(data = {}) {
    const url = '/leak/report/detail';
    return httpGet(url, data);
  },
  /**
   * @name 调整TPS
   */
  async adjustTPS(data = {}) {
    const url = '/scene/task/tps';
    return httpPut(url, data);
  },
  /**
   * @name 调整TPS
   */
  async getTpsValue(data = {}) {
    const url = '/scene/task/queryTaskTps';
    return httpGet(url, data);
  },
};

export default PressureTestReportService;
