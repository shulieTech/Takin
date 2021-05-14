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

declare var centerServerUrl: string;

const UserService = {
  async queryCodeImg(data) {
    const url = '/verification/code';
    return httpGet(url, data);
  },
  async troLogin(data) {
    const url = '/login';
    return httpPost(url, data);
  },
  async troLogout(data) {
    const url = '/logout';
    return httpGet(url, data);
  },
  async updatePassword(data) {
    const url = '/user/pwd/update';
    return httpPut(url, data);
  },
  async menuList(data) {
    const url = '/user/menu/list';
    return httpGet(url, data);
  },
  async queryUserResource(data) {
    const url = '/auth/user/resource';
    return httpGet(url, data);
  },
  async queryBtnResource(data) {
    const url = '/auth/resource/user/action';
    return httpGet(url, data);
  }
};

export default UserService;
