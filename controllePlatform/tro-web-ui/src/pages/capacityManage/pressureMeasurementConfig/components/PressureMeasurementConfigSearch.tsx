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
import { CommonSelect, DatePick } from 'racc';

const getFormData = (): FormDataType[] => {
  return [
    {
      key: '1',
      label: '',
      node: <Input placeholder="压测任务名称" />
    },
    {
      key: '2',
      label: '',
      node: <CommonSelect placeholder="压测任务类型" />
    },
    {
      key: 'time',
      label: '',
      node: (
        <DatePick
          type="range"
          rangePickerProps={{ placeholder: ['压测开始时间', '压测开始时间'] }}
        />
      )
    }
  ];
};

export default getFormData;
