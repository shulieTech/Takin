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

import { Col, Row } from 'antd';
import React from 'react';
import styles from './index.less';
interface MainPageLayoutProps {
  title?: string | React.ReactNode;
  extra?: string | React.ReactNode;
}
const MainPageLayout: React.FC<MainPageLayoutProps> = props => {
  return (
    <div className="pd-2x">
      <Row type="flex" justify="space-between" align="middle">
        <Col>
          <h1 className={styles.title}>{props.title}</h1>
        </Col>
        <Col>{props.extra}</Col>
      </Row>
      {props.children}
    </div>
  );
};
export default MainPageLayout;
