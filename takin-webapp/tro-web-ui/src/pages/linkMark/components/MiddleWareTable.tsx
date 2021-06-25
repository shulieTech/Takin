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

import { Pagination } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import { ColumnProps } from 'antd/lib/table';
import { connect } from 'dva';
import { CommonForm, CommonSelect, CommonTable, useStateReducer } from 'racc';
import { FormDataType } from 'racc/dist/common-form/type';
import React, { useEffect } from 'react';
import { customColumnProps } from 'src/components/custom-table/utils';
import LinkMarkService from '../service';
import Header from './Header';

interface Props {
  title?: string;
}

const getColumns = (): ColumnProps<any>[] => {
  return [
    {
      ...customColumnProps,
      title: '中间件类型',
      dataIndex: 'middleWareType'
    },
    {
      ...customColumnProps,
      title: '中间件',
      dataIndex: 'middleWareName'
    },
    {
      ...customColumnProps,
      title: '版本',
      dataIndex: 'version'
    },
    {
      ...customColumnProps,
      title: '涉及业务流程',
      dataIndex: 'bussinessProcessCount'
    },
    {
      ...customColumnProps,
      title: '涉及系统流程',
      dataIndex: 'systemProcessCount'
    }
  ];
};

const getFormData = (state): FormDataType[] => {
  const businessFlow =
    state.businessFlow &&
    state.businessFlow.map((item, k) => {
      return { label: item.businessFlowName, value: item.id };
    });
  const systemFlow =
    state.systemFlow &&
    state.systemFlow.map((item, k) => {
      return { label: item.systemProcessName, value: item.id };
    });
  return [
    {
      key: 'middleWareType',
      label: '',
      node: (
        <CommonSelect
          placeholder="中间件"
          dataSource={state.middlewareName ? state.middlewareName : []}
          onRender={item => (
            <CommonSelect.Option key={item.value} value={item.value}>
              {item.label}
            </CommonSelect.Option>
          )}
        />
      )
    },
    {
      key: 'businessProcess',
      label: '',
      node: (
        <CommonSelect
          placeholder="业务流程"
          dataSource={businessFlow ? businessFlow : []}
          onRender={item => (
            <CommonSelect.Option key={item.value} value={item.value}>
              {item.label}
            </CommonSelect.Option>
          )}
        />
      )
    },
    {
      key: 'systemProcess',
      label: '',
      node: (
        <CommonSelect
          placeholder="系统流程"
          dataSource={systemFlow ? systemFlow : []}
          onRender={item => (
            <CommonSelect.Option key={item.value} value={item.value}>
              {item.label}
            </CommonSelect.Option>
          )}
        />
      )
    }
  ];
};

export interface MiddleWareState {
  isReload?: boolean;
  middleware?: any;
  businessFlow?: any;
  systemFlow?: any;
  data?: any;
  form?: any;
  searchParams?: any;
  total?: number;
  middlewareName?: any;
}

const MiddleWareTable: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<MiddleWareState>({
    isReload: false,
    middleware: null,
    businessFlow: null,
    middlewareName: null,
    systemFlow: null,
    data: null,
    form: null as WrappedFormUtils,
    searchParams: { current: 0, pageSize: 10 } as any,
    total: 20
  });

  useEffect(() => {
    queryBusinessFlow();
    querySystemFlow();
    queryMiddlewareName();
  }, []);

  useEffect(() => {
    queryLinkMiddleware({ ...state.searchParams });
  }, [state.searchParams.current, state.searchParams.pageSize]);

  /**
   * @name 获取中间件名字
   */
  const queryMiddlewareName = async () => {
    const {
      data: { success, data }
    } = await LinkMarkService.queryMiddlewareName({});
    if (success) {
      setState({
        middlewareName: data
      });
    }
  };

  /**
   * @name 获取统计中间件信息
   */
  const queryLinkMiddleware = async value => {
    const {
      total,
      data: { success, data }
    } = await LinkMarkService.queryLinkMiddleware({ ...value });
    if (success) {
      setState({
        data,
        total
      });
    }
  };

  /**
   * @name 获取业务流程
   */
  const queryBusinessFlow = async () => {
    const {
      data: { success, data }
    } = await LinkMarkService.queryBusinessFlow({});
    if (success) {
      setState({
        businessFlow: data
      });
    }
  };

  /**
   * @name 获取系统流程
   */
  const querySystemFlow = async () => {
    const {
      data: { success, data }
    } = await LinkMarkService.querySystemFlow({});
    if (success) {
      setState({
        systemFlow: data
      });
    }
  };

  /**
   * @name 搜索
   */
  const handleSearch = () => {
    state.form.validateFields(async (err, values) => {
      queryLinkMiddleware({ ...values, ...state.searchParams });
    });
  };

  /**
   * @name 重置
   */
  const handleReset = () => {
    setState({
      searchParams: { pageSize: 10, current: 0 }
    });
    state.form.validateFields(async (err, values) => {
      queryLinkMiddleware({ ...values, current: 0, pageSize: 10 });
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
    <div>
      <Header title="中间件覆盖情况" />
      <div style={{ marginTop: 20, marginBottom: 40 }}>
        <CommonForm
          getForm={form => setState({ form })}
          formData={getFormData(state)}
          rowNum={6}
          onSubmit={handleSearch}
          onReset={handleReset}
        />
        <CommonTable
          rowKey="middleWareId"
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
      </div>
    </div>
  );
};
export default connect(({ common }) => ({ ...common }))(MiddleWareTable);
