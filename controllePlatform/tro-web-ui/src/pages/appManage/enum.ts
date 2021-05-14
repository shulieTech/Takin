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
 * @name 应用配置状态颜色
 */
export const appConfigStatusColorMap = {
  0: '#11BBD5',
  1: '#FFB64A',
  2: '#A2A6B1',
  3: '#FE7D61'
};

/**
 * @name 应用配置状态
 */
export const appConfigStatusMap = {
  0: '正常',
  1: '待配置',
  2: '待检测',
  3: '异常'
};

export enum ShadowConsumerBean {
  MQ类型 = 'type',
  状态 = 'enabled',
  groupId = 'topicGroup',
  最后修改时间 = 'gmtUpdate'
}

/** @name 接口类型 */
export enum InterfaceType {
  HTTP = 1,
  DUBBO,
  RABBITMQ
}

export enum DbBean {
  应用 = 'applicationName',
  类型 = 'dbType',
  业务数据源地址 = 'url',
  用户名 = 'userName',
  方案类型 = 'dsType',
  配置代码 = 'config',
  数据源地址 = 'shadowDbUrl',
  数据源用户名 = 'shadowDbUserName',
  密码 = 'shadowDbPassword',
  minldle = 'shadowDbMinIdle',
  maxActive = 'shadowDbMaxActive'
}
