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

import { httpGet, httpPost, httpDelete, httpPut } from 'src/utils/request';

const LinkMarkService = {
  /**
   * @name 获取链路统计数据
   */
  async queryLinkStatistic(data = {}) {
    const url = '/link/statistic';
    return httpGet(url, data);
  },
  /**
   * @name 获取趋势图数据
   */
  async queryStatisticChart(data = {}) {
    const url = '/link/statistic/chart';
    return httpGet(url, data);
  },

  /**
   * @name 获取统计所有中间件
   */
  async queryMiddlewareName(data = {}) {
    const url = '/link/middleWare/name';
    return httpGet(url, data);
  },
  /**
   * @name 获取统计中间件信息
   */
  async queryLinkMiddleware(data = {}) {
    const url = '/link/statistic/middleware';
    return httpGet(url, data);
  },
  /**
   * @name 获取业务流程
   */
  async queryBusinessFlow(data = {}) {
    const url = '/link/businessFlow';
    return httpGet(url, data);
  },
  /**
   * @name 获取系统流程
   */
  async querySystemFlow(data = {}) {
    const url = '/link/tech/linkmanage/all';
    return httpGet(url, data);
  }
};

export default LinkMarkService;
