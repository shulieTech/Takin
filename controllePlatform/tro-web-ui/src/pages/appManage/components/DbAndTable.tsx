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

import React, { Fragment, useEffect } from 'react';
import { useStateReducer } from 'racc';
import AppManageService from '../service';
import LinkDbTable from './LinkDbTable';
interface Props {
  id?: string;
  detailData?: any;
  detailState?: any;
  action?: any;
}
const DbAndTable: React.FC<Props> = props => {
  const [state, setState] = useStateReducer({
    dataSource: null,
    // planType: null,
    isReload: false,
    loading: false
  });
  const { detailState, action } = props;

  useEffect(() => {
    queryList();
  }, [state.isReload]);

  /**
   * @name 获取影子库表列表
   */
  const queryList = async () => {
    setState({
      loading: true
    });
    const {
      data: { data, success }
    } = await AppManageService.queryDbAndTableList({ applicationId: props.id });
    if (success) {
      setState({
        // planType: data.planType,
        dataSource: data,
        loading: false
      });
      return;
    }
    setState({
      loading: false
    });
  };

  return (
    <LinkDbTable
      detailState={detailState}
      setState={setState}
      state={state}
      id={props.id}
      detailData={props.detailData}
      action={action}
    />
  );
};
export default DbAndTable;
