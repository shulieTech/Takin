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
import { Button, Divider, Badge } from 'antd';

const getPressureTestReportColumns = (state, setState): ColumnProps<any>[] => {
  return [
    {
      ...customColumnProps,
      title: '压测报告ID',
      dataIndex: 'id'
    },
    {
      ...customColumnProps,
      title: '压测场景名称',
      dataIndex: 'sceneName'
    },
    {
      ...customColumnProps,
      title: '最大并发',
      dataIndex: 'concurrent'
    },
    {
      ...customColumnProps,
      title: '压测时长',
      dataIndex: 'totalTime'
    },
    {
      ...customColumnProps,
      title: '消耗流量（vum）',
      dataIndex: 'amount'
    },
    {
      ...customColumnProps,
      title: '压测开始时间',
      dataIndex: 'startTime'
    },
    {
      ...customColumnProps,
      title: '压测结果',
      dataIndex: 'conclusion',
      render: text => {
        return (
          <Badge
            text={text === 1 ? '通过' : '不通过'}
            color={text === 1 ? '#11BBD5' : '#FE7D61'}
          />
        );
      }
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
            <Link
              to={`/pressureTestManage/pressureTestReport/details?id=${row.id}`}
            >
              查看报告
            </Link>
          </Fragment>
        );
      }
    }
  ];
};

export default getPressureTestReportColumns;
