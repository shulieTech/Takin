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
import { connect } from 'dva';
import EntryRuleSearch from './components/EntryRuleSearch';
import EntryRuleTable from './components/EntryRuleTable';
import EntryRuleTableAction from './components/EntryRuleTableAction';

interface EntryRuleProps {
  location?: { query?: any };
  dictionaryMap?: any;
}

export interface EntryRuleState {
  isReload?: boolean;
  searchParams: {
    current: string | number;
    pageSize: string | number;
  };
}

const EntryRule: React.FC<EntryRuleProps> = props => {
  const [state, setState] = useStateReducer<EntryRuleState>({
    isReload: false,
    searchParams: {
      current: 0,
      pageSize: 10
    }
  });

  const { dictionaryMap } = props;

  return (
    <Fragment>
      <SearchTable
        key="id"
        commonFormProps={{
          formData: EntryRuleSearch(state, dictionaryMap),
          rowNum: 6
        }}
        ajaxProps={{ url: '/api/get', method: 'GET' }}
        toggleRoload={state.isReload}
        commonTableProps={{
          columns: EntryRuleTable(state, setState)
        }}
        tableAction={<EntryRuleTableAction state={state} setState={setState} />}
      />
    </Fragment>
  );
};
export default connect(({ common }) => ({ ...common }))(EntryRule);
