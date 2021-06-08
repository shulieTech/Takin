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
import AddAndEditDbDrawer from './AddAndEditDbDrawer';
import { openNotification } from 'src/common/custom-notification/CustomNotification';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';
import AddEditDbModal from '../modals/AddEditDbModal';

const getLinkDbColumns = (
  state,
  setState,
  detailState,
  appId,
  detailData
): ColumnProps<any>[] => {
  const btnAuthority: any =
    localStorage.getItem('trowebBtnResource') &&
    JSON.parse(localStorage.getItem('trowebBtnResource'));
  /**
   * @name 确认是否禁用、启用
   */
  const handleConfirm = async (id, status) => {
    const {
      data: { data, success }
    } = await AppManageService.openAndClose({
      id,
      status
    });
    if (success) {
      const txt = status === 0 ? '启用' : '禁用';
      openNotification(`${txt}成功`);
      setState({
        isReload: !state.isReload
      });
    }
  };

  /**
   * @name 确认是否删除
   */
  const handleDelete = async id => {
    const {
      data: { data, success }
    } = await AppManageService.deleteDbTable({
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
      title: '服务器地址',
      dataIndex: 'url',
      width: 400
    },
    {
      ...customColumnProps,
      title: '类型',
      dataIndex: 'dbType',
      render: text => {
        return <span>{(text && text.label) || '-'}</span>;
      }
    },
    {
      ...customColumnProps,
      title: '方案类型',
      dataIndex: 'dsType',
      render: text => {
        return <span>{(text && text.label) || '-'}</span>;
      }
    },
    {
      ...customColumnProps,
      title: '状态',
      dataIndex: 'status',
      render: (text, row) => {
        return (
          <Badge
            text={text === 0 ? '已启用' : '已禁用'}
            color={text === 0 ? '#22BAE4' : '#A4A5A9'}
          />
        );
      }
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
        const txt = row.status === 0 ? '禁用' : '启用';

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
                onConfirm={() =>
                  handleConfirm(row.id, row.status === 0 ? 1 : 0)
                }
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
            {detailState.isNewAgent === true ? (
              <AuthorityBtn
                isShow={
                  btnAuthority && btnAuthority.appManage_3_update && row.canEdit
                }
              >
                <AddEditDbModal
                  id={row.id}
                  applicationId={appId}
                  btnText="编辑"
                  detailData={detailData}
                  onSuccess={() => {
                    setState({
                      isReload: !state.isReload
                    });
                  }}
                />
              </AuthorityBtn>
            ) : detailState.isNewAgent === false ? (
              <AuthorityBtn
                isShow={
                  btnAuthority && btnAuthority.appManage_3_update && row.canEdit
                }
              >
                <AddAndEditDbDrawer
                  action="edit"
                  disabled={
                    detailState.switchStatus === 'OPENING' ||
                    detailState.switchStatus === 'CLOSING'
                      ? true
                      : false
                  }
                  titles="编辑"
                  id={row.id}
                  onSccuess={() => {
                    setState({
                      isReload: !state.isReload
                    });
                  }}
                />
              </AuthorityBtn>
            ) : null}

            <AuthorityBtn
              isShow={
                btnAuthority && btnAuthority.appManage_4_delete && row.canRemove
              }
            >
              <Fragment>
                <CustomPopconfirm
                  title="删除后不可恢复，确定要删除吗？"
                  okText="确定删除"
                  okColor="#FE7D61"
                  onConfirm={() => handleDelete(row.id)}
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
              </Fragment>
            </AuthorityBtn>
          </Fragment>
        );
      }
    }
  ];
};

export default getLinkDbColumns;
