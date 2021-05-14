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

export interface DescriptCardProps {
  columns: DescriptCardColumnsBean[];
  dataSource: Object;
  emptyNode?: string | React.ReactNode;
}

export interface DescriptCardColumnsBean {
  header?: string | React.ReactNode;
  columns: {
    title: string | React.ReactNode;
    dataIndex: string;
    isCombine?: boolean;
    render?: (text: any, datasource: any, index: number) => React.ReactNode;
  }[];
  span?: number;
  isAlignSelf?: boolean;
  labelStyle?: React.CSSProperties;
  extra?: React.ReactNode;
}

export interface DescriptCardItemProps {
  header: string | React.ReactNode;
  columns: {
    title: string | React.ReactNode;
    dataIndex: string;
    render?: (item: any, index: number) => React.ReactNode;
  }[];
  extra?: React.ReactNode;
  dataSource?: any;
  emptyNode?: React.ReactNode;
}
