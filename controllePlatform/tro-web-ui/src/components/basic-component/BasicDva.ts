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

import { Basic } from 'src/types';

export class BasicDva<T> {
  state: T;
  initState: T;
  namespace: string;
  reducers?: Basic.Reducers<T>;
  effects?: Basic.Effects;
  resetState: Basic.ReducersFun<T>;
  constructor(ModelType: Basic.BaseModel<T>) {
    this.initState = ModelType.state;
    this.state = ModelType.state;
    this.namespace = ModelType.namespace;
    /**
     * @name 重置state
     */
    this.resetState = () => {
      return this.state;
    };
    this.reducers = {
      ...ModelType.reducers,
      updateState: this.updateState,
      resetState: this.resetState
    };
    this.effects = ModelType.effects;
  }
  /**
   * @name 更新state
   */
  updateState(state: T, { payload }) {
    return { ...state, ...payload };
  }
  render(): Basic.BaseModel<T> {
    return {
      namespace: this.namespace,
      state: this.state,
      reducers: this.reducers,
      effects: this.effects
    };
  }
}
