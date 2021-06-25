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

export enum SceneBean {
  误报类型 = 'category',
  具体描述 = 'detail',
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
