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

import React, { useEffect } from 'react';
import SearchTable from 'src/components/search-table';
import { useStateReducer } from 'racc';
import getColumns from './components/DemoTable';
import { WrappedFormUtils } from 'antd/lib/form/Form';

import getFormData from './components/DemoSearch';

interface DemoProps {}

export interface DemoState {
  isReload?: boolean;
  searchParams: {
    current: string | number;
    pageSize: string | number;
  };
}

const Demo: React.FC<DemoProps> = props => {
  const [state, setState] = useStateReducer<DemoState>({
    isReload: false,
    searchParams: {
      current: 0,
      pageSize: 10
    }
  });

  return (
    <SearchTable
      commonTableProps={{
        columns: getColumns(state, setState),
        size: 'small'
      }}
      commonFormProps={{ formData: getFormData(), rowNum: 6 }}
      ajaxProps={{ url: '/demo', method: 'GET' }}
      searchParams={{ ...state.searchParams }}
      toggleRoload={state.isReload}
    />
  );
};
export default Demo;
