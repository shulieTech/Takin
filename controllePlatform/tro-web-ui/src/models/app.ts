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

/**
 * @name 全局应用model
 */
import menuData from 'src/common/menu';
import { BasicDva } from 'src/components/basic-component/BasicDva';
import { filterBreadCrumbs } from 'src/components/page-layout/utils';

const initState = {
  /**
   * @name 菜单收缩
   */
  collapsed: false,
  /**
   * @name 面包屑
   */
  breadCrumbs: [],
  /** @name 链路梳理去调试工具id */
  debugToolId: undefined
};
export type AppModelState = Partial<typeof initState>;

const appModel = new BasicDva<AppModelState>({
  namespace: 'app',
  state: initState,
  effects: {
    /**
     * @name 获取当前面包屑
     */
    *filterBreadCrumbs({ payload }, { call, put }) {
      const breadCrumbs = [];
      filterBreadCrumbs(menuData, payload, breadCrumbs);
      yield put({
        type: 'updateState',
        payload: {
          breadCrumbs
        }
      });
    }
  }
}).render();

export default appModel;
