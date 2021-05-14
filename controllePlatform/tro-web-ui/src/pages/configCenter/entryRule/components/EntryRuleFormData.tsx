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

import { FormDataType } from 'racc/dist/common-form/type';
import { Input } from 'antd';
import { CommonSelect } from 'racc';

const getEntryRuleFormData = (
  state,
  action,
  setState,
  dictionaryMap
): FormDataType[] => {
  const { EntryRuleDetail, allAppList } = state;

  const basicFormData = [
    {
      key: 'applicationName',
      label: '应用',
      options: {
        initialValue:
          action !== 'add'
            ? EntryRuleDetail && EntryRuleDetail.applicationName
            : undefined,
        rules: [
          {
            required: true,
            message: '请选择应用'
          }
        ]
      },
      node: (
        <CommonSelect
          placeholder="请选择"
          dataSource={allAppList ? allAppList : []}
        />
      )
    },
    {
      key: 'api',
      label: '入口地址',
      options: {
        initialValue:
          action !== 'add' ? EntryRuleDetail && EntryRuleDetail.api : undefined,
        rules: [
          {
            required: true,
            message: '请输入入口地址'
          }
        ]
      },
      node: <Input placeholder="请输入" />
    },
    {
      key: 'method',
      label: '请求类型',
      options: {
        initialValue:
          action !== 'add'
            ? EntryRuleDetail && EntryRuleDetail.requestMethod
            : undefined,
        rules: [
          {
            required: true,
            message: '请选择请求类型'
          }
        ]
      },
      node: (
        <CommonSelect
          dataSource={
            dictionaryMap && dictionaryMap.HTTP_METHOD_TYPE
              ? dictionaryMap.HTTP_METHOD_TYPE
              : []
          }
        />
      )
    }
  ];
  return basicFormData;
};
export default getEntryRuleFormData;
