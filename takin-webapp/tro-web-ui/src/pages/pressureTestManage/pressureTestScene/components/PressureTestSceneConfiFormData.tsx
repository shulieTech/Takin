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
import { CommonSelect } from 'racc';
import { FormDataType } from 'racc/dist/common-form/type';
import { Input } from 'antd';

const getPressureTestSceneConfiFormData = (
  state,
  setState,
  action
): FormDataType[] => {
  const { appDetail } = state;

  const handleChangeAppName = e => {
    setState({
      appName: e.target.value
    });
  };

  const basicFormData = [
    {
      key: 'applicationName',
      label: '压测场景名称',
      options: {
        initialValue:
          action !== 'add' ? appDetail && appDetail.applicationName : undefined,
        rules: [
          {
            required: true,
            message: '请输入应用名'
          }
        ]
      },
      node: (
        <Fragment>
          <Input placeholder="请输入" />
        </Fragment>
      )
    }
  ];
  return basicFormData;
};
export default getPressureTestSceneConfiFormData;
