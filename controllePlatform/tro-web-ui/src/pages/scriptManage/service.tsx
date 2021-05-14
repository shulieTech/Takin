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

const ScriptManageService = {
  /**
   * @name 新增脚本
   */
  async addScript(data = {}) {
    const url = '/scriptManage';
    return httpPost(url, data);
  },
  /**
   * @name 编辑脚本
   */
  async editScript(data = {}) {
    const url = '/scriptManage';
    return httpPut(url, data);
  },
  /**
   * @name 删除脚本
   */
  async deleteScript(data = {}) {
    const url = '/scriptManage';
    return httpDelete(url, data);
  },
  /**
   * @name 查询脚本详情
   */
  async queryScript(data = {}) {
    const url = '/scriptManage';
    return httpGet(url, data);
  },
  /**
   * @name 上传文件
   */
  async uploadFiles(data = {}) {
    const url = '/file/upload';
    return httpPost(url, data);
  },
  /**
   * @name 删除文件
   */
  async deleteFiles(data = {}) {
    const url = '/file';
    return httpDelete(url, data);
  },
  /**
   * @name 获取下载大文件插件地址
   */
  async getBigFileDownload(data = {}) {
    const url = '/cloud/client/download';
    return httpGet(url, data);
  },
  /**
   * @name 获取脚本标签列表
   */
  async queryScriptTagList(data = {}) {
    const url = '/scriptManage/listScriptTag';
    return httpGet(url, data);
  },
  /**
   * @name 新增脚本标签
   */
  async addScriptTags(data = {}) {
    const url = '/scriptManage/createScriptTagRef';
    return httpPost(url, data);
  },
  /**
   * @name 下载脚本文件
   */
  async downloadScript(data = {}) {
    const url = '/scriptManage/getZipFileUrl';
    return httpGet(url, data);
  },
  /**
   * @name 下载单个文件
   */
  async downloadSingleScript(data = {}) {
    const url = '/scriptManage/getFileDownLoadUrl';
    return httpGet(url, data);
  },
  /**
   * @name 下载文件(cloud迁移)
   */
  async downloadFileByPath(data = {}) {
    const url = '/file/downloadFileByPath';
    return httpGet(url, data);
  },
  /**
   * @name 获取文件代码
   */
  async queryScriptCode(data = {}) {
    const url = '/scriptManage/explainScriptFile';
    return httpGet(url, data);
  },
  /**
   * @name 获取插件列表
   */
  async queryPluginList(data = {}) {
    const url = '/scriptManage/support/plugin/list';
    return httpGet(url, data);
  },
  /**
   * @name 获取插件版本列表
   */
  async queryPluginVersionList(data = {}) {
    const url = '/scriptManage/support/plugin/version';
    return httpGet(url, data);
  },
  /**
   * @name 上传附件
   */
  async uploadAttachments(data = {}) {
    const url = '/file/attachment/upload';
    return httpPost(url, data);
  }
};

export default ScriptManageService;
