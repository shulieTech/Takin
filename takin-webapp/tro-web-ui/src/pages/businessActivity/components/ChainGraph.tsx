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
import G6 from '@antv/g6';
import { Modal } from 'antd';
import { useStateReducer } from 'racc';
import React, { Fragment, useContext, useEffect } from 'react';
import { AddEditSystemPageContext, getEntranceInfo } from '../addEditPage';
import BusinessActivityService from '../service';

const registryEdges = () => {
  // const edgeTypeColorMap = {
  //   type1: ['#531dab', '#391085', '#391085'],
  //   type2: ['#d9d9d9', '#bfbfbf', '#8c8c8c'],
  //   type3: ['#d3adf7', '#b37feb', '#9254de']
  // };

  const defaultConf = {
    style: {
      lineAppendWidth: 5,
      lineDash: [0, 0],
      lineDashOffset: 0,
      opacity: 1,
      labelCfg: {
        style: {
          fillOpacity: 1
        }
      }
    },
    /**
     * 绘制边
     * @override
     * @param  {Object} cfg   边的配置项
     * @param  {G.Group} group 边的容器
     * @return {G.Shape} 图形
     */
    drawShape(cfg, group) {
      const item = group.get('item');
      const shapeStyle = this.getShapeStyle(cfg, item);
      const shape = group.addShape('path', {
        className: 'edge-path',
        attrs: shapeStyle
      });
      return shape;
    },
    // drawLabel(cfg, group) {
    //   const labelCfg = cfg.labelCfg || {};
    //   const labelStyle = this.getLabelStyle(cfg, labelCfg, group);
    //   const text = group.addShape('text', {
    //     attrs: {
    //       ...labelStyle,
    //       text: cfg.label,
    //       fontSize: 12,
    //       fill: '#404040',
    //       cursor: 'pointer'
    //     },
    //     className: 'edge-label'
    //   });

    //   return text;
    // },

    /**
     * 获取图形的配置项
     * @internal 仅在定义这一类节点使用，用户创建和更新节点
     * @param  {Object} cfg 节点的配置项
     * @return {Object} 图形的配置项
     */
    getShapeStyle(cfg, item) {
      const { startPoint, endPoint } = cfg;
      const type = item.get('type');

      const defaultStyle = this.getStateStyle('default', true, item);

      if (type === 'node') {
        return Object.assign({}, cfg.style, defaultStyle);
      }

      const controlPoints = this.getControlPoints(cfg);
      let points = [startPoint]; // 添加起始点
      // 添加控制点
      if (controlPoints) {
        points = points.concat(controlPoints);
      }
      // 添加结束点
      points.push(endPoint);
      const path = this.getPath(points);

      const style = Object.assign({}, { path }, cfg.style, defaultStyle);
      return style;
    },
    getControlPoints(cfg) {
      let controlPoints = cfg.controlPoints; // 指定controlPoints

      if (!controlPoints || !controlPoints.length) {
        const { startPoint, endPoint } = cfg;
        const innerPoint = G6.Util.getControlPoint(
          startPoint,
          endPoint,
          0.5,
          cfg.edgeOffset || 0
        );
        controlPoints = [innerPoint];
      }
      return controlPoints;
    },
    /**
     * 获取三次贝塞尔曲线的path
     *
     * @param {array} points 起始点和两个控制点
     * @returns
     */
    getPath(points) {
      const path = [];
      path.push(['M', points[0].x, points[0].y]);
      path.push(['Q', points[1].x, points[1].y, points[2].x, points[2].y]);
      return path;
    },
    /**
     * 根据不同状态，获取不同状态下的样式值
     * @param {string} name
     * @param {string} value
     * @param {Item} item
     */
    getStateStyle() {
      // const model = item.getModel();
      // const { style = {} } = model;

      const defaultStyle = Object.assign({}, this.style);

      // 更新颜色
      return {
        ...defaultStyle,
        lineWidth: 1
        // stroke:
        //   edgeTypeColorMap[model.edgeType] &&
        //   edgeTypeColorMap[model.edgeType][0],
        // ...style
      };
    }
  };

  G6.registerEdge('quadratic-label-edge', defaultConf, 'quadratic');
};

interface Props {}
const ChainGraph: React.FC<Props> = props => {
  const { state: rootState, setState: rootSetState } = useContext(
    AddEditSystemPageContext
  );
  const [state, setState] = useStateReducer({
    nodes: [
      {
        id: '7aa163be71acf49da31b7b82a91a81ba',
        label: 'user_center',
        root: false,
        type: 'APP',
        title: null
      },
      {
        id: '5569b93fb9188236e3d4ec40d7198120',
        label: 'dms_mall',
        root: false,
        type: 'APP',
        title: null
      },
      {
        id: '196c51d41215d1b187b1351a322b1cd4',
        label: 'mysql 192.168.0.62:3306',
        root: false,
        type: 'MYSQL',
        title: '节点[192.168.0.62:3306]'
      },
      {
        id: 'de950de7469ade4536f2b250c0136d8a',
        label: 'base_center',
        root: false,
        type: 'APP',
        title: null
      },
      {
        id: '7a8f263e2c0336b3446d192eaa4b35e3',
        label: 'redis 192.168.0.62:6379',
        root: false,
        type: 'REDIS',
        title: '节点[192.168.0.62:6379]'
      },
      {
        id: '9dfa441e9f88d7c9a6b32cfb4928a346',
        label: '入口',
        root: true,
        type: 'APP',
        title: null
      },
      {
        id: '4fb1567e62b0aa4ea4d0ccf0f2c051f2',
        label: 'partner_center',
        root: false,
        type: 'APP',
        title: null
      }
    ],
    edges: [
      {
        source: '5569b93fb9188236e3d4ec40d7198120',
        target: 'de950de7469ade4536f2b250c0136d8a',
        label: 'DUBBO',
        type: 'DUBBO',
        extendInfo: [
          '类：io.shulie.center.base.api.service.ChannelService，方法(参数）：getChannelById~(Long)',
          '类：io.shulie.center.base.api.service.BrandService，方法(参数）：getBrandById~(Long)'
        ]
      },
      {
        source: '4fb1567e62b0aa4ea4d0ccf0f2c051f2',
        target: '196c51d41215d1b187b1351a322b1cd4',
        label: 'MYSQL',
        type: 'MYSQL',
        extendInfo: [
          '数据库：partner_center',
          '数据库：partner_center',
          '数据库：partner_center'
        ]
      },
      {
        source: '5569b93fb9188236e3d4ec40d7198120',
        target: '7aa163be71acf49da31b7b82a91a81ba',
        label: 'DUBBO',
        type: 'DUBBO',
        extendInfo: [
          '类：io.shulie.center.user.api.service.UserService，方法(参数）：getUserByToken~(String&String&Long&String)'
        ]
      },
      {
        source: '7aa163be71acf49da31b7b82a91a81ba',
        target: '196c51d41215d1b187b1351a322b1cd4',
        label: 'MYSQL',
        type: 'MYSQL',
        extendInfo: [
          '数据库：user_center',
          '数据库：user_center',
          '数据库：user_center',
          '数据库：user_center',
          '数据库：user_center',
          '数据库：user_center'
        ]
      },
      {
        source: '5569b93fb9188236e3d4ec40d7198120',
        target: '4fb1567e62b0aa4ea4d0ccf0f2c051f2',
        label: 'DUBBO',
        type: 'DUBBO',
        extendInfo: [
          '类：io.shulie.center.partner.api.service.PartnerBaseService，方法(参数）：getPartnerDetailByUid~(Long&PartnerDetailQueryOptions)'
        ]
      },
      {
        source: '7aa163be71acf49da31b7b82a91a81ba',
        target: '7a8f263e2c0336b3446d192eaa4b35e3',
        label: 'REDIS',
        type: 'REDIS',
        extendInfo: [
          'DB：0:del，操作方式：',
          'DB：0:pexpire，操作方式：',
          'DB：0:get，操作方式：',
          'DB：0:sadd，操作方式：',
          'DB：0:append，操作方式：',
          'DB：0:publish，操作方式：',
          'DB：0:hmset，操作方式：',
          'DB：0:setex，操作方式：',
          'DB：0:hgetAll，操作方式：'
        ]
      },
      {
        source: '9dfa441e9f88d7c9a6b32cfb4928a346',
        target: '5569b93fb9188236e3d4ec40d7198120',
        label: 'HTTP',
        type: 'HTTP',
        extendInfo: ['请求方式：POST，请求路径：/mall/login/login']
      },
      {
        source: '5569b93fb9188236e3d4ec40d7198120',
        target: '7a8f263e2c0336b3446d192eaa4b35e3',
        label: 'REDIS',
        type: 'REDIS',
        extendInfo: [
          'DB：0:publish，操作方式：',
          'DB：0:append，操作方式：',
          'DB：0:sadd，操作方式：',
          'DB：0:pexpire，操作方式：',
          'DB：0:hmset，操作方式：'
        ]
      },
      {
        source: '5569b93fb9188236e3d4ec40d7198120',
        target: '7aa163be71acf49da31b7b82a91a81ba',
        label: 'HTTP',
        type: 'HTTP',
        extendInfo: [
          '请求方式：GET，请求路径：/uic/login/token',
          '请求方式：POST，请求路径：/uic/login/login'
        ]
      }
    ],
    graph: null
  });
  useEffect(() => {
    registryEdges();
    initGraph();
  }, []);
  useEffect(() => {
    if (rootState.service && state.graph) {
      queryChartInfo();
    }
  }, [rootState.service, state.graph, rootState.serviceList]);
  const queryChartInfo = async () => {
    const {
      data: { data, success }
    } = await BusinessActivityService.queryChartInfo({
      ...getEntranceInfo(rootState.serviceList, rootState.service),
      applicationName: rootState.app,
      linkId: rootState.service,
      type: rootState.serviceType
    });
    if (success) {
      if (data.nodes && data.nodes.length) {
        state.graph.changeData({
          nodes: transformNode(data.nodes),
          edges: transformEdge(data.nodes, data.edges)
        });
        // const rootNode = state.nodes.find(item => item.root);
        // if (rootNode) {
        //   console.log(rootNode);
        //   state.graph.focusItem(rootNode.id);
        // }
        return;
      }
      state.graph.clear();
    }
  };
  const graphId = 'graph';
  const initGraph = () => {
    const width = document.getElementById(graphId).scrollWidth;
    const height = document.getElementById(graphId).scrollHeight || 620;
    const graph = new G6.Graph({
      width,
      height,
      container: graphId,
      fitViewPadding: 16,
      // renderer: 'svg',
      fitView: true,
      // fitCenter: true,
      // linkCenter: true,
      modes: {
        default: [
          'drag-canvas',
          'drag-node',
          'zoom-canvas',
          {
            type: 'tooltip',
            // shouldBegin: (evt: any) => {
            //   const { _label } = evt.item._cfg.model;
            //   const labelLength = getLabelMaxLength(_label);
            //   if (_label.length > labelLength) {
            //     return true;
            //   }
            //   return false;
            // },
            formatText: (model: any) => {
              return `<div style="padding: 21px 24px;border: 1px solid rgba(255, 255, 255, 0.2);background:
              #161E35;border-radius: 4px;font-size: 14px;color: #fff">
                   ${model._label}
                </div>`;
            }
          }
        ]
      },
      // maxZoom: 2,
      // minZoom: 0.5,
      // fitViewPadding: [0, 120, 0, 0],
      // plugins: [getMenu()],
      layout: {
        type: 'radial',
        // center: [0, 0], // 可选，默认为图的中心
        linkDistance: 1350, // 可选，边长
        // maxIteration: 10, // 可选
        // focusNode: 'e620b2ab7cb170bbc866160edbea50db', // 可选
        unitRadius: 250, // 可选
        preventOverlap: true, // 可选，必须配合 nodeSize
        // nodeSize: 430, // 可选
        // strictRadial: true, // 可选
        nodeSpacing: 40
        // maxPreventOverlapIteration: 1,
        // sortBy: 'id'
        // workerEnabled: true // 可选，开启 web-worker
      }
      // layout: {
      //   type: 'dagre',
      //   rankdir: 'LR',
      //   align: 'UL',
      //   ranksep: 80
      //   // nodesep: 10,
      //   // ranksepFunc: node => {
      //   //   if (node.root) {
      //   //     return 80;
      //   //   }
      //   //   return -10;
      //   // },
      //   // controlPoints: true
      // }
    });
    setState({ graph });
    // tslint:disable-next-line:no-this-assignment
    graph.on('node:click', ev => {
      const title = ev.item._cfg.model.title;
      if (!title) {
        return;
      }
      clickNode(title);
    });
    graph.on('edge:click', ev => {
      const extendInfo = ev.item._cfg.model.extendInfo;
      if (!extendInfo || !extendInfo.length) {
        return;
      }
      clickEdge(extendInfo);
    });
    // graph.read({
    //   nodes: transformNode(state.nodes),
    //   edges: transformEdge(state.nodes, state.edges)
    // });
  };
  const clickNode = title => {
    Modal.info({
      content: <div>{title}</div>,
      okButtonProps: { style: { display: 'none' } },
      icon: ' ',
      maskClosable: true
    });
  };
  const clickEdge = (extendInfo: string[]) => {
    Modal.info({
      content: (
        <div>
          {extendInfo.map((item, index) => (
            <div style={{ marginBottom: 8 }} key={index}>
              {item}
            </div>
          ))}
        </div>
      ),
      okButtonProps: { style: { display: 'none' } },
      icon: ' ',
      maskClosable: true
    });
  };
  const transformNode = nodes => {
    if (!nodes) {
      return [];
    }
    return nodes.map(item => {
      let label = item.label;
      const maxLength = getLabelMaxLength(label);
      if (item.label.length > maxLength) {
        label = `${label.slice(0, maxLength)}...`;
      }
      return {
        ...item,
        label: `${label}\n`,
        _label: item.label,
        // anchorPoint: [0, 0.5],
        // icon: {
        //   show: item.img,
        //   img: item.img,
        //   width: 50,
        //   height: 50
        // },
        size: 50,
        type: 'circle',
        style: {
          lineWidth: item.root ? 8 : 4,
          stroke: item.root ? '#4AC5CB' : getNodeColor(item),
          fill: '#fff'
        },
        labelCfg: {
          position: 'bottom',
          // offset: 10,
          style: {
            // lineWidth: 10,
            // fill: '#fff',
            // textBaseline: 'middle',
            fontSize: item.root ? 20 : 14,
            fontWeight: item.root ? 800 : 500
          }
        }
      };
    });
  };
  return (
    <Fragment>
      <div style={{ position: 'relative' }} id={graphId} />
    </Fragment>
  );
};
export default ChainGraph;

const getLabelMaxLength = label => {
  const enLength = getLabelCnNum(label);
  let result = 0;
  if (!enLength) {
    result = 12;
  } else if (enLength < 6) {
    result = 8;
  } else {
    result = 5;
  }
  return result;
};

const getLabelCnNum = label => {
  let len = 0;
  for (let i = 0; i < label.length; i = i + 1) {
    if (label.charCodeAt(i) > 127 || label.charCodeAt(i) === 94) {
      len += 1;
    }
  }
  return len;
};

const getNodeColor = item => {
  let color = '';
  switch (item.type) {
    /** @name 应用 */
    // case '0':
    //   color = '#33A29D';
    //   break;
    /** @name DB */
    case '2':
      color = '#FBBD36';
      break;
    /** @name MQ */
    case '3':
      color = '#FF911F';
      break;
    /** @name 未知 */
    // case '1000':
    //   color = '#D5D5D5';
    //   break;
    /** @name 其他 */
    default:
      color = '#D5D5D5';
      break;
  }
  return color;
};

const getEdgeType = item => {
  if (item.source === item.target) {
    return 'loop';
  }
  return 'quadratic-label-edge';
};

const getOffset = ({ id, source, target, nodes, edges }) => {
  const repeatList = edges.filter(
    item => item.source === source && item.target === target
  );
  /** @name 没有重复，判断是否双向调用 */
  if (repeatList.length === 1) {
    const isBoth = edges.find(
      item => item.source === target && item.target === source
    );
    if (isBoth) {
      return 80;
    }
    return 0;
  }
  /** @name 重复，初始值80，累加140 */
  const index = repeatList.findIndex(item => item.id === id);
  return index * 180 + 100;
};

const transformEdge = (nodes, edges) => {
  if (!edges) {
    return [];
  }
  return edges.map(item => ({
    // edgeType: item.id,
    ...item,
    edgeOffset: getOffset({ ...item, nodes, edges }),
    size: 2,
    type: getEdgeType(item),
    // shape: 'quadratic-label-edge',
    // type: item.round ? 'quadratic' : 'line',
    curveOffset: -65,
    style: {
      endArrow: {
        path: 'M 0,0 L 8,4 L 8,-4 Z',
        fill: '#4AC5CB'
      },
      stroke: '#4AC5CB'
    },
    labelCfg: {
      style: {
        fill: '#666666',
        background: {
          fill: 'rgb(248, 248, 248)',
          padding: [2, 2, 2, 2]
          // radius: 2,
        }
      }
    }
  }));
};
