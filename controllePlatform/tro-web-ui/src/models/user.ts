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

import { notification } from 'antd';
import UserService from 'src/services/user';
import router from 'umi/router';

const { troLogin, troLogout } = UserService;

export default {
  namespace: 'user',
  state: {
    userInfo: {},
    userAuthority: [],
    login: false
  },
  effects: {
    *login({ payload }, { call, put }) {
      const {
        data: { code, data, msg }
      } = yield call(troLogin, payload);
      if (code === 200) {
        yield put({
          type: 'updateState',
          payload: {
            userInfo: data
          }
        });
        yield put({ type: 'getToken' });
      }
      // if (code === 300) {
      //   message.error(msg);
      // }
    },
    *troLogin({ payload }, { call, put }) {
      const {
        data: { success, data, error }
      } = yield call(troLogin, payload);
      if (success) {
        notification.success({
          message: '通知',
          description: '登录成功',
          duration: 1.5
        });
        // router.push('/linkMark');
        localStorage.setItem('troweb-userName', payload.username);
        localStorage.setItem('troweb-role', data.userType);
        localStorage.setItem('troweb-userId', data.id);
      }
    },
    *troLogout({ payload }, { call, put }) {
      const {
        data: { success, data, error }
      } = yield call(troLogout, payload);
      if (success) {
        localStorage.removeItem('troweb-role');
        localStorage.removeItem('troweb-userName');
        localStorage.removeItem('full-link-token');
        localStorage.removeItem('trowebUserResource');
        localStorage.removeItem('trowebBtnResource');
        localStorage.removeItem('auth-cookie');
        localStorage.removeItem('troweb-expire');
        yield put({
          type: 'updateState',
          payload: {
            userInfo: {},
            userAuthority: []
          }
        });
        if (data && data.indexUrl) {
          location.href = `${data.indexUrl}`;
          return;
        }
        router.push('/login');
      }
    }
  },

  reducers: {
    updateState(state, { payload }) {
      return { ...state, ...payload };
    }
  }
};
