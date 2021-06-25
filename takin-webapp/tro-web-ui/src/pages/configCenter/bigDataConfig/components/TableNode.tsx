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
 * @author chuxu
 */
import { ColumnProps } from 'antd/lib/table';
import React, { Fragment } from 'react';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';
import { customColumnProps } from 'src/components/custom-table/utils';
import { MapBtnAuthority } from 'src/utils/utils';
import { BigDataBean } from '../enum';
import { BigDataConfigState } from '../indexPage';
import EditModal from '../modals/EditModal';

const getColumns = (
  state: BigDataConfigState,
  setState: (state: Partial<BigDataConfigState>) => void
): ColumnProps<any>[] => {
  return [
    {
      ...customColumnProps,
      title: '序号',
      dataIndex: 'order',
      render: (text, row, index) => index + 1
    },
    {
      ...customColumnProps,
      title: 'key',
      dataIndex: BigDataBean.key
    },
    {
      ...customColumnProps,
      title: '说明',
      dataIndex: BigDataBean.说明
    },
    {
      ...customColumnProps,
      title: 'value',
      dataIndex: BigDataBean.value
    },
    {
      ...customColumnProps,
      title: '最后修改时间',
      dataIndex: BigDataBean.最后修改时间
    },
    {
      ...customColumnProps,
      title: '操作',
      dataIndex: 'actions',
      render: (text, row) => (
        <Fragment>
          <AuthorityBtn
            isShow={MapBtnAuthority('configCenter_bigDataConfig_3_update')}
          >
            <EditModal
              onSuccess={() => setState({ reload: !state.reload })}
              details={row}
              id={row.id}
            />
          </AuthorityBtn>
        </Fragment>
      )
    }
  ];
};

export default getColumns;
