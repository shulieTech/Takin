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
import Header from 'src/pages/linkMark/components/Header';
import { CommonTable } from 'racc';
import { Pagination } from 'antd';
import { ColumnProps } from 'antd/lib/table';
import { customColumnProps } from 'src/components/custom-table/utils';

interface Props {
  state?: any;
  setState?: (value) => void;
}
const ErrorTable: React.FC<Props> = props => {
  const { state, setState } = props;

  const getErrorListColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...customColumnProps,
        title: '应用名称',
        dataIndex: 'applicationName'
      },
      {
        ...customColumnProps,
        title: '异常描述',
        dataIndex: 'exceptionInfo'
      }
    ];
  };

  const handleChange = (current, pageSize) => {
    setState({
      searchParams: {
        pageSize,
        current
      }
    });
  };

  return (
    <Fragment>
      <Header title="问题列表" />
      <CommonTable
        style={{ marginTop: 10 }}
        rowKey="middleWareId"
        columns={getErrorListColumns()}
        size="small"
        dataSource={state.dataSource}
        pageProps={{
          total: state.dataSource && state.dataSource.length,
          current: state.searchParams.current
        }}
        onPageChange={(current, pageSize) => handleChange(current, pageSize)}
      />
    </Fragment>
  );
};
export default ErrorTable;
