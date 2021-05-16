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

const getScriptManageFormData = (state, setState): FormDataType[] => {
  return [
    {
      key: 'scriptName',
      label: '',
      node: <Input placeholder="脚本名称" />
    },
    {
      key: 'tags',
      label: '',
      node: (
        <CommonSelect
          placeholder="标签"
          mode="multiple"
          dataSource={state.tagList ? state.tagList : []}
          dropdownMatchSelectWidth={false}
          showSearch
          filterOption={(input, option) =>
            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >=
            0
          }
        />
      )
    },
    {
      key: 'businessActivity',
      label: '',
      node: (
        <CommonSelect
          placeholder="业务活动"
          dataSource={
            state.businessActivityList ? state.businessActivityList : []
          }
          dropdownMatchSelectWidth={false}
          showSearch
          filterOption={(input, option) =>
            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >=
            0
          }
        />
      )
    },
    {
      key: 'businessFlow',
      label: '',
      node: (
        <CommonSelect
          placeholder="业务流程"
          dataSource={state.businessFlowList ? state.businessFlowList : []}
          dropdownMatchSelectWidth={false}
          showSearch
          filterOption={(input, option) =>
            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >=
            0
          }
        />
      )
    }
  ];
};

export default getScriptManageFormData;
