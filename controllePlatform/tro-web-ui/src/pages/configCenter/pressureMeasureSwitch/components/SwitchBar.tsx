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
import { Row, Col, Badge, Divider, Switch, Modal, Tooltip, Button } from 'antd';
import styles from './../index.less';
import AppManageService from 'src/pages/appManage/service';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';

interface Props {
  state?: any;
  setState?: (value) => void;
}
const SwitchBar: React.FC<Props> = props => {
  const btnAuthority: any =
    localStorage.getItem('trowebBtnResource') &&
    JSON.parse(localStorage.getItem('trowebBtnResource'));
  const { state, setState } = props;
  const { confirm } = Modal;
  const showModal = status => {
    let content;
    if (status === 'OPENED') {
      content =
        '操作后所有应用配置失效，请确保当前没有正在进行的压测，避免出现压测数据写入生产等异常';
    } else {
      content =
        '成功开启开关后配置生效，开启过程中请不要进行压测，避免出现压测数据写入生产等异常';
    }

    confirm({
      content,
      title: '风险操作，谨慎处理',
      okType: 'danger',
      okText: status === 'OPENED' ? '确认关闭' : '确认开启',
      onOk() {
        editSwitchStatus(status === 'OPENED' ? false : true);
      }
    });
  };

  /**
   * @name 开启或关闭压测开关
   */
  const editSwitchStatus = async status => {
    const {
      data: { data, success }
    } = await AppManageService.editSwitchStatus({ pressureEnable: status });
    if (success) {
      setState({
        isReload: !state.isReload
      });
    }
  };

  /**
   * @name 重试开启或关闭压测开关
   */
  const retryEditSwitchStatus = async () => {
    const {
      data: { data, success }
    } = await AppManageService.retryEditSwitchStatus();
    if (success) {
      setState({
        isReload: !state.isReload
      });
    }
  };

  return (
    <Row type="flex" className={styles.switchWrap} align="middle">
      <Col style={{ marginRight: 24 }}>
        <img
          style={{ width: 72 }}
          src={require('./../../../../assets/switch_icon.png')}
        />
      </Col>
      <Col
        style={{
          paddingTop: 10,
          marginRight: 64,
          fontFamily: 'PingFangSC-Semibold,PingFang SC',
          color: '#393B4F'
        }}
      >
        <p className={styles.switchLabel}>开关状态</p>
        <Badge
          text={
            <span style={{ fontSize: 20, fontWeight: 600 }}>
              {state.switchStatus === 'OPENED'
                ? '已开启'
                : state.switchStatus === 'OPENING'
                ? '开启中'
                : state.switchStatus === 'CLOSING'
                ? '关闭中'
                : state.switchStatus === 'OPEN_FAILING'
                ? '开启异常'
                : state.switchStatus === 'CLOSE_FAILING'
                ? '关闭异常'
                : '已关闭'}
              {(state.switchStatus === 'OPENING' ||
                state.switchStatus === 'CLOSING') && (
                <span style={{ marginLeft: 8, color: '#FE7D61' }}>
                  {`${
                    state.switchStatus === 'OPENING' ? '开启' : '关闭'
                  }过程约20分钟，刷新页面查看结果`}
                </span>
              )}
            </span>}
          color={
            state.switchStatus === 'OPENED'
              ? '#11BBD5'
              : state.switchStatus === 'OPENING'
              ? '#FFB64A'
              : state.switchStatus === 'CLOSING'
              ? '#FFB64A'
              : '#FE7D61'
          }
        />
      </Col>
      <Col style={{ paddingTop: 10 }}>
        <p className={styles.switchLabel}>状态说明</p>
        <p>{state.statusInfo}</p>
      </Col>
      <AuthorityBtn
        isShow={
          btnAuthority &&
          btnAuthority.configCenter_pressureMeasureSwitch_6_enable_disable
        }
      >
        {state.switchStatus === 'OPEN_FAILING' ||
        state.switchStatus === 'CLOSE_FAILING' ||
        state.switchStatus === 'OPENED' ||
        state.switchStatus === 'CLOSED' ? (
          <Col style={{ marginRight: 40, marginLeft: 40 }}>
            <Divider type="vertical" style={{ height: 51, marginTop: 10 }} />
          </Col>
        ) : null}
        <Col style={{ paddingTop: 10 }}>
          {state.switchStatus === 'OPEN_FAILING' ||
          state.switchStatus === 'CLOSE_FAILING' ? (
            <Button type="primary" onClick={() => retryEditSwitchStatus()}>
              重试
            </Button>
          ) : state.switchStatus === 'OPENED' ||
            state.switchStatus === 'CLOSED' ? (
            <Button
              type="primary"
              onClick={() => showModal(state.switchStatus)}
            >
              {state.switchStatus === 'OPENED' ? '关闭' : '开启'}
            </Button>
          ) : null}
        </Col>
      </AuthorityBtn>
    </Row>
  );
};
export default SwitchBar;
