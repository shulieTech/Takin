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
import CustomTable from 'src/components/custom-table';
import { customColumnProps } from 'src/components/custom-table/utils';
import { ColumnProps } from 'antd/lib/table';
import CustomPopconfirm from 'src/components/custom-popconfirm/CustomPopconfirm';
import { Button, message, Pagination } from 'antd';
import AppManageService from '../service';
import { useStateReducer } from 'racc';
import styles from './../index.less';
interface Props {
  dataSource: any;
}

const FunctionJurisdictionList: React.FC<Props> = props => {
  const { dataSource } = props;

  const getRoleMemberColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...customColumnProps,
        title: '权限名称',
        dataIndex: 'name'
      }
    ];
  };

  return (
    <CustomTable
      rowKey={(row, index) => index.toString()}
      rowSelection={{
        selectedRowKeys: ['0', '1', '2', '3', '4', '5', '6'],
        getCheckboxProps: record => ({
          disabled: true
        })
      }}
      columns={getRoleMemberColumns()}
      dataSource={dataSource}
    />
  );
};
export default FunctionJurisdictionList;
