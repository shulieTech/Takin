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

import G6 from '@antv/g6';

const RegistryCustomEdge = () => {
  quadraticEdge();
};

export default RegistryCustomEdge;

const quadraticEdge = () => {
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

    // afterDraw(cfg, group) {
    //   // get the first shape in the group, it is the edge's path here=
    //   const shape = group.get('children')[0];
    //   const lineDash = [4, 2, 1, 2];
    //   let index = 0;
    //   // Define the animation
    //   shape.animate(
    //     () => {
    //       index = index + 1;
    //       if (index > 9) {
    //         index = 0;
    //       }
    //       const res = {
    //         lineDash,
    //         lineDashOffset: -index
    //       };
    //       // returns the modified configurations here, lineDash and lineDashOffset here
    //       return res;
    //     },
    //     {
    //       repeat: true, // whether executes the animation repeatly
    //       duration: 3000 // the duration for executing once
    //     }
    //   );
    // },

    // afterDraw(cfg, group) {
    //   // get the first shape in the group, it is the edge's path here=
    //   const shape = group.get('children')[0];
    //   // the start position of the edge's path
    //   const startPoint = shape.getPoint(0);

    //   // add red circle shape
    //   const circle = group.addShape('circle', {
    //     attrs: {
    //       x: startPoint.x,
    //       y: startPoint.y,
    //       fill: '#1890ff',
    //       r: 3,
    //     },
    //     name: 'circle-shape',
    //   });

    //   // animation for the red circle
    //   circle.animate(
    //     (ratio) => {
    //       const tmpPoint = shape.getPoint(ratio);
    //       // returns the modified configurations here, x and y here
    //       return {
    //         x: tmpPoint.x,
    //         y: tmpPoint.y,
    //       };
    //     },
    //     {
    //       repeat: true, // Whether executes the animation repeatly
    //       duration: 3000, // the duration for executing once
    //     },
    //   );
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
        // lineWidth: 1,
        // stroke: 'red',
        //   edgeTypeColorMap[model.edgeType] &&
        //   edgeTypeColorMap[model.edgeType][0],
        // ...style
      };
    },
    // setState(name, value, item) {
    //   const shape = item.get('keyShape');
    //   const lineDash = [4, 2, 1, 2];
    //   if (name === 'running') {
    //     if (value) {
    //       let index = 0;
    //       shape.animate(
    //         () => {
    //           index = index + 1;
    //           if (index > 9) {
    //             index = 0;
    //           }
    //           const res = {
    //             lineDash,
    //             lineDashOffset: -index
    //           };
    //           // return the params for this frame
    //           return res;
    //         },
    //         {
    //           repeat: true,
    //           duration: 3000
    //         }
    //       );
    //       // shape.attr({
    //       //   size: 10
    //       // });
    //     } else {
    //       shape.stopAnimate();
    //       shape.attr('lineDash', null);
    //     }
    //   }
    // }
  };

  G6.registerEdge('quadratic-label-edge', defaultConf, 'quadratic');
};
