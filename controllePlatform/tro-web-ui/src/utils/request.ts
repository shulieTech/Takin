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

import axios, { AxiosRequestConfig } from 'axios';
import { message, Modal } from 'antd';
import { Basic } from 'src/types';
import BaseResponse = Basic.BaseResponse;
declare var window: Window;
declare var serverUrl: string;

axios.defaults.withCredentials = true;
let outloginFlag = false;
// axios.interceptors.request.use(
//   req => {
//     const token = getAuthorization();
//     if (!req.headers) {
//       if (token) {
//         req.headers = { Authorization: getAuthorization() };
//       }
//     } else {
//       if (token) {
//         req.headers.Authorization = getAuthorization();
//       }
//     }
//     return req;
//   },
//   error => {
//     return Promise.resolve(error);
//   }
// );
axios.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    if (axios.isCancel(error)) {
      const response = {
        config: {} as Basic.BaseResponse,
        headers: {},
        status: -999,
        statusText: '中断请求',
        data: undefined
      };
      return Promise.resolve(response);
    }
    return Promise.resolve(error.response);
  }
);
function parseJSON(response: BaseResponse) {
  if (response.headers['x-total-count']) {
    response.headers.totalCount = response.headers['x-total-count'];
    response.total = +response.headers['x-total-count'];
  }
  return response;
}
const getBackLogin = response => {
  outloginFlag = true;
  Modal.warning({
    content: '请登录',
    okText: '确认',
    onOk: () => {
      outloginFlag = false;
      window.g_app._store.dispatch({
        type: 'user/troLogout'
      });
    }
  });
};
function checkStatus(response: BaseResponse) {
  if (response.status >= 200 && response.status < 300) {
    return response;
  }
  if (response.status === 401) {
    if (!outloginFlag) {
      getBackLogin(response);
    }
  }
  return {
    config: response.config,
    headers: response.headers,
    status: response.status,
    statusText: response.statusText,
    data: response.data
  };
}
function getAuthorization() {
  // let idToken = localStorage.getItem('jhi-authenticationToken'); // eslint-disable-line
  // let idToken = getToken();
  // if (idToken !== undefined && idToken !== 'undefined' && idToken !== null) {
  //   idToken = idToken.replace('"', '');
  //   idToken = idToken.replace('"', '');
  //   return `Bearer ${idToken}`;
  // }
  return undefined;
}
export enum Method {
  GET = 'GET',
  POST = 'POST',
  PUT = 'PUT',
  DELETE = 'DELETE'
}
export interface RequestParams {
  method: Method;
  url: string;
  payload?: any;
}
const getUrl = (url: string, options: any) => {
  return `${options && options.domain ? options.domain : serverUrl}${url}`;
};
export function httpGet<T = any>(url: string, data?: any, options?: any) {
  const timestr = Date.now();
  const myurl = `${
    options && options.domain ? options.domain : serverUrl
  }${url}${url.indexOf('?') > -1 ? '&' : '?'}timestr=${timestr}`;
  // const myurl = `${url}${url.indexOf('?') > -1 ? '&' : '?'}timestr=${timestr}`;
  return httpRequest<T>({
    url: myurl,
    payload: data,
    method: Method.GET,
    ...options,
    headers: {
      'x-token': localStorage.getItem('full-link-token'),
      'Auth-Cookie': localStorage.getItem('auth-cookie')
    }
  });
}
export function httpPost<T>(url, data?: any, options?: any) {
  return httpRequest<T>({
    url: getUrl(url, options),
    payload: data,
    method: Method.POST,
    ...options,
    headers: {
      'x-token': localStorage.getItem('full-link-token'),
      'Auth-Cookie': localStorage.getItem('auth-cookie'),
      'Access-Token': localStorage.getItem('Access-Token')
    }
  });
}
export function httpPut<T>(url, data?: any, options?: any) {
  return httpRequest<T>({
    url: getUrl(url, options),
    payload: data,
    method: Method.PUT,
    ...options,
    headers: {
      'x-token': localStorage.getItem('full-link-token'),
      'Auth-Cookie': localStorage.getItem('auth-cookie')
    }
  });
}
export function httpDelete<T>(url, data?: any, options?: any) {
  return httpRequest<T>({
    url: getUrl(url, options),
    payload: data,
    method: Method.DELETE,
    ...options,
    headers: {
      'x-token': localStorage.getItem('full-link-token'),
      'Auth-Cookie': localStorage.getItem('auth-cookie')
    }
  });
}

export function httpRequest<T>(req: RequestParams): Promise<BaseResponse<T>> {
  return request({
    ...req,
    [req.method === Method.GET ? 'params' : 'data']: req.payload
  }).then(errorProcess);
}
export default function request(options: AxiosRequestConfig) {
  return axios(options)
    .then(checkStatus)
    .then(parseJSON);
}
export function errorProcess(response: BaseResponse) {
  const { status, data, config } = response;
  const statusFilter = config.headers.statusFilter;
  if (statusFilter) {
    switch (statusFilter.type) {
      case 'all':
        break;
      case 'blacklist':
        if (!statusFilter.list.find(item => +item === +status)) {
          if (data && data.error.msg) {
            message.error(data.error.msg);
            return response;
          }
          const errorMsg = getErrorMessage(status);
          if (errorMsg) {
            message.error(`错误代码：${status} ，${errorMsg}`);
          }
        }
        break;
      case 'whitelist':
        if (statusFilter.list.find(item => +item === +status)) {
          if (data && data.error.msg) {
            message.error(data.error.msg);
            return response;
          }
          const errorMsg = getErrorMessage(status);
          if (errorMsg) {
            message.error(`错误代码：${status} ，${errorMsg}`);
          }
        }
        break;
      default:
        break;
    }
  } else {
    // console.log(data);
    if (data && data.error && !data.success) {
      message.error(data.error.msg);
      return response;
    }
    const errorMsg = getErrorMessage(status);
    if (errorMsg) {
      message.error(`错误代码：${status} ，${errorMsg}`);
    }
  }

  return response;
}

function getErrorMessage(statusCode: number): string | undefined {
  const statusMsgMap = {
    400: 'Bad Request/错误请求!',
    // 401: 'Unauthorized/未授权!',
    403: 'Forbidden/禁止!',
    404: 'Not Found/未找到资源!',
    405: 'Method Not Allowed/方法未允许!',
    406: 'Not Acceptable/无法访问!',
    407: 'Proxy Authentication Required/代理服务器认证要求!',
    408: 'Request Timeout/请求超时!',
    409: 'Conflict/冲突!',
    410: 'Gone/已经不存在!',
    417: 'Expectation Failed/请求头信息期望失败!',
    500: 'Internal Server Error/内部服务器错误!',
    501: 'Not Implemented/未实现!',
    502: 'Bad Gateway/错误的网关!`',
    503: 'Service Unavailable/服务无法获得!',
    504: 'Gateway Timeout/网关超时!',
    505: 'HTTP Version Not Supported/不支持的 HTTP 版本!'
  };
  return statusMsgMap[statusCode];
}
