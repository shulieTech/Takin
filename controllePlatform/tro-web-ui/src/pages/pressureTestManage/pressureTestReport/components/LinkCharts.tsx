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

import { InputNumber, message, Tabs } from 'antd';
import { CommonModal } from 'racc';
import React, { useState } from 'react';
import { TestMode } from '../../pressureTestScene/enum';
import PressureTestReportService from '../service';
import styles from './../index.less';
import LineCharts from './LineCharts';
interface Props {
  tabList?: any[];
  chartsInfo?: any;
  state?: any;
  setState?: (value) => void;
  isLive?: boolean;
}
const LinkCharts: React.FC<Props> = props => {
  const { TabPane } = Tabs;
  const { chartsInfo, setState, state, tabList } = props;
  const [targetTps, setTargetTps] = useState<number>(undefined);
  const handleChangeTab = value => {
    setState({
      tabKey: value
    });
  };
  const getDefaultValue = async () => {
    const {
      data: { data, success }
    } = await PressureTestReportService.getTpsValue({
      reportId: state.detailData.id,
      sceneId: state.detailData.sceneId
    });
    if (success) {
      setTargetTps(data);
    }
  };
  const adjustTps = () => {
    if (!targetTps) {
      message.info('请填写TPS');
      return;
    }
    return new Promise(async resolve => {
      const {
        data: { success }
      } = await PressureTestReportService.adjustTPS({
        targetTps,
        reportId: state.detailData.id,
        sceneId: state.detailData.sceneId
      });
      if (success) {
        message.success('调整成功');
        resolve(true);
      }
      resolve(false);
    });
  };
  return (
    <div
      style={{
        display: 'flex',
        height: 800,
        marginTop: 16
      }}
    >
      <div className={styles.leftSelected}>
        {tabList.map((item, key) => {
          return (
            <p
              key={key}
              className={
                state.tabKey === item.value
                  ? styles.appItemActive
                  : styles.appItem
              }
              onClick={() => handleChangeTab(item.value)}
            >
              {item.label}
            </p>
          );
        })}
      </div>
      <div className={styles.riskMachineList}>
        <LineCharts isLive={props.isLive} chartsInfo={chartsInfo} />
      </div>
      {props.isLive && state.detailData.pressureType === TestMode.TPS模式 && (
        <CommonModal
          modalProps={{
            okText: '确定',
            cancelText: '取消',
            title: '调整TPS',
            destroyOnClose: true
          }}
          btnText="调整TPS"
          btnProps={{ style: { transform: 'translateY(8px)' } }}
          beforeOk={adjustTps}
          onClick={() => getDefaultValue()}
        >
          TPS：
          <InputNumber
            value={targetTps}
            onChange={value => setTargetTps(value)}
            precision={0}
            min={0}
          />
        </CommonModal>
      )}
    </div>
  );
};
export default LinkCharts;
