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
import styles from './../index.less';
import { Tabs, Tooltip, Icon } from 'antd';
import BottleneckAPIList from './BottleneckAPIList';
import RiskMachine from './RiskMachine';
interface Props {
  state?: any;
  setState?: (value) => void;
  id?: string;
}
const ProblemAnalysis: React.FC<Props> = props => {
  const { state, setState, id } = props;
  const tabsData = [
    {
      title: (
        <span>
          瓶颈接口
          <Tooltip
            title={
              <div>
                瓶颈接口列表是指通过对链路中各接口的性能指标表现进行权重分析后，将其中对链路性能影响较大的接口进行排名展示的列表。
                <br />
                计算逻辑：
                <br />
                1.对所有接口的性能指标进行权重计算，如本接口TPS在链路中所占权重、本接口RT在链路中所占权重；
                <br />
                2.根据各指标权重加权平均计算出各接口的综合性能权重；
                <br />
                3.根据综合性能权重进行降序排序，其中大于平均权重的接口即为瓶颈接口。
              </div>
            }
            placement="rightTop"
            trigger="click"
          >
            <Icon type="question-circle" style={{ marginLeft: 4 }} />
          </Tooltip>
        </span>
      ),
      tabNode: <BottleneckAPIList id={id} />
    },
    {
      title: (
        <span>
          风险机器
          <Tooltip
            title={
              <div>
                风险机器列表是指通过分析压测过程中各节点的资源使用情况，并将其中可能存在风险的机器进行展示的列表。
                <br />
                风险判断逻辑：
                <br />
                1.机器宕机；
                <br />
                2.CPU利用率、内存利用率、磁盘I/O等待率、网络带宽使用率超过80%；
                <br />
                3.CPU load大于2。
              </div>
            }
            placement="rightTop"
            trigger="click"
          >
            <Icon type="question-circle" style={{ marginLeft: 4 }} />
          </Tooltip>
        </span>
      ),
      tabNode: <RiskMachine state={state} setState={setState} id={id} />
    }
  ];
  return (
    <div className={styles.tabsBg}>
      <Tabs animated={false} defaultActiveKey="0">
        {tabsData.map((item, index) => {
          return (
            <Tabs.TabPane tab={item.title} key={`${index}`}>
              {item.tabNode}
            </Tabs.TabPane>
          );
        })}
      </Tabs>
    </div>
  );
};
export default ProblemAnalysis;
