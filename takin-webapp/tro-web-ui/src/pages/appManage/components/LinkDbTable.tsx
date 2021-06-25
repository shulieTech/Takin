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

import React, { Fragment } from 'react';
import CustomTable from 'src/components/custom-table';
import TableTitle from 'src/common/table-title/TableTitle';
import styles from './../index.less';
import AddAndEditDbDrawer from './AddAndEditDbDrawer';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';
import getLinkDbColumns from './LinkDbColumn';
import AddEditDbModal from '../modals/AddEditDbModal';

interface Props {
  state?: any;
  setState?: (value) => void;
  id?: string;
  detailData?: any;
  detailState?: any;
  action?: any;
}

const LinkDbTable: React.FC<Props> = props => {
  const { state, setState, id, detailData, detailState, action } = props;
  const btnAuthority: any =
    localStorage.getItem('trowebBtnResource') &&
    JSON.parse(localStorage.getItem('trowebBtnResource'));

  return (
    <div
      className={styles.tableWrap}
      style={{ height: document.body.clientHeight - 160 }}
    >
      <TableTitle
        title="关联影子库表"
        extraNode={
          <div className={styles.addAction}>
            {detailState.isNewAgent === true ? (
              <AuthorityBtn
                isShow={btnAuthority && btnAuthority.appManage_2_create}
              >
                <AddEditDbModal
                  applicationId={id}
                  btnText="新增影子库表"
                  detailData={detailData}
                  onSuccess={() => {
                    setState({
                      isReload: !state.isReload
                    });
                  }}
                />
              </AuthorityBtn>
            ) : detailState.isNewAgent === false ? (
              <AuthorityBtn
                isShow={btnAuthority && btnAuthority.appManage_2_create}
              >
                <AddAndEditDbDrawer
                  disabled={
                    detailState.switchStatus === 'OPENING' ||
                    detailState.switchStatus === 'CLOSING'
                      ? true
                      : false
                  }
                  titles="新增影子库表"
                  action="add"
                  id={id}
                  detailData={detailData}
                  onSccuess={() => {
                    setState({
                      isReload: !state.isReload
                    });
                  }}
                />
              </AuthorityBtn>
            ) : null}
          </div>}
      />

      <CustomTable
        rowKey="id"
        style={{ marginTop: 30 }}
        loading={state.loading}
        columns={getLinkDbColumns(state, setState, detailState, id, detailData)}
        dataSource={state.dataSource}
      />
    </div>
  );
};
export default LinkDbTable;
