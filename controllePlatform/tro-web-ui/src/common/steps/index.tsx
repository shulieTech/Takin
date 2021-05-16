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
 *
 * @name 步骤条
 * @author Xunhuan
 *
 */

import React from 'react';
import { Row, Col } from 'antd';
import styles from './index.less';

interface Props {
  columnsData: any;
  active?: any;
}

const StepsComponent: React.FC<Props> = props => {
  return (
    <div className={styles.steps}>
      <Row justify={'center'}>
        {props.columnsData.map((item, index) => (
          <Col
            key={index}
            span={Math.floor(24 / (props.columnsData.length || 1))}
          >
            <div
              className={
                index === props.columnsData.length - 1
                  ? styles.stepsItemLast
                  : styles.stepsItem
              }
            >
              <span className={index === props.active - 1 ? styles.active : ''} >{item.label}</span>
            </div>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default StepsComponent;
