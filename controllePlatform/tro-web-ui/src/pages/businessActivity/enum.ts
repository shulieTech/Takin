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
 * @name 变更状态
 */
export const ChangeStatus = {
  0: '#3BD9FF',
  1: '#FE7D61'
};

export enum SystemFlowEnum {
  应用 = 'applicationName',
  服务类型 = 'serviceType',
  服务 = 'providerServices',
  节点 = 'instances',
  调用服务 = 'callServices',
  下游应用 = 'downStreams',
  中间件类型 = 'middleWareType',
  中间件名称 = 'middleWareName',
  版本号 = 'version',
  问题类型 = '1',
  问题描述 = '2',
  建议解决方案 = '3'
}

export enum ActivityBean {
  业务活动名称 = 'activityName',
  业务活动类型 = 'isCore',
  业务活动级别 = 'link_level',
  业务域 = 'businessDomain',
  所属应用 = 'applicationName',
  服务类型 = 'type',
  '服务/入口' = 'entranceName',
  问题描述 = 'title',
  问题类型 = 'type',
  建议解决方案 = 'suggest',
  服务名称 = 'serviceName',
  上游应用 = 'beforeApps',
  外部服务 = 'outService',
  下游 = 'callService',
  节点 = 'node',
  表名称 = 'tableName',
  Topic = 'topic',
  文件路径 = 'filePath',
  文件名称 = 'fileName',
  负责人 = 'manager',
  地址 = 'node'
}

export enum NodeType {
  应用 = 'APP',
  缓存 = 'CACHE',
  数据库 = 'DB',
  消息队列 = 'MQ',
  文件 = 'OSS',
  外部应用 = 'OUTER',
  未知应用 = 'UNKNOWN',
  入口 = 'VIRTUAL'
}

export const nodeImgUrlMap = {
  [NodeType.应用]: 'app_icon',
  [NodeType.外部应用]: 'outer_icon',
  [NodeType.数据库]: 'db_icon',
  [NodeType.文件]: 'oss_icon',
  [NodeType.未知应用]: 'unknow_icon',
  [NodeType.消息队列]: 'mq_icon',
  [NodeType.缓存]: 'cache_icon',
  [NodeType.入口]: 'root_icon'
};

export interface NodeBean {
  id: string;
  label: string;
  _label: string;
  nodeType: NodeType;
  manager: string;
  providerService: {
    label: string;
    dataSource: any[];
  }[];
  callService: {
    label: string;
    nodeType: NodeType;
    dataSource: any[];
  }[];
  nodes: any[];
  db: any[];
  mq: any[];
  oss: any[];
}

export const debugToolId = 'debugToolId';

export enum VerifyStatus {
  未验证,
  验证中,
  验证完成,
}
