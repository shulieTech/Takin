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
import { CommonDrawer, CommonForm, useStateReducer } from 'racc';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import { Input, message } from 'antd';
import BlacklistService from '../service';

import { FormDataType } from 'racc/dist/common-form/type';

interface Props {
  action?: string;
  id?: string | Number;
  titles?: string | React.ReactNode;
  disabled?: boolean;
  onSccuess?: () => void;
  detailData: any;
  blistId?: string | Number;
}
interface State {
  form: any;
  blacklistDetail: any;
}
const AddAndEditBlacklistDrawer: React.FC<Props> = props => {
  const { action, id, titles, detailData, blistId } = props;

  const [state, setState] = useStateReducer<State>({
    form: null as WrappedFormUtils,
    blacklistDetail: {} as any
  });
  const { blacklistDetail } = state;
  const handleClick = async () => {
    if (action === 'edit') {
      queryBlacklistDetail();
    }
  };

  /**
   * @name 获取黑名单详情
   */
  const queryBlacklistDetail = async () => {
    const {
      data: { success, data }
    } = await BlacklistService.queryBlacklistDetail({ id: blistId });
    if (success) {
      setState({
        blacklistDetail: data
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
         * @name 增加黑名单
         */
        if (action === 'add') {
          const {
            data: { success, data }
          } = await BlacklistService.addBlacklist({
            ...result,
            applicationId: id
          });
          if (success) {
            message.success('增加黑名单成功');
            props.onSccuess();
            resolve(true);
          }
        }

        /**
         * @name 编辑黑名单
         */
        if (action === 'edit') {
          const {
            data: { success, data }
          } = await BlacklistService.editBlacklist({
            ...result,
            blistId
          });

          if (success) {
            message.success('编辑黑名单成功');
            props.onSccuess();
            resolve(true);
          }
        }
        resolve(false);
      });
    });
  };

  const getBlacklistFormData = (): FormDataType[] => [
    {
      key: 'applicationName',
      label: '应用',
      options: {
        initialValue: detailData && detailData.applicationName,
        rules: [
          {
            required: true,
            message: '请输入应用名'
          }
        ]
      },
      node: <Input disabled={true} />
    },
    {
      key: 'redisKey',
      label: 'redis key',
      options: {
        initialValue:
          action !== 'add'
            ? blacklistDetail && blacklistDetail.redisKey
            : undefined,
        rules: [
          {
            required: true,
            message: '请输入redis key'
          }
        ]
      },
      node: <Input placeholder="请输入redis key" />
    }
  ];

  return (
    <CommonDrawer
      btnText={titles}
      drawerProps={{
        width: 650,
        title: action === 'add' ? '新增黑名单' : '编辑黑名单',
        maskClosable: false
      }}
      drawerFooterProps={{
        okText: action === 'add' ? '确认新增' : '确认编辑',
        style: { zIndex: 10 }
      }}
      btnProps={
        action === 'edit' && {
          type: 'link'
        }
      }
      beforeOk={() => handleSubmit()}
      onClick={() => handleClick()}
    >
      <CommonForm
        getForm={form => setState({ form })}
        formData={getBlacklistFormData()}
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
export default AddAndEditBlacklistDrawer;
