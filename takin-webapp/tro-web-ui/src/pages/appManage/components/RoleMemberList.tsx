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
import { ColumnProps } from 'antd/lib/table';
import CustomPopconfirm from 'src/components/custom-popconfirm/CustomPopconfirm';
import { Button, message, Pagination, Icon } from 'antd';
import AppManageService from '../service';
import { useStateReducer } from 'racc';
import styles from './../index.less';
import AddRoleMemberModal from '../modals/AddRoleMemberModal';
interface Props {
  roleId?: string;
  roleMemberState: any;
  setRoleMemberState: any;
}
interface RoleMemberListState {
  isReload: boolean;
  searchParams: {
    current: number;
    pageSize: number;
  };
  total: number;
  roleMemberList: any[];
  selectedKeys: any[];
  initDataSource: any[];
  loading: boolean;
}
const RoleMemberList: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<RoleMemberListState>({
    isReload: false,
    searchParams: {
      current: 0,
      pageSize: 10
    },
    total: 0,
    roleMemberList: null,
    selectedKeys: [],
    initDataSource: null,
    loading: false
  });

  const { roleId } = props;
  const { isReload, searchParams, roleMemberList, loading } = state;

  useEffect(() => {
    if (roleId) {
      queryMemberConfigRoleList();
    }
  }, [
    isReload,
    searchParams.current,
    searchParams.pageSize,
    roleId,
    props.roleMemberState.isReload
  ]);

  useEffect(() => {
    if (roleId) {
      querySelectedRoleMember();
    }
  }, [
    isReload,
    searchParams.current,
    searchParams.pageSize,
    roleId,
    props.roleMemberState.isReload
  ]);

  /**
   * @name 获取初始被选中的员工列表
   */
  const querySelectedRoleMember = async () => {
    const {
      total,
      data: { success, data }
    } = await AppManageService.queryMemberConfigRoleList({
      roleId,
      needPage: false
    });
    if (success) {
      setState({
        initDataSource: data
      });
    }
  };

  /**
   * @name 获取角色成员列表
   */
  const queryMemberConfigRoleList = async () => {
    setState({
      loading: true
    });
    const {
      total,
      data: { data, success }
    } = await AppManageService.queryMemberConfigRoleList({
      roleId,
      needPage: true,
      current: searchParams.current,
      pageSize: searchParams.pageSize
    });
    if (success) {
      setState({
        total,
        roleMemberList: data,
        loading: false
      });
      return;
    }
    setState({
      loading: false
    });
  };

  /**
   * @name 移除成员
   */
  const handleDelete = async id => {
    const {
      data: { data, success }
    } = await AppManageService.deleteRoleMember({ roleId, userIdList: id });
    if (success) {
      message.success('移除成员成功！');
      setState({
        isReload: !state.isReload,
        selectedKeys: []
      });
    }
  };

  const handleSelect = selectedKeys => {
    setState({ selectedKeys });
  };

  const getRoleMemberColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...customColumnProps,
        title: '角色名称',
        dataIndex: 'name'
      },
      {
        ...customColumnProps,
        title: '角色分组',
        dataIndex: 'deptName'
      },
      {
        ...customColumnProps,
        title: '添加时间',
        dataIndex: 'createTime'
      },
      {
        ...customColumnProps,
        title: '操作',
        dataIndex: 'action',
        render: (text, row) => {
          return (
            <CustomPopconfirm
              okText="确认移除"
              title={'是否确认移除'}
              okColor="#FE7D61"
              onConfirm={() => handleDelete([row.id])}
            >
              <Button type="link">移除</Button>
            </CustomPopconfirm>
          );
        }
      }
    ];
  };

  const handleChangePage = async (current, pageSize) => {
    setState({
      searchParams: {
        pageSize,
        current: current - 1
      },
      selectedKeys: []
    });
  };

  const handlePageSizeChange = async (current, pageSize) => {
    setState({
      searchParams: {
        pageSize,
        current: 0
      },
      selectedKeys: []
    });
  };

  return (
    <div style={{ position: 'relative', height: 578, textAlign: 'right' }}>
      <div>
        <AddRoleMemberModal
          btnText={
            <Button type="primary">
              <Icon type="plus" />
            </Button>
          }
          roleId={roleId}
          roleMemberState={props.roleMemberState}
          setRoleMemberState={props.setRoleMemberState}
          initDataSource={state.initDataSource}
        />
      </div>
      <div style={{ height: 485, overflowY: 'scroll' }}>
        <CustomTable
          rowKey="id"
          rowSelection={{
            selectedRowKeys: state.selectedKeys,
            onChange: handleSelect
          }}
          loading={loading}
          columns={getRoleMemberColumns()}
          dataSource={roleMemberList}
        />
      </div>

      <div className={styles.roleFooter}>
        <CustomPopconfirm
          okText="确认移除"
          title={'是否确认移除'}
          okColor="#FE7D61"
          onConfirm={() => handleDelete(state.selectedKeys)}
        >
          <Button
            type="link"
            style={{
              color:
                state.selectedKeys.length === 0 ? 'rgba(17,187,213,0.45)' : null
            }}
            disabled={state.selectedKeys.length === 0 ? true : false}
          >
            批量移除
          </Button>
        </CustomPopconfirm>
        <Pagination
          style={{ display: 'inline-block', float: 'right' }}
          total={state.total}
          current={searchParams.current + 1}
          pageSize={searchParams.pageSize}
          showTotal={(t, range) =>
            `共 ${state.total} 条数据 第${searchParams.current +
              1}页 / 共 ${Math.ceil(
              state.total / (searchParams.pageSize || 10)
            )}页`
          }
          showSizeChanger={true}
          onChange={(current, pageSize) => handleChangePage(current, pageSize)}
          onShowSizeChange={handlePageSizeChange}
        />
      </div>
    </div>
  );
};
export default RoleMemberList;
