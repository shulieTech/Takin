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
import { CommonModal, CommonTable, useStateReducer, CommonSelect } from 'racc';
import Table, { ColumnProps, TableRowSelection } from 'antd/lib/table';
import { customColumnProps } from 'src/components/custom-table/utils';
import { Typography, Row, Tabs, Transfer, Cascader, Col } from 'antd';
import styles from './../index.less';
import Loading from 'src/common/loading';
import AppManageService from '../service';
import Search from 'antd/lib/input/Search';
import difference from 'lodash/difference';
import uniqBy from 'lodash/uniqBy';
import CustomTable from 'src/components/custom-table';

interface Props {
  btnText: string | React.ReactNode;
  roleId?: string;
  roleMemberState: any;
  setRoleMemberState: any;
  initDataSource: any[];
}

interface State {
  reverse: boolean;
  pagination: {
    current: number;
    pageSize: number;
    total: number;
  };
  loading: boolean;
  targetKeys: any[];
  leftDataSource: any[];
  rightDataSource: any[];
  totalDataSource: any[];
  dataSource: any[];
  deptList: any[];
  deptIds: any[];
  userName: string;
}

const AddRoleMemberModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    reverse: false,
    pagination: {
      current: 0,
      pageSize: 10,
      total: 0
    },
    loading: false,
    /** 初始被选中的人员 */
    targetKeys: [],
    /** 左侧展示数据 */
    leftDataSource: [],
    dataSource: null,
    rightDataSource: [],
    totalDataSource: [],
    deptList: [],
    deptIds: null,
    userName: null
  });
  const { Paragraph } = Typography;
  const { dataSource, pagination } = state;
  const { roleId, initDataSource } = props;

  const handleClick = () => {
    setState({
      targetKeys:
        initDataSource &&
        initDataSource.map((item, key) => {
          return item.id;
        })
    });
    queryDept();
    queryUserList(
      {
        current: pagination.current,
        pageSize: pagination.pageSize,
        deptIds: state.deptIds,
        userName: state.userName
      },
      initDataSource &&
        initDataSource.map((item, key) => {
          return item.id;
        }),
      initDataSource,
      initDataSource
    );
  };

  /**
   * @name 获取所有员工列表
   */
  const queryUserList = async (value, selectedKeys, totalDatas, rightDatas) => {
    setState({
      loading: true
    });
    const {
      total,
      data: { success, data }
    } = await AppManageService.queryUserList({
      ...value
    });
    if (success) {
      setState({
        dataSource: data,
        leftDataSource: data.map(item => ({
          ...item,
          disabled: selectedKeys.includes(item.id)
        })),
        totalDataSource: uniqBy(totalDatas.concat(data), 'id'),
        pagination: {
          total,
          current: value && value.current + 1,
          pageSize: value && value.pageSize
        },
        rightDataSource: rightDatas,
        loading: false
      });
      return;
    }
    setState({
      loading: false
    });
  };

  /**
   * @name 获取部门结构数据
   */
  const queryDept = async () => {
    const {
      total,
      data: { success, data }
    } = await AppManageService.queryDept({});
    if (success) {
      setState({
        deptList: data
      });
    }
  };

  /**
   * @name 添加角色成员
   */
  const handleAddRole = async () => {
    return await new Promise(async resolve => {
      const {
        total,
        data: { success, data }
      } = await AppManageService.addRoleMember({
        roleId,
        userIdList: state.targetKeys
      });
      if (success) {
        props.setRoleMemberState({
          isReload: !props.roleMemberState.isReload
        });
        setState({
          pagination: {
            current: 0,
            pageSize: 10,
            total: 0
          }
        });
        resolve(true);
        return;
      }
      resolve(false);
    });
  };

  const handleCancle = async () => {
    setState({
      pagination: {
        current: 0,
        pageSize: 10,
        total: 0
      },
      userName: null,
      deptIds: null
    });
  };

  const handleChangeDept = async value => {
    setState({
      pagination: {
        current: 0,
        pageSize: pagination.pageSize,
        total: 0
      },
      deptIds: value.length > 0 ? [value[value.length - 1]] : null
    });
    queryUserList(
      {
        current: 0,
        pageSize: pagination.pageSize,
        deptIds: value.length > 0 ? [value[value.length - 1]] : null,
        userName: state.userName
      },
      state.targetKeys,
      state.totalDataSource,
      state.rightDataSource
    );
  };

  const handleChangeName = async e => {
    setState({
      userName: e.target.value
    });
  };

  const handleSelectName = async () => {
    setState({
      pagination: {
        current: 0,
        pageSize: pagination.pageSize,
        total: 0
      },
      userName: state.userName
    });
    queryUserList(
      {
        current: 0,
        pageSize: pagination.pageSize,
        deptIds: state.deptIds,
        userName: state.userName
      },
      state.targetKeys,
      state.totalDataSource,
      state.rightDataSource
    );
  };

  const tableColumns = [
    {
      dataIndex: 'name',
      title: '角色成员'
    },
    {
      dataIndex: 'deptName',
      title: '角色分组'
    }
  ];

  const onChange = nextTargetKeys => {
    setState({
      targetKeys: nextTargetKeys,
      reverse: !state.reverse,
      rightDataSource: state.totalDataSource.filter(item =>
        nextTargetKeys.includes(item.id)
      ),
      leftDataSource: state.dataSource.map(item => ({
        ...item,
        disabled: nextTargetKeys.includes(item.id)
      }))
    });
  };

  const TableTransfer = ({ leftColumns, rightColumns, ...restProps }) => (
    <Transfer rowKey={record => record.id} {...restProps}>
      {({
        direction,
        onItemSelectAll,
        onItemSelect,
        selectedKeys: listSelectedKeys,
        disabled: listDisabled
      }) => {
        const columns = direction === 'left' ? leftColumns : rightColumns;
        const rowSelection = {
          getCheckboxProps: item => ({
            disabled: listDisabled || item.disabled
          }),
          onSelectAll(selected, selectedRows) {
            const treeSelectedKeys = selectedRows
              .filter(item => !item.disabled)
              .map(({ id }) => id);
            const diffKeys = selected
              ? difference(treeSelectedKeys, listSelectedKeys)
              : difference(listSelectedKeys, treeSelectedKeys);
            onItemSelectAll(diffKeys, selected);
          },
          onSelect({ id }, selected) {
            onItemSelect(id, selected);
          },
          selectedRowKeys: listSelectedKeys
        };

        const handleTableChange = paginationObj => {
          queryUserList(
            {
              current: paginationObj.current - 1,
              pageSize: paginationObj.pageSize,
              deptIds: state.deptIds,
              userName: state.userName
            },
            state.targetKeys,
            state.totalDataSource,
            state.rightDataSource
          );
        };

        return (
          <CustomTable
            rowKey={record => record.id}
            rowSelection={rowSelection}
            columns={columns}
            dataSource={
              direction === 'left'
                ? state.leftDataSource
                : state.rightDataSource
            }
            size="small"
            style={{ pointerEvents: listDisabled ? 'none' : null }}
            onRow={({ id, disabled: itemDisabled }) => ({
              onClick: () => {
                if (itemDisabled) {
                  return;
                }
                onItemSelect(id, !listSelectedKeys.includes(id));
              }
            })}
            loading={direction === 'left' && state.loading}
            onChange={handleTableChange}
            pagination={direction === 'left' ? state.pagination : false}
          />
        );
      }}
    </Transfer>
  );

  return (
    <CommonModal
      modalProps={{
        width: 1096,
        title: <p style={{ fontSize: 16 }}>添加角色成员</p>
      }}
      btnProps={{ type: 'link' }}
      btnText={props.btnText}
      onClick={() => handleClick()}
      beforeOk={() => handleAddRole()}
      afterCancel={() => {
        handleCancle();
      }}
    >
      <div>
        <Row type="flex" style={{ marginBottom: 16 }}>
          <Col span={5}>
            <Cascader
              placeholder="请筛选角色部门"
              options={state.deptList}
              fieldNames={{
                label: 'title',
                value: 'id',
                children: 'children'
              }}
              onChange={handleChangeDept}
              //   changeOnSelect
            />
          </Col>
          <Col span={5}>
            <Search
              onPressEnter={handleSelectName}
              onChange={handleChangeName}
              onSearch={handleSelectName}
              placeholder="搜索角色成员"
            />
          </Col>
        </Row>
        <TableTransfer
          dataSource={state.totalDataSource ? state.totalDataSource : []}
          targetKeys={state.targetKeys}
          onChange={onChange}
          leftColumns={tableColumns}
          rightColumns={tableColumns}
          showSelectAll={false}
          listStyle={{ height: 530, overflow: 'auto', width: '45%' }}
        />
      </div>
    </CommonModal>
  );
};
export default AddRoleMemberModal;
