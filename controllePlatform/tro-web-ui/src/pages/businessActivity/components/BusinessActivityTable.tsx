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
import { Badge, Divider, Popconfirm, Tooltip } from 'antd';
import _ from 'lodash';
import { customColumnProps } from 'src/components/custom-table/utils';
import { ChangeStatus } from '../enum';
import BusinessActivityService from '../service';
import Link from 'umi/link';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';

const getBusinessActivityColumns = (
  BusinessActivityState,
  setBusinessActivityState
): ColumnProps<any>[] => {
  const btnAuthority: any =
    localStorage.getItem('trowebBtnResource') &&
    JSON.parse(localStorage.getItem('trowebBtnResource'));
  const userType: string = localStorage.getItem('troweb-role');
  const expire: string = localStorage.getItem('troweb-expire');

  /**
   * @name 删除
   */
  const handleDelete = async linkId => {
    const {
      data: { success, data }
    } = await BusinessActivityService.deleteBusinessActivity({ linkId });
    if (success) {
      setBusinessActivityState({
        isReload: !BusinessActivityState.isReload
      });
    }
  };

  return [
    {
      ...customColumnProps,
      title: '业务活动名称',
      dataIndex: 'businessActiveName',
      width: 200
    },
    {
      ...customColumnProps,
      title: '业务域',
      dataIndex: 'businessDomain'
    },
    {
      ...customColumnProps,
      title: '系统流程名称',
      dataIndex: 'systemProcessName',
      width: 200
    },
    {
      ...customColumnProps,
      title: '变更状态',
      dataIndex: 'ischange',
      render: (text, row) => {
        return (
          <Badge
            text={text === '0' ? '正常' : '已变更'}
            color={ChangeStatus[text]}
          />
        );
      }
    },
    {
      ...customColumnProps,
      title: '涵盖中间件',
      dataIndex: 'middleWareList',
      render: text => {
        return (
          <span>
            {text && text.length > 0
              ? text.map((item, k) => {
                return (
                    <span key={k}>
                      {item}
                      {text.length !== k + 1 && '、'}
                    </span>
                );
              })
              : '--'}
          </span>
        );
      }
    },
    {
      ...customColumnProps,
      title: '创建时间',
      dataIndex: 'createTime'
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
          {row.candelete === '0' ? (
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
                onConfirm={() => handleDelete(row.businessActiceId)}
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
                <a style={{ color: 'rgba(33,208,244,0.5)', marginRight: 8 }}>
                  删除
                </a>
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
            <Link
              to={`/businessActivity/addBusinessActivity?action=edit&id=${row.businessActiceId}`}
            >
              编辑
            </Link>
          </AuthorityBtn>
        </Fragment>
      )
    }
  ];
};

export default getBusinessActivityColumns;
