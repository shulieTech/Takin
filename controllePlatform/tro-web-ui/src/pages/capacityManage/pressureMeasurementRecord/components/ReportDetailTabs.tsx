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

import React, { Fragment } from 'react';
import { Tabs } from 'antd';

import styles from './../index.less';
import DetailTabs from 'src/common/detail-tabs';

interface Props {
  detailData?: any;
  extCode?: string;
}
const ReportDetailTabs: React.FC<Props> = props => {
  const { detailData, extCode } = props;

  const tabsData = [
    {
      title: '整体情况',
      tabNode: <div>1</div>
    },
    {
      title: '业务性能',
      tabNode: <div>2</div>
    },
    {
      title: '机器性能',
      tabNode: <div>3</div>
    },
    {
      title: '压测复盘',
      tabNode: <div>4</div>
    },
    {
      title: '优化策略',
      tabNode: <div>5</div>
    },
    {
      title: '性能趋势',
      tabNode: <div>6</div>
    }
  ];

  return (
    <DetailTabs>
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
export default ReportDetailTabs;
