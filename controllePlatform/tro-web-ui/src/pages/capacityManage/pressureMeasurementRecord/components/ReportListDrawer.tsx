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
import { CommonDrawer, CommonTable } from 'racc';
import getReportListSearchFormData from './ReportListSearch';
import getReportListColumns from './ReportListTable';
import DrawerSearchTable from 'src/components/drawer-search-table';
interface Props {
  title?: string;
}
const ReportList: React.FC<Props> = props => {
  const { title } = props;
  return (
    <CommonDrawer
      btnText={title}
      drawerProps={{ width: 650, maskClosable: false, title: '报告详情' }}
      footer={null}
      btnProps={{
        type: 'link',
        style: {
          color: '#21D0F4',
          fontSize: 12,
          padding: 0,
          height: 0
        }
      }}
    >
      <DrawerSearchTable
        commonTableProps={{
          columns: getReportListColumns(),
          size: 'small'
        }}
        commonFormProps={{ formData: getReportListSearchFormData(), rowNum: 2 }}
        ajaxProps={{ url: '/console/link/guard/guardmanage', method: 'GET' }}
      />
    </CommonDrawer>
  );
};
export default ReportList;
