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

import { Affix, Col, Row } from 'antd';
import React, { Fragment } from 'react';
import styles from './index.less';
interface Props {
  title: string | React.ReactNode;
  extra?: React.ReactNode;
}
const FixedTopLayout: React.FC<Props> = props => {
  return (
    <div
      style={{
        width: '100%',
        height: '100%',
        position: 'relative'
      }}
    >
      <Row type="flex" justify="space-between" className={styles.fixedTopWrap}>
        <Col className={styles.fixedTopTitle}>{props.title}</Col>
        <Col>{props.extra}</Col>
      </Row>
      <div style={{ marginTop: 54, height: 'calc(100% - 54px)' }}>
        {props.children}
      </div>
    </div>
  );
};
export default FixedTopLayout;
