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

import React, { useEffect, useState } from 'react';
import SearchTable from 'src/components/search-table';

import { useStateReducer } from 'racc';
import getUserManageFormData from './components/UserManageSearch';
import getUserManageColumns from './components/UserManageTable';
import UserTableAction from './components/UserTableAction';

interface AppManageProps {
  isReload?: boolean;
  searchParams: {
    current: string | number;
    pageSize: string | number;
  };
}

export interface AppManageState {
  isReload?: boolean;
  searchParams: {
    current: string | number;
    pageSize: string | number;
  };
}

const UserManage: React.FC<AppManageProps> = props => {
  const [state, setState] = useStateReducer<AppManageState>({
    isReload: false,
    searchParams: {
      current: 0,
      pageSize: 10
    }
  });

  return (
    <SearchTable
      commonTableProps={{
        columns: getUserManageColumns(state, setState),
        size: 'small'
      }}
      commonFormProps={{ formData: getUserManageFormData(), rowNum: 6 }}
      ajaxProps={{ url: '/user/list', method: 'GET' }}
      searchParams={{ ...state.searchParams }}
      toggleRoload={state.isReload}
      tableAction={<UserTableAction state={state} setState={setState} />}
    />
  );
};
export default UserManage;
