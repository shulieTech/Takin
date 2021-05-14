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
import { DatePick } from 'racc';

const getPressureTestReportFormData = (): FormDataType[] => {
  return [
    {
      key: 'sceneName',
      label: '',
      node: <Input placeholder="压测场景名称" />
    },
    {
      key: 'time',
      label: '',
      node: (
        <DatePick
          type="range"
          rangePickerProps={{
            placeholder: ['压测开始时间', '压测结束时间'],
            showTime: true,
            style: { width: '100%' }
          }}
        />
      )
    }
  ];
};

export default getPressureTestReportFormData;
