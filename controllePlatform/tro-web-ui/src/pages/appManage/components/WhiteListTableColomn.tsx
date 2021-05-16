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
import {
  Badge,
  Button,
  Dropdown,
  Icon,
  Menu,
  message,
  Modal,
  Popconfirm,
  Switch,
  Tag
} from 'antd';
import { ColumnProps } from 'antd/lib/table';
import React, { Fragment } from 'react';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';
import { customColumnProps } from 'src/components/custom-table/utils';
import { InterfaceType } from '../enum';
import WhiteListScopeModal from '../modals/WhiteListScopeModal';
import AppManageService from '../service';
import styles from './../index.less';
import AddWhiteListDrawer from './AddWhiteListDrawer';

const getWhiteListColumns = (
  state,
  setState,
  detailState,
  applicationId,
  action,
  detailData
): ColumnProps<any>[] => {
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
      applicationId,
      ids,
      type: useYn
    });
    if (success) {
      const txt = useYn === 0 ? '取消' : '加入';
      // openNotification(`${txt}成功`, '');
      message.config({
        top: 150
      });
      message.success(`${txt}成功`);
      setState({
        isReload: !state.isReload
      });
    }
  };

  const { confirm } = Modal;
  const showModal = (id, useYn) => {
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
        handleConfirm([id], useYn);
      }
    });
  };

  const handleDelete = async (dbId: string) => {
    const {
      data: { success }
    } = await AppManageService.deleteWhiteList({ dbIds: [dbId] });
    if (success) {
      message.success('删除成功');
      setState({ isReload: !state.isReload });
    }
  };

  /**
   * @name 全局生效
   */
  const handleAddGlobal = async wlistId => {
    const {
      data: { success }
    } = await AppManageService.whiteListGlobal({ wlistId });
    if (success) {
      message.success('白名单全局生效');
      setState({ isReload: !state.isReload });
    }
  };

  return [
    {
      ...customColumnProps,
      title: '接口名称',
      dataIndex: 'interfaceName',
      width: 320,
      render: (text, row) => {
        return (
          <div>
            <span style={{ marginRight: 8 }}>{text}</span>
            {row.tags &&
              row.tags.map((item, k) => {
                return <Tag key={k}>{item}</Tag>;
              })}
          </div>
        );
      }
    },
    {
      ...customColumnProps,
      title: '状态',
      dataIndex: 'useYn',
      render: (text, row) => {
        return (
          <Badge
            text={text === 0 ? '未加入' : '已加入'}
            color={text === 0 ? '#A2A6B1' : '#11BBD5'}
          />
        );
      }
    },
    {
      ...customColumnProps,
      title: '接口类型',
      dataIndex: 'interfaceType',
      render: text => {
        return (
          <Tag className={styles.tagStyle}>
            {InterfaceType[text] || 'UNKNOWN'}
          </Tag>
        );
      }
    },
    {
      ...customColumnProps,
      title: '最后修改时间',
      dataIndex: 'gmtUpdate'
    },
    {
      ...customColumnProps,
      title: '生效范围',
      dataIndex: 'isGlobal',
      render: (text, row) => {
        const menu = (
          <Menu>
            <Menu.Item>
              <Button type="link" onClick={() => handleAddGlobal(row.dbId)}>
                全局生效
              </Button>
            </Menu.Item>
            <Menu.Item>
              <WhiteListScopeModal
                btnText="部分生效"
                wlistId={row.dbId}
                interfaceName={row.interfaceName}
                onSccuess={() => {
                  setState({
                    isReload: !state.isReload
                  });
                }}
              />
            </Menu.Item>
          </Menu>
        );
        return (
          <div>
            <span>{text ? '全局生效' : '部分生效'}</span>
            {row.dbId && (
              <Dropdown overlay={menu} trigger={['click']}>
                <a
                  className="ant-dropdown-link"
                  onClick={e => e.preventDefault()}
                >
                  <Icon type="down" />
                </a>
              </Dropdown>
            )}
          </div>
        );
      }
    },
    {
      ...customColumnProps,
      title: '操作',
      dataIndex: 'action',
      render: (text, row) => {
        const txt = row.useYn === 0 ? '加入白名单' : '取消白名单';
        return (
          <Fragment>
            <AuthorityBtn
              isShow={
                btnAuthority &&
                btnAuthority.appManage_6_enable_disable &&
                row.canEnableDisable
              }
            >
              <Switch
                checkedChildren="加入"
                unCheckedChildren="取消"
                onClick={() => showModal(row.wlistId, row.useYn === 0 ? 1 : 0)}
                checked={row.useYn === 0 ? false : true}
              />
            </AuthorityBtn>
            <Fragment>
              <AuthorityBtn
                isShow={
                  btnAuthority && btnAuthority.appManage_3_update && row.canEdit
                }
              >
                <span style={{ marginLeft: 8 }}>
                  <AddWhiteListDrawer
                    disabled={false}
                    title="编辑"
                    action="edit"
                    id={row.dbId}
                    onSccuess={() => {
                      setState({
                        isReload: !state.isReload
                      });
                    }}
                    detailData={detailData}
                    details={row}
                  />
                </span>
              </AuthorityBtn>
              <AuthorityBtn
                isShow={
                  btnAuthority &&
                  btnAuthority.appManage_4_delete &&
                  row.canRemove
                }
              >
                <Popconfirm
                  title="删除后无法恢复，是否确认删除?"
                  onConfirm={() => handleDelete(row.dbId)}
                >
                  <Button type="link" className="mg-l1x">
                    删除
                  </Button>
                </Popconfirm>
              </AuthorityBtn>
            </Fragment>
          </Fragment>
        );
      }
    }
  ];
};

export default getWhiteListColumns;
