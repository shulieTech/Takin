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

import { useCreateContext } from 'racc';
import { WrappedFormUtils } from 'antd/lib/form/Form';

export const getInitState = () => ({
  searchParams: { current: 0, pageSize: 10, tabKey: '' } as {
    current: number;
    pageSize: number;
    tabKey: string;
  },
  total: 0,
  dataSource: [],
  checkedKeys: [],
  checkedRows: [],
  loading: true,
  form: null as WrappedFormUtils,
  flag: false,
  toggleRoload: false
});

export type State = ReturnType<typeof getInitState>;

export const SearchTableContext = useCreateContext<State>();
