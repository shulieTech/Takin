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
import styles from './index.less';
import { Alert } from 'antd';
import SwitchBar from './components/SwitchBar';
import { useStateReducer } from 'racc';
import AppManageService from 'src/pages/appManage/service';
import ErrorTable from './components/ErrorTable';
import SwitchExplanationDrawer from './components/SwitchExplanationDrawer';
import SkeletonLoading from 'src/common/loading/SkeletonLoading';
interface Props {}
interface State {
  isReload: false;
  switchStatus: string;
  statusInfo: string;
  searchParams: {
    current: number;
    pageSize: number;
  };
  dataSource: any;
  loading: boolean;
}
const PressureMeasureSwitch: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    isReload: false,
    switchStatus: null,
    statusInfo: '',
    dataSource: [],
    searchParams: {
      current: 0,
      pageSize: 10
    },
    loading: false
  });

  useEffect(() => {
    querySwitchStatus();
  }, [state.isReload]);

  /**
   * @name 获取压测开关状态
   */
  const querySwitchStatus = async () => {
    setState({
      loading: true
    });
    const {
      data: { data, success }
    } = await AppManageService.querySwitchStatus({});
    if (success) {
      setState({
        switchStatus: data.switchStatus,
        statusInfo:
          data.switchStatus === 'OPENED'
            ? '压测配置已生效，确认各项压测配置正常后可进行安全压测。'
            : data.status === 'OPENING'
            ? '执行开启中，应用配置无法修改，所有压测配置失效。当前状态不能压测。'
            : data.status === 'CLOSING'
            ? '执行关闭中，应用配置无法修改，所有压测配置失效。当前状态不能压测。'
            : '所有压测配置失效。当前状态不能做压测任务，以免出现压测数据写入生产等风险。',
        dataSource: data.errorList,
        loading: false
      });
    }
    setState({
      loading: false
    });
  };
  return state.loading ? (
    <SkeletonLoading />
  ) : (
    <div style={{ padding: '24px 16px' }}>
      <div className={styles.title}>全局压测开关</div>
      <Alert
        type="warning"
        message={
          <p style={{ color: '#646676' }}>
            风险操作！操作前请仔细阅读
            <SwitchExplanationDrawer />
          </p>}
        showIcon
        style={{ marginTop: 40, marginBottom: 10 }}
      />
      <SwitchBar state={state} setState={setState} />
      {(state.switchStatus === 'CLOSE_FAILING' ||
        state.switchStatus === 'OPEN_FAILING') && (
        <Alert
          type="error"
          message={
            <p
              style={{
                color: '#393B4F',
                fontSize: 14,
                fontWeight: 600,
                lineHeight: '20px'
              }}
            >
              {state.switchStatus === 'CLOSE_FAILING'
                ? '关闭过程中存在以下异常，请尽快处理'
                : '部分开启失败，请尽快处理以下问题'}
            </p>
          }
          description={
            <p style={{ color: '#646676' }}>
              {state.switchStatus === 'CLOSE_FAILING'
                ? '建议重启问题所在应用后，点击右上角「重试」获取最新处理结果，异常处理完毕后才可进行后续操作。'
                : '建议重启问题所在应用后，点击右上角「重试」获取最新处理结果。异常全部处理完成后状态开启，可以进行后续操作。'}
            </p>}
          showIcon
          style={{ marginTop: 10, marginBottom: 10 }}
        />
      )}
      {(state.switchStatus === 'CLOSE_FAILING' ||
        state.switchStatus === 'OPEN_FAILING') && (
        <ErrorTable state={state} setState={setState} />
      )}
    </div>
  );
};
export default PressureMeasureSwitch;
