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
 * @name
 * @author MingShined
 */
import { useStateReducer } from 'racc';
import React, { useEffect } from 'react';
import { httpGet, httpPost } from 'src/utils/request';
import { filterSearchParams } from 'src/utils/utils';
import FooterNode from './components/FooterNode';
import SearchNode from './components/SearchNode';
import TableNode from './components/TableNode';
import TitleNode from './components/TitleNode';
import { getInitState, SearchTableContext } from './context';
import styles from './index.less';
import { SearchTableProps } from './type';

const SearchTable: React.FC<SearchTableProps> = props => {
  const [state, setState] = useStateReducer(getInitState());
  useEffect(() => {
    if (!state.flag && props.searchParams) {
      return;
    }
    setState({ searchParams: { ...state.searchParams }, checkedKeys: [], checkedRows: [] });
    // setState({ searchParams: { ...state.searchParams, current: 0 } });
    queryList(true);
  }, [props.toggleRoload]);
  useEffect(() => {
    if (!props.searchParams) {
      return;
    }
    const searchParams = {
      ...getInitState().searchParams,
      ...props.searchParams
    };
    setState({
      searchParams,
      flag: true
    });
  }, [props.searchParams]);
  useEffect(() => {
    if (!state.flag) {
      return;
    }
    queryList();
  }, [state.searchParams, state.toggleRoload]);
  const queryList = async (reload: boolean = false) => {
    // return;
    // if (props.onCheck) {
    //   const checkedKeys = reload ? [] : state.checkedKeys;
    //   const checkedRows = reload ? [] : state.checkedRows;
    //   props.onCheck(checkedKeys, checkedRows);
    // }
    setState({ loading: true });
    const { method, url } = props.ajaxProps;
    const getSearchParams = { ...state.searchParams };
    if (method === 'GET') {
      Object.keys(getSearchParams).forEach(item => {
        if (Array.isArray(getSearchParams[item])) {
          getSearchParams[item] = getSearchParams[item].join(',');
        }
      });
    }
    const ajaxEvent =
      method === 'GET'
        ? httpGet(url, filterSearchParams(getSearchParams))
        : httpPost(url, filterSearchParams(state.searchParams));
    const {
      data: { data, success },
      total
    } = await ajaxEvent;
    if (props.onSearch) {
      props.onSearch(state.searchParams, data);
    }
    if (success) {
      setState({
        total,
        dataSource: data,
        loading: false,
        // checkedKeys: reload ? [] : state.checkedKeys,
        flag: true
      });
    }
  };
  return (
    <SearchTableContext.Provider value={{ state, setState }}>
      <div
        className={props.theme === 'dark' && styles.searchWrap}
        style={{ padding: '8px 31px 64px 31px' }}
      >
        {props.title && (
          <h1 className="ft-18 ft-white mg-t1x">{props.title}</h1>
        )}
        {props.filterData && <TitleNode {...props} />}
        {props.commonFormProps && <SearchNode {...props} />}
      </div>
      <TableNode {...props} />
      <FooterNode {...props} />
    </SearchTableContext.Provider>
  );
};
export default SearchTable;

SearchTable.defaultProps = {
  theme: 'dark'
};
