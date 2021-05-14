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
import CustomTable from 'src/components/custom-table';
import { customColumnProps } from 'src/components/custom-table/utils';
import PressureTestReportService from '../service';
import { useStateReducer } from 'racc';
import { ColumnProps } from 'antd/lib/table';
import { Pagination } from 'antd';
interface Props {
  id?: string;
}
interface State {
  searchParams: {
    current: number;
    pageSize: number;
  };
  data: any;
  total: number;
  loading: boolean;
}
const WarningDetailList: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    searchParams: {
      current: 0,
      pageSize: 10
    },
    data: null,
    total: 0,
    loading: false
  });

  useEffect(() => {
    const { id } = props;
    queryWaringDetailList({ reportId: id, ...state.searchParams });
  }, [state.searchParams.current, state.searchParams.pageSize]);

  /**
   * @name 获取警告详情列表
   */
  const queryWaringDetailList = async value => {
    setState({
      loading: true
    });
    const {
      total,
      data: { success, data }
    } = await PressureTestReportService.queryWaringDetailList({
      ...value
    });
    if (success) {
      setState({
        data,
        total
      });
    }
    setState({
      loading: false
    });
  };

  const getColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...customColumnProps,
        title: 'SLA名称',
        dataIndex: 'slaName'
      },
      {
        ...customColumnProps,
        title: '警告对象',
        dataIndex: 'businessActivityName'
      },
      {
        ...customColumnProps,
        title: '规则明细',
        dataIndex: 'content'
      },
      {
        ...customColumnProps,
        title: '警告时间',
        dataIndex: 'warnTime'
      }
    ];
  };

  const handleChange = async (current, pageSize) => {
    setState({
      searchParams: {
        pageSize,
        current: current - 1
      }
    });
  };

  const handlePageSizeChange = async (current, pageSize) => {
    setState({
      searchParams: {
        pageSize,
        current: 0
      }
    });
  };
  return (
    <Fragment>
      <CustomTable
        loading={state.loading}
        rowKey={(row, index) => index.toString()}
        columns={getColumns()}
        size="small"
        dataSource={state.data ? state.data : []}
      />
      <Pagination
        style={{ marginTop: 20, textAlign: 'right' }}
        total={state.total}
        current={state.searchParams.current + 1}
        pageSize={state.searchParams.pageSize}
        showTotal={(t, range) =>
          `共 ${state.total} 条数据 第${state.searchParams.current +
            1}页 / 共 ${Math.ceil(
            state.total / (state.searchParams.pageSize || 10)
          )}页`
        }
        showSizeChanger={true}
        onChange={(current, pageSize) => handleChange(current, pageSize)}
        onShowSizeChange={handlePageSizeChange}
      />
    </Fragment>
  );
};
export default WarningDetailList;
