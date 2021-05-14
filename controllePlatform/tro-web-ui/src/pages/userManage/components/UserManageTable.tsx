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
import React, { Fragment } from 'react';
import { ColumnProps } from 'antd/lib/table';
import _ from 'lodash';
import { customColumnProps } from 'src/components/custom-table/utils';
import Link from 'umi/link';
import { Badge, Popconfirm, Switch, Divider, message } from 'antd';
import AddAndEditUserDrawer from './AddAndEditUserDrawer';
// import { appConfigStatusMap, appConfigStatusColorMap } from '../enum';

const getUserManageColumns = (state, setState): ColumnProps<any>[] => {
  return [
    {
      ...customColumnProps,
      title: '客户名称',
      dataIndex: 'nick'
    },
    {
      ...customColumnProps,
      title: '客户key',
      dataIndex: 'key'
    },
    {
      ...customColumnProps,
      title: '状态',
      dataIndex: 'status',
      render: (text, row) => {
        return (
          <Badge
            text={text === 0 ? '正常' : '禁用'}
            color={text === 0 ? '#11BBD5' : '#FE7D61'}
          />
        );
      }
    },
    {
      ...customColumnProps,
      title: '使用模式',
      dataIndex: 'model',
      render: (text, row) => {
        return (
          <Badge
            text={text === 0 ? '体验模式' : '正式模式'}
            color={text === 0 ? '#A2A6B1' : '#11BBD5'}
          />
        );
      }
    },
    {
      ...customColumnProps,
      title: '账号',
      dataIndex: 'name'
    },
    {
      ...customColumnProps,
      title: '最后修改时间',
      dataIndex: 'gmtUpdate'
    },

    {
      ...customColumnProps,
      title: '操作',
      dataIndex: 'action',
      align: 'right',
      render: (text, row) => {
        return (
          <Fragment>
            <AddAndEditUserDrawer
              titles="编辑"
              action="edit"
              id={row.id}
              onSccuess={() => {
                setState({
                  isReload: !state.id
                });
              }}
            />
          </Fragment>
        );
      }
    }
  ];
};

export default getUserManageColumns;
