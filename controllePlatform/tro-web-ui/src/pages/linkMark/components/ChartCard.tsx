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

import React, { Fragment, useEffect } from 'react';
import { useStateReducer } from 'racc';
import LinkMarkService from '../service';

import styles from './../index.less';
import Header from './Header';
import { Progress, Statistic } from 'antd';
import { Chart, Geom, Axis, Tooltip, Legend } from 'bizcharts';

interface Props {}
export interface LinkCardState {
  isReload?: boolean;
  chartData?: any;
}

const cols = {
  month: {
    range: [0, 1]
  }
};
const ChartCard: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<LinkCardState>({
    isReload: false,
    chartData: null
  });

  useEffect(() => {
    queryMiddleware();
  }, [state.isReload]);

  /**
   * @name 获取链路统计数据
   */
  const queryMiddleware = async () => {
    const {
      data: { success, data }
    } = await LinkMarkService.queryStatisticChart({});
    if (success) {
      setState({
        chartData: data
      });
    }
  };

  return (
    <div>
      <Header title="业务流程覆盖情况" />
      <div className={styles.chartWrap}>
        <div className={styles.leftWrap}>
          <Progress
            type="circle"
            width={150}
            strokeWidth={12}
            percent={
              state.chartData && state.chartData.businessFlowPressureRate
                ? state.chartData.businessFlowPressureRate * 100
                : 0
            }
            format={percent => (
              <div>
                <p className={styles.percent}>
                  <Statistic
                    value={
                      state.chartData &&
                      state.chartData.businessFlowPressureRate * 100
                    }
                    precision={1}
                  />
                  <span className={styles.percentIcon}>%</span>
                </p>
                <p className={styles.percentName}>当前覆盖率</p>
              </div>
            )}
          />
          <div className={styles.chartInfo}>
            <p>
              压测覆盖数：
              <span className={styles.chartInfoNum}>
                {state.chartData && state.chartData.businessPressureCount
                  ? state.chartData.businessPressureCount
                  : 0}
              </span>
            </p>
            <p>
              接入总数量：
              <span
                className={styles.chartInfoNum}
                style={{ display: 'inline-block', marginTop: 8 }}
              >
                {state.chartData && state.chartData.businessFlowTotalCount
                  ? state.chartData.businessFlowTotalCount
                  : 0}
              </span>
            </p>
          </div>
        </div>
        <div className={styles.rightWrap}>
          <Chart
            padding={40}
            width={800}
            height={250}
            data={state.chartData && state.chartData.businessCover}
            scale={cols}
            forceFit
          >
            <div className={styles.chartTitle}>覆盖数趋势</div>
            <Axis name="month" />
            <Axis
              name="cover"
              label={{
                formatter: val => `${val}`
              }}
            />
            <Tooltip
              crosshairs={{
                type: 'y'
              }}
            />
            <Geom
              type="line"
              position="month*cover"
              size={2}
              shape={'smooth'}
              tooltip={[
                'month*cover',
                (month, cover) => {
                  return {
                    name: '覆盖数',
                    value: cover
                  };
                }
              ]}
            />
            <Geom
              type="point"
              position="month*cover"
              size={4}
              shape={'circle'}
              style={{
                stroke: '#fff',
                lineWidth: 1
              }}
            />
          </Chart>
        </div>
      </div>
      <Header title="应用覆盖情况" />
      <div className={styles.chartWrap}>
        <div className={styles.leftWrap}>
          <Progress
            type="circle"
            width={150}
            strokeWidth={12}
            percent={
              state.chartData && state.chartData.applicationPressureRate
                ? state.chartData.applicationPressureRate * 100
                : 0
            }
            format={percent => (
              <div>
                <p className={styles.percent}>
                  <Statistic
                    value={
                      state.chartData &&
                      state.chartData.applicationPressureRate * 100
                    }
                    precision={1}
                  />
                  <span className={styles.percentIcon}>%</span>
                </p>
                <p className={styles.percentName}>当前覆盖率</p>
              </div>
            )}
          />
          <div className={styles.chartInfo}>
            <p>
              压测覆盖数：
              <span className={styles.chartInfoNum}>
                {state.chartData && state.chartData.applicationPressureCount
                  ? state.chartData.applicationPressureCount
                  : 0}
              </span>
            </p>
            <p>
              接入总数量：
              <span
                className={styles.chartInfoNum}
                style={{ display: 'inline-block', marginTop: 8 }}
              >
                {state.chartData && state.chartData.applicationTotalCount
                  ? state.chartData.applicationTotalCount
                  : 0}
              </span>
            </p>
          </div>
        </div>
        <div className={styles.rightWrap2}>
          <Chart
            padding={40}
            width={450}
            height={250}
            data={state.chartData && state.chartData.applicationRemote}
            scale={cols}
            forceFit
          >
            <div className={styles.chartTitle}>应用接入趋势</div>
            <Axis name="month" />
            <Axis
              name="cover"
              label={{
                formatter: val => `${val}`
              }}
            />
            <Tooltip
              crosshairs={{
                type: 'y'
              }}
            />
            <Geom
              type="line"
              position="month*cover"
              size={2}
              shape={'smooth'}
              tooltip={[
                'month*cover',
                (month, cover) => {
                  return {
                    name: '应用接入数',
                    value: cover
                  };
                }
              ]}
            />
            <Geom
              type="point"
              position="month*cover"
              size={4}
              shape={'circle'}
              style={{
                stroke: '#fff',
                lineWidth: 1
              }}
            />
          </Chart>
          <Chart
            padding={40}
            width={450}
            height={250}
            data={state.chartData && state.chartData.systemProcess}
            scale={cols}
            forceFit
          >
            <div className={styles.chartTitle}>系统流程点接入趋势</div>
            <Axis name="month" />
            <Axis
              name="cover"
              label={{
                formatter: val => `${val}`
              }}
            />
            <Tooltip
              crosshairs={{
                type: 'y'
              }}
            />
            <Geom
              type="line"
              position="month*cover"
              size={2}
              shape={'smooth'}
              tooltip={[
                'month*cover',
                (month, cover) => {
                  return {
                    name: '系统流程点接入数',
                    value: cover
                  };
                }
              ]}
            />
            <Geom
              type="point"
              position="month*cover"
              size={4}
              shape={'circle'}
              style={{
                stroke: '#fff',
                lineWidth: 1
              }}
            />
          </Chart>
        </div>
      </div>
    </div>
  );
};
export default ChartCard;
