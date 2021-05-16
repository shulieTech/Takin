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
import { useStateReducer } from 'racc';
import CustomTable from 'src/components/custom-table';
import { Divider, Pagination, Checkbox, Row, Col, Button, Input } from 'antd';
import styles from './../index.less';
import AppManageService from '../service';
import getNodeManageListColumns from './NodeManageListColumn';

interface Props {
  id?: string;
  detailData?: any;
  detailState?: any;
  action?: string;
}
interface State {
  isReload: boolean;
  nodeManageList: any[];
  loading: boolean;
  ip: string;
  total: number;
  searchParams: {
    current: number;
    pageSize: number;
  };
  nodeNum: number;
  onlineNodeNum: number;
  errorMsg: string;
}
const NodeManageList: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    isReload: false,
    nodeManageList: null,
    loading: false,
    ip: null,
    total: 0,
    searchParams: {
      current: 0,
      pageSize: 10
    },
    nodeNum: null,
    onlineNodeNum: null,
    errorMsg: null
  });
  const { Search } = Input;

  const { id } = props;

  useEffect(() => {
    queryNodeNum();
  }, []);

  useEffect(() => {
    queryNodeManageList({ ip: state.ip, ...state.searchParams });
  }, [state.isReload, state.searchParams.current, state.searchParams.pageSize]);

  /**
   * @name 获取节点数
   */
  const queryNodeNum = async () => {
    const {
      data: { success, data }
    } = await AppManageService.queryNodeNum({
      applicationId: id
    });
    if (success) {
      setState({
        nodeNum: data.allNodeNum,
        onlineNodeNum: data.onlineNodeNum,
        errorMsg: data.errorMsg
      });
    }
  };

  /**
   * @name 获取节点列表
   */
  const queryNodeManageList = async values => {
    setState({
      loading: true
    });
    const {
      total,
      data: { success, data }
    } = await AppManageService.queryNodeManageList({
      applicationId: id,
      ...values
    });
    if (success) {
      setState({
        total,
        nodeManageList: data,
        loading: false
      });
      return;
    }
    setState({
      loading: false
    });
  };

  const handleChangePage = async (current, pageSize) => {
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
      <div
        className={styles.tableWrap}
        style={{ height: document.body.clientHeight - 160 }}
      >
        <Row
          type="flex"
          align="middle"
          style={{ marginBottom: 20, marginTop: 20 }}
        >
          <Col span={6}>
            <Search
              placeholder="搜索IP地址"
              enterButton
              onSearch={() =>
                setState({
                  isReload: !state.isReload
                })
              }
              onChange={e =>
                setState({
                  ip: e.target.value
                })
              }
              value={state.ip}
            />
          </Col>
          <Col
            style={{
              fontSize: '13px',
              fontWeight: 500,
              color: '#8C8C8C',
              marginLeft: 24
            }}
          >
            <span>节点总数</span>
            <span
              style={{ fontSize: '16px', color: '#393B4F', marginLeft: 24 }}
            >
              {state.nodeNum}
            </span>
            <Divider type="vertical" style={{ margin: '0 30px', height: 24 }} />
            <span>在线节点</span>
            <span
              style={{ fontSize: '16px', color: '#FE7D61', marginLeft: 24 }}
            >
              {state.onlineNodeNum}
            </span>
            <span style={{ color: '#FE7D61', marginLeft: 16 }}>
              {state.errorMsg}
            </span>
          </Col>
        </Row>
        <CustomTable
          rowKey="wlistId"
          loading={state.loading}
          columns={getNodeManageListColumns()}
          dataSource={state.nodeManageList ? state.nodeManageList : []}
        />
      </div>
      <div
        style={{
          marginTop: 20,
          // textAlign: 'right',
          position: 'fixed',
          padding: '8px 40px',
          bottom: 0,
          right: 10,
          width: 'calc(100% - 178px)',
          backgroundColor: '#fff',
          boxShadow:
            '0px 2px 20px 0px rgba(92,80,133,0.15),0px -4px 8px 0px rgba(222,223,233,0.3)'
        }}
      >
        <Pagination
          style={{ display: 'inline-block', float: 'right' }}
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
          onChange={(current, pageSize) => handleChangePage(current, pageSize)}
          onShowSizeChange={handlePageSizeChange}
        />
      </div>
    </Fragment>
  );
};
export default NodeManageList;
