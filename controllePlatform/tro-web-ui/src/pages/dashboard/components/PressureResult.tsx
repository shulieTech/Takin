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
import styles from './../index.less';
import { Tooltip, Icon, Row, Col, Table } from 'antd';
import Link from 'umi/link';
import Loading from 'src/common/loading';
interface Props {
  data?: any;
}
const PressureResult: React.FC<Props> = props => {
  const { data } = props;

  const columns = [
    {
      title: '压测任务',
      dataIndex: 'sceneName',
      key: 'sceneName',
      render: text => {
        return (
          <div
            style={{
              fontSize: '18px',
              fontFamily: 'PingFangSC-Regular,PingFang SC'
            }}
          >
            {text}
          </div>
        );
      }
    },
    {
      title: '压测结果',
      dataIndex: 'conclusion',
      key: 'conclusion',
      render: (text, row) => {
        return (
          <Fragment>
            <span
              style={{
                display: 'inline-block',
                width: 69,
                height: 24,
                backgroundColor: text === 1 ? '#11BBD5' : '#FE7D61',
                color: '#fff',
                textAlign: 'center',
                lineHeight: '24px',
                borderRadius: 13
              }}
            >
              {text === 1 ? '通过' : '不通过'}
            </span>
            {text !== 1 && (
              <span style={{ color: '#FE7D61', marginLeft: 8 }}>
                {row.errorMsg}
              </span>
            )}
          </Fragment>
        );
      }
    },
    {
      title: '压测开始时间',
      dataIndex: 'startTime',
      key: 'startTime',
      render: text => {
        return <div style={{ color: '#A2A6B1' }}>开始时间：{text}</div>;
      }
    },
    {
      title: '操作',
      dataIndex: 'action',
      key: 'action',
      align: 'right',
      render: (text, row) => {
        return (
          <Link
            to={`/pressureTestManage/pressureTestReport/details?id=${row.id}`}
          >
            查看报告
          </Link>
        );
      }
    }
  ];
  if (data) {
    return (
      <div className={styles.border} style={{ marginLeft: 32 }}>
        <Row type="flex" align="middle" justify="space-between">
          <Col>
            <span className={styles.blueline} />
            <span className={`${styles.boldTitle}`}>近期压测结果</span>
          </Col>
          <Col>
            <Link
              to="/pressureTestManage/pressureTestReport"
              className={styles.more}
            >
              更多 <Icon type="right" />
            </Link>
          </Col>
        </Row>
        <Table
          rowkey={record => record.id}
          style={{ marginTop: 24 }}
          dataSource={data}
          columns={columns}
          showHeader={false}
          size="small"
          pagination={false}
        />
      </div>
    );
  }
  return <Loading />;
};
export default PressureResult;
