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
import { BasePageLayout } from 'src/components/page-layout';

import { useStateReducer } from 'racc';
import AppManageService from './service';
import AppDetailHeader from './components/AppDetailHeader';
import AppDetailTabs from './components/AppDetailTabs';
import CustomSkeleton from 'src/common/custom-skeleton';
import TableWarning from './components/TableWarning';
import { Pagination, Checkbox } from 'antd';

interface Props {
  location?: { query?: any };
}
const AppManageDetail: React.FC<Props> = props => {
  const [state, setState] = useStateReducer({
    detailData: {} as any,
    isReload: false,
    switchStatus: null,
    visible: false,
    isNewAgent: null
  });

  const { location } = props;
  const { query } = location;
  const { id, tabKey, action } = query;

  useEffect(() => {
    queryAppDetail(id);
    querySwitchStatus();
    queryAgentStatus();
  }, [state.isReload]);

  /**
   * @name 获取压测开关状态
   */
  const querySwitchStatus = async () => {
    const {
      data: { data, success }
    } = await AppManageService.querySwitchStatus({});
    if (success) {
      setState({
        switchStatus: data.switchStatus
      });
    }
  };

  /**
   * @name 获取agent版本（true新false旧）
   */
  const queryAgentStatus = async () => {
    const {
      data: { data, success }
    } = await AppManageService.queryAgentStatus({});
    if (success) {
      setState({
        isNewAgent: data
      });
    }
  };

  /**
   * @name 获取应用详情
   */
  const queryAppDetail = async value => {
    const {
      data: { data, success }
    } = await AppManageService.queryAppManageDetail({
      id: value
    });
    if (success) {
      setState({
        detailData: data
      });
    }
  };
  return (
    <Fragment>
      {JSON.stringify(state.detailData) !== '{}' ? (
        <div style={{ overflowY: 'hidden', height: '100%' }}>
          <div
            style={{
              padding: 20,
              height: '100%',
              overflowY: 'hidden'
            }}
          >
            <AppDetailHeader
              detailData={state.detailData}
              id={id}
              state={state}
              setState={setState}
              action={action}
            />
            {state.switchStatus !== 'OPENED' && (
              <TableWarning state={state} setState={setState} />
            )}
            <AppDetailTabs
              detailState={state}
              detailData={state.detailData}
              tabKey={tabKey}
              id={id}
              action={action}
            />
          </div>
        </div>
      ) : (
        <CustomSkeleton />
      )}
    </Fragment>
  );
};
export default AppManageDetail;
