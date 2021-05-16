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
import React, { Fragment } from 'react';
import { Row, Col } from 'antd';
import styles from './index.less';
interface DetailsLayoutProps {
  siderStyle?: React.CSSProperties;
  sider: React.ReactNode;
}
const DetailsLayout: React.FC<DetailsLayoutProps> = props => {
  return (
    <Row className="flex h-100p">
      <Col style={props.siderStyle} className={styles.detailsSider}>
        {props.sider}
      </Col>
      <Col className="flex-1" style={{ padding: '8px 16px' }}>
        {props.children}
      </Col>
    </Row>
  );
};
export default DetailsLayout;
