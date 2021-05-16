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
import WhitelistSwitchService from '../service';
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
    if (status === 1) {
      content =
        '关闭白名单校验后有可能导致压测流量泄露至未接入的应用，请谨慎操作！';
    } else {
      content =
        '开启白名单校验后仅白名单内的接口可被压测流量调用，是否确认开启？';
    }

    confirm({
      content,
      title: `高风险操作，是否${status === 1 ? '关闭' : '开启'}白名单校验机制`,
      okType: 'danger',
      okText: status === 1 ? '确认关闭' : '确认开启',
      onOk() {
        status === 1 ? closeSwitchStatus() : openSwitchStatus();
      }
    });
  };

  /**
   * @name 开启白名单开关
   */
  const openSwitchStatus = async () => {
    const {
      data: { data, success }
    } = await WhitelistSwitchService.openSwitchStatus({});
    if (success) {
      setState({
        isReload: !state.isReload
      });
    }
  };

  /**
   * @name 关闭白名单开关
   */
  const closeSwitchStatus = async () => {
    const {
      data: { data, success }
    } = await WhitelistSwitchService.closeSwitchStatus({});
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
              {state.switchStatus === 1 ? '已开启' : '已关闭'}
            </span>}
          color={state.switchStatus === 1 ? '#11BBD5' : '#FE7D61'}
        />
      </Col>
      <Col style={{ paddingTop: 10 }}>
        <p className={styles.switchLabel}>状态说明</p>
        <p>{state.statusInfo}</p>
      </Col>
      <AuthorityBtn
        isShow={
          btnAuthority &&
          btnAuthority.configCenter_whitelistSwitch_6_enable_disable
        }
      >
        <Col style={{ marginRight: 40, marginLeft: 40 }}>
          <Divider type="vertical" style={{ height: 51, marginTop: 10 }} />
        </Col>
        <Col style={{ paddingTop: 10 }}>
          <Button type="primary" onClick={() => showModal(state.switchStatus)}>
            {state.switchStatus === 1 ? '关闭' : '开启'}
          </Button>
        </Col>
      </AuthorityBtn>
    </Row>
  );
};
export default SwitchBar;
