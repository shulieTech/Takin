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
import { FormDataType } from 'racc/dist/common-form/type';
import React from 'react';

const getFormData = (state, setState): FormDataType[] => {
  return [
    { key: 'activityName', label: '', node: <Input placeholder="业务活动名称" /> },
    // { key: 'entrance', label: '', node: <Input placeholder="入口" /> },
    // {
    //   key: 'middleWareArr',
    //   label: '',
    //   node: (
    //     <Cascader
    //       options={state.middlewareCascade ? state.middlewareCascade : []}
    //       changeOnSelect
    //       placeholder="中间件/中间件版本"
    //       showSearch={{ filter: filterCascaderOptions }}
    //     />
    //   )
    // }
  ];
};

export default getFormData;
