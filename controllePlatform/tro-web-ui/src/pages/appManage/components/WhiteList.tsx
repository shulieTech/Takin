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

import {
  Alert,
  Button,
  Col,
  Icon,
  Input,
  message,
  Modal,
  Pagination,
  Popover,
  Row
} from 'antd';
import { CommonSelect, useStateReducer } from 'racc';
import React, { Fragment, useEffect } from 'react';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';
import TableTitle from 'src/common/table-title/TableTitle';
import CustomTable from 'src/components/custom-table';
import WhitelistSwitchService from 'src/pages/configCenter/whitelistSwitch/service';
import LinkMarkService from 'src/pages/linkMark/service';
import AppManageService from '../service';
import styles from './../index.less';
import AddWhiteListDrawer from './AddWhiteListDrawer';
import getWhiteListColumns from './WhiteListTableColomn';

interface Props {
  id?: string;
  detailData?: any;
  detailState?: any;
  action?: string;
}
interface State {
  isReload: boolean;
  whiteListList: any[];
  loading: boolean;
  selectedRowKeys: any[];
  allSystemFlow: any[];
  searchValues: any;
  total: number;
  searchParams: {
    current: number;
    pageSize: number;
  };
  whitelistSwitchStatus: number;
}
const WhiteList: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    isReload: false,
    whiteListList: null,
    loading: false,
    selectedRowKeys: [],
    allSystemFlow: null,
    searchValues: {
      interfaceType: undefined,
      useYn: undefined,
      interfaceName: null
    },
    total: 0,
    searchParams: {
      current: 0,
      pageSize: 10
    },
    whitelistSwitchStatus: null
  });
  const { Search } = Input;
  const { confirm } = Modal;
  const { detailData, id, detailState, action } = props;
  const { selectedRowKeys } = state;

  useEffect(() => {
    // queryAllSystemFlow();
    querySwitchStatus();
  }, []);

  useEffect(() => {
    queryWhiteListList({ ...state.searchValues, ...state.searchParams });
  }, [state.isReload, state.searchParams.current, state.searchParams.pageSize]);
  const btnAuthority: any =
    localStorage.getItem('trowebBtnResource') &&
    JSON.parse(localStorage.getItem('trowebBtnResource'));
  /**
   * @name 确认是否禁用、启用白名单
   */
  const handleConfirm = async (ids, useYn) => {
    const {
      data: { data, success }
    } = await AppManageService.openAndCloseWhiteList({
      ids,
      type: useYn,
      applicationId: id
    });
    if (success) {
      const txt = useYn === 0 ? '取消' : '加入';

      message.config({
        top: 150
      });
      message.success(`批量${txt}白名单成功！`);
      setState({
        isReload: !state.isReload,
        selectedRowKeys: []
      });
    }
  };

  const showModal = (ids, useYn) => {
    let content;
    if (useYn === 0) {
      content = '取消白名单代表压测流量不可调用该接口';
    } else {
      content = '加入白名单代表压测流量可调用该接口';
    }

    confirm({
      content,
      title:
        useYn === 0 ? '是否确认取消白名单？' : '风险操作，是否确认加入白名单？',
      okButtonProps: useYn === 0 ? { type: 'primary' } : { type: 'danger' },
      okText: useYn === 0 ? '确认取消' : '确认加入',
      onOk() {
        handleConfirm(ids, useYn);
      }
    });
  };

  /**
   * @name 获取白名单列表
   */
  const queryWhiteListList = async values => {
    setState({
      loading: true
    });
    const {
      total,
      data: { success, data }
    } = await AppManageService.queryWhiteListList({
      applicationId: id,
      ...values
    });
    if (success) {
      setState({
        total,
        whiteListList: data,
        loading: false,
        selectedRowKeys: []
      });
      return;
    }
    setState({
      loading: false
    });
  };

  /**
   * @name 获取系统流程列表
   */
  const queryAllSystemFlow = async () => {
    const {
      data: { success, data }
    } = await LinkMarkService.querySystemFlow({});
    if (success) {
      setState({
        allSystemFlow:
          data &&
          data.map((item, key) => {
            return { label: item.systemProcessName, value: item.id };
          })
      });
      return;
    }
  };

  /**
   * @name 获取白名单开关状态
   */
  const querySwitchStatus = async () => {
    const {
      data: { data, success }
    } = await WhitelistSwitchService.queryWhitelistSwitchStatus({});
    if (success) {
      setState({
        whitelistSwitchStatus: data.switchFlag
      });
    }
  };

  const handleChangeSelectRows = value => {
    setState({
      selectedRowKeys: value
    });
  };

  const handleChangeAll = () => {
    const array =
      state.whiteListList &&
      state.whiteListList.map((item, k) => {
        return item.id;
      });
    if (
      state.whiteListList &&
      selectedRowKeys.length === state.whiteListList.length
    ) {
      setState({
        selectedRowKeys: []
      });
    } else {
      setState({
        selectedRowKeys: array
      });
    }
  };

  const handleChange = (key, value) => {
    setState({
      searchValues: { ...state.searchValues, [key]: value }
    });
    queryWhiteListList({
      ...state.searchParams,
      ...state.searchValues,
      [key]: value
    });
  };

  const handleChangeInterfaceName = e => {
    setState({
      searchValues: { ...state.searchValues, interfaceName: e.target.value }
    });
  };

  const handleReset = () => {
    setState({
      searchValues: {
        interfaceType: undefined,
        useYn: undefined,
        interfaceName: null
      },
      selectedRowKeys: []
    });
    queryWhiteListList({ ...state.searchParams });
  };

  const handleRefresh = () => {
    queryWhiteListList({
      ...state.searchValues,
      ...state.searchParams
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
        {state.whitelistSwitchStatus === 0 && (
          <Alert
            type="warning"
            message={
              <p style={{ color: '#646676' }}>
                白名单总开关已关闭，您可以进行白名单配置但所有白名单配置均暂不生效
              </p>}
            showIcon
            style={{ marginTop: 0, marginBottom: 22 }}
          />
        )}

        <TableTitle
          title="白名单"
          tip={
            <Popover
              trigger="click"
              placement="rightTop"
              content={
                <div className={styles.note}>
                  <p className={styles.noteTitle}>白名单说明</p>
                  <p className={styles.noteContent}>
                    白名单是压测流量是否可以调用某个接口的校验机制，可有效防止压测流量泄露至未接入的应用
                  </p>
                  <div>
                    <p>
                      <span className={styles.noteNum}>1</span>
                      <span className={styles.noteSubTitle}>加入白名单</span>
                    </p>
                    <p className={styles.noteSubContent}>
                      加入白名单代表压测流量可调用该接口
                    </p>
                    <p>
                      <span className={styles.noteNum}>2</span>
                      <span className={styles.noteSubTitle}>取消白名单</span>
                    </p>
                    <p className={styles.noteSubContent}>
                      取消白名单代表压测流量不可调用该接口
                    </p>
                    <p>
                      <span className={styles.noteNum}>3</span>
                      <span className={styles.noteSubTitle}>生效范围</span>
                    </p>
                    <p className={styles.noteSubContent}>
                      白名单默认全局生效，当遇到多个应用的接口名称重名时，多个应用的白名单配置会同步。若需要对部分应用开放白名单，需将白名单设置为部分生效。添加生效应用，表示该应用可访问白名单。未添加的则不能访问。
                    </p>
                  </div>
                </div>}
            >
              <Icon
                type="question-circle"
                style={{
                  marginLeft: 8,
                  cursor: 'pointer'
                }}
              />
            </Popover>
          }
          extraNode={
            <AuthorityBtn
              isShow={btnAuthority && btnAuthority.appManage_2_create}
            >
              <div className={styles.addAction}>
                <AddWhiteListDrawer
                  disabled={
                    detailState.switchStatus === 'OPENING' ||
                    detailState.switchStatus === 'CLOSING'
                      ? true
                      : false
                  }
                  title="新增白名单"
                  action="add"
                  detailData={detailData}
                  id={id}
                  onSccuess={() => {
                    setState({
                      isReload: !state.isReload
                    });
                  }}
                />
              </div>
            </AuthorityBtn>
          }
        />
        <Row type="flex" style={{ marginBottom: 20, marginTop: 20 }}>
          <Col span={4}>
            <CommonSelect
              style={{ width: '90%' }}
              placeholder="接口类型:全部"
              dataSource={[
                { label: 'HTTP', value: 1 },
                { label: 'DUBBO', value: 2 }
                // { label: 'RABBITMQ', value: 3 }
              ]}
              onChange={value => handleChange('interfaceType', value)}
              value={state.searchValues.interfaceType}
            />
          </Col>
          <Col span={4}>
            <CommonSelect
              style={{ width: '90%' }}
              placeholder="状态:全部"
              dataSource={[
                { label: '已加入', value: 1 },
                { label: '未加入', value: 0 }
              ]}
              onChange={value => handleChange('useYn', value)}
              value={state.searchValues.useYn}
            />
          </Col>
          <Col span={6}>
            <Search
              placeholder="搜索接口名称"
              enterButton
              onSearch={value => handleChange('interfaceName', value)}
              onChange={handleChangeInterfaceName}
              value={state.searchValues.interfaceName}
            />
          </Col>
          <Col span={2} style={{ marginLeft: 16, marginTop: 8 }}>
            <Button type="link" onClick={handleReset}>
              重置
            </Button>
          </Col>
        </Row>
        <div style={{ textAlign: 'right' }}>
          <Icon
            type="redo"
            style={{ cursor: 'pointer' }}
            onClick={handleRefresh}
          />
        </div>

        <CustomTable
          rowKey="wlistId"
          rowSelection={{
            selectedRowKeys,
            onChange: value => handleChangeSelectRows(value)
          }}
          loading={state.loading}
          columns={getWhiteListColumns(
            state,
            setState,
            detailState,
            id,
            action,
            detailData
          )}
          dataSource={state.whiteListList ? state.whiteListList : []}
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
        {
          <AuthorityBtn
            isShow={btnAuthority && btnAuthority.appManage_6_enable_disable}
          >
            <Button
              type="link"
              style={{
                marginRight: 16,
                marginTop: 8,
                color:
                  state.selectedRowKeys.length === 0
                    ? 'rgba(17,187,213,0.45)'
                    : null
              }}
              onClick={() => showModal(state.selectedRowKeys, 1)}
              disabled={state.selectedRowKeys.length === 0 ? true : false}
            >
              批量加入白名单
            </Button>
          </AuthorityBtn>
        }
        {
          <AuthorityBtn
            isShow={btnAuthority && btnAuthority.appManage_6_enable_disable}
          >
            <Button
              type="link"
              style={{
                color:
                  state.selectedRowKeys.length === 0
                    ? 'rgba(17,187,213,0.45)'
                    : null
              }}
              onClick={() => showModal(state.selectedRowKeys, 0)}
              disabled={state.selectedRowKeys.length === 0 ? true : false}
            >
              批量取消白名单
            </Button>
          </AuthorityBtn>
        }
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
export default WhiteList;
