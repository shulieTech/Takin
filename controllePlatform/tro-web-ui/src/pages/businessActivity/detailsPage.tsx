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
import { Graph } from '@antv/g6';
import { Icon, message } from 'antd';
import { useCreateContext, useStateReducer } from 'racc';
import React, { useEffect } from 'react';
import SkeletonLoading from 'src/common/loading/SkeletonLoading';
import { Basic } from 'src/types';
import ContentNode from './chain/ContentNode';
import { transformEdges, transformNodes } from './chain/GraphNode';
import HeaderNode from './chain/HeaderNode';
import { NodeBean } from './enum';
import BusinessActivityService from './service';

const getInitState = () => ({
  siderVisible: true,
  infoBarVisible: true,
  nodeVisible: false,
  nodeInfo: null as NodeBean,
  details: null,
  graph: null as Graph,
  node: null as HTMLElement,
  reload: false
});
type State = ReturnType<typeof getInitState>;
export const BusinessActivityDetailsContext = useCreateContext<State>();

interface Props extends Basic.BaseProps {}
const BusinessActivityDetails: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>(getInitState());
  const id = props.location.query.id;
  useEffect(() => {
    queryActivityDetails();
    return () => message.destroy();
  }, [state.reload]);
  const queryActivityDetails = async () => {
    const {
      data: { data, success }
    } = await BusinessActivityService.getBusinessActivityDetails({
      id
    });
    if (success && data) {
      if (state.graph && state.details && data.verifiedFlag) {
        afterVertifyFlow(data);
      }
      setState({ details: data });
    }
  };
  const afterVertifyFlow = data => {
    const { nodes, edges } = data.topology;
    const length = nodes.length - state.details.topology.nodes.length;
    message.success(
      <span>
        节点检测成功，成功扫描出{length}个节点
        <Icon
          onClick={() => message.destroy()}
          type="close"
          style={{ transform: 'translate(6px, -2px)' }}
        />
      </span>,
      0
    );
    state.graph.changeData({
      nodes: nodes.map(item => transformNodes(item)),
      edges: edges.map(item => transformEdges(item))
    });
  };
  if (!state.details) {
    return <SkeletonLoading />;
  }
  return (
    <BusinessActivityDetailsContext.Provider value={{ state, setState }}>
      <div
        style={{
          background: '#F8F9FA',
          height: '100%',
          display: 'flex',
          flexDirection: 'column',
          overflow: 'hidden'
        }}
      >
        <HeaderNode />
        <ContentNode />
      </div>
    </BusinessActivityDetailsContext.Provider>
  );
};
export default BusinessActivityDetails;
