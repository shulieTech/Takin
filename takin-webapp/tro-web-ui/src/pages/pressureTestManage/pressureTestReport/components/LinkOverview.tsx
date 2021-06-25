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
import Header from 'src/pages/linkMark/components/Header';
import { CommonTable } from 'racc';
import { customColumnProps } from 'src/components/custom-table/utils';
import { ColumnProps } from 'antd/lib/table';
interface Props {
  dataSource: any;
}
const LinkOverview: React.FC<Props> = props => {
  const { dataSource } = props;
  const getLinkOverviewColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...customColumnProps,
        title: '业务活动',
        dataIndex: 'businessActivityName'
      },
      {
        ...customColumnProps,
        title: '总请求数',
        dataIndex: 'totalRequest'
      },
      {
        ...customColumnProps,
        title: '平均TPS（实际/目标）',
        dataIndex: 'tps',
        render: (text, row) => {
          return (
            <Fragment>
              <span
                style={{
                  color:
                    Number(text.result) < Number(text.value) ? '#FE7D61' : ''
                }}
              >
                {text.result}
              </span>
              <span style={{ margin: '0 8px' }}>/</span>
              <span>{text.value}</span>
            </Fragment>
          );
        }
      },
      {
        ...customColumnProps,
        title: '平均RT（实际/目标）',
        dataIndex: 'avgRT',
        render: (text, row) => {
          return (
            <Fragment>
              <span
                style={{
                  color:
                    Number(text.result) > Number(text.value) ? '#FE7D61' : ''
                }}
              >
                {text.result}ms
              </span>
              <span style={{ margin: '0 8px' }}>/</span>
              <span>{text.value}ms</span>
            </Fragment>
          );
        }
      },
      {
        ...customColumnProps,
        title: '成功率（实际/目标）',
        dataIndex: 'sucessRate',
        render: (text, row) => {
          return (
            <Fragment>
              <span
                style={{
                  color:
                    Number(text.result) < Number(text.value) ? '#FE7D61' : ''
                }}
              >
                {text.result}%
              </span>
              <span style={{ margin: '0 8px' }}>/</span>
              <span>{text.value}%</span>
            </Fragment>
          );
        }
      },
      {
        ...customColumnProps,
        title: 'SA（实际/目标）',
        dataIndex: 'sa',
        render: (text, row) => {
          return (
            <Fragment>
              <span
                style={{
                  color:
                    Number(text.result) < Number(text.value) ? '#FE7D61' : ''
                }}
              >
                {text.result}%
              </span>
              <span style={{ margin: '0 8px' }}>/</span>
              <span>{text.value}%</span>
            </Fragment>
          );
        }
      }
    ];
  };
  return (
    <Fragment>
      <Header title="链路概览" />
      <CommonTable
        size="small"
        style={{ marginTop: 8 }}
        columns={getLinkOverviewColumns()}
        dataSource={dataSource}
      />
    </Fragment>
  );
};
export default LinkOverview;
