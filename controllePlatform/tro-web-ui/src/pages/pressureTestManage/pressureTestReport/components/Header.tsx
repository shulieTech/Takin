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
import { Row, Col } from 'antd';
interface Props {
  list: any[];
  isExtra: boolean;
}
const Header: React.FC<Props> = props => {
  return (
    <Row type="flex" style={{ position: 'relative' }}>
      {props.list.map((item, k) => {
        return (
          <Col key={k} style={{ marginRight: 40 }}>
            <span style={{ color: '#A2A6B1' }}>{item.label}</span>：
            <span style={{ color: '#646676' }}>
              {item.value ? item.value : '-'}
            </span>
          </Col>
        );
      })}
      {props.isExtra && (
        <p
          style={{
            color: '#A2A6B1',
            position: 'absolute',
            top: 0,
            right: 20
          }}
        >
          数据每 5s 刷新一次
        </p>
      )}
    </Row>
  );
};
export default Header;
