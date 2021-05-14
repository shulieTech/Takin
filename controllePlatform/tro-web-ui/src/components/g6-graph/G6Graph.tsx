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
import { NodeConfig } from '@antv/g6/lib/types';
import React, { useEffect, useState } from 'react';
import RegistryCustomEdge from './CustomEdge';
import styles from './index.less';
import { G6GraphProps } from './type';
import { getEdgeType, getOffset } from './utils';

const G6Graph: React.FC<G6GraphProps> = props => {
  const { nodes, edges } = props.data;
  const [graph, setGraph] = useState<Graph>(null);
  // const [init, setInit] = useState(false);
  useEffect(() => {
    RegistryCustomEdge();
    initGraph();
  }, []);
  // useEffect(() => {
  //   const { data } = props;
  //   if (!graph || !data) {
  //     return;
  //   }
  //   graph.changeData({
  //     nodes: transformEdges(data.nodes),
  //     edges: transformNodes(data.edges)
  //   });
  // }, [props.data]);
  const initGraph = () => {
    const node = document.getElementById(props.id);
    const width = node.scrollWidth;
    const height = node.scrollHeight;
    const minimap = new G6.Minimap({
      size: [100, 100]
    });
    const toolbar = new G6.ToolBar({});
    const defaultPlugins: any = [];
    if (props.showMiniMap) {
      defaultPlugins.push(minimap);
    }
    if (props.showToolBar) {
      defaultPlugins.push(toolbar);
    }
    const defaultModes = ['drag-canvas', 'drag-node', 'zoom-canvas'];
    const _graph = new G6.Graph({
      width,
      height,
      fitViewPadding: 16,
      fitView: true,
      layout: {
        type: 'radial',
        // center: [0, 0], // 可选，默认为图的中心
        linkDistance: 950, // 可选，边长
        // linkDistance: 1350, // 可选，边长
        // maxIteration: 10, // 可选
        // focusNode: 'e620b2ab7cb170bbc866160edbea50db', // 可选
        // unitRadius: 250, // 可选
        preventOverlap: true, // 可选，必须配合 nodeSize
        // nodeSize: 430, // 可选
        // strictRadial: true, // 可选
        nodeSpacing: 100,
        nodeSize: [54, 80]
        // maxPreventOverlapIteration: 1,
        // sortBy: 'id'
        // workerEnabled: true // 可选，开启 web-worker
      },
      ...props.options,
      plugins:
        props.options && props.options.plugins
          ? [...defaultPlugins, ...props.options.plugins]
          : defaultPlugins,
      modes: {
        default:
          props.options && props.options.modes
            ? [...defaultModes, ...props.options.modes.default]
            : defaultModes
      },
      container: props.id
    });
    _graph.on('node:click', ev => {
      if (props.handleNodeClick) {
        props.handleNodeClick(ev, _graph);
      }
    });
    _graph.on('edge:click', ev => {
      if (props.handleEdgeClick) {
        props.handleEdgeClick(ev, _graph);
      }
    });
    _graph.on('click', ev => {
      if (props.handleGraphClick) {
        props.handleGraphClick(ev, _graph);
      }
    });
    _graph.on('node:mouseenter', ev => {
      if (props.handleNodeMouseenter) {
        props.handleNodeMouseenter(ev, _graph);
      }
    });
    _graph.on('edge:mouseenter', ev => {
      if (props.handleEdgeMouseenter) {
        props.handleEdgeMouseenter(ev, _graph);
      }
    });
    _graph.on('node:mouseleave', ev => {
      if (props.handleNodeMouseleave) {
        props.handleNodeMouseleave(ev, _graph);
      }
    });
    _graph.on('edge:mouseleave', ev => {
      if (props.handleEdgeMouseleave) {
        props.handleEdgeMouseleave(ev, _graph);
      }
    });
    // _graph.fitView();
    setGraph(_graph);
    _graph.read({
      nodes: transformNode(nodes),
      edges: transformEdge(edges)
    });
    if (props.onReady) {
      props.onReady(_graph, node);
    }
  };
  const transformNode = (_nodes: NodeConfig[]): NodeConfig[] => {
    if (!_nodes) {
      return [];
    }
    return _nodes.map(item => {
      return {
        ...item,
        size: 50,
        type: 'circle',
        ...(props.transformNodes ? props.transformNodes(item) : {})
        // _label: item.label,
        // label: `${label}\n`
      };
    });
  };
  const transformEdge = _edges => {
    if (!_edges) {
      return [];
    }
    return _edges.map(item => ({
      ...item,
      edgeOffset: getOffset({ ...item, edges: _edges }),
      size: 2,
      type: getEdgeType(item),
      // curveOffset: -65,
      style: {
        endArrow: {
          path: 'M 0,0 L 8,4 L 8,-4 Z',
          fill: '#08AAD2'
        },
        stroke: '#DADBDC'
      },
      labelCfg: {
        style: {
          fill: '#5A626F',
          background: {
            fill: '#E1E5E7',
            padding: [2, 2, 2, 2]
            // radius: 2,
          }
        }
      },
      ...(props.transformEdges ? props.transformEdges(item) : {})
    }));
  };
  return (
    <div style={{ height: '100%', position: 'relative' }}>
      {(!nodes || !nodes.length || !edges || !edges.length) && (
        <div
          style={{
            textAlign: 'center',
            position: 'absolute',
            left: '50%',
            top: '50%',
            transform: 'translate(-50%, -50%)'
          }}
        >
          {props.emptyNode || '暂无数据'}
        </div>
      )}
      <div
        className={styles.graph}
        style={{ position: 'relative', height: '100%' }}
        id={props.id}
      />
    </div>
  );
};
export default G6Graph;

G6Graph.defaultProps = {
  showMiniMap: true,
  showToolBar: true
};
