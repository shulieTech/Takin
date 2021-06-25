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

import { MenuBean, MenuType } from 'src/common/menu/type';

const menuList: MenuBean[] = [
  {
    title: '系统概览',
    path: '/dashboard',
    type: MenuType.Item,
    icon: 'dashboard',
    key: 'dashboard'
  },
  {
    title: '链路梳理',
    path: '/linkTease',
    type: MenuType.SubMenu,
    icon: 'home',
    key: 'linkTease',
    children: [
      {
        title: '业务活动',
        path: '/businessActivity',
        type: MenuType.Item,
        key: 'businessActivity',
        children: [
          {
            title: '新增业务活动',
            path: '/businessActivity/addEdit',
            type: MenuType.NoMenu,
            key: 'businessActivity_addBusinessActivity'
          }
        ]
      }
    ]
  },
  {
    title: '应用配置',
    path: '/appManages',
    type: MenuType.SubMenu,
    icon: 'shop',
    key: 'appManage',
    children: [
      {
        title: '应用管理',
        path: '/appManage',
        type: MenuType.Item,
        key: 'appManage',
        children: [
          {
            title: '应用详情',
            path: '/appManages/details',
            type: MenuType.NoMenu,
            key: 'appManage_details'
          },
        ]
      }
    ]
  },
  {
    title: '脚本管理',
    path: '/scriptManage',
    type: MenuType.Item,
    icon: 'shop',
    key: 'scriptManage',
    children: [
      {
        title: '脚本配置',
        path: '/scriptManage/scriptConfig',
        type: MenuType.NoMenu,
        key: 'scriptManage_scriptConfig'
      }
    ]
  },
  {
    title: '压测管理',
    path: '/pressureTestManage',
    type: MenuType.SubMenu,
    icon: 'hourglass',
    key: 'pressureTestManage',
    children: [
      {
        title: '压测场景',
        path: '/pressureTestManage/pressureTestScene',
        type: MenuType.Item,
        key: 'pressureTestManage_pressureTestScene',
        children: [
          {
            title: '压测场景配置',
            path:
              '/pressureTestManage/pressureTestScene/pressureTestSceneConfig',
            type: MenuType.NoMenu,
            key: 'pressureTestManage_pressureTestScene_pressureTestSceneConfig'
          }
        ]
      },
      {
        title: '压测报告',
        path: '/pressureTestManage/pressureTestReport',
        type: MenuType.Item,
        key: 'pressureTestManage_pressureTestReport',
        children: [
          {
            title: '压测实况',
            path: '/pressureTestManage/pressureTestReport/pressureTestLive',
            type: MenuType.NoMenu,
            key: 'pressureTestManage_pressureTestReport_pressureTestLive'
          },
          {
            title: '压测报告详请',
            path: '/pressureTestManage/pressureTestReport/details',
            type: MenuType.NoMenu,
            key: 'pressureTestManage_pressureTestReport_details'
          }
        ]
      }
    ]
  },
  {
    title: '设置中心',
    path: '/configCenter',
    type: MenuType.SubMenu,
    icon: 'setting',
    key: 'configCenter',
    children: [
      {
        title: '压测开关设置',
        path: '/configCenter/pressureMeasureSwitch',
        type: MenuType.Item,
        key: 'configCenter_pressureMeasureSwitch'
      },
      {
        title: '白名单开关设置',
        path: '/configCenter/whitelistSwitch',
        type: MenuType.Item,
        key: 'configCenter_whitelistSwitch'
      },
      {
        title: '入口规则',
        path: '/configCenter/entryRule',
        type: MenuType.Item,
        key: 'configCenter_entryRule'
      },
      {
        title: '操作日志',
        path: '/configCenter/operationLog',
        type: MenuType.Item,
        key: 'configCenter_operationLog'
      },
      {
        title: '开关配置',
        path: '/configCenter/bigDataConfig',
        type: MenuType.Item,
        key: 'configCenter_bigDataConfig'
      }
    ]
  }
];

export default menuList;
