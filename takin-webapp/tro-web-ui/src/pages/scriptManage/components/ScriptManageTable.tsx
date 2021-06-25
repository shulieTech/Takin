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
import {
  Button,
  Divider,
  Icon,
  message,
  Popconfirm,
  Popover,
  Tag,
  Tooltip
} from 'antd';
import AddTagsModal from '../modals/AddTagsModal';
import styles from './../index.less';
import { Link, router } from 'umi';
import ScriptManageService from '../service';
import { fileToObject } from 'antd/lib/upload/utils';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';
import request from 'src/utils/request';
declare var serverUrl: string;
const getScriptManageColumns = (state, setState): ColumnProps<any>[] => {
  const btnAuthority: any =
    localStorage.getItem('trowebBtnResource') &&
    JSON.parse(localStorage.getItem('trowebBtnResource'));
  const userType: string = localStorage.getItem('troweb-role');
  const expire: string = localStorage.getItem('troweb-expire');
  /**
   * @name  删除脚本
   */
  const handleDeleteScript = async Id => {
    const {
      data: { data, success }
    } = await ScriptManageService.deleteScript({
      scriptId: Id
    });
    if (success) {
      message.success('删除成功');
      setState({
        isReload: !state.isReload
      });
    }
  };

  /**
   * @name  下载打包脚本
   */
  const handleDownload = async (Id, fileName) => {
    const {
      data: { data, success }
    } = await ScriptManageService.downloadScript({
      scriptId: Id
    });
    if (success) {
      downloadFile(data.content, `${fileName}.zip`);
    }
  };

  const downloadFile = async (filePath, fileName) => {
    const { data, status, headers } = await request({
      url: `${serverUrl}/file/downloadFileByPath?filePath=${filePath}`,
      responseType: 'blob',
      headers: {
        'x-token': localStorage.getItem('full-link-token'),
        'Auth-Cookie': localStorage.getItem('auth-cookie')
      }
    });
    const blob = new Blob([data], { type: `` });

    // 获取heads中的filename文件名
    const downloadElement = document.createElement('a');
    // 创建下载的链接
    const href = window.URL.createObjectURL(blob);

    downloadElement.href = href;
    // 下载后文件名
    downloadElement.download = fileName;
    document.body.appendChild(downloadElement);
    // 点击下载
    downloadElement.click();
    // 下载完成移除元素
    document.body.removeChild(downloadElement);
    // 释放掉blob对象
    window.URL.revokeObjectURL(href);
  };

  /**
   * @name  下载单个脚本
   */
  const handleDownloadFile = async (Id, fileName) => {
    const {
      data: { data, success }
    } = await ScriptManageService.downloadSingleScript({
      filePath: Id
    });
    if (success) {
      downloadFile(data.content, fileName);
      // location.href = `${data.content}`;
      // message.success('下载成功');
    }
  };

  return [
    {
      ...customColumnProps,
      title: '序号',
      dataIndex: 'id'
    },
    {
      ...customColumnProps,
      title: '关联业务',
      dataIndex: 'relatedBusiness',
      render: (text, row) => {
        return (
          <div style={{ display: 'flex', alignContent: 'center' }}>
            {row.type === '1' ? (
              <Tooltip title="业务活动">
                <img
                  style={{ width: 24, marginRight: 8 }}
                  src={require('./../../../assets/businessActivity.png')}
                />
              </Tooltip>
            ) : (
              <Tooltip title="业务流程">
                <img
                  style={{ width: 24, marginRight: 8 }}
                  src={require('./../../../assets/businessFlow.png')}
                />
              </Tooltip>
            )}
            <Tooltip title={text}>
              <span
                style={{
                  display: 'inline-block',
                  width: 100,
                  overflow: 'hidden',
                  textOverflow: 'ellipsis',
                  whiteSpace: 'nowrap',
                  cursor: 'pointer'
                }}
              >
                {text}
              </span>
            </Tooltip>
          </div>
        );
      }
    },
    {
      ...customColumnProps,
      title: '相关文件',
      dataIndex: 'relatedFiles',
      render: (text, row) => {
        return text && text.length > 0 ? (
          <div>
            <Tag>
              <span
                style={{
                  display: 'inline-block',
                  width: 80,
                  lineHeight: '15px',
                  overflow: 'hidden',
                  textOverflow: 'ellipsis',
                  whiteSpace: 'nowrap'
                }}
              >
                {text[0] && text[0].fileName}
              </span>
            </Tag>
            <Popover
              placement="bottom"
              trigger="click"
              title="相关文件"
              content={
                <div className={styles.files}>
                  {text.map((item, k) => {
                    return (
                      <p key={k}>
                        {item && item.fileName}
                        <AuthorityBtn
                          isShow={
                            btnAuthority &&
                            btnAuthority.scriptManage_7_download &&
                            row.canDownload
                          }
                        >
                          <a
                            style={{ float: 'right', marginRight: 8 }}
                            onClick={() =>
                              handleDownloadFile(
                                item && item.downloadUrl,
                                item && item.fileName
                              )
                            }
                          >
                            下载
                          </a>
                        </AuthorityBtn>
                        <Divider />
                      </p>
                    );
                  })}
                </div>}
            >
              <Tag>...</Tag>
            </Popover>
          </div>
        ) : (
          '-'
        );
      }
    },
    {
      ...customColumnProps,
      title: '修改时间',
      dataIndex: 'updateTime'
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
      render: (text, row) => {
        return (
          <Fragment>
            <AuthorityBtn
              isShow={
                btnAuthority &&
                btnAuthority.scriptManage_3_update &&
                row.canEdit
              }
            >
              <Link
                style={{ marginRight: 8 }}
                to={`/scriptManage/scriptConfig?action=edit&id=${row.id}`}
              >
                编辑
              </Link>
            </AuthorityBtn>
            <AuthorityBtn
              isShow={
                btnAuthority &&
                btnAuthority.scriptManage_7_download &&
                row.canDownload
              }
            >
              <Button
                style={{ marginRight: 8 }}
                type="link"
                onClick={() => handleDownload(row.id, row.scriptName)}
              >
                下载
              </Button>
            </AuthorityBtn>
            <AuthorityBtn
              isShow={
                btnAuthority &&
                btnAuthority.scriptManage_4_delete &&
                row.canRemove
              }
            >
              <Popconfirm
                title="确定删除脚本吗？"
                onConfirm={() => handleDeleteScript(row.id)}
              >
                <Button type="link">删除</Button>
              </Popconfirm>
            </AuthorityBtn>
          </Fragment>
        );
      }
    }
  ];
};

export default getScriptManageColumns;
