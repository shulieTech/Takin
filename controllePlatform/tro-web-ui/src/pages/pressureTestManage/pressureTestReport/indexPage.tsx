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

import React, { useEffect, useState, Fragment } from 'react';
import SearchTable from 'src/components/search-table';

import { useStateReducer } from 'racc';

import getPressureTestReportColumns from './components/PressureTestReportColumn';
import getPressureTestReportFormData from './components/PressureTestReportFormData';

interface PressureTestReportProps {
  location?: { query?: any };
}

export interface PressureTestReportState {
  isReload?: boolean;
  searchParams: {
    current: string | number;
    pageSize: string | number;
  };
  searchParamss: any;
}

const PressureTestReport: React.FC<PressureTestReportProps> = props => {
  const [state, setState] = useStateReducer<PressureTestReportState>({
    isReload: false,
    searchParams: {
      current: 0,
      pageSize: 10
    },
    searchParamss: props.location.query
  });

  const { location } = props;
  const { query } = location;
  const { sceneName } = query;

  return (
    <Fragment>
      <SearchTable
        commonTableProps={{
          columns: getPressureTestReportColumns(state, setState)
        }}
        commonFormProps={{
          formData: getPressureTestReportFormData(),
          rowNum: 4
        }}
        ajaxProps={{ url: '/report/listReport', method: 'GET' }}
        searchParams={{ ...state.searchParams, sceneName }}
        toggleRoload={state.isReload}
        datekeys={[
          {
            originKey: 'time',
            separateKey: ['startTime', 'endTime']
          }
        ]}
      />
    </Fragment>
  );
};
export default PressureTestReport;
