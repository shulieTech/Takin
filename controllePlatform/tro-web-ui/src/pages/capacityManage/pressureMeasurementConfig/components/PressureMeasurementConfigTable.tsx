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

const getColumns = (state, setState): ColumnProps<any>[] => {
  return [
    {
      ...customColumnProps,
      title: '压测任务名称',
      dataIndex: 'projectName'
    },
    {
      ...customColumnProps,
      title: '任务类型',
      dataIndex: 'methodInfo'
    },
    {
      ...customColumnProps,
      title: '压测引擎',
      dataIndex: 'updateTime'
    },
    {
      ...customColumnProps,
      title: '最新压测时间',
      dataIndex: 'updateTime'
    },
    {
      ...customColumnProps,
      title: '当前压测状态',
      dataIndex: 'updateTime'
    },
    {
      ...customColumnProps,
      title: '操作',
      dataIndex: 'action',
      render: (text, row) => {
        return <Fragment>1</Fragment>;
      }
    }
  ];
};

export default getColumns;
