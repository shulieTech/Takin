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
import { Collapse } from 'antd';
import CustomTable from 'src/components/custom-table';
import styles from './../index.less';
import { customColumnProps } from 'src/components/custom-table/utils';
import { ColumnProps } from 'antd/lib/table';
import BusinessActivityPressureTestDetailModal from '../modals/BusinessActivityPressureTestDetailModal';
interface Props {
  data?: any;
  id?: string;
}
const BusinessActivityFailList: React.FC<Props> = props => {
  const { data, id } = props;

  const getBusinessActivityFailColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...customColumnProps,
        title: '业务活动',
        dataIndex: 'businessActivityName'
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
    <div className={styles.failWrap}>
      <Collapse expandIconPosition="right">
        <Collapse.Panel
          header={
            <span
              style={{
                color: '#393B4F',
                fontSize: '18px',
                fontWeight: 600,
                marginLeft: 16
              }}
            >
              <span
                style={{
                  color: '#FE7D61',
                  fontSize: '24px',
                  fontWeight: 500,
                  marginRight: 6
                }}
              >
                {data && data.length}
              </span>
              个业务活动不达标
            </span>
          }
          key="1"
        >
          <CustomTable
            columns={getBusinessActivityFailColumns()}
            dataSource={data}
          />
        </Collapse.Panel>
      </Collapse>
    </div>
  );
};
export default BusinessActivityFailList;
