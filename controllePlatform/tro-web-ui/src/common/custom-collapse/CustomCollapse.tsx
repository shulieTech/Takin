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

import React, { Fragment, ReactNode } from 'react';
import { BasePageLayout } from 'src/components/page-layout';
import { Collapse, Empty, Skeleton } from 'antd';
import styles from './index.less';
interface Props {
  dataSource: any[];
  extra?: any | ReactNode;
  style?: React.CSSProperties;
}
const CustomCollapse: React.FC<Props> = props => {
  const { dataSource } = props;
  const { Panel } = Collapse;

  const customPanelStyle = {
    background: '#ffffff',
    borderRadius: 2,
    marginBottom: 8,
    border: '1px solid #F0F0F0',
    overflow: 'hidden'
  };
  return dataSource && dataSource.length > 0 ? (
    <div className={styles.collapseWrap} style={props.style || {}}>
      <Collapse
        defaultActiveKey={['0']}
        //   expandIconPosition="right"
        bordered={false}
      >
        {dataSource.map((item, k) => {
          return (
            <Panel
              style={customPanelStyle}
              header={
                <div style={{ position: 'relative' }}>
                  <div className={styles.title}>{item.title}</div>
                  <p className={styles.subTitle}>{item.subTitle}</p>
                  {props.extra}
                </div>}
              key={k}
            >
              {props.children}
            </Panel>
          );
        })}
      </Collapse>
    </div>
  ) : (
    <Empty />
  );
};
export default CustomCollapse;
