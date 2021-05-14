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
import { Input } from 'antd';
import { connect } from 'dva';
import { CommonForm, CommonSelect } from 'racc';
import { FormDataType } from 'racc/dist/common-form/type';
import React, { Fragment, useEffect } from 'react';
import BusinessSelect from 'src/common/business-select';
import { CommonModelState } from 'src/models/common';
import { ActivityBean } from '../enum';
import styles from '../index.less';
import { AddEditActivityModalState } from '../modals/AddEditActivityModal';
import BusinessActivityService from '../service';

interface AddEditFormProps extends CommonModelState, AddEditActivityModalState {
  setState: (state: Partial<AddEditActivityModalState>) => void;
}
const AddEditForm: React.FC<AddEditFormProps> = props => {
  const { setState, app, serviceType, form } = props;
  const disabled = !props.app;
  useEffect(() => {
    if (app && serviceType) {
      queryServiceList();
    }
  }, [app, serviceType]);
  const queryServiceList = async () => {
    const {
      data: { data, success }
    } = await BusinessActivityService.queryServiceList({
      applicationName: app,
      type: serviceType
    });
    if (success) {
      setState({ serviceList: data });
    }
  };
  const getFormData = (): FormDataType[] => {
    return [
      {
        key: ActivityBean.业务活动名称,
        label: '业务活动名称',
        options: {
          rules: [{ required: true, message: '请填写业务活动名称' }],
          initialValue: props.systemName
        },
        node: <Input style={{ fontSize: 12 }} placeholder="业务活动名称" />
      },
      {
        key: ActivityBean.业务活动类型,
        label: '业务活动类型',
        options: {
          rules: [{ required: true, message: '请选择业务活动类型' }],
          initialValue: props.isCore
        },
        node: (
          <CommonSelect
            placeholder="请选择业务活动类型"
            showSearch
            dataSource={props.dictionaryMap && props.dictionaryMap.isCore}
            optionFilterProp="children"
          />
        )
      },
      {
        key: ActivityBean.业务活动级别,
        label: '业务活动级别',
        options: {
          rules: [{ required: true, message: '请选择业务活动级别' }],
          initialValue: props.link_level
        },
        node: (
          <CommonSelect
            placeholder="请选择业务活动级别"
            showSearch
            dataSource={props.dictionaryMap && props.dictionaryMap.link_level}
            optionFilterProp="children"
          />
        )
      },
      {
        key: ActivityBean.业务域,
        label: '业务域',
        options: {
          rules: [{ required: true, message: '请选择业务域' }],
          initialValue: props.businessDomain
        },
        node: (
          <CommonSelect
            placeholder="请选择业务域"
            showSearch
            dataSource={props.dictionaryMap && props.dictionaryMap.domain}
            optionFilterProp="children"
          />
        )
      },
      {
        key: ActivityBean.所属应用,
        label: '应用',
        options: {
          rules: [{ required: true, message: '请选择应用' }],
          initialValue: props.app
        },
        node: (
          <BusinessSelect
            onChange={(value: any, options: any) => {
              setState({
                app: value,
                appName: options && options.props.children,
                service: undefined
              });
              form.resetFields([
                ActivityBean.服务类型,
                ActivityBean['服务/入口']
              ]);
            }}
            url="/application/names"
            placeholder="请选择应用"
            showSearch
            optionFilterProp="children"
            onLoad={() => setState({ loading: false })}
            dropdownClassName={styles.select}
          />
        )
      },
      {
        key: ActivityBean.服务类型,
        label: '服务类型',
        options: {
          rules: [{ required: true, message: '请选择服务类型' }],
          initialValue: props.serviceType
        },
        node: (
          <BusinessSelect
            url="/application/entrances/types"
            onChange={_serviceType => {
              setState({ serviceType: _serviceType, service: undefined });
              form.resetFields([ActivityBean['服务/入口']]);
            }}
            placeholder="请选择服务类型"
            showSearch
            optionFilterProp="children"
            dropdownClassName={styles.select}
          />
        )
      },
      {
        key: ActivityBean['服务/入口'],
        label: '服务',
        options: {
          rules: [{ required: true, message: '请输入服务' }],
          initialValue: props.service
        },
        node: <Input placeholder="请输入服务" />
      }
    ];
  };
  return (
    <Fragment>
      <CommonForm
        btnProps={{ isResetBtn: false, isSubmitBtn: false }}
        getForm={f => setState({ form: f })}
        formItemProps={{
          labelCol: { span: 7 },
          wrapperCol: { span: 15, push: 1 }
        }}
        rowNum={1}
        formData={getFormData()}
      />
    </Fragment>
  );
};
export default connect(({ common }) => ({ ...common }))(AddEditForm);
