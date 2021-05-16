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
 * @author chuxu
 */
import React, { Fragment, ReactNode } from 'react';
import { Row, Col, Divider, Tooltip } from 'antd';

import styles from './index.less';
interface Props {
  leftWrapData?: {
    label: string | ReactNode;
    value: string | ReactNode;
    isTooltip?: boolean;
  };
  rightWrapData?: {
    label: string | ReactNode;
    value: string | ReactNode;
    isTooltip?: boolean;
  }[];
  extra?: string | React.ReactNode;
}
const DetailHeader: React.FC<Props> = props => {
  const { leftWrapData, rightWrapData } = props;
  return (
    <Row type="flex" style={{ position: 'relative' }}>
      {leftWrapData && (
        <Col>
          <p className={styles.label}>{leftWrapData.label}</p>
          {leftWrapData.isTooltip ? (
            <Tooltip title={leftWrapData.value}>
              <p className={styles.leftValue}>{leftWrapData.value || '--'}</p>
            </Tooltip>
          ) : (
            <p className={styles.leftValue}>{leftWrapData.value || '--'}</p>
          )}
        </Col>
      )}
      {leftWrapData && rightWrapData && (
        <Col>
          <Divider
            type="vertical"
            style={{ height: '80%', marginLeft: 40, marginRight: 40 }}
          />
        </Col>
      )}
      {rightWrapData && (
        <Col>
          <Row type="flex">
            {rightWrapData.map((item, k) => {
              return (
                <Col
                  key={k}
                  style={{
                    marginTop: 10,
                    marginRight: 40,
                    maxWidth: 240,
                    maxHeight: 40,
                    overflow: 'hidden'
                  }}
                >
                  <p className={styles.label}>{item.label}</p>
                  {/* <Tooltip title={item.value}> */}
                  <p className={styles.rightValue}>{item.value || '--'}</p>
                  {/* </Tooltip> */}
                </Col>
              );
            })}
          </Row>
        </Col>
      )}
      {props.extra}
    </Row>
  );
};
export default DetailHeader;
