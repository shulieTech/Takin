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
import React, { Fragment } from 'react';
import styles from './index.less';
interface Props {
  dataSource: {
    name: string;
    value: string;
    color?: string;
    extra?: string;
  }[];
  title: string | React.ReactNode;
  img?: React.ReactNode;
}
const CustomDetailHeader: React.FC<Props> = props => {
  const { title, dataSource } = props;
  return (
    <Row type="flex">
      <Col>{props.img}</Col>
      <Col style={{ marginLeft: 16 }}>
        <div className={styles.title}>{title}</div>
        <div>
          {dataSource &&
            dataSource.map((item, key) => {
              return (
                <p className={styles.details} key={key}>
                  <span className={styles.detailsLabel}>{item.name}：</span>
                  <span
                    className={styles.detailsValue}
                    style={{ color: item.color }}
                  >
                    {item.value}
                    {item.extra ? (
                      <span style={{ color: '#595959', marginLeft: 4 }}>
                        {item.extra}
                      </span>
                    ) : null}
                  </span>
                </p>
              );
            })}
        </div>
      </Col>
    </Row>
  );
};
export default CustomDetailHeader;
