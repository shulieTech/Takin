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
import { Badge, Button, Popconfirm, Tooltip } from 'antd';
import { ColumnProps } from 'antd/lib/table';
import moment from 'moment';
import React, { Fragment } from 'react';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';
import { customColumnProps } from 'src/components/custom-table/utils';
import { CommonModelState } from 'src/models/common';
import { ChangeStatus } from '../enum';
import AddEditActivityModal from '../modals/AddEditActivityModal';
import BusinessActivityService from '../service';

const getColumns = (
  systemFlowState,
  setSystemFlowState,
  props: CommonModelState
): ColumnProps<any>[] => {
  const userType: string = localStorage.getItem('troweb-role');
  const expire: string = localStorage.getItem('troweb-expire');
  /**
   * @name 删除,刷新列表
   */
  const handleDelete = async activityId => {
    const {
      data: { success, data }
    } = await BusinessActivityService.deleteSystemFlow(activityId);
    if (success) {
      setSystemFlowState({
        isReload: !systemFlowState.isReload
      });
    }
  };

  const btnAuthority: any =
    localStorage.getItem('trowebBtnResource') &&
    JSON.parse(localStorage.getItem('trowebBtnResource'));

  return [
    {
      ...customColumnProps,
      title: '业务活动名称',
      dataIndex: 'activityName',
      width: 350
    },
    {
      ...customColumnProps,
      title: '业务域',
      dataIndex: 'businessDomain',
      render: text =>
        text && props.dictionaryMap
          ? props.dictionaryMap.domain.find(item => +item.value === +text).label
          : '-'
    },
    {
      ...customColumnProps,
      title: '变更状态',
      dataIndex: 'isChange',
      render: (text, row) => {
        return (
          <Badge
            text={
              +text === 0
                ? '正常'
                : row.changeType === '1'
                ? '已变更(入口)'
                : '已变更(关联链路)'
            }
            color={ChangeStatus[text]}
          />
        );
      }
    },
    {
      ...customColumnProps,
      title: '创建时间',
      dataIndex: 'createTime',
      render: text => moment(text).format('YYYY-MM-DD HH:mm:ss') || '--'
    },
    {
      ...customColumnProps,
      title: '负责人',
      dataIndex: 'managerName'
    },
    {
      ...customColumnProps,
      title: '操作',
      dataIndex: 'action',
      render: (text, row, index) => (
        <Fragment>
          {row.canDelete === 0 ? (
            <AuthorityBtn
              isShow={
                btnAuthority &&
                btnAuthority.businessActivity_4_delete &&
                row.canRemove
              }
            >
              <Popconfirm
                title="确定要删除吗？"
                okText="确认删除"
                cancelText="取消"
                onConfirm={() => handleDelete(row.activityId)}
              >
                <a href="#" style={{ color: '#21D0F4', marginRight: 8 }}>
                  删除
                </a>
              </Popconfirm>
            </AuthorityBtn>
          ) : (
            <AuthorityBtn
              isShow={
                btnAuthority &&
                btnAuthority.businessActivity_4_delete &&
                row.canRemove
              }
            >
              <Tooltip title="存在关联业务流程">
                <a style={{ color: '#CBD3DE', marginRight: 8 }}>删除</a>
              </Tooltip>
            </AuthorityBtn>
          )}
          <AuthorityBtn
            isShow={
              btnAuthority &&
              btnAuthority.businessActivity_3_update &&
              row.canEdit
            }
          >
            <AddEditActivityModal
              id={row.activityId}
              onSuccess={() =>
                setSystemFlowState({ isReload: !systemFlowState.isReload })
              }
            />
          </AuthorityBtn>
        </Fragment>
      )
    }
  ];
};

export default getColumns;
