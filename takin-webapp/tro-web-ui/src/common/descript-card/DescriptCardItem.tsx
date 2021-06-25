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
import React from 'react';
import { Card, Row, Col } from 'antd';
import styles from './index.less';
import { DescriptCardItemProps } from './type';

const DescriptCardItem: React.FC<DescriptCardItemProps> = props => {
  const dataSource = props.dataSource;
  return (
    <Card bodyStyle={{ padding: 0 }} extra={props.extra}>
      <h2 className={styles.title} style={{ padding: '8px 12px' }}>
        {props.header}
      </h2>
      {props.columns.map((item, index) => (
        <Row
          className={styles.cardItem}
          type="flex"
          key={item.dataIndex}
          style={{
            borderBottom: index === props.columns.length - 1 && 'none'
          }}
        >
          <Col className={styles.label}>{item.title}</Col>
          <Col className={styles.text}>
            {dataSource &&
            (dataSource[item.dataIndex] ||
              dataSource[item.dataIndex] === 0 ||
              dataSource[item.dataIndex] === false)
              ? item.render
                ? item.render(dataSource[item.dataIndex], dataSource)
                : dataSource[item.dataIndex]
              : props.emptyNode}
          </Col>
        </Row>
      ))}
    </Card>
  );
};
export default DescriptCardItem;

DescriptCardItem.defaultProps = {
  emptyNode: '-'
};
