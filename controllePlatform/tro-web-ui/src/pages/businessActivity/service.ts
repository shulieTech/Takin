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

import { httpDelete, httpGet, httpPost, httpPut } from 'src/utils/request';

const BusinessActivityService = {
  /**
   * @name 获取系统流程详情
   */
  async querySystemFlowDetail(data = {}) {
    const url = '/link/tech/linkManage/detail';
    return httpGet(url, data);
  },
  /**
   * @name 新增系统流程
   */
  async addSystemFlow(data = {}) {
    const url = '/link/tech/linkManage';
    return httpPost(url, data);
  },
  /**
   * @name 编辑系统流程
   */
  async editSystemFlow(data = {}) {
    const url = '/link/tech/linkManage';
    return httpPut(url, data);
  },
  /**
   * @name 删除系统流程
   */
  async deleteSystemFlow(activityId) {
    const url = `/activities/delete?activityId=${activityId}`;
    return httpDelete(url);
  },
  /**
   * @name 获取入口地址
   */
  async queryEntrance(data = {}) {
    const url = '/link/linkManage/getEntrance';
    return httpGet(url, data);
  },
  /**
   * @name 获取所有应用
   */
  async queryAllApp(data = {}) {
    const url = '/application/center/list/dictionary';
    return httpGet(url, data);
  },
  // /**
  //  * @name 获取关联链路和关联中间件
  //  */
  // async queryLinkManage(data = {}) {
  //   const url = '/link/linkManage';
  //   return httpGet(url, data);
  // },
  /**
   * @name 获取中间件类型、中间件、中间件版本
   */
  async queryMiddleware(data = {}) {
    const url = '/link/linkmanage/middleware';
    return httpGet(url, data);
  },
  /**
   * @name 确认变更系统流程
   */
  async changeSystemFlow(data = {}) {
    const url = '/link/change';
    return httpPut(url, data);
  },
  /**
   * @name 获取应用明细列表
   */
  async queryAppListDetail(data = {}) {
    const url = '/link/tech/linkManage/appDetail';
    return httpGet(url, data);
  },
  /**
   * @name 链路拓扑图
   */
  async queryChartInfo(data) {
    const url = '/application/entrances/topology';
    return httpGet(url, data);
  },
  /**
   * @name 服务列表
   */
  async queryServiceList(data) {
    const url = '/application/entrances';
    return httpGet(url, data);
  },
  /**
   * @name 链路详情
   */
  async queryChainList(data) {
    const url = '/application/entrances/detail';
    return httpGet(url, data);
  },
  /**
   * @name 新增系统流程
   */
  async createSystemProcess(data) {
    const url = '/activities/create';
    return httpPost(url, data);
  },
  /**
   * @name 修改系统流程
   */
  async updateSystemProcess(data) {
    const url = '/activities/update';
    return httpPut(url, data);
  },
  /**
   * @name 获取系统流程详情
   */
  async querySystemProcess(data) {
    const url = '/activities/activity';
    return httpGet(url, data);
  },
  /**
   * @name 获取系统流程列表
   */
  async querySystemFlowList(data = {}) {
    const url = '/link/tech/linkmanage/canRelate/all';
    return httpGet(url, data);
  },
  /**
   * @name 获取业务活动详情
   */
  async queryBusinessActivityDetail(data = {}) {
    const url = '/link/business/manage/detail';
    return httpGet(url, data);
  },
  // /**
  //  * @name 获取业务流程中间件
  //  */
  // async querySceneMiddlewares(data = {}) {
  //   const url = '/api/link/scene/middlewares';
  //   return httpGet(url, data);
  // },

  /**
   * @name 获取业务活动中间件以及中间件版本
   */
  async queryMiddlewareCascade(data = {}) {
    const url = '/link/midlleWare/cascade';
    return httpGet(url, data);
  },
  /**
   * @name 获取关联链路和关联中间件
   */
  async queryLinkManage(data = {}) {
    const url = '/link/tech/linkManage/detail';
    return httpGet(url, data);
  },

  /**
   * @name 获取关联链路和关联中间件(不受权限控制)
   */
  async queryLinkManageDic(data = {}) {
    const url = '/link/tech/linkManage/detail/dictionary';
    return httpGet(url, data);
  },
  /**
   * @name 新增业务活动
   */
  async addBusinessActivity(data = {}) {
    const url = '/link/business/linkManage';
    return httpPost(url, data);
  },
  /**
   * @name 编辑业务活动
   */
  async editBusinessActivity(data = {}) {
    const url = '/link/business/linkManage';
    return httpPut(url, data);
  },
  /**
   * @name 删除业务活动
   */
  async deleteBusinessActivity(data = {}) {
    const url = '/link/business/linkManage';
    return httpDelete(url, data);
  },
  /**
   * @name 业务活动详情
   */
  async getBusinessActivityDetails(data = {}) {
    const url = '/activities/activity';
    return httpGet(url, data);
  },

  /**
   * @name 获取漏数验证脚本
   */
  async queryMissingDataScriptList(data = {}) {
    const url = '/leak/sql/batch';
    return httpPost(url, data);
  },
  /**
   * @name 获取所有数据源列表
   */
  async queryAllDataSourceList(data = {}) {
    const url = '/datasource/list/dictionary';
    return httpGet(url, data);
  },
  /**
   * @name 标记为外部应用
   */
  async markOuterApp(data = {}) {
    const url = '/application/entrances/updateUnknownNode';
    return httpPost(url, data);
  },

  /**
   * @name 新增数据源
   */
  async addDataSource(data = {}) {
    const url = '/leak/sql/create';
    return httpPost(url, data);
  },
  /**
   * @name 编辑数据源
   */
  async editDataSource(data = {}) {
    const url = '/leak/sql/update';
    return httpPut(url, data);
  },
  /**
   * @name 删除数据源
   */

  async deleteDataSource(data = {}) {
    const url = '/leak/sql/delete';
    return httpDelete(url, data);
  },
  /**
   * @name 获取数据源详情
   */
  async queryDataSourceDetail(data = {}) {
    const url = '/leak/sql/detail';
    return httpGet(url, data);
  },
  /**
   * @name 调试sql
   */
  async debugSql(data = {}) {
    const url = '/leak/sql/test';
    return httpPost(url, data);
  },
  /**
   * @name 流量验证
   */
  async verificateFlow(data = {}) {
    const url = '/activities/startVerify';
    return httpPost(url, data);
  },
  /**
   * @name 关联脚本列表
   */
  async queryScriptList(data = {}) {
    const url = '/scriptManage/list';
    return httpPost(url, data);
  },
  /**
   * @name 流量验证状态
   */
  async verificateFlowStatus(data = {}) {
    const url = '/activities/verifyStat';
    return httpGet(url, data);
  },
};

export default BusinessActivityService;
