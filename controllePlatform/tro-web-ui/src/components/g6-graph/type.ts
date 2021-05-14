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

import { Graph } from '@antv/g6';
import {
  EdgeConfig,
  GraphData,
  GraphOptions,
  NodeConfig
} from '@antv/g6/lib/types';

export interface G6GraphProps {
  onReady?: (graph: Graph, node?: HTMLElement) => void;
  options?: Partial<GraphOptions>;
  showToolBar?: boolean;
  showMiniMap?: boolean;
  data: GraphData;
  transformNodes?: (node: NodeConfig) => Partial<NodeConfig>;
  transformEdges?: (edge: EdgeConfig) => Partial<EdgeConfig>;
  handleNodeClick?: (ev: any, graph?: Graph) => void;
  handleNodeMouseenter?: (ev: any, graph?: Graph) => void;
  handleNodeMouseleave?: (ev: any, graph?: Graph) => void;
  handleEdgeMouseenter?: (ev: any, graph?: Graph) => void;
  handleEdgeMouseleave?: (ev: any, graph?: Graph) => void;
  handleEdgeClick?: (ev: any, graph?: Graph) => void;
  handleGraphClick?: (ev: any, graph?: Graph) => void;
  id: string;
  emptyNode?: React.ReactNode;
}
