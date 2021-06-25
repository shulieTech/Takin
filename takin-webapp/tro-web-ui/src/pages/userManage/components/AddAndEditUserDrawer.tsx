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

import { message } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import { CommonDrawer, CommonForm, useStateReducer } from 'racc';
import React from 'react';
import UserManageService from '../service';
import getUserFormData from './UserFormData';

interface Props {
  action?: string;
  id?: string | Number;
  titles?: string | React.ReactNode;
  onSccuess?: () => void;
}
interface State {
  form: any;
  userDetail: any;
}
const AddAndEditUserDrawer: React.FC<Props> = props => {
  const { action, id, titles } = props;
  const [state, setState] = useStateReducer<State>({
    form: null as WrappedFormUtils,
    userDetail: {} as any
  });

  const handleClick = async () => {
    if (action === 'edit') {
      queryUserDetail();
    }
  };

  /**
   * @name 获取用户详情
   */
  const queryUserDetail = async () => {
    const {
      data: { success, data }
    } = await UserManageService.queryUserDetail({ id });
    if (success) {
      setState({
        userDetail: data
      });
    }
  };

  /**
   * @name 提交
   */
  const handleSubmit = async () => {
    return await new Promise(resolve => {
      state.form.validateFields(async (err, values) => {
        if (err) {
          message.error('请检查表单必填项');
          resolve(false);
          return false;
        }

        const result = {
          ...values
        };

        /**
         * @name 增加用户
         */
        if (action === 'add') {
          const {
            data: { success, data }
          } = await UserManageService.addUser(result);
          if (success) {
            message.success('增加客户成功');
            props.onSccuess();
            resolve(true);
          }
        }

        /**
         * @name 编辑客户
         */
        if (action === 'edit') {
          const {
            data: { success, data }
          } = await UserManageService.editUser({
            ...result,
            id
          });
          if (success) {
            message.success('修改客户成功');
            props.onSccuess();
            resolve(true);
          }
        }
        resolve(false);
      });
    });
  };

  return (
    <CommonDrawer
      btnText={titles}
      drawerProps={{
        width: 650,
        title: action === 'add' ? '新增客户' : '编辑客户',
        maskClosable: false
      }}
      drawerFooterProps={{
        okText: action === 'add' ? '确认新增' : '确认编辑',
        style: { zIndex: 10 }
      }}
      btnProps={
        action !== 'add' && {
          type: 'link'
        }
      }
      beforeOk={() => handleSubmit()}
      onClick={() => handleClick()}
    >
      <CommonForm
        getForm={form => setState({ form })}
        formData={getUserFormData(state, action, setState)}
        btnProps={{
          isResetBtn: false,
          isSubmitBtn: false
        }}
        rowNum={1}
        formItemProps={{ labelCol: { span: 6 }, wrapperCol: { span: 14 } }}
      />
    </CommonDrawer>
  );
};
export default AddAndEditUserDrawer;
