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
import { Button, message } from 'antd';
import React, { Fragment, useContext } from 'react';
import { router } from 'umi';
import { AddEditSystemPageContext, getEntranceInfo } from '../addEditPage';
import BusinessActivityService from '../service';
interface Props {
  id: string;
}
const AddEditExtra: React.FC<Props> = props => {
  const { state, setState } = useContext(AddEditSystemPageContext);
  const handleValidate = () => {
    let result = true;
    if (!state.systemName || !state.systemName.trim()) {
      message.info('请填写业务活动名称');
      result = false;
      return result;
    }
    if (!state.isCore) {
      message.info('请选择业务活动类型');
      result = false;
      return result;
    }
    if (!state.link_level) {
      message.info('请选择业务活动级别');
      result = false;
      return result;
    }
    if (!state.businessDomain) {
      message.info('请选择业务域');
      result = false;
      return result;
    }
    if (!state.app) {
      message.info('请选择应用');
      result = false;
      return result;
    }
    if (!state.serviceType) {
      message.info('请选择服务类型');
      result = false;
      return result;
    }
    if (!state.service) {
      message.info('请选择服务');
      result = false;
      return result;
    }
    return result;
  };
  const handleSubmit = async () => {
    const result = handleValidate();
    if (!result) {
      return;
    }
    if (props.id) {
      handleUpdate();
      return;
    }
    handleCreate();
  };
  const handleUpdate = async () => {
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
      ...getEntranceInfo(state.serviceList, state.service)
    });
    if (success) {
      message.success('成功修改业务活动');
      router.push('/businessActivity');
    }
  };
  const handleCreate = async () => {
    const {
      data: { success }
    } = await BusinessActivityService.createSystemProcess({
      activityName: state.systemName,
      applicationId: state.app,
      applicationName: state.appName,
      entranceName: state.serviceName,
      isCore: state.isCore,
      link_level: state.link_level,
      businessDomain: state.businessDomain,
      linkId: state.service,
      type: state.serviceType,
      ...getEntranceInfo(state.serviceList, state.service)
    });
    if (success) {
      message.success('成功新增业务活动');
      router.push('/businessActivity');
    }
  };
  return (
    <Fragment>
      <Button type="primary" onClick={handleSubmit}>
        保存
      </Button>
    </Fragment>
  );
};
export default AddEditExtra;
