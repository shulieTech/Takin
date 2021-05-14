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

import { message } from 'antd';
import { AxiosRequestConfig } from 'axios';
import download from 'downloadjs';
import request from './request';

export function tryToParseJson(jsonString: string): any | undefined {
  let json;
  try {
    json = JSON.parse(jsonString);
  } catch (e) {
    // 不是正常的 JSON 字符串，不做任何事。
  }
  return json;
}

/**
 * @name  Cascader的搜索
 */
export function filterCascaderOptions(inputValue, path) {
  return path.some(
    option =>
      option.label
        .toLowerCase()
        .indexOf(inputValue && inputValue.toLowerCase()) > -1
  );
}

/**
 * @name 下载excel
 */
export async function downloadRequest(
  url,
  title?,
  requestOption?: AxiosRequestConfig
) {
  const { data, status, headers } = await request({
    url,
    responseType: 'blob',
    withCredentials: true,
    ...requestOption
  });
  const mTitle =
    decodeURIComponent(headers['content-file-original-name'] || '') ||
    (headers['content-disposition'] || '').replace(
      'attachment;filename=',
      ''
    ) ||
    title;
  if (status === 200) {
    download(data, mTitle);
    return true;
  }
  const reader = new FileReader();
  reader.onload = ({ target: { result } }: any) => {
    const res = tryToParseJson(result);
    message.config({
      maxCount: 1
    });
    message.error(res.message);
  };
  // reader.readAsText(data);
}

/**
 * @name 处理搜索条件非空
 * @param values 数据源
 */
export function filterSearchParams(values) {
  delete values.total;
  Object.keys(values).map(item => {
    if (values[item] !== 0 && !values[item]) {
      delete values[item];
    }
  });
  return values;
}

/** @name 获取用户权限 */
export const MapUserAuthority = (key: string) => {
  const authority = JSON.parse(localStorage.getItem('trowebUserResource'));
  return authority && authority[key];
};

/** @name 获取权限 */
export const MapBtnAuthority = (key: string) => {
  const authority = JSON.parse(localStorage.getItem('trowebBtnResource'));
  return authority && authority[key];
};

/** @name 下拉模糊搜索 */
export const filter = (inputValue, path) => {
  return path.some(
    option =>
      option.name.toLowerCase().indexOf(inputValue.toLowerCase()) > -1
  );
};
