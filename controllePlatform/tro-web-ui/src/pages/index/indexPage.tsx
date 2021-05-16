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

import React, { Fragment, useEffect } from 'react';
import UserService from 'src/services/user';
import router from 'umi/router';
import { useStateReducer } from 'racc';
interface Props {
  location?: any;
}
const Demo: React.FC<Props> = props => {
  const { location } = props;
  const { query } = location;
  const { SESSION } = query;

  useEffect(() => {
    if (SESSION) {
      localStorage.setItem('auth-cookie', `SESSION=${SESSION}`);
      queryUserResource();
      queryBtnResource();
    }

    if (!SESSION && !localStorage.getItem('trowebUserResource')) {
      queryUserResource();
    }

    if (!SESSION && !localStorage.getItem('trowebBtnResource')) {
      queryBtnResource();
    }
  }, []);
  /**
   * @name 获取菜单权限
   */
  const queryUserResource = async () => {
    const {
      headers,
      data: { data, success }
    } = await UserService.queryUserResource({});
    if (success) {
      localStorage.setItem('trowebUserResource', JSON.stringify(data));
      if (!localStorage.getItem('troweb-role')) {
        localStorage.setItem('troweb-role', headers['x-user-type']);
      }
      if (!localStorage.getItem('troweb-expire')) {
        localStorage.setItem('troweb-expire', headers['x-expire']);
      }

      const urlString = Object.keys(data) && Object.keys(data)[0];
      const urls = urlString && urlString.replace(/_/, '/');
      const url = urls ? `/${urls}` : '/';
      router.push(url);
    }
  };

  /**
   * @name 获取按钮权限
   */
  const queryBtnResource = async () => {
    const {
      data: { data, success }
    } = await UserService.queryBtnResource({});
    if (success) {
      localStorage.setItem('trowebBtnResource', JSON.stringify(data));
    }
  };

  return <Fragment />;
};
export default Demo;
