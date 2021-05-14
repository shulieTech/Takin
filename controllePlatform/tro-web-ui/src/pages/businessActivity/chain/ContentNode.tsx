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
import { Col, Icon, Row } from 'antd';
import React, { useContext } from 'react';
import { BusinessActivityDetailsContext } from '../detailsPage';
import styles from '../index.less';
import ActionNode from './ActionNode';
import GraphNode from './GraphNode';
import NodeInfoDrawer from './NodeInfoDrawer';
import SiderNode from './SiderNode';

export const infoBarHeight = 40;

interface Props {}
const ContentNode: React.FC<Props> = props => {
  const { state, setState } = useContext(BusinessActivityDetailsContext);
  return (
    <div
      style={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        flex: 1
      }}
    >
      {state.infoBarVisible && (
        <div className={styles.infoBar} style={{ height: infoBarHeight }}>
          ForceCop已梳理出该链路
          <span className={styles.num}>
            {state.details.topology.nodes.length}
          </span>
          个节点及其调用关系，您还可通过「流量验证」操作进行链路信息进一步确认与完善
          {/* 个节点及其调用关系，您还可通过「流量验证」、「添加链路分支」进等操作行链路信息进一步确认与完善 */}
          <Icon
            onClick={() => setState({ infoBarVisible: false })}
            className={styles.close}
            type="close"
          />
        </div>
      )}
      <Row
        type="flex"
        style={{
          height: '100%',
          padding: 8,
          position: 'relative',
          overflow: 'hidden',
          flex: 1
        }}
      >
        {state.siderVisible && (
          <Col style={{ width: 280, height: '100%' }}>
            <SiderNode />
          </Col>
        )}
        <Col className="flex-1" style={{ height: '100%' }}>
          <GraphNode />
        </Col>
        <ActionNode />
        <NodeInfoDrawer />
      </Row>
    </div>
  );
};
export default ContentNode;
