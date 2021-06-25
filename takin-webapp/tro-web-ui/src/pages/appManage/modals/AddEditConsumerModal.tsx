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

/**
 * @name
 * @author MingShined
 */
import { Input, message } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import { connect } from 'dva';
import { CommonForm, CommonModal, CommonSelect, useStateReducer } from 'racc';
import { FormDataType } from 'racc/dist/common-form/type';
import React from 'react';
import { CommonModelState } from 'src/models/common';
import { ShadowConsumerBean } from '../enum';
import AppManageService from '../service';
interface Props extends CommonModelState {
  id?: string;
  btnText: string;
  onSuccess: () => void;
  applicationId: string;
}
const AddEditConsumerModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer({
    form: null as WrappedFormUtils,
    details: {}
  });
  const text = props.id ? '编辑' : '新增';
  const getDetails = async () => {
    if (!props.id) {
      return;
    }
    const {
      data: { data, success }
    } = await AppManageService.getShdowConsumer({ id: props.id });
    if (success) {
      setState({ details: data });
    }
  };
  const getFormData = (): FormDataType[] => {
    return [
      {
        key: ShadowConsumerBean.MQ类型,
        label: 'MQ类型',
        options: {
          initialValue: state.details[ShadowConsumerBean.MQ类型],
          rules: [{ required: true, message: '请选择MQ类型' }]
        },
        node: (
          <CommonSelect
            placeholder="请选择MQ类型"
            dataSource={props.dictionaryMap.SHADOW_CONSUMER}
          />
        )
      },
      {
        key: ShadowConsumerBean.groupId,
        options: {
          initialValue: state.details[ShadowConsumerBean.groupId],
          rules: [{ required: true, message: '请输入topic#consumer group id' }]
        },
        label: 'topic#consumer group id',
        node: <Input placeholder="请输入topic#consumer group id" />
      }
    ];
  };
  const handleSubmit = () => {
    return new Promise(async resolve => {
      state.form.validateFields(async (err, values) => {
        if (err) {
          message.info('请检查表单必填项');
          resolve(false);
          return;
        }
        if (values[ShadowConsumerBean.groupId].indexOf('#') === -1) {
          message.info('topic#consumer group id请以#分割');
          resolve(false);
          return;
        }
        const result = {
          applicationId: props.applicationId,
          ...values
        };
        const ajaxEvent = props.id
          ? AppManageService.updateShdowConsumer({
            ...state.details,
            ...result
          })
          : AppManageService.createShdowConsumer(result);
        const {
          data: { success }
        } = await ajaxEvent;
        if (success) {
          message.success(`${text}成功`);
          resolve(true);
          props.onSuccess();
          return;
        }
        resolve(false);
      });
    });
  };
  return (
    <CommonModal
      beforeOk={handleSubmit}
      modalProps={{ title: text, width: 720, destroyOnClose: true }}
      btnText={props.btnText}
      btnProps={{ type: props.id ? 'link' : 'primary' }}
      onClick={getDetails}
    >
      <CommonForm
        rowNum={1}
        formItemProps={{ labelCol: { span: 8 }, wrapperCol: { span: 14 } }}
        btnProps={{ isResetBtn: false, isSubmitBtn: false }}
        getForm={form => setState({ form })}
        formData={getFormData()}
      />
    </CommonModal>
  );
};
export default connect(({ common }) => ({ ...common }))(AddEditConsumerModal);
