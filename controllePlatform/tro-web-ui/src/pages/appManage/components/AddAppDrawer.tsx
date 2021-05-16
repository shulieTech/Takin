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
import { openNotification } from 'src/common/custom-notification/CustomNotification';
import AppManageService from '../service';
import getAppFormData from './AppFormData';
interface Props {
  action?: string;
  id?: string | Number;
  titles?: string | React.ReactNode;
  disabled?: boolean;
  onSccuess?: () => void;
}
interface State {
  form: any;
  appDetail: any;
  appName: string;
}
const AddAppDrawer: React.FC<Props> = props => {
  const { action, id, titles } = props;
  const [state, setState] = useStateReducer<State>({
    form: null as WrappedFormUtils,
    appDetail: {} as any,
    appName: undefined
  });

  const handleClick = async () => {
    if (action === 'edit') {
      queryAppDetail();
    }
  };

  /**
   * @name 获取应用详情
   */
  const queryAppDetail = async () => {
    const {
      data: { success, data }
    } = await AppManageService.queryAppManageDetail({ id });
    if (success) {
      setState({
        appDetail: data,
        appName: data.applicationName
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
         * @name 增加应用
         */
        if (action === 'add') {
          const {
            data: { success, data }
          } = await AppManageService.addApp(result);
          if (success) {
            message.success('增加应用成功');
            props.onSccuess();
            resolve(true);
          }
        }

        /**
         * @name 编辑应用
         */
        if (action === 'edit') {
          const {
            data: { success, data }
          } = await AppManageService.editApp({
            ...result,
            id
          });

          if (success) {
            openNotification('编辑应用成功', '');
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
        title: action === 'add' ? '新增应用' : '编辑应用',
        maskClosable: false
      }}
      drawerFooterProps={{
        okText: action === 'add' ? '确认新增' : '确认编辑',
        style: { zIndex: 10 }
      }}
      btnProps={{
        disabled: props.disabled,
        type: action === 'edit' ? 'link' : 'default'
      }}
      beforeOk={() => handleSubmit()}
      onClick={() => handleClick()}
    >
      <CommonForm
        getForm={form => setState({ form })}
        formData={getAppFormData(state, action, setState)}
        btnProps={{
          isResetBtn: false,
          isSubmitBtn: false
        }}
        rowNum={1}
        formItemProps={{ labelCol: { span: 7 }, wrapperCol: { span: 13 } }}
      />
    </CommonDrawer>
  );
};
export default AddAppDrawer;
