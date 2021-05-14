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

const getScriptTagFormData = (state, setState): FormDataType[] => {
  const handleChange = (value, option) => {
    setState({
      tags: value,
      tagsValue: option.map((item, k) => {
        return item.props.children;
      })
    });
  };

  const basicFormData = [
    {
      key: 'tagNames',
      label: '脚本标签',
      options: {
        initialValue: state.tags,
        rules: [
          {
            required: false,
            message: '请输入脚本标签'
          }
        ]
      },
      node: (
        <CommonSelect
          style={{ width: 250 }}
          mode="tags"
          placeholder="请输入脚本标签"
          dataSource={state.tagList}
          onChange={(value, option) => handleChange(value, option)}
        />
      )
    }
  ];
  return basicFormData;
};
export default getScriptTagFormData;
