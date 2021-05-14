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

import React, { Fragment, useEffect } from 'react';
import Header from 'src/pages/linkMark/components/Header';
import { CommonTable, useStateReducer } from 'racc';
import { customColumnProps } from 'src/components/custom-table/utils';
import { ColumnProps } from 'antd/lib/table';
import RequestDetailModal from '../modals/RequestDetailModal';
import { Badge } from 'antd';
import PressureTestReportService from '../service';
interface Props {
  dataSource: any;
}
interface State {
  data: any;
  loading: boolean;
}
const RequestDetailList: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    data: null,
    loading: false
  });

  const getRequestListColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...customColumnProps,
        title: '请求入口',
        dataIndex: 'interfaceName'
      },
      {
        ...customColumnProps,
        title: '应用（IP）',
        dataIndex: 'applicationName'
      },
      {
        ...customColumnProps,
        title: '状态',
        dataIndex: 'succeeded',
        render: text => {
          return (
            <Badge
              text={text ? '成功' : '失败'}
              color={text ? '#BBBBBB' : '#FF846A'}
            />
          );
        }
      },
      {
        ...customColumnProps,
        title: '总耗时（ms）',
        dataIndex: 'totalRt'
      },
      {
        ...customColumnProps,
        title: '开始时间',
        dataIndex: 'startTime'
      },
      {
        ...customColumnProps,
        title: 'TraceID',
        dataIndex: 'traceId'
      },
      {
        ...customColumnProps,
        title: '操作',
        dataIndex: 'action',
        render: (text, row) => {
          return (
            <RequestDetailModal btnText="请求详情" traceId={row.traceId} />
          );
        }
      }
    ];
  };
  return (
    <Fragment>
      <Header title="请求流量明细" />
      <CommonTable
        size="small"
        style={{ marginTop: 8 }}
        columns={getRequestListColumns()}
        dataSource={props.dataSource ? props.dataSource : []}
      />
    </Fragment>
  );
};
export default RequestDetailList;
