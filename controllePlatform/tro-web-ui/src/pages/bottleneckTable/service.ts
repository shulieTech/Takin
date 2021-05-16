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

const MissionManageService = {
  /**
   * @name 获取字典
   */
  async queryPatrolSceneAndDashbordList(data = {}) {
    const url = '/link/dictionary';
    return httpGet(url, data);
  },
  /**
   * @name 添加误报
   */
  async mistake(data = {}) {
    const url = '/patrol/manager/exception/mistake';
    return httpPost(url, data);
  },
  /**
   * @name 获取场景详情
   */
  async queryMistakeDetail(exceptionId) {
    const url = `/patrol/manager/exception/mistake_info?exceptionId=${exceptionId}`;
    return httpPost(url);
  },
  /**
   * @name 新增场景
   */
  async addScene(data = {}) {
    const url = '/patrol/manager/scene/create';
    return httpPost(url, data);
  },
  /**
   * @name 场景详情
   */
  async exceptionDetail(id) {
    const url = `/patrol/manager/exception/detail?exceptionId=${id}`;
    return httpPost(url);
  },

  async read(data = {}) {
    const url = '/patrol/manager/exception_config/read';
    return httpPost(url, data);
  },
};

export default MissionManageService;
