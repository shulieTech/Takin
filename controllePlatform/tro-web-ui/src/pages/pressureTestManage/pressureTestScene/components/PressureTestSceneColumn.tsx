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
import { Badge, Button, message } from 'antd';
import { ColumnProps } from 'antd/lib/table';
import React, { Fragment } from 'react';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';
import CustomPopconfirm from 'src/components/custom-popconfirm/CustomPopconfirm';
import { customColumnProps } from 'src/components/custom-table/utils';
import Link from 'umi/link';
import router from 'umi/router';
import PressureTestSceneService from '../service';

const getPressureTestSceneColumns = (
  state,
  setState,
  dictionaryMap
): ColumnProps<any>[] => {
  const btnAuthority: any =
    localStorage.getItem('trowebBtnResource') &&
    JSON.parse(localStorage.getItem('trowebBtnResource'));
  const menuAuthority: any =
    localStorage.getItem('trowebUserResource') &&
    JSON.parse(localStorage.getItem('trowebUserResource'));

  const userType: string = localStorage.getItem('troweb-role');
  const expire: string = localStorage.getItem('troweb-expire');
  const handleConfirm = () => {
    setState({
      visible: true
    });
  };

  /**
   * @name 删除压测场景
   */
  const handleDelete = async id => {
    const {
      data: { data, success }
    } = await PressureTestSceneService.deletePressureTestScene({ id });
    if (success) {
      message.success('删除压测场景成功！');
      // router.push('/pressureTestManage/pressureTestScene');
      setState({
        isReload: !state.isReload
      });
    }
  };

  /**
   * @name 开启压测
   */
  const handleStart = async (sceneId, reportId) => {
    setState({
      startStatus: 'loading'
    });
    const {
      data: { data, success }
    } = await PressureTestSceneService.checkStartStatus({ sceneId, reportId });
    if (success && data.data !== 0) {
      if (data.data === 2) {
        setState({
          startStatus: 'success'
        });
        const startTime: any = new Date().getTime();
        localStorage.setItem('startTime', startTime);
        message.success('开启压测场景成功！');
        router.push(
          `/pressureTestManage/pressureTestReport/pressureTestLive?id=${sceneId}`
        );
      } else if (data.data === 1) {
        setTimeout(() => {
          handleStart(sceneId, reportId);
        }, 500);
      }
    } else {
      setState({
        startStatus: 'fail',
        startErrorList: data.msg
      });
    }
  };

  /**
   * @name 停止压测
   */
  const handleStop = async sceneId => {
    const {
      data: { data, success }
    } = await PressureTestSceneService.stopPressureTestScene({ sceneId });
    if (success) {
      message.success('停止压测场景成功！');
      setState({
        isReload: !state.isReload
      });
    }
  };

  const handleClickStart = async sceneId => {
    setState({
      sceneId,
      missingDataStatus: true,
      hasMissingData: false
    });
  };

  return [
    {
      ...customColumnProps,
      title: 'ID',
      dataIndex: 'id'
    },
    {
      ...customColumnProps,
      title: '压测场景名称',
      dataIndex: 'sceneName'
    },
    {
      ...customColumnProps,
      title: '状态',
      dataIndex: 'status',
      render: text => {
        return (
          <Badge
            text={text === 0 ? '待启动' : text === 1 ? '启动中' : '压测中'}
            color={text === 2 ? '#11BBD5' : '#A2A6B1'}
          />
        );
      }
    },
    {
      ...customColumnProps,
      title: '最新压测时间',
      dataIndex: 'lastPtTime'
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
      align: 'right',
      render: (text, row) => {
        return (
          <Fragment>
            {row.status === 0 && (
              <AuthorityBtn
                isShow={
                  btnAuthority &&
                  btnAuthority.pressureTestManage_pressureTestScene_5_start_stop &&
                  row.canStartStop
                }
              >
                <Button
                  onClick={() => {
                    handleClickStart(row.id);
                  }}
                  type="link"
                  style={{ marginRight: 8 }}
                >
                  启动
                </Button>
              </AuthorityBtn>
            )}
            {row.status === 1 && (
              <Button disabled type="link" style={{ marginRight: 8 }}>
                启动中
              </Button>
            )}
            {row.status === 0 && (
              <AuthorityBtn
                isShow={
                  btnAuthority &&
                  btnAuthority.pressureTestManage_pressureTestScene_3_update &&
                  row.canEdit
                }
              >
                <Link
                  style={{ marginRight: 8 }}
                  to={`/pressureTestManage/pressureTestScene/pressureTestSceneConfig?action=edit&id=${row.id}`}
                >
                  编辑
                </Link>
              </AuthorityBtn>
            )}
            {row.hasReport &&
              row.status !== 2 &&
              btnAuthority.pressureTestManage_pressureTestScene_5_start_stop &&
              menuAuthority &&
              menuAuthority.pressureTestManage_pressureTestReport && (
                <Link
                  style={{ marginRight: 8 }}
                  to={`/pressureTestManage/pressureTestReport?sceneName=${row.sceneName}`}
                >
                  查看报告
                </Link>
              )}
            {row.status === 0 && (
              <AuthorityBtn
                isShow={
                  btnAuthority &&
                  btnAuthority.pressureTestManage_pressureTestScene_4_delete &&
                  row.canRemove
                }
              >
                <CustomPopconfirm
                  okText="确认删除"
                  title={'是否确认删除'}
                  okColor="#FE7D61"
                  onConfirm={() => handleDelete(row.id)}
                >
                  <Button type="link" style={{ marginRight: 8 }}>
                    删除
                  </Button>
                </CustomPopconfirm>
              </AuthorityBtn>
            )}
            {row.status === 2 && (
              <AuthorityBtn
                isShow={
                  btnAuthority &&
                  btnAuthority.pressureTestManage_pressureTestScene_5_start_stop &&
                  row.canStartStop
                }
              >
                <CustomPopconfirm
                  okText="确认停止"
                  title={'是否确认停止'}
                  okColor="#FE7D61"
                  onConfirm={() => handleStop(row.id)}
                >
                  <Button type="link" style={{ marginRight: 8 }}>
                    停止
                  </Button>
                </CustomPopconfirm>
              </AuthorityBtn>
            )}
            {row.status === 2 && (
              <AuthorityBtn
                isShow={
                  btnAuthority &&
                  btnAuthority.pressureTestManage_pressureTestScene_5_start_stop &&
                  row.canStartStop
                }
              >
                <Link
                  to={`/pressureTestManage/pressureTestReport/pressureTestLive?id=${row.id}`}
                >
                  压测实况
                </Link>
              </AuthorityBtn>
            )}
          </Fragment>
        );
      }
    }
  ];
};

export default getPressureTestSceneColumns;
