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
 * @name
 * @author MingShined
 */
import { CommonTabs } from 'racc';
import React, { Fragment } from 'react';
import Header from 'src/pages/linkMark/components/Header';
import ChainDetails from './ChainDetails';
import ChainGraph from './ChainGraph';

interface Props {}
const ChainInfo: React.FC<Props> = props => {
  const dataSource = [
    {
      tab: '链路拓扑',
      component: <ChainGraph />
    },
    {
      tab: '链路详情',
      component: <ChainDetails />
    },
    // {
    //   tab: '中间件列表',
    //   component: <MiddlewareList />
    // },
  ];
  return (
    <Fragment>
      <Header title="链路信息" />
      <CommonTabs
        dataSource={dataSource}
        tabsProps={{ defaultActiveKey: '0' }}
        onRender={(item, index) => (
          <CommonTabs.TabPane key={index.toString()} tab={item.tab}>
            {item.component}
          </CommonTabs.TabPane>
        )}
      />
    </Fragment>
  );
};
export default ChainInfo;
