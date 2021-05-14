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
import getMissionManageFormData from './components/MissionManageSearch';
import getMissionManageColumns from './components/MissionManageTable';
import MissionManageService from './service';

interface MissionManageProps {}

const getInitState = () => ({
  isReload: false,
  // searchParams: {
  //   current: 0,
  //   pageSize: 10,
  //   renterId: 1
  // },
  PATROL_EXCEPTION_TYPE: null,
  PATROL_EXCEPTION_LEVEL: null
});
export type MissionManageState = ReturnType<typeof getInitState>;

const MissionManage: React.FC<MissionManageProps> = props => {
  const [state, setState] = useStateReducer<MissionManageState>(getInitState());

  useEffect(() => {
    queryPatrolSceneAndDashbordList();
  }, []);

  const queryPatrolSceneAndDashbordList = async () => {
    const {
      data: { data, success }
    } = await MissionManageService.queryPatrolSceneAndDashbordList({});
    if (success) {
      setState({
        PATROL_EXCEPTION_TYPE:
          data.PATROL_EXCEPTION_TYPE &&
          data.PATROL_EXCEPTION_TYPE.map((item1, k1) => {
            return {
              label: item1.label,
              value: item1.value
            };
          }),
        PATROL_EXCEPTION_LEVEL:
          data.PATROL_EXCEPTION_LEVEL &&
          data.PATROL_EXCEPTION_LEVEL.map((item, k) => {
            return {
              label: item.label,
              value: item.value
            };
          })
      });
    }
  };

  return (
    <SearchTable
      commonTableProps={{
        columns: getMissionManageColumns(state, setState)
      }}
      commonFormProps={{ formData: getMissionManageFormData(state), rowNum: 6 }}
      ajaxProps={{ url: '/patrol/manager/exception/query', method: 'POST' }}
      toggleRoload={state.isReload}
    />
  );
};
export default MissionManage;
