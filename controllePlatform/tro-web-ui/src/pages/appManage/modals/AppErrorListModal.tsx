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
import { CommonModal, CommonTable, useStateReducer } from 'racc';
import { ColumnProps } from 'antd/lib/table';
import { Divider, Pagination, Popover, Tag } from 'antd';
import { customColumnProps } from 'src/components/custom-table/utils';
import CustomTable from 'src/components/custom-table';
import AppManageService from '../service';
import styles from './../index.less';
interface Props {
  btnText?: string | React.ReactNode;
  appId?: number | string;
}

interface State {
  isReload?: boolean;
  searchParams: {
    current: number;
    pageSize: number;
  };
  data: any[];
  total?: number;
}
const AppErrorListModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    isReload: false,
    searchParams: {
      current: 0,
      pageSize: 10
    },
    data: null,
    total: 0
  });
  const { appId } = props;

  const handleClick = () => {
    queryWaringDetailList({ applicationId: appId });
  };

  /**
   * @name 获取应用异常列表
   */
  const queryWaringDetailList = async value => {
    const {
      total,
      data: { success, data }
    } = await AppManageService.queryAppErrorList({
      ...value
    });
    if (success) {
      setState({
        data,
        total
      });
    }
  };

  const getColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...customColumnProps,
        title: '异常编码',
        dataIndex: 'id'
      },
      {
        ...customColumnProps,
        title: '异常描述',
        dataIndex: 'description',
        width: 250
      },
      {
        ...customColumnProps,
        title: '异常详情',
        dataIndex: 'detail',
        width: 350
      },
      {
        ...customColumnProps,
        title: '异常时间',
        dataIndex: 'time',
        width: 120
      },
      {
        ...customColumnProps,
        title: 'Agent ID',
        dataIndex: 'agentIds',
        width: 100,
        render: text => {
          return text && text.length >= 1 ? (
            <div style={{ display: 'flex' }}>
              <Tag>
                <span
                  style={{
                    width: 50,
                    display: 'inline-block',
                    overflow: 'hidden',
                    textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap'
                  }}
                >
                  {text[0]}
                </span>
              </Tag>
              <Popover
                placement="bottom"
                trigger="click"
                title="异常Agent Id"
                content={
                  <div className={styles.agentIds}>
                    {text.map((item, k) => {
                      return (
                        <p key={k}>
                          {item} <Divider />
                        </p>
                      );
                    })}
                  </div>}
              >
                <Tag>...</Tag>
              </Popover>
            </div>
          ) : (
            '-'
          );
        }
      }
    ];
  };

  return (
    <CommonModal
      modalProps={{
        width: 1096,
        footer: null,
        title: '异常列表'
      }}
      btnProps={{ type: 'link' }}
      btnText={props.btnText}
      onClick={() => handleClick()}
    >
      <div style={{ minHeight: 500 }}>
        <CustomTable
          rowKey={(row, index) => index.toString()}
          columns={getColumns()}
          size="small"
          dataSource={state.data ? state.data : []}
        />
      </div>

      {/* <Pagination
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
      /> */}
    </CommonModal>
  );
};
export default AppErrorListModal;
