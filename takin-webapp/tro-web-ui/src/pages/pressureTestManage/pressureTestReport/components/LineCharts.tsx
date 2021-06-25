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

import ReactEcharts from 'echarts-for-react';
import React from 'react';
interface Props {
  chartsInfo: any;
  isLive?: boolean;
}
const LineCharts: React.FC<Props> = props => {
  const { chartsInfo } = props;
  return (
    <ReactEcharts
      style={{ width: '100%', height: 800 }}
      option={{
        color: ['#11BBD5'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            animation: false
          }
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
        dataZoom: [
          {
            show: true,
            type: 'slider',
            top: '10px',
            left: '60px',
            right: '60px',
            // height: '10px',
            realtime: true,
            start: 0,
            end: 100,
            xAxisIndex: [0, 1, 2, 3, 4]
          }
          // {
          //   type: 'inside',
          //   realtime: true,
          //   start: 0,
          //   end: 100,
          //   xAxisIndex: [0, 1, 2, 3]
          // }
        ],
        axisPointer: {
          link: { xAxisIndex: 'all' }
        },
        grid: [
          {
            top: '13%',
            left: 50,
            right: 50,
            height: '22%',
            width: '42%'
          },
          {
            left: '55%',
            top: '13%',
            height: '22%',
            width: '42%'
          },
          {
            left: 50,
            right: 50,
            top: '45%',
            height: '22%',
            width: '42%'
          },
          {
            left: '55%',
            top: '45%',
            height: '22%',
            width: '42%'
          },
          {
            left: 50,
            right: 50,
            top: '75%',
            height: '22%',
            width: '42%'
          }
        ],
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            data: chartsInfo.time
          },
          {
            gridIndex: 1,
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
            data: chartsInfo.time
          },
          {
            gridIndex: 3,
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            data: chartsInfo.time
          },
          {
            gridIndex: 4,
            type: 'category',
            boundaryGap: false,
            axisLine: { onZero: true },
            data: chartsInfo.time
          }
        ],
        yAxis: [
          {
            name: '并发数',
            type: 'value'
          },
          {
            gridIndex: 1,
            name: 'TPS',
            type: 'value'
          },
          {
            gridIndex: 2,
            name: '平均RT',
            type: 'value'
          },
          {
            gridIndex: 3,
            name: '请求成功率',
            type: 'value'
          },
          {
            gridIndex: 4,
            name: 'SA',
            type: 'value'
          }
        ],
        series: [
          {
            name: '并发数',
            type: 'line',
            showSymbol: true,
            hoverAnimation: false,
            data: chartsInfo.concurrent,
            smooth: true
          },
          {
            name: 'TPS',
            type: 'line',
            showSymbol: true,
            xAxisIndex: 1,
            yAxisIndex: 1,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.tps,
            smooth: true
            // markLine: {
            //   symbol: 'none',
            //   data: [
            //     {
            //       silent: false,
            //       yAxis: 230,
            //       lineStyle: {
            //         type: 'dash',
            //         color: '#FE7D61'
            //       }
            //     }
            //   ]
            // }
          },
          {
            name: '平均RT',
            type: 'line',
            xAxisIndex: 2,
            yAxisIndex: 2,
            showSymbol: true,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.rt,
            smooth: true
          },
          {
            name: '请求成功率',
            type: 'line',
            xAxisIndex: 3,
            yAxisIndex: 3,
            showSymbol: true,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.successRate,
            smooth: true
          },
          {
            name: 'SA',
            type: 'line',
            xAxisIndex: 4,
            yAxisIndex: 4,
            showSymbol: true,
            // symbolSize: 8,
            hoverAnimation: false,
            data: chartsInfo.sa,
            smooth: true
          }
        ]
      }}
    />
  );
};
export default LineCharts;
