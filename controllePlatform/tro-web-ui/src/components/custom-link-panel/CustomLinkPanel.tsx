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
import { Collapse, Icon } from 'antd';
import styles from './index.less';

interface Props {
  title: any;
}
const CustomLinkPanel: React.FC<Props> = props => {
  const { Panel } = Collapse;
  return (
    <Collapse
      defaultActiveKey={['1']}
      expandIconPosition="right"
      style={{ marginTop: 16, position: 'relative' }}
      expandIcon={({ isActive }) => (
        <Icon type="caret-right" rotate={isActive ? 90 : -90} />
      )}
    >
      <Panel
        className={styles.linkCard}
        header={<p className={styles.panelTitle}>{props.title}</p>}
        key="1"
      >
        {props.children}
      </Panel>
    </Collapse>
  );
};
export default CustomLinkPanel;
