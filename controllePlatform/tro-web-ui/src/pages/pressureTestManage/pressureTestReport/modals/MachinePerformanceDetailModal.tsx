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
import { CommonModal, CommonTable, useStateReducer } from 'racc';
import { ColumnProps } from 'antd/lib/table';
import { Pagination, Row, Col, Divider } from 'antd';
import { customColumnProps } from 'src/components/custom-table/utils';
import PressureTestReportService from '../service';
import styles from './../index.less';
import MachinePerformanceCharts from '../components/MachinePerformanceCharts';
interface Props {
  btnText?: string | React.ReactNode;
  reportId?: string;
  applicationName?: string;
  ip?: string;
}

interface State {
  detailData: any;
}
const MachinePerformanceDetailModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    detailData: null
  });

  const { detailData } = state;

  const handleClick = () => {
    const { ip, applicationName, reportId } = props;
    queryMachinePerformanceDetail({ applicationName, reportId, machineIp: ip });
  };

  /**
   * @name 获取风险机器性能详情
   */
  const queryMachinePerformanceDetail = async value => {
    const {
      data: { success, data }
    } = await PressureTestReportService.queryMachinePerformanceDetail({
      ...value
    });
    if (success) {
      setState({
        detailData: data
      });
    }
  };

  const topData = [
    {
      label: '主机IP',
      value: detailData && detailData.machineIp
    },
    {
      label: 'CPU',
      value: `${detailData && detailData.cpuNum}核`
    },
    {
      label: '内存',
      value: `${detailData && detailData.memorySize}G`
    },
    {
      label: '磁盘',
      value: `${detailData && detailData.diskSize}G`
    },
    {
      label: '宽带',
      value: `${detailData && detailData.mbps}Mbps`
    }
  ];

  return (
    <CommonModal
      modalProps={{
        width: 1100,
        footer: null,
        title: '机器性能详情'
      }}
      btnProps={{ type: 'link' }}
      btnText={props.btnText}
      onClick={() => handleClick()}
    >
      <div className={styles.topWrap}>
        <Row type="flex">
          <Col className={styles.topWrapTitle}>机器规格</Col>
          <Col>
            <Divider
              type="vertical"
              style={{
                height: '26px',
                marginLeft: 32,
                marginRight: 32,
                marginTop: 10
              }}
            />
          </Col>
          <Col className={styles.contentWrap} style={{ width: '90%' }}>
            <Row type="flex" justify="space-between">
              {topData.map((item, key) => {
                return (
                  <Col key={key} span={4}>
                    <p style={{ color: 'rgba(162, 166, 177, 1)' }}>
                      {item.label}:
                    </p>
                    <p style={{ fontSize: '14px' }}>{item.value}</p>
                  </Col>
                );
              })}
            </Row>
          </Col>
        </Row>
      </div>
      <div className={styles.machinePerformanceCharts}>
        <MachinePerformanceCharts
          chartsInfo={
            detailData && detailData.tpsTarget ? detailData.tpsTarget : []
          }
        />
      </div>
    </CommonModal>
  );
};
export default MachinePerformanceDetailModal;
