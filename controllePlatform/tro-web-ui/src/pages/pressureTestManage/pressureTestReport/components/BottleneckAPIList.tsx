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
import { ColumnProps } from 'antd/lib/table';
import { customColumnProps } from 'src/components/custom-table/utils';
import PressureTestReportService from '../service';
import { useStateReducer } from 'racc';
import { Pagination } from 'antd';
interface Props {
  id?: string;
}
interface State {
  searchParams: {
    current: number;
    pageSize: number;
  };
  data: any[];
  total?: number;
  loading?: boolean;
}
const BottleneckAPIList: React.FC<Props> = props => {
  const { id } = props;

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
    queryBottleneckAPIList({ reportId: id, ...state.searchParams });
  }, [state.searchParams.current, state.searchParams.pageSize]);
  /**
   * @name 获取瓶颈接口列表
   */
  const queryBottleneckAPIList = async value => {
    setState({
      loading: true
    });
    const {
      total,
      data: { success, data }
    } = await PressureTestReportService.queryBottleneckAPIList({
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
        dataSource={state.data ? state.data : []}
        columns={getBottleneckAPIListColumns()}
        rowKey={(row, index) => index.toString()}
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
export default BottleneckAPIList;

const getBottleneckAPIListColumns = (): ColumnProps<any>[] => {
  return [
    {
      ...customColumnProps,
      title: '排名',
      dataIndex: 'rank'
    },
    {
      ...customColumnProps,
      title: '应用',
      dataIndex: 'applicationName'
    },
    {
      ...customColumnProps,
      title: '接口',
      dataIndex: 'interfaceName'
    },
    {
      ...customColumnProps,
      title: 'TPS',
      dataIndex: 'tps'
    },
    {
      ...customColumnProps,
      title: 'RT',
      dataIndex: 'rt',
      render: text => {
        return <span>{text}ms</span>;
      }
    },
    {
      ...customColumnProps,
      title: '成功率',
      dataIndex: 'successRate',
      render: text => {
        return <span>{text}%</span>;
      }
    }
  ];
};
