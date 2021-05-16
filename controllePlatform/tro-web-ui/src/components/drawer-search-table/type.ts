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

import CommonFormProps from 'racc/dist/common-form/type';
import { CommonTableProps } from 'racc/dist/common-table/CommonTable';
import { CommonSelectProps } from 'racc/dist/common-select/type';
import { CheckboxGroupProps } from 'antd/lib/checkbox';
import { RadioGroupProps } from 'antd/lib/radio';

export interface SearchTableProps {
  title?: string | React.ReactNode;
  extra?: React.ReactNode;
  tableAction?: React.ReactNode;
  footerAction?: React.ReactNode;
  commonFormProps?: Partial<CommonFormProps>;
  commonTableProps?: CommonTableProps;
  filterData?: FilterDataProps[] | any;
  ajaxProps: { method: 'GET' | 'POST'; url: string };
  datekeys?: { originKey: string; separateKey: string[] }[];
  cascaderKeys?: { originKey: string; separateKey: string[] }[];
  tabsData?: {
    label: string | React.ReactNode;
    value: any;
    num?: number;
  }[];
  tabKey?: string;
  onCheck?: (checkedKeys: any[], checkedRows?: any[]) => void;
  onSearch?: (searchParams: any, dataSource: any[]) => void;
  toggleRoload?: boolean;
  onTabSearch?: (searchParams?: any) => boolean;
  onTabReset?: (searchParams: any) => void;
  searchParams?: any;
  renderTable?: (dataSource: any[]) => React.ReactNode;
  dataKey?: string;
}

export interface FilterDataProps {
  dataSource: { label: string; value: any }[];
  type?: 'checkbox' | 'radio' | 'select';
  key: string;
  label?: string | React.ReactNode;
  commonSelectProps?: CommonSelectProps;
  checkboxGroupProps?: CheckboxGroupProps;
  radioGroupProps?: RadioGroupProps;
  hideAllOption?: boolean;
}
