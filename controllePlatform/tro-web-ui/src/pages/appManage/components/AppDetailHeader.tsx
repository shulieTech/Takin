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
import {
  Popconfirm,
  Switch,
  Tooltip,
  message,
  Button,
  Dropdown,
  Menu,
  Icon
} from 'antd';

import styles from './../index.less';
import DetailHeader from 'src/common/detail-header';
import AppManageService from '../service';
import copy from 'copy-to-clipboard';
import AddAppDrawer from './AddAppDrawer';
import router from 'umi/router';
import { appConfigStatusMap, appConfigStatusColorMap } from '../enum';
import { openNotification } from 'src/common/custom-notification/CustomNotification';
import AppErrorListModal from '../modals/AppErrorListModal';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';
import ImportFileModal from '../modals/ImportFileModal';
interface Props {
  id?: string | number;
  detailData?: any;
  state?: any;
  setState?: (value) => void;
  action?: string;
}

declare var serverUrl: string;
const AppDetailHeader: React.FC<Props> = props => {
  const { detailData, state, setState, id } = props;
  const btnAuthority: any =
    localStorage.getItem('trowebBtnResource') &&
    JSON.parse(localStorage.getItem('trowebBtnResource'));

  /**
   * @name  删除应用
   */
  const handleDeleteApp = async Id => {
    const {
      data: { data, success }
    } = await AppManageService.deleteApp({
      id: Id
    });
    if (success) {
      openNotification('删除应用成功', '');
      router.push('/appManage');
    }
  };

  const handleExport = async () => {
    const {
      data: { data, success }
    } = await AppManageService.exportAppConfig({
      id
    });
    if (success) {
      message.success('导出成功');
      location.href = `${serverUrl}${data}`;
    }
  };

  const leftData = {
    label: '应用名称',
    value: detailData && detailData.applicationName,
    isTooltip: true
  };
  const rightData = [
    { label: '最后修改时间', value: detailData && detailData.updateTime },
    {
      label: '应用状态',
      value: (
        <Fragment>
          <span
            style={{
              color:
                appConfigStatusColorMap[`${detailData && detailData.accessStatus}`
]
            }}
          >
            {appConfigStatusMap[`${detailData && detailData.accessStatus}`]}
          </span>
          {detailData && detailData.accessStatus === 3 && (
            <AppErrorListModal
              appId={id}
              btnText={
                <img
                  style={{ width: 16 }}
                  src={require('./../../../assets/error_icon.png')}
                />
              }
            />
          )}
        </Fragment>
      )
    }
  ];

  return (
    <DetailHeader
      leftWrapData={leftData}
      rightWrapData={rightData}
      extra={
        <div className={styles.extraWrap}>
          {
            <Dropdown
              overlay={
                <Menu>
                  <Menu.Item>
                    <AuthorityBtn
                      isShow={
                        btnAuthority &&
                        btnAuthority.appManage_4_delete &&
                        detailData &&
                        detailData.canRemove
                      }
                    >
                      <Popconfirm
                        title="确定删除应用吗？"
                        onConfirm={() => handleDeleteApp(id)}
                        disabled={
                          state.switchStatus === 'OPENING' ||
                          state.switchStatus === 'CLOSING'
                            ? true
                            : false
                        }
                      >
                        <Button
                          disabled={
                            state.switchStatus === 'OPENING' ||
                            state.switchStatus === 'CLOSING'
                              ? true
                              : false
                          }
                          type="link"
                          style={{ marginRight: 8, borderColor: '' }}
                        >
                          删除
                        </Button>
                      </Popconfirm>
                    </AuthorityBtn>
                  </Menu.Item>
                  <Menu.Item>
                    <AuthorityBtn
                      isShow={
                        btnAuthority &&
                        btnAuthority.appManage_3_update &&
                        detailData &&
                        detailData.canEdit
                      }
                    >
                      <AddAppDrawer
                        action="edit"
                        titles="编辑"
                        id={id}
                        disabled={
                          state.switchStatus === 'OPENING' ||
                          state.switchStatus === 'CLOSING'
                            ? true
                            : false
                        }
                        onSccuess={() => {
                          setState({
                            isReload: !state.isReload
                          });
                        }}
                      />
                    </AuthorityBtn>
                  </Menu.Item>
                </Menu>
              }
              trigger={['click']}
            >
              <Icon type="more" />
            </Dropdown>
          }
        </div>}
    />
  );
};
export default AppDetailHeader;
