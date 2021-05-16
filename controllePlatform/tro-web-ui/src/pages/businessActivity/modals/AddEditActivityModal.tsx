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
import { Col, message, Row } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import { CommonModal, useStateReducer } from 'racc';
import React from 'react';
import { getEntranceInfo } from '../addEditPage';
import AddEditForm from '../components/AddEditForm';
import BusinessActivityService from '../service';
interface Props {
  id?: string;
  onSuccess: () => void;
}
const getInitState = () => ({
  systemName: undefined,
  service: undefined,
  serviceName: undefined,
  serviceType: undefined,
  app: undefined,
  appName: undefined,
  serviceList: [],
  loading: true,
  isCore: undefined,
  link_level: undefined,
  businessDomain: undefined,
  form: null as WrappedFormUtils
});
export type AddEditActivityModalState = ReturnType<typeof getInitState>;
const AddEditActivityModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<AddEditActivityModalState>(
    getInitState()
  );
  const { id } = props;
  const queryDetails = async () => {
    if (!id) {
      setState(getInitState());
      return;
    }
    const {
      data: { data, success }
    } = await BusinessActivityService.querySystemProcess({
      id
    });
    if (success) {
      setState({
        systemName: data.activityName,
        app: data.applicationName,
        serviceType: data.type,
        service: data.entranceName,
        isCore: data.isCore,
        link_level: data.link_level,
        businessDomain: data.businessDomain
      });
    }
  };
  const handleSubmit = async () => {
    return new Promise(async resolve => {
      state.form.validateFields((err, values) => {
        if (err) {
          message.info('请检查表单必填项');
          resolve(false);
          return;
        }
        const result = id ? handleUpdate(values) : handleCreate(values);
        resolve(result);
      });
    });
  };
  const handleCreate = async values => {
    const {
      data: { success }
    } = await BusinessActivityService.createSystemProcess({
      activityName: state.systemName,
      applicationName: state.appName,
      entranceName: state.serviceName,
      linkId: state.service,
      type: state.serviceType,
      ...values,
      ...getEntranceInfo(state.serviceList, state.service)
    });
    if (success) {
      message.success('成功新增业务活动');
      props.onSuccess();
      return true;
    }
  };
  const handleUpdate = async values => {
    const {
      data: { success }
    } = await BusinessActivityService.updateSystemProcess({
      activityName: state.systemName,
      applicationId: state.app,
      applicationName: state.appName,
      entranceName: state.serviceName,
      linkId: state.service,
      type: state.serviceType,
      activityId: props.id,
      isCore: state.isCore,
      link_level: state.link_level,
      businessDomain: state.businessDomain,
      ...values,
      ...getEntranceInfo(state.serviceList, state.service)
    });
    if (success) {
      message.success('成功修改业务活动');
      props.onSuccess();
      return true;
    }
  };
  return (
    <CommonModal
      btnProps={{ type: id ? 'link' : 'primary' }}
      modalProps={{
        title: `${id ? '编辑' : '新增'}业务活动`,
        width: '95%',
        centered: true,
        bodyStyle: { minHeight: 500, overflow: 'hidden' },
        style: { top: 15 },
        destroyOnClose: true
      }}
      btnText={id ? '编辑' : '新增业务活动'}
      onClick={queryDetails}
      beforeOk={handleSubmit}
    >
      <Row type="flex">
        <Col style={{ width: 400 }}>
          <AddEditForm {...state} setState={setState} />
        </Col>
      </Row>
    </CommonModal>
  );
};
export default AddEditActivityModal;
