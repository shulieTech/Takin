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
import styles from './../index.less';

const getNodeManageListColumns = (): ColumnProps<any>[] => {
  return [
    {
      ...customColumnProps,
      title: 'Agent ID',
      dataIndex: 'agentId'
    },
    {
      ...customColumnProps,
      title: 'IP',
      dataIndex: 'ip'
    },
    {
      ...customColumnProps,
      title: '进程号',
      dataIndex: 'processNumber'
    },
    {
      ...customColumnProps,
      title: 'Agent 版本',
      dataIndex: 'agentVersion',
      render: text => {
        return <span>V {text} </span>;
      }
    },
    {
      ...customColumnProps,
      title: 'Agent 语言',
      dataIndex: 'agentLang'
    },
    {
      ...customColumnProps,
      title: '更新时间',
      dataIndex: 'updateTime'
    }
  ];
};

export default getNodeManageListColumns;
