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
import ReactEcharts from 'echarts-for-react';
interface Props {
  chartsInfo: any;
}

const MachinePerformanceCharts: React.FC<Props> = props => {
  const { chartsInfo } = props;
  const tpss = chartsInfo.tps ? chartsInfo.tps : [];
  const cpuLoad = chartsInfo.load ? chartsInfo.load : [];
  const maxTps = Math.max(...tpss);
  const maxCpu = Math.max(...cpuLoad);
  const tpsInterval = Math.ceil(maxTps / 5) === 0 ? 1 : Math.ceil(maxTps / 5);
  const cpuInterval = Math.ceil(maxCpu / 5) === 0 ? 1 : Math.ceil(maxCpu / 5);

  return (
    <ReactEcharts
      style={{ width: '100%', height: 700 }}
      option={{
        // color: ['#18BCFE'],
        legend: {
          data: [
            'CPU利用率',
            'TPS',
            'CPU load',
            '内存利用率',
            '磁盘I/O等待率',
            '网络宽带使用率'
          ]
        },
        tooltip: {
          trigger: 'axis'
          //   formatter: datas => {
          //     let res = `TPS : ${datas[0].axisValue} <br/>`;

          //     // tslint:disable-next-line:no-increment-decrement
          //     for (let i = 0, length = datas.length; i < length; i++) {
          //       res =
          //         // tslint:disable-next-line:prefer-template
          //         res + datas[i].seriesName + ' : ' + datas[i].value + '<br/>';
          //     }
          //     return res;
          //   }
        },
        // toolbox: {
        //   feature: {
        //     dataZoom: {
        //       yAxisIndex: 'none'
        //     },
        //     restore: {},
        //     saveAsImage: {}
        //   }
        // },
        //   dataZoom: [
        //     {
        //       show: true,
        //       type: 'slider',
        //       top: '10px',
        //       left: '60px',
        //       right: '60px',
        //       // height: '10px',
        //       realtime: true,
        //       start: 0,
        //       end: 100,
        //       xAxisIndex: [0, 1, 2, 3, 4]
        //     }
        //     // {
        //     //   type: 'inside',
        //     //   realtime: true,
        //     //   start: 0,
        //     //   end: 100,
        //     //   xAxisIndex: [0, 1, 2, 3]
        //     // }
        //   ],
        axisPointer: {
          //   link: { xAxisIndex: 'all' }
        },
        grid: [
          {
            top: '8%',
            left: 50,
            right: 50,
            height: '20%',
            width: '40%'
          },
          {
            left: '55%',
            top: '8%',
            height: '20%',
            width: '40%'
          },
          {
            left: 50,
            right: 50,
            top: '40%',
            height: '20%',
            width: '40%'
          },
          {
            left: '55%',
            top: '40%',
            height: '20%',
            width: '40%'
          },
          {
            left: 52,
            top: '73%',
            height: '20%',
            width: '40%'
          }
        ],
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            position: 'bottom',
            data: chartsInfo.time
          },
          {
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            position: 'bottom',
            data: chartsInfo.time
          },
          {
            gridIndex: 1,
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            position: 'bottom',
            data: chartsInfo.time
          },
          {
            gridIndex: 1,
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            position: 'bottom',
            data: chartsInfo.time
          },
          {
            gridIndex: 2,
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            data: chartsInfo.time
          },
          {
            gridIndex: 2,
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            position: 'bottom',
            data: chartsInfo.time
          },
          {
            gridIndex: 3,
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            position: 'bottom',
            data: chartsInfo.time
          },
          {
            gridIndex: 3,
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            position: 'bottom',
            data: chartsInfo.time
          },
          {
            gridIndex: 4,
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            position: 'bottom',
            data: chartsInfo.time
          },
          {
            gridIndex: 4,
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            position: 'bottom',
            data: chartsInfo.time
          }
        ],
        yAxis: [
          {
            name: 'CPU利用率',
            type: 'value',
            position: 'left',
            min: 0,
            max: 100,
            axisLabel: {
              formatter: '{value}%'
            }
          },
          {
            name: 'TPS',
            type: 'value',
            position: 'right',
            splitNumber: 5,
            interval: tpsInterval
          },
          {
            gridIndex: 1,
            name: 'CPU load',
            type: 'value',
            position: 'left'
          },
          {
            gridIndex: 1,
            name: 'TPS',
            type: 'value',
            position: 'right',
            splitNumber: 5,
            interval: tpsInterval
          },
          {
            gridIndex: 2,
            name: '内存利用率(%)',
            type: 'value',
            position: 'left',
            min: 0,
            max: 100,
            axisLabel: {
              formatter: '{value}%'
            }
          },
          {
            gridIndex: 2,
            name: 'TPS',
            type: 'value',
            position: 'right',
            splitNumber: 5,
            interval: tpsInterval
          },
          {
            gridIndex: 3,
            name: '磁盘I/O等待率(%)',
            type: 'value',
            position: 'left',
            min: 0,
            max: 100,
            axisLabel: {
              formatter: '{value}%'
            }
          },
          {
            gridIndex: 3,
            name: 'TPS',
            type: 'value',
            position: 'right',
            splitNumber: 5,
            interval: tpsInterval
          },
          {
            gridIndex: 4,
            name: '网络宽带使用率(%)',
            type: 'value',
            position: 'left',
            min: 0,
            max: 100,
            axisLabel: {
              formatter: '{value}%'
            }
          },
          {
            gridIndex: 4,
            name: 'TPS',
            type: 'value',
            position: 'right',
            splitNumber: 5,
            interval: tpsInterval
          }
        ],
        series: [
          {
            name: 'CPU利用率',
            type: 'line',
            showSymbol: true,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.cpu,
            smooth: true,
            color: '#048BFE'
          },
          {
            name: 'TPS',
            type: 'line',
            showSymbol: true,
            yAxisIndex: 1,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.tps,
            smooth: true,
            color: '#31D5E0'
          },
          {
            name: 'CPU load',
            type: 'line',
            xAxisIndex: 2,
            yAxisIndex: 2,
            showSymbol: true,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.load,
            smooth: true,
            color: '#888AFF',
            splitNumber: 5,
            interval: cpuInterval
          },
          {
            name: 'TPS',
            type: 'line',
            showSymbol: true,
            xAxisIndex: 2,
            yAxisIndex: 3,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.tps,
            smooth: true,
            color: '#31D5E0'
          },
          {
            name: '内存利用率',
            type: 'line',
            xAxisIndex: 4,
            yAxisIndex: 4,
            showSymbol: true,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.memory,
            smooth: true,
            color: '#FFB049'
          },
          {
            name: 'TPS',
            type: 'line',
            showSymbol: true,
            xAxisIndex: 4,
            yAxisIndex: 5,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.tps,
            smooth: true,
            color: '#31D5E0'
          },
          {
            name: '磁盘I/O等待率',
            type: 'line',
            xAxisIndex: 6,
            yAxisIndex: 6,
            showSymbol: true,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.io,
            smooth: true,
            color: '#FF836D'
          },
          {
            name: 'TPS',
            type: 'line',
            showSymbol: true,
            xAxisIndex: 6,
            yAxisIndex: 7,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.tps,
            smooth: true,
            color: '#31D5E0'
          },
          {
            name: '网络宽带使用率',
            type: 'line',
            xAxisIndex: 8,
            yAxisIndex: 8,
            showSymbol: true,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.mbps,
            smooth: true,
            color: '#FF8394'
          },
          {
            name: 'TPS',
            type: 'line',
            showSymbol: true,
            xAxisIndex: 8,
            yAxisIndex: 9,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.tps,
            smooth: true,
            color: '#31D5E0'
          }
        ]
      }}
    />
  );
};
export default MachinePerformanceCharts;
