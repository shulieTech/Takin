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

import { Col, Divider, Row } from 'antd';
import { useStateReducer } from 'racc';
import React from 'react';
import styles from './../index.less';
import LinkCharts from './LinkCharts';
import ProblemAnalysis from './ProblemAnalysis';
import ReportLinkOverviewDetail from './ReportLinkOverviewDetail';
import RequestList from './RequestList';
import WarningDetailList from './WarningDetailList';
import WaterLevel from './WaterLevel';

interface Props {
  id?: string;
  detailData?: any;
  state?: any;
  setState?: (value) => void;
  reportCountData: any;
  failedCount: number;
}
interface State {
  tabKey: number;
}
const ModuleTabs: React.FC<Props> = props => {
  const {
    id,
    detailData,
    state,
    setState,
    reportCountData,
    failedCount
  } = props;
  const [tabState, setTabState] = useStateReducer<State>({
    tabKey: 0
  });
  const data = [
    {
      title: '压测概览',
      firstLineTxt: '个业务活动',
      firstLineNum: {
        value: reportCountData && reportCountData.businessActivityCount,
        color: '#354153'
      },
      renderTabNode: (
        <LinkCharts
          tabList={state.tabList}
          chartsInfo={state.chartsInfo}
          setState={setState}
          state={state}
        />
      )
    },
    {
      title: '压测明细',
      firstLineTxt: '个业务活动',
      firstLineNum: {
        value: reportCountData && reportCountData.businessActivityCount,
        color: '#354153'
      },
      secondLineTxt: '个业务活动不达标',
      secondLineNum: {
        value: reportCountData && reportCountData.notpassBusinessActivityCount,
        color: '#FE7D61'
      },
      renderTabNode: (
        <ReportLinkOverviewDetail detailData={detailData} id={id} />
      )
    },
    {
      title: '告警明细',
      firstLineTxt: '次警告',
      firstLineNum: {
        value: reportCountData && reportCountData.warnCount,
        color: '#FE7D61'
      },
      renderTabNode: <WarningDetailList id={id} />
    },
    {
      title: '请求流量明细',
      firstLineTxt: '次失败请求',
      firstLineNum: {
        value: failedCount,
        color: '#FE7D61'
      },
      renderTabNode: <RequestList id={id} />
    }
  ];

  const handleChangeTab = async key => {
    setTabState({
      tabKey: key
    });
  };

  return (
    <div className={styles.tabsWrap}>
      <Row
        type="flex"
        style={{
          width: '100%',
          border: '1px solid rgba(239,239,239,1)',
          marginBottom: 16
        }}
      >
        {data.map((item: any, key) => {
          return (
            <Col
              key={key}
              style={{ width: '25%', position: 'relative' }}
              onClick={() => handleChangeTab(key)}
            >
              <div
                className={`${styles.moduleTabsWrap} ${tabState.tabKey ===
                  key && styles.moduleTabsWrapActive}`}
              >
                <p
                  className={`${styles.tabTitle} ${tabState.tabKey === key &&
                    styles.tabTitleActive}`}
                >
                  {item.title}
                </p>
                <p className={styles.firstLine}>
                  <span
                    className={`${styles.errorColor}`}
                    style={{ color: item.firstLineNum.color }}
                  >
                    {item.firstLineNum.value}
                  </span>
                  {item.firstLineTxt}
                </p>
                <p className={styles.secondLine}>
                  <span
                    className={styles.errorColor}
                    style={{
                      color: item.secondLineNum && item.secondLineNum.color
                    }}
                  >
                    {item.secondLineNum && item.secondLineNum.value}
                  </span>
                  {item.secondLineTxt}
                </p>
              </div>
              {data.length - 1 !== key && (
                <Divider
                  type="vertical"
                  style={{
                    position: 'absolute',
                    height: 60,
                    right: -8,
                    top: 30
                  }}
                />
              )}
            </Col>
          );
        })}
      </Row>
      <div>{data[tabState.tabKey].renderTabNode}</div>
    </div>
  );
};
export default ModuleTabs;
