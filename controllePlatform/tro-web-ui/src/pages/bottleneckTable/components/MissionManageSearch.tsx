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

import { FormDataType } from 'racc/dist/common-form/type';
import { Input } from 'antd';
import React from 'react';
import { CommonSelect } from 'racc';

const getMissionManageFormData = (state): FormDataType[] => {
  return [
    {
      key: 'id',
      label: '',
      node: (
        <Input placeholder="请输入瓶颈ID" />
      )
    },
    {
      key: 'businessName',
      label: '',
      node: (
        <Input placeholder="请输入巡检任务" />
      )
    },
    {
      key: 'type',
      label: '',
      node: (
        <CommonSelect
          placeholder="请选择瓶颈类型"
          dataSource={state.PATROL_EXCEPTION_TYPE || []}
          onRender={item => (
            <CommonSelect.Option key={item.value} value={item.value}>
              {item.label}
            </CommonSelect.Option>
          )}
        />
      )
    },
    {
      key: 'level',
      label: '',
      node: (
        <CommonSelect
          placeholder="请选择瓶颈程度"
          dataSource={state.PATROL_EXCEPTION_LEVEL || []}
          onRender={item => (
            <CommonSelect.Option key={item.value} value={item.value}>
              {item.label}
            </CommonSelect.Option>
          )}
        />
      )
    }
  ];
};

export default getMissionManageFormData;
