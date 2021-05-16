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
import { customColumnProps } from 'src/components/custom-table/utils';
import CustomTable from 'src/components/custom-table';
import { Typography, Row, Tabs } from 'antd';
import styles from './../index.less';
import Loading from 'src/common/loading';
import AppManageService from '../service';
import RoleMemberList from '../components/RoleMemberList';
import FunctionJurisdictionList from '../components/FunctionJurisdictionList';

interface Props {
  btnText: string | React.ReactNode;
  appName?: string;
  appId?: string;
}

interface State {
  roleList: any[];
  roleKey: string;
  roleInfo: any;
  isReload: boolean;
}
const AppMemberConfigModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    roleList: null,
    roleKey: null,
    roleInfo: null,
    isReload: false
  });
  const { Paragraph } = Typography;
  const { appName, appId } = props;
  const { roleList, roleKey, roleInfo } = state;

  const handleClick = () => {
    queryAppRoleList({
      applicationId: appId
    });
  };

  /**
   * @name 获取角色列表
   */
  const queryAppRoleList = async value => {
    const {
      data: { success, data }
    } = await AppManageService.queryAppRoleList({
      ...value
    });
    if (success) {
      setState({
        roleList: data,
        roleKey: data && data[0] && data[0].id,
        roleInfo: data && data[0]
      });
    }
  };

  const handleChangeTab = (id, item) => {
    setState({
      roleKey: id,
      roleInfo: item
    });
  };

  return (
    <CommonModal
      modalProps={{
        width: 1234,
        footer: null,
        title: <p style={{ fontSize: 16 }}>角色成员配置({appName})</p>
      }}
      btnProps={{ type: 'link' }}
      btnText={props.btnText}
      onClick={() => handleClick()}
    >
      <Row type="flex" style={{ height: 700, overflow: 'scroll' }}>
        <div className={styles.leftSelected}>
          {roleList &&
            roleList.map((item, key) => {
              return (
                <p
                  key={item.id}
                  className={
                    state.roleKey === item.id ? styles.itemActive : styles.item
                  }
                  onClick={() => handleChangeTab(item.id, item)}
                >
                  {item.name}
                </p>
              );
            })}
        </div>
        <div className={styles.roleRightWrap}>
          <Row
            type="flex"
            align="middle"
            style={{ padding: 16, borderBottom: '1px solid #F2F2F2' }}
          >
            <p className={styles.roleTitle}>{roleInfo && roleInfo.name}</p>
            <p className={styles.roleDes}>{roleInfo && roleInfo.description}</p>
          </Row>
          <Tabs defaultActiveKey="1">
            <Tabs.TabPane tab="角色成员" key="1">
              <RoleMemberList
                roleId={roleKey}
                roleMemberState={state}
                setRoleMemberState={setState}
              />
            </Tabs.TabPane>
            <Tabs.TabPane tab="功能权限" key="2">
              <FunctionJurisdictionList
                dataSource={roleInfo && roleInfo.actionList}
              />
            </Tabs.TabPane>
          </Tabs>
        </div>
      </Row>
    </CommonModal>
  );
};
export default AppMemberConfigModal;
