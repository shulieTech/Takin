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

import { Input } from 'antd';
import { CommonSelect } from 'racc';
import { FormDataType } from 'racc/dist/common-form/type';
import React from 'react';
import { BigDataBean } from '../enum';
import { BigDataConfigState } from '../indexPage';

const getFormData = (
  state: BigDataConfigState,
  setState: (state: Partial<BigDataConfigState>) => void
): FormDataType[] => {
  return [
    {
      key: BigDataBean.key,
      label: '',
      node: <CommonSelect dataSource={[]} placeholder="key" />
    },
    {
      key: BigDataBean.说明,
      label: '',
      node: <Input placeholder="说明" />
    }
  ];
};

export default getFormData;
