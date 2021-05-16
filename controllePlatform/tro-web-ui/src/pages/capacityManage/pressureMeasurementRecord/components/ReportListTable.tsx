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
import ReportList from './ReportListDrawer';
import Link from 'umi/link';

const getReportListColumns = (): ColumnProps<any>[] => {
  return [
    {
      ...customColumnProps,
      title: '业务流程名称',
      dataIndex: 'projectName'
    },
    {
      ...customColumnProps,
      title: '压测开始时间',
      dataIndex: 'updateTime',
      width: 90
    },
    {
      ...customColumnProps,
      title: '压测结束时间',
      dataIndex: 'updateTime',
      width: 90
    },
    {
      ...customColumnProps,
      title: '压测时长',
      dataIndex: '1'
    },
    {
      ...customColumnProps,
      title: '压测结果',
      dataIndex: '1'
    },
    {
      ...customColumnProps,
      title: '操作',
      dataIndex: 'action',
      width: 70,
      render: (text, row) => {
        return (
          <Fragment>
            <Link
              to={`/capacityManage/pressureMeasurementRecord/details?id=${row.id}`}
            >
              报告详情
            </Link>
          </Fragment>
        );
      }
    }
  ];
};

export default getReportListColumns;
