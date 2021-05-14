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
 * @name 步骤1-基本信息
 */

import { Input } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import { FormDataType } from 'racc/dist/common-form/type';
import React from 'react';
import { FormCardMultipleDataSourceBean } from 'src/components/form-card-multiple/type';
import RelatePlugin from './RelatePlugin';
import TwoSelect from './TwoSelect';

interface Props {}

const BaseInfo = (state, setState, props): FormCardMultipleDataSourceBean => {
  /** @name 基本信息 */
  const getBaseInfoFormData = (): FormDataType[] => {
    const { location } = props;
    const { query } = location;
    const { action, configType, relatedId } = query;

    const { detailData } = state;

    return [
      {
        key: 'scriptName',
        label: '脚本名称',
        options: {
          initialValue: action !== 'add' ? detailData.scriptName : undefined,
          rules: [{ required: true, message: '请输入正确的脚本名称', max: 20 }]
        },
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 16 } },
        node: (
          <Input
            placeholder="请输入(最多20个字符)"
            disabled={action !== 'add' ? true : false}
          />
        )
      },
      {
        key: 'relatedObj',
        label: '关联业务',
        options: {
          initialValue:
            action !== 'add'
              ? {
                relatedType: detailData.relatedType,
                relatedId: detailData.relatedId
              }
              : relatedId && configType
              ? {
                relatedId,
                relatedType: configType
              }
              : undefined,
          rules: [{ required: true, message: '请选择关联业务' }]
        },
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 16 } },
        node: (
          <TwoSelect
            businessActivityList={
              state.bussinessActiveList ? state.bussinessActiveList : []
            }
            onChange={value => {
              setState({ detailData: { ...detailData, ...value, pluginConfigs: null } });
              const form: WrappedFormUtils = state.form;
              form.setFields({
                pluginConfigs: []
              });
            }}
            businessFlowList={
              state.businessFlowList ? state.businessFlowList : []
            }
          />
        )
      },
      {
        key: 'pluginConfigs',
        label: '关联插件',
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 16 } },
        options: {
          initialValue: detailData.pluginConfigs,
          rules: [
            {
              required: false
            }
          ]
        },
        node: (
          <RelatePlugin
            relatedId={detailData.relatedId}
            relatedType={detailData.relatedType}
            getRequired={required => setState({ required })}
          />
        )
      }
    ];
  };

  return {
    title: '基础信息',
    rowNum: 1,
    span: 14,
    formData: getBaseInfoFormData()
  };
};

export default BaseInfo;
