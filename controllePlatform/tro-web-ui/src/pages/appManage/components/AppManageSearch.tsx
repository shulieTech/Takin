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

const getFormData = (): FormDataType[] => {
  return [
    {
      key: 'applicationName',
      label: '',
      node: <Input placeholder="应用名称" />
    },
    {
      key: 'accessStatus',
      label: '',
      node: (
        <CommonSelect
          placeholder="接入状态"
          dataSource={[
            { label: '正常', value: 0 },
            { label: '待配置', value: 1 },
            { label: '待检测', value: 2 },
            { label: '异常', value: 3 }
          ]}
        />
      )
    }
  ];
};

export default getFormData;
