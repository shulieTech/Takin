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
import React from 'react';
import { customColumnProps } from 'src/components/custom-table/utils';

const OperationLogTable = (state, setState): ColumnProps<any>[] => {
  return [
    {
      ...customColumnProps,
      title: '操作模块',
      dataIndex: 'modules',
      width: 200,
      render: text => {
        return text && text.length > 0 ? (
          <span>
            {text.map((item, key) => {
              if (key === text.length - 1) {
                return <span key={key}>{item}</span>;
              }
              return <span key={key}>{item} - </span>;
            })}
          </span>
        ) : (
          <span>-</span>
        );
      }
    },
    {
      ...customColumnProps,
      title: '操作类型',
      dataIndex: 'type',
      width: 80
    },
    {
      ...customColumnProps,
      title: '操作描述',
      dataIndex: 'content',
      width: 300
    },
    {
      ...customColumnProps,
      title: '操作人',
      dataIndex: 'userName',
      width: 80
    },
    {
      ...customColumnProps,
      title: '操作时间',
      dataIndex: 'startTime',
      width: 150
    }
  ];
};

export default OperationLogTable;
