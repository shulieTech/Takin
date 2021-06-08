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

/**
 * @name
 * @author chuxu
 */
import React, { Fragment } from 'react';
import { ColumnProps } from 'antd/lib/table';
import _ from 'lodash';
import { customColumnProps } from 'src/components/custom-table/utils';
import { Badge } from 'antd';
import AppManageService from '../service';
import CustomPopconfirm from 'src/components/custom-popconfirm/CustomPopconfirm';
import ConfigBaffleDrawer from './ConfigBaffleDrawer';
import { openNotification } from 'src/common/custom-notification/CustomNotification';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';

const getColumns = (
  state,
  setState,
  detailState,
  action
): ColumnProps<any>[] => {
  const btnAuthority: any =
    localStorage.getItem('trowebBtnResource') &&
    JSON.parse(localStorage.getItem('trowebBtnResource'));
  /**
   * @name 确认是否禁用、启用出口挡板
   */
  const handleConfirm = async (id, isEnable) => {
    const {
      data: { data, success }
    } = await AppManageService.openAndCloseExitJob({
      id,
      isEnable
    });
    if (success) {
      const txt = isEnable === true ? '启用' : '禁用';
      openNotification(`${txt}成功`);
      setState({
        isReload: !state.isReload
      });
    }
  };

  /**
   * @name 删除出口挡板
   */
  const handleDeleteExit = async id => {
    const {
      data: { data, success }
    } = await AppManageService.deleteBaffleConfig({
      id
    });
    if (success) {
      openNotification(`删除成功`);
      setState({
        isReload: !state.isReload
      });
    }
  };

  return [
    {
      ...customColumnProps,
      title: '类名#方法名',
      dataIndex: 'methodInfo'
    },
    {
      ...customColumnProps,
      title: '状态',
      dataIndex: 'isEnable',
      render: (text, row) => {
        return (
          <Badge
            text={text === false ? '已禁用' : '已启用'}
            color={text === false ? '#A2A6B1' : '#11BBD5'}
          />
        );
      }
    },
    {
      ...customColumnProps,
      title: '备注',
      dataIndex: 'remark',
      ellipsis: true,
      width: 200
    },
    {
      ...customColumnProps,
      title: '最后修改时间',
      dataIndex: 'updateTime'
    },
    {
      ...customColumnProps,
      title: '操作',
      dataIndex: 'action',
      render: (text, row) => {
        const txt = row.isEnable === false ? '启用' : '禁用';
        return (
          <Fragment>
            <AuthorityBtn
              isShow={
                btnAuthority &&
                btnAuthority.appManage_6_enable_disable &&
                row.canEnableDisable
              }
            >
              <CustomPopconfirm
                title={`是否确认${txt}`}
                okText={`确认${txt}`}
                okColor="#FE7D61"
                onConfirm={() => handleConfirm(row.id, !row.isEnable)}
              >
                <a
                  disabled={
                    detailState.switchStatus === 'OPENING' ||
                    detailState.switchStatus === 'CLOSING'
                      ? true
                      : false
                  }
                  style={{ marginRight: 8 }}
                >
                  {txt}
                </a>
              </CustomPopconfirm>
            </AuthorityBtn>
            <AuthorityBtn
              isShow={
                btnAuthority && btnAuthority.appManage_3_update && row.canEdit
              }
            >
              <ConfigBaffleDrawer
                disabled={
                  detailState.switchStatus === 'OPENING' ||
                  detailState.switchStatus === 'CLOSING'
                    ? true
                    : false
                }
                action="edit"
                title="编辑"
                id={row.id}
                onSccuess={() => {
                  setState({
                    isReload: !state.isReload
                  });
                }}
              />
            </AuthorityBtn>
            <AuthorityBtn
              isShow={
                btnAuthority && btnAuthority.appManage_4_delete && row.canRemove
              }
            >
              <CustomPopconfirm
                title="删除后不可恢复，确定要删除吗？"
                okText="确定删除"
                okColor="#FE7D61"
                onConfirm={() => handleDeleteExit(row.id)}
              >
                <a
                  disabled={
                    detailState.switchStatus === 'OPENING' ||
                    detailState.switchStatus === 'CLOSING'
                      ? true
                      : false
                  }
                  style={{ marginLeft: 8 }}
                >
                  删除
                </a>
              </CustomPopconfirm>
            </AuthorityBtn>
          </Fragment>
        );
      }
    }
  ];
};

export default getColumns;
