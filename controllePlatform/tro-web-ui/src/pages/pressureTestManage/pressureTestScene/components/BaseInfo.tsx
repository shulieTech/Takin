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

import React from 'react';

import { FormCardMultipleDataSourceBean } from 'src/components/form-card-multiple/type';
import { FormDataType } from 'racc/dist/common-form/type';
import { Input } from 'antd';
import { CommonSelect } from 'racc';

interface Props {}

const BaseInfo = (state, setState, props): FormCardMultipleDataSourceBean => {
  /** @name 基本信息 */
  const getBaseInfoFormData = (): FormDataType[] => {
    const { location } = props;
    const { query } = location;
    const { action } = query;

    const { detailData } = state;

    return [
      {
        key: 'pressureTestSceneName',
        label: '压测场景名称',
        options: {
          initialValue:
            action !== 'add' ? detailData.pressureTestSceneName : undefined,
          rules: [{ required: true, message: '请输入压测场景名称' }]
        },
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 16 } },
        node: <Input placeholder="请输入" />
      }
    ];
  };

  return {
    title: '基本信息',
    rowNum: 1,
    span: 14,
    formData: getBaseInfoFormData()
  };
};

export default BaseInfo;
