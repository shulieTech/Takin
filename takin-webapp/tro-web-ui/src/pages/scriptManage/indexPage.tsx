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

import { useStateReducer } from 'racc';
import React, { useEffect } from 'react';
import SearchTable from 'src/components/search-table';
import PressureTestSceneService from '../pressureTestManage/pressureTestScene/service';
import getScriptManageFormData from './components/ScriptManageSearch';
import getScriptManageColumns from './components/ScriptManageTable';
import ScriptManageTableAction from './components/ScriptManageTableAction';

interface ScriptManageProps {
  isReload?: boolean;
  searchParams: {
    current: string | number;
    pageSize: string | number;
  };
  location?: any;
}

export interface ScriptManageState {
  switchStatus: string;
  isReload?: boolean;
  searchParams: {
    current: string | number;
    pageSize: string | number;
  };
  searchParamss?: any;
  businessActivityList: any[];
  businessFlowList: any[];
  tagList: any[];
}

const ScriptManage: React.FC<ScriptManageProps> = props => {
  const [state, setState] = useStateReducer<ScriptManageState>({
    isReload: false,
    switchStatus: null,
    searchParams: {
      current: 0,
      pageSize: 10
    },
    searchParamss: props.location.query,
    businessActivityList: null,
    businessFlowList: null,
    tagList: null
  });

  useEffect(() => {
    queryBussinessActive();
  }, []);

  /**
   * @name 获取所有业务活动
   */
  const queryBussinessActive = async () => {
    const {
      data: { success, data }
    } = await PressureTestSceneService.queryBussinessActive({});
    if (success) {
      setState({
        businessActivityList:
          data &&
          data.map(item => {
            return { label: item.businessActiveName, value: item.id };
          })
      });
    }
  };

  return (
    <SearchTable
      key="id"
      commonTableProps={{
        columns: getScriptManageColumns(state, setState)
      }}
      commonFormProps={{
        formData: getScriptManageFormData(state, setState),
        rowNum: 6
      }}
      ajaxProps={{ url: '/scriptManage/list', method: 'POST' }}
      searchParams={state.searchParamss}
      toggleRoload={state.isReload}
      tableAction={
        <ScriptManageTableAction state={state} setState={setState} />}
    />
  );
};
export default ScriptManage;
