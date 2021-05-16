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
import G6, { Graph } from '@antv/g6';
import { GraphOptions } from '@antv/g6/lib/types';
import { Empty } from 'antd';
import React, { Fragment, useContext, useEffect } from 'react';
import G6Graph from 'src/components/g6-graph';
import { getEdgeType, getLabelMaxLength } from 'src/components/g6-graph/utils';
import { BusinessActivityDetailsContext } from '../detailsPage';
import { NodeType } from '../enum';
interface Props {}
const GraphNode: React.FC<Props> = props => {
  const { state, setState } = useContext(BusinessActivityDetailsContext);
  useEffect(() => {
    changeSize();
  }, [state.siderVisible]);
  let graphHeight = null;
  const changeSize = () => {
    const { graph, node } = state;
    if (!graph) {
      return;
    }
    if (!graphHeight) {
      graphHeight = graph.getHeight();
    }
    // if (!state.infoBarVisible) {
    //   graphHeight = graphHeight + infoBarHeight;
    // }
    const siderVisible = state.siderVisible;
    const width = siderVisible ? node.scrollWidth - 280 : node.scrollWidth;
    graph.changeSize(width, graphHeight);
  };
  const handleGraphClick = () => {
    setState({ nodeVisible: false });
  };
  const handleHoverNode = (ev, graph: Graph, active: boolean) => {
    const nodeItem = ev.item;
    const edges = nodeItem.getEdges();
    edges.forEach(edge => graph.setItemState(edge, 'hover', active));
  };
  const handleHoverEdge = (ev, graph: Graph, active: boolean) => {
    const edgeItem = ev.item;
    graph.setItemState(edgeItem, 'hover', active);
  };
  return (
    <G6Graph
      id="g6_graph"
      // data={{ nodes: [], edges: [] }}
      emptyNode={EmptyNode}
      data={{
        nodes: state.details.topology.nodes || [],
        edges: state.details.topology.edges || []
      }}
      handleNodeMouseenter={(ev, graph) => handleHoverNode(ev, graph, true)}
      handleNodeMouseleave={(ev, graph) => handleHoverNode(ev, graph, false)}
      handleEdgeMouseenter={(ev, graph) => handleHoverEdge(ev, graph, true)}
      handleEdgeMouseleave={(ev, graph) => handleHoverEdge(ev, graph, false)}
      onReady={(graph, node) => setState({ graph, node })}
      handleNodeClick={(ev, graph) => {
        if (!ev.item._cfg.model.root) {
          setState({
            siderVisible: false,
            nodeVisible: true,
            nodeInfo: ev.item._cfg.model
          });
        }
      }}
      handleGraphClick={handleGraphClick}
      options={options}
      transformNodes={transformNodes}
      transformEdges={transformEdges}
    />
  );
};
export default GraphNode;

export const transformNodes = (item: any) => {
  let label = item.label as any;
  const maxLength = getLabelMaxLength(label);
  if (label.length > maxLength) {
    label = `${label.slice(0, maxLength)}...`;
  }
  return {
    ...item,
    type: 'image',
    size: [54, 80],
    img: require(`../../../assets/${nodeImgUrlMap[item.nodeType]}.png`),
    labelCfg: {
      style: {
        fontSize: 15,
        fill: '#434343',
        fontWeight: 600
      }
    },
    style: {
      cursor: 'pointer'
    },
    // style: {
    //   shadowColor: 'red',
    //   stroke: 'red',
    //   lineWidth: 4
    // },
    _label: item.label,
    label: `${label}\n`
  };
};

export const transformEdges = item => ({
  ...item,
  type: getEdgeType(item),
  // edgeOffset: getOffset({ ...item, edges: _edges }),
  style: {
    endArrow: {
      size: 1,
      path: G6.Arrow.triangle(5, 5, 5),
      fill: 'l(0) 0:#1FCEE8 1:#08AAD2',
      stroke: 'l(0) 0:#1FCEE8 1:#08AAD2'
    },
    stroke: '#CBCBCB'
  },
  labelCfg: {
    style: {
      fill: '#5A626F',
      fontWeight: 'bold',
      fontSize: 10,
      fontStyle: 'italic',
      background: {
        fill: '#E1E5E7',
        padding: [4, 4, 4, 4],
        radius: 4
      }
    }
  },
  size: 1
});

export const options: Partial<GraphOptions> = {
  edgeStateStyles: {
    active: { lineWidth: 2, stroke: '#11BBD5' },
    hover: { lineWidth: 2, stroke: '#11BBD5' }
  },
  minZoom: 0.2,
  maxZoom: 5,
  modes: {
    default: [
      {
        type: 'tooltip',
        shouldBegin: (evt: any) => {
          const { _label } = evt.item._cfg.model;
          const labelLength = getLabelMaxLength(_label);
          if (_label.length > labelLength) {
            return true;
          }
          return false;
        },
        formatText: (model: any) => {
          return `<div style="padding: 8px 16px;border: 1px solid rgba(255, 255, 255, 0.2);background:
        #161E35;border-radius: 4px;font-size: 14px;color: #fff;max-width:300px;word-break:break-all;">
             ${model._label}
          </div>`;
        }
      },
      {
        type: 'edge-tooltip',
        shouldBegin: (evt: any) => {
          const { infos } = evt.item._cfg.model;
          if (infos || !infos.length) {
            return true;
          }
          return false;
        },
        formatText: (model: any) => {
          const text: string = model.infos
            .map(item => `<div>${item}</div>`)
            .join(` `);
          return `<div style="padding: 8px 16px;
        box-shadow: 0px 0px 12px 0px rgba(177, 192, 192, 0.45);background: #2E333B;max-width: 300px;border-radius: 4px;font-size: 12px;max-height: 300px;word-break:break-all;color:#fff;font-size:13px;font-weight:500;overflow:hidden;text-overflow:ellipsis;">
             ${text}
          </div>`;
        }
      },
      {
        type: 'activate-relations',
        trigger: 'click'
      }
    ]
  },
  layout: {
    type: 'dagre',
    rankdir: 'LR',
    // align: 'UR',
    nodesepFunc: d => {
      return 70;
    },
    controlPoints: true,
    ranksep: 100
    // controlPoints: true,
  }
};

export const nodeImgUrlMap = {
  [NodeType.应用]: 'app_icon',
  [NodeType.外部应用]: 'outer_icon',
  [NodeType.数据库]: 'db_icon',
  [NodeType.文件]: 'oss_icon',
  [NodeType.未知应用]: 'unknow_icon',
  [NodeType.消息队列]: 'mq_icon',
  [NodeType.缓存]: 'cache_icon',
  [NodeType.入口]: 'root_icon'
};

export const EmptyNode: React.ReactNode = (
  <Empty
    description={
      <Fragment>
        <h1 style={{ fontSize: 18, color: '#454545' }}>链路暂无节点</h1>
        <p
          style={{
            fontSize: 14,
            color: '#8C8C8C'
          }}
        >
          链路暂无节点，请检查链路相关节点是否接入探针
        </p>
      </Fragment>
    }
  />
);
