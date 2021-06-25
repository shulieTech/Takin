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
 * @author MingShined
 */
import { Col, Divider, Icon, Row } from 'antd';
import React, { useContext } from 'react';
import { router } from 'umi';
import { BusinessActivityDetailsContext } from '../detailsPage';
import { ActivityBean } from '../enum';
import styles from '../index.less';

interface Props {}
const HeaderNode: React.FC<Props> = props => {
  const { state } = useContext(BusinessActivityDetailsContext);
  const divider: React.ReactNode = (
    <Divider
      type="vertical"
      style={{ height: 16, width: 1, background: '#f0f0f0', margin: '0 16px' }}
    />
  );
  return (
    <Row
      align="middle"
      style={{ padding: '8px 24px', background: '#fff', height: 56 }}
      type="flex"
      justify="space-between"
    >
      <Col className="flex" style={{ alignItems: 'center' }}>
        <span
          style={{
            fontWeight: 'bold',
            // boxShadow: '0px 0px 12px 0px rgba(177, 192, 192, 0.45)',
            color: '#11BBD5',
            fontSize: 16,
            cursor: 'pointer'
          }}
          onClick={() => router.push('/businessActivity')}
        >
          <Icon theme="outlined" className={styles.leftIcon} type="left" />
          返回
        </span>
        {divider}
        <span
          style={{
            fontWeight: 600,
            fontSize: 18,
            // padding: '0 16px',
            // margin: '0 16px',
            // borderRight: '1px solid #E8E8E8',
            // borderLeft: '1px solid #E8E8E8',
            color: '#353535'
          }}
        >
          业务活动详情
        </span>
        {divider}
        <span style={{ color: '#353535', fontSize: 14 }}>
          {state.details[ActivityBean.业务活动名称]}
        </span>
      </Col>
      <Col>负责人：{state.details[ActivityBean.负责人] || '--'}</Col>
    </Row>
  );
};
export default HeaderNode;
