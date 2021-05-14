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
import { appConfigStatusMap, appConfigStatusColorMap } from '../enum';
import AppManageService from '../service';

const getColumns = (state, setState): ColumnProps<any>[] => {
  const userType: string = localStorage.getItem('troweb-role');
  const expire: string = localStorage.getItem('troweb-expire');

  /**
   * @name 确认是否允许压测
   */
  const handleConfirm = async (id, enable) => {
    const {
      data: { data, success }
    } = await AppManageService.editAppStatus({
      id,
      enable
    });
    if (success) {
      message.success('操作成功');
      setState({
        isReload: !state.isReload
      });
    }
  };

  return [
    {
      ...customColumnProps,
      title: '应用名称',
      dataIndex: 'applicationName'
    },
    {
      ...customColumnProps,
      title: '接入状态',
      dataIndex: 'accessStatus',
      render: (text, row) => {
        return (
          <Badge
            text={appConfigStatusMap[`${text}`]}
            color={appConfigStatusColorMap[`${text}`]}
          />
        );
      }
    },
    {
      ...customColumnProps,
      title: '最后修改时间',
      dataIndex: 'updateTime'
    },
    {
      ...customColumnProps,
      title: '负责人',
      dataIndex: 'managerName'
    },
    {
      ...customColumnProps,
      title: '操作',
      dataIndex: 'action',
      align: 'right',
      render: (text, row) => {
        return (
          <Fragment>
            <Link to={`/appManage/details?tabKey=0&id=${row.id}`}>
              应用详情
            </Link>
          </Fragment>
        );
      }
    }
  ];
};

export default getColumns;
