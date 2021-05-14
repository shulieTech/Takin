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
const PressureScene: React.FC<Props> = props => {
  const { data } = props;

  const columns = [
    {
      title: '压测场景时间',
      dataIndex: 'sceneName',
      key: 'sceneName',
      render: text => {
        return <div style={{ fontSize: '18px' }}>{text}</div>;
      }
    },
    {
      title: '最大并发',
      dataIndex: 'threadNum',
      key: 'threadNum',
      render: text => {
        return <div style={{ color: '#A2A6B1' }}>最大并发：{text}</div>;
      }
    },
    {
      title: '开始时间',
      dataIndex: 'lastPtTime',
      key: 'lastPtTime',
      render: text => {
        return <div style={{ color: '#A2A6B1' }}>开始时间：{text}</div>;
      }
    },
    {
      title: '查看实况',
      dataIndex: 'action',
      key: 'action',
      align: 'right',
      render: (text, row) => {
        return (
          <Link
            to={`/pressureTestManage/pressureTestReport/pressureTestLive?id=${row.id}`}
          >
            查看实况
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
            <span className={`${styles.boldTitle}`}>正在压测场景</span>
          </Col>
          <Col>
            <Link
              to="/pressureTestManage/pressureTestScene"
              className={styles.more}
            >
              更多 <Icon type="right" />
            </Link>
          </Col>
        </Row>
        {data && data.length > 0 ? (
          <Table
            rowKey={record => record.id}
            style={{ marginTop: 24 }}
            dataSource={data}
            columns={columns}
            showHeader={false}
            size="small"
            pagination={false}
          />
        ) : (
          <div className={styles.defaultWrap}>
            <div className={styles.circle} />
            <p className={styles.defaultTxt}>没有正在进行的压测</p>
          </div>
        )}
      </div>
    );
  }
  return <Loading />;
};
export default PressureScene;
