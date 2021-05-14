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
import { Select, Input, message } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import { connect } from 'dva';
import { CommonForm, CommonModal, useStateReducer, CommonSelect } from 'racc';
import { FormDataType } from 'racc/dist/common-form/type';
import React, { useEffect } from 'react';
import { CommonModelState } from 'src/models/common';
import { SceneBean } from '../enum';
import MissionManageService from '../service';
const { TextArea } = Input;
const { Option } = Select;
interface Props extends CommonModelState {
  id?: string;
  btnText: string;
  onSuccess: () => void;
}
const NewKanbanModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer({
    form: null as WrappedFormUtils,
    PATROL_EXCEPTION_STATUS_CHANGE_1: [],
    details: {},
    disabled: false
  });
  const text = props.btnText === '误报' ? '误报判定' : '误报说明';

  const getDetails = async () => {
    queryDetial();
    const {
      data: { data, success }
    } = await MissionManageService.queryPatrolSceneAndDashbordList({});
    if (success) {
      setState({
        PATROL_EXCEPTION_STATUS_CHANGE_1:
          data.PATROL_EXCEPTION_STATUS_CHANGE_1 &&
          data.PATROL_EXCEPTION_STATUS_CHANGE_1.map((item1, k1) => {
            return {
              label: item1.label,
              value: item1.value
            };
          })
      });
    }
  };

  const queryDetial = async () => {
    if (props.btnText !== '误报') {
      const {
        data: { data, success }
      } = await MissionManageService.queryMistakeDetail(props.id);
      if (success) {
        setState({
          details: data,
          disabled: true
        });
      }
    }
  };
  const getFormData = (): FormDataType[] => {
    return [
      {
        key: SceneBean.误报类型,
        label: '误报类型',
        options: {
          initialValue: state.details[SceneBean.误报类型] ? `${state.details[SceneBean.误报类型]}` : [],
          rules: [{ required: true, message: '请输入误报类型' }]
        },
        node: (
          <Select placeholder="请输入误报类型" disabled={state.disabled}>
            {state.PATROL_EXCEPTION_STATUS_CHANGE_1 && state.PATROL_EXCEPTION_STATUS_CHANGE_1.map(ite => {
              return (
                <Option value={ite.value} key={ite.value}>{ite.label}</Option>
              );
            })}
          </Select>
        )
      },
      {
        key: SceneBean.具体描述,
        label: '具体描述',
        options: {
          initialValue: state.details[SceneBean.具体描述],
          rules: [{ required: true, message: '请输入描述，200字以内' }]
        },
        node: <TextArea placeholder="请输入描述，200字以内" disabled={state.disabled} maxLength={200} />
      },
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

        const result = {
          id: props.id,
          ...values
        };
        const {
          data: { success }
        } = await MissionManageService.mistake(result);
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
  let modalProps = {};
  if (props.btnText === '查看误报') {
    modalProps = { title: text, width: 480, destroyOnClose: true, footer: null };
  } else {
    modalProps = { title: text, width: 480, destroyOnClose: true };
  }

  return (
    <CommonModal
      beforeOk={handleSubmit}
      modalProps={...modalProps}
      btnText={props.btnText}
      btnProps={{ type: 'link' }}
      onClick={getDetails}
    >
      <CommonForm
        rowNum={1}
        formItemProps={{ labelCol: { span: 6 }, wrapperCol: { span: 16 } }}
        btnProps={{ isResetBtn: false, isSubmitBtn: false }}
        getForm={form => setState({ form })}
        formData={getFormData()}
      />
    </CommonModal>
  );
};
export default (NewKanbanModal);
