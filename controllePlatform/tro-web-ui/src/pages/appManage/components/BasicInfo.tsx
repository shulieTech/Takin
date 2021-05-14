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
import DescriptCard from 'src/common/descript-card';
import { DescriptCardColumnsBean } from 'src/common/descript-card/type';
interface Props {
  detailData?: any;
}
const BasicInfo: React.FC<Props> = props => {
  const { detailData } = props;
  const commonProps = {
    span: 24,
    isAlignSelf: true
    // labelStyle: { width: 200 }
  };
  const columns: DescriptCardColumnsBean[] = [
    {
      header: '基本信息',
      columns: [
        {
          title: '应用说明',
          dataIndex: 'applicationDesc'
        }
      ],
      ...commonProps
    },
    {
      header: '脚本路径',
      columns: [
        {
          title: '影子库结构脚本路径',
          dataIndex: 'ddlScriptPath'
        },
        {
          title: '数据库清理脚本路径',
          dataIndex: 'cleanScriptPath'
        },
        {
          title: '基础数据准备脚本路径',
          dataIndex: 'readyScriptPath'
        },
        {
          title: '铺底数据脚本路径',
          dataIndex: 'basicScriptPath'
        },
        {
          title: '缓存预热脚本路径',
          dataIndex: 'cacheScriptPath'
        }
      ],
      ...commonProps
    }
  ];
  return (
    <Fragment>
      <DescriptCard dataSource={detailData} columns={columns} />
    </Fragment>
  );
};
export default BasicInfo;
