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
import moment from 'moment';

const OperationLogSearch = (): FormDataType[] => {
  return [
    {
      key: 'userName',
      label: '',
      node: <Input placeholder="操作人" />
    },
    {
      key: 'time',
      label: '',
      node: (
        <DatePick
          type="range"
          rangePickerProps={{
            placeholder: ['操作开始时间', '操作结束时间'],
            // showTime: false,
            style: { width: '100%' },
            disabledDate: current => current > moment().endOf('day')
          }}
        />
      )
    }
  ];
};

export default OperationLogSearch;
