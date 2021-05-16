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

import { Col, message, Modal, Row, Switch } from 'antd';
import { connect } from 'dva';
import { useStateReducer } from 'racc';
import React, { Fragment, useEffect } from 'react';
import SearchTable from 'src/components/search-table';
import AppManageService from 'src/pages/appManage/service';
import { router } from 'umi';
import getPressureTestSceneColumns from './components/PressureTestSceneColumn';
import getPressureTestSceneFormData from './components/PressureTestSceneFormData';
import PressureTestSceneTableAction from './components/TableAction';
import styles from './index.less';
import PressureTestSceneService from './service';

interface PressureTestSceneProps {
  dictionaryMap?: any;
}

export interface PressureTestSceneState {
  switchStatus: string;
  isReload?: boolean;
  visible: boolean;
  searchParams: {
    current: string | number;
    pageSize: string | number;
  };
  configStatus: string;
  configErrorList: string[];
  startStatus: string;
  startErrorList: string[];
  missingDataSwitch: boolean;
  missingDataStatus: boolean;
  sceneId: Number;
  hasMissingData: boolean;
}

const PressureTestScene: React.FC<PressureTestSceneProps> = props => {
  const [state, setState] = useStateReducer<PressureTestSceneState>({
    isReload: false,
    switchStatus: null,
    visible: false,
    searchParams: {
      current: 0,
      pageSize: 10
    },
    /**
     * @name 配置监测状态
     */
    configStatus: 'ready',
    configErrorList: null,
    /**
     * @name 启动状态
     */
    startStatus: 'ready',
    startErrorList: null,
    missingDataSwitch: false,
    missingDataStatus: false,
    sceneId: null,
    hasMissingData: false
  });

  useEffect(() => {
    querySwitchStatus();
  }, []);

  /**
   * @name 获取压测开关状态
   */
  const querySwitchStatus = async () => {
    const {
      data: { data, success }
    } = await AppManageService.querySwitchStatus({});
    if (success) {
      setState({
        switchStatus: data.switchStatus
      });
    }
  };

  /**
   * @name 启动检查并开启压测
   */
  const handleCheckAndStart = async sceneId => {
    setState({
      visible: true,
      configStatus: 'loading',
      missingDataStatus: false
    });
    const {
      data: { data, success }
    } = await PressureTestSceneService.startPressureTestScene({
      sceneId,
      leakSqlEnable: state.missingDataSwitch
    });
    if (success && data.data) {
      setState({
        configStatus: 'success'
      });
      handleStart(sceneId, data.data);
    } else {
      setState({
        configStatus: 'fail',
        configErrorList: data && data.msg
      });
    }
  };

  /**
   * @name 开启压测
   */
  const handleStart = async (sceneId, reportId) => {
    setState({
      startStatus: 'loading'
    });
    const {
      data: { data, success }
    } = await PressureTestSceneService.checkStartStatus({ sceneId, reportId });
    if (success && data.data !== 0) {
      if (data.data === 2) {
        setState({
          startStatus: 'success'
        });
        const startTime: any = new Date().getTime();
        localStorage.setItem('startTime', startTime);
        message.success('开启压测场景成功！');
        router.push(
          `/pressureTestManage/pressureTestReport/pressureTestLive?id=${sceneId}`
        );
      } else if (data.data === 1) {
        setTimeout(() => {
          handleStart(sceneId, reportId);
        }, 500);
      }
    } else {
      setState({
        startStatus: 'fail',
        startErrorList: data.msg
      });
    }
  };

  const handleCancel = () => {
    setState({
      visible: false,
      hasMissingData: false,
      missingDataStatus: false,
      missingDataSwitch: false
    });
  };

  const handleChangeMissingDataSwitch = () => {
    setState({
      missingDataSwitch: !state.missingDataSwitch
    });
  };

  return (
    <Fragment>
      <SearchTable
        commonTableProps={{
          columns: getPressureTestSceneColumns(
            state,
            setState,
            props.dictionaryMap
          )
        }}
        commonFormProps={{
          formData: getPressureTestSceneFormData(),
          rowNum: 4
        }}
        ajaxProps={{ url: '/scenemanage/list', method: 'GET' }}
        toggleRoload={state.isReload}
        tableAction={
          <PressureTestSceneTableAction state={state} setState={setState} />}
        datekeys={[
          {
            originKey: 'time',
            separateKey: ['lastPtStartTime', 'lastPtEndTime']
          }
        ]}
      />
      <Modal
        title={state.hasMissingData ? '是否要开启数据验证' : '启动压测'}
        visible={state.missingDataStatus}
        onOk={() => {
          handleCheckAndStart(state.sceneId);
        }}
        onCancel={() => {
          setState({
            missingDataStatus: false,
            missingDataSwitch: false,
            sceneId: null
          });
        }}
      >
        <div style={{ color: '#8C8C8C', lineHeight: '22px', fontSize: 14 }}>
          {state.hasMissingData
            ? '开启数据验证将会产生额外的性能消耗，建议仅在试跑时开启'
            : '是否确认启动压测？'}
        </div>
        {state.hasMissingData && (
          <Row
            type="flex"
            justify="space-between"
            style={{
              marginTop: 16,
              borderTop: '1px solid #F6F6F6',
              padding: '16px 0',
              borderBottom: '1px solid #F6F6F6'
            }}
          >
            <Col style={{ fontSize: 14, color: '#595959' }}>数据验证开关</Col>
            <Col>
              <Switch
                checked={state.missingDataSwitch}
                onChange={handleChangeMissingDataSwitch}
              />
            </Col>
          </Row>
        )}
      </Modal>
      <Modal
        title="启动进度"
        visible={state.visible}
        footer={null}
        closable={
          state.configStatus === 'fail' || state.startStatus === 'fail'
            ? true
            : false
        }
        bodyStyle={{
          width: 522,
          minHeight: 279
        }}
        onCancel={() => handleCancel()}
      >
        {state.configStatus === 'fail' ? (
          <div>
            <p className={styles.modalErrorImg}>
              <img
                style={{ width: 50 }}
                src={require('./../../../assets/config_fail.png')}
              />
            </p>
            <p className={styles.modalFailTitle}>
              压测配置检测未通过，请调整后重试
            </p>
            <div className={styles.modalErrorWrap}>
              <p className={styles.modalErrorWrapTitle}>异常内容</p>
              {state.configErrorList &&
                state.configErrorList.map((item, k) => {
                  return (
                    <p key={k}>
                      {k + 1}、{item}
                    </p>
                  );
                })}
            </div>
          </div>
        ) : state.startStatus === 'fail' ? (
          <div>
            <p className={styles.modalErrorImg}>
              <img
                style={{ width: 50 }}
                src={require('./../../../assets/config_fail.png')}
              />
            </p>
            <p className={styles.modalFailTitle}>启动失败</p>
            {state.startErrorList &&
              state.startErrorList.map((item, k) => {
                return (
                  <p key={k} className={styles.errorReason}>
                    {item}
                  </p>
                );
              })}
          </div>
        ) : (
          <Row
            type="flex"
            align="middle"
            justify="center"
            style={{ marginTop: 24 }}
          >
            <Col style={{ textAlign: 'center' }}>
              <p>
                <img
                  style={{ width: 72, marginBottom: 8 }}
                  src={require(`./../../../assets/${
                    state.configStatus === 'ready' ||
                    state.configStatus === 'loading'
                      ? 'config_ready'
                      : 'config_success'
                  }.png`)}
                />
              </p>
              <span style={{ color: '#474C50' }}>
                {state.configStatus === 'success'
                  ? '压测配置检查无误'
                  : '压测配置检查中···'}
              </span>
            </Col>
            <Col>
              <div
                style={{
                  width: 80,
                  height: 1,
                  border:
                    state.configStatus === 'success'
                      ? '1px dotted #29C7D7'
                      : '1px dotted #CACED5',
                  marginBottom: 8,
                  marginRight: 8
                }}
              />
            </Col>
            <Col
              style={{
                textAlign: 'center'
              }}
            >
              <p>
                <img
                  style={{ width: 72, marginBottom: 8 }}
                  src={require(`./../../../assets/${
                    state.startStatus === 'ready'
                      ? 'start_ready'
                      : state.startStatus === 'loading'
                      ? 'start_ready'
                      : 'start_ing'
                  }.png`)}
                />
              </p>
              <span
                style={{
                  color: state.startStatus === 'ready' ? '#E4EAF0' : '#474C50'
                }}
              >
                {state.startStatus === 'ready' ? '启动压测' : '启动中'}
              </span>
            </Col>
          </Row>
        )}
      </Modal>
    </Fragment>
  );
};
export default connect(({ common }) => ({ ...common }))(PressureTestScene);
