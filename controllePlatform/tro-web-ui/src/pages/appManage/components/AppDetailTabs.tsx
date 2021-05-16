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

import { Tabs } from 'antd';
import React from 'react';
import DetailTabs from 'src/common/detail-tabs';
import BasicInfo from './BasicInfo';
import BlackList from './BlackList';
import NodeManageList from './NodeManageList';
import WhiteList from './WhiteList';

interface Props {
  detailData?: any;
  tabKey?: string;
  id?: string;
  detailState?: any;
  action?: any;
}
const AppDetailTabs: React.FC<Props> = props => {
  const { detailData, id, detailState, action } = props;

  const tabsData = [
    {
      title: '基础信息',
      tabNode: <BasicInfo detailData={detailData} />
    },

    {
      title: '白名单',
      tabNode: (
        <WhiteList
          id={id}
          detailData={detailData}
          detailState={detailState}
          action={action}
        />
      )
    },
    {
      title: '黑名单',
      tabNode: (
        <BlackList
          id={id}
          detailData={detailData}
          detailState={detailState}
          action={action}
        />
      )
    },
    {
      title: '节点管理',
      tabNode: (
        <NodeManageList
          id={id}
          detailData={detailData}
          detailState={detailState}
          action={action}
        />
      )
    }
  ];

  return (
    <DetailTabs defaultActiveKey={props.tabKey}>
      {tabsData.map((item, index) => {
        return (
          <Tabs.TabPane tab={item.title} key={`${index}`}>
            {item.tabNode}
          </Tabs.TabPane>
        );
      })}
    </DetailTabs>
  );
};
export default AppDetailTabs;
