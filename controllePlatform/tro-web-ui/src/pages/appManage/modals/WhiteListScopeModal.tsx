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

import { message, Table, Transfer } from 'antd';
import { TableRowSelection } from 'antd/lib/table';
import { difference } from 'lodash';
import { CommonModal, useStateReducer } from 'racc';
import React, { Fragment } from 'react';
import AppManageService from '../service';
interface Props {
  btnText?: string | React.ReactDOM;
  wlistId: string;
  interfaceName: string;
  onSccuess?: () => void;
}
const getInitState = () => ({
  dataSource: [],
  selectKeys: [],
  loading: false,
  /**
   * @name 初始被选中的应用，array
   */
  targetKeys: []
});
export type WhiteListScopeModalState = ReturnType<typeof getInitState>;
const WhiteListScopeModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<WhiteListScopeModalState>(
    getInitState()
  );

  const onChange = nextTargetKeys => {
    setState({
      targetKeys: nextTargetKeys,
      selectKeys: []
    });
  };
  const leftTableColumns = [
    {
      dataIndex: 'app',
      title: '应用名称'
    }
  ];
  const rightTableColumns = [
    {
      dataIndex: 'app',
      title: '应用名称'
    }
  ];

  const handleClick = () => {
    queryAppList();
  };

  /**
   * @name 获取应用列表
   */
  const queryAppList = async () => {
    const {
      data: { success, data }
    } = await AppManageService.queryAppList({
      wlistId: props.wlistId
    });
    if (success) {
      setState({
        dataSource:
          data &&
          data.allAppNames &&
          data.allAppNames.map((item, k) => {
            return { key: item };
          }),
        targetKeys: data.effectiveAppNames
      });
    }
  };

  /**
   * @name 设置白名单部分生效问题
   */
  const handleSubmit = async () => {
    return await new Promise(async resolve => {
      const {
        total,
        data: { success, data }
      } = await AppManageService.whiteListPart({
        effectiveAppName: state.targetKeys,
        wlistId: props.wlistId
      });
      if (success) {
        message.success('局部生效设置成功');
        props.onSccuess();
        resolve(true);
        return;
      }
      resolve(false);
    });
  };

  return (
    <CommonModal
      modalProps={{
        width: 1096,
        title: '白名单生效范围'
      }}
      btnProps={{ type: 'link' }}
      btnText={props.btnText}
      beforeOk={handleSubmit}
      onClick={() => handleClick()}
    >
      <Transfer
        dataSource={state.dataSource}
        showSearch
        listStyle={{
          width: 480,
          height: 500
        }}
        targetKeys={state.targetKeys}
        onChange={onChange}
        render={item => item.key}
        searchPlaceholder={'请输入应用名称'}
      />
    </CommonModal>
  );
};
export default WhiteListScopeModal;
