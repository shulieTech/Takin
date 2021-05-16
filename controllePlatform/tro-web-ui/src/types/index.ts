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

import { AxiosResponse } from 'axios';
import { Location } from 'history';
import { WrappedFormUtils } from 'antd/lib/form/Form';

export namespace Basic {
  export interface BaseResponse<T = any> extends AxiosResponse<DataProps> {
    /** 接口列表总条数 */
    total?: number;
    /** 请求头 */
    headers: {
      'X-Total-Count'?: number;
      'x-total-count'?: number;
      totalCount?: number;
      [propName: string]: any;
    };
  }
  interface DataProps {
    data?: any;
    error?: { code: string; msg: string };
    status: number;
    success: boolean;
  }
  export interface BaseProps<T = any> {
    /** dispatch */
    dispatch?: (action: Action) => void;
    /** antd Rc Form实例 */
    form?: WrappedFormUtils<T>;
    /** location路由 */
    location?: Location & { query: any };
    /** 其他拓展Props */
    // [propsName: string]: any;
  }
  export interface BaseModel<T> {
    /** 模块名  */
    namespace: string;
    /** 状态  */
    state: T;
    reducers?: Reducers<T>;
    effects?: Effects;
    subscriptions?: Object;
  }
  interface EffectsCommandMap {
    put: (action: Action) => any;
    call: Function;
    select: Function;
    take: Function;
    cancel: Function;
    [key: string]: any;
  }
  export interface Effects {
    [propName: string]: (action: Action, effects: EffectsCommandMap) => void;
  }
  export interface ReducersFun<State> {
    (state: State, params?: Action): State;
  }
  export interface Reducers<State> {
    [propName: string]: ReducersFun<State>;
  }
  interface Action<T = any> {
    type?: string;
    payload?: T;
    [propName: string]: any;
  }
}
