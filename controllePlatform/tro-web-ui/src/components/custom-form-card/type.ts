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

import CommonFormProps, { FormDataType } from 'racc/dist/common-form/type';
import { WrappedFormUtils } from 'antd/lib/form/Form';

export interface FormCardProps {
  dataSource: FormCardDataSourceBean[];
  commonFormProps?: Partial<CommonFormProps>;
  form: WrappedFormUtils;
  getForm?: (form: WrappedFormUtils) => void;
}

export interface FormCardDataSourceBean {
  title?: React.ReactNode | string;
  titleStyle?: React.CSSProperties;
  extra?: React.ReactNode | string;
  formData: FormDataType[];
  span?: number;
  commonFormProps?: Partial<CommonFormProps>;
}
