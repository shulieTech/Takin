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
import { CommonTable, useStateReducer } from 'racc';
import getColumns from './ExitTableColumn';
import CustomTable from 'src/components/custom-table';
import TableTitle from 'src/common/table-title/TableTitle';
import { Divider } from 'antd';
import styles from './../index.less';
import ConfigBaffleDrawer from './ConfigBaffleDrawer';
import AppManageService from '../service';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';

interface Props {
  id?: string;
  detailData?: any;
  detailState?: any;
  action?: string;
}
interface State {
  isReload: boolean;
  jobList: any[];
  loading: boolean;
}
const ExitJob: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    isReload: false,
    jobList: null,
    loading: false
  });
  const { detailData, id, detailState, action } = props;
  const btnAuthority: any =
    localStorage.getItem('trowebBtnResource') &&
    JSON.parse(localStorage.getItem('trowebBtnResource'));

  useEffect(() => {
    queryJobList();
  }, [state.isReload]);

  /**
   * @name 获取挡板列表
   */
  const queryJobList = async () => {
    setState({
      loading: true
    });
    const {
      data: { success, data }
    } = await AppManageService.queryAllJobList({ applicationId: id });
    if (success) {
      setState({
        jobList: data,
        loading: false
      });
      return;
    }
    setState({
      loading: false
    });
  };

  return (
    <div className={styles.tableWrap}>
      <TableTitle
        title="出口挡板"
        extraNode={
          <AuthorityBtn
            isShow={btnAuthority && btnAuthority.appManage_2_create}
          >
            <div className={styles.addAction}>
              <ConfigBaffleDrawer
                disabled={
                  detailState.switchStatus === 'OPENING' ||
                  detailState.switchStatus === 'CLOSING'
                    ? true
                    : false
                }
                title="新增挡板"
                action="add"
                detailData={detailData}
                id={id}
                onSccuess={() => {
                  setState({
                    isReload: !state.isReload
                  });
                }}
              />
            </div>
          </AuthorityBtn>
        }
      />
      {/* <Divider /> */}
      <CustomTable
        style={{ marginTop: 30 }}
        loading={state.loading}
        columns={getColumns(state, setState, detailState, action)}
        dataSource={state.jobList ? state.jobList : []}
      />
    </div>
  );
};
export default ExitJob;
