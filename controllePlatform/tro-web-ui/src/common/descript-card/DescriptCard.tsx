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
import { DescriptCardProps, DescriptCardColumnsBean } from './type';
import { Row, Col, Card } from 'antd';
import styles from './index.less';

const DescriptCard: React.FC<DescriptCardProps> = ({
  columns,
  dataSource,
  emptyNode
}) => {
  let maxLength: number = 0;
  columns.forEach(item => {
    if (item.columns.length > maxLength) {
      maxLength = item.columns.length;
    }
  });
  const renderColumns: any[] = columns.map(item => {
    if (item.columns.length < maxLength && !item.isAlignSelf) {
      return {
        ...item,
        columns: [...Array(maxLength).keys()].map(index => {
          if (item.columns[index]) {
            return item.columns[index];
          }
          return { label: '', key: `${index}${Date.now()}` };
        })
      };
    }
    return { ...item };
  });
  return (
    <Row gutter={24}>
      {renderColumns.map((item, index) => (
        <Col
          className="mg-b2x"
          span={item.span || 8}
          key={`${index}${Date.now() - 1}`}
        >
          <Card bodyStyle={{ padding: 0 }}>
            <Row
              style={{ padding: '8px 12px' }}
              type="flex"
              justify="space-between"
              align="middle"
            >
              <Col>
                <h2 className={styles.title}>{item.header}</h2>
              </Col>
              <Col>{item.extra}</Col>
            </Row>
            {item.columns.map((item2, index2) => {
              const dataIndex = item2.dataIndex || '';
              let curValue = dataSource[dataIndex];
              if (dataIndex.indexOf('.') !== -1) {
                dataIndex.split(',').forEach(key => {
                  if (!curValue) {
                    curValue = undefined;
                    return;
                  }
                  curValue = curValue[key];
                });
              }
              return (
                <Row
                  className={styles.cardItem}
                  type="flex"
                  key={`${item2.dataIndex}${Date.now()}`}
                >
                  <Col className={styles.label} style={item.labelStyle} span={6}>
                    {item2.title}
                  </Col>
                  <Col className={styles.text} span={18}>
                    {dataSource
                      ? item2.render && (curValue || item2.isCombine)
                        ? item2.render(curValue, dataSource, index)
                        : curValue || curValue === 0
                        ? curValue
                        : emptyNode
                      : emptyNode}
                  </Col>
                </Row>
              );
            })}
          </Card>
        </Col>
      ))}
    </Row>
  );
};
export default DescriptCard;

DescriptCard.defaultProps = {
  emptyNode: '-'
};
