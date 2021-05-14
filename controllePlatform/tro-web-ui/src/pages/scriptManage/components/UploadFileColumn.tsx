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
import { message, Checkbox, Icon, Tooltip, Tag, Divider } from 'antd';
import PressureTestSceneService from '../service';
import EditCodeModal from '../modals/EditCodeModal';

const getUploadFileColumns = (
  state,
  setState,
  dictionaryMap
): ColumnProps<any>[] => {
  /**
   * @name 选择是否拆分
   */
  const handleSplit = async (fileId, uploadId, isSplit) => {
    if (fileId) {
      setState({
        fileList: state.fileList.map(item => {
          if (item.id === fileId) {
            return { ...item, isSplit: isSplit === 0 ? 1 : 0 };
          }
          return { ...item };
        })
      });
    }
    if (uploadId) {
      setState({
        fileList: state.fileList.map(item => {
          if (item.uploadId === uploadId) {
            return { ...item, isSplit: isSplit === 0 ? 1 : 0 };
          }
          return { ...item };
        })
      });
    }
  };

  /**
   * @name 删除新上传文件
   */
  const handleDeleteFiles = async (uploadId, topic) => {
    const {
      data: { data, success }
    } = await PressureTestSceneService.deleteFiles({ uploadId, topic });
    if (success) {
      message.success('删除文件成功！');
      setState({
        fileList: state.fileList.filter(item => {
          return uploadId !== item.uploadId;
        })
      });
    }
  };

  /**
   * @name 删除上传文件
   */
  const handleDelete = async item => {
    if (item.id) {
      setState({
        fileList: state.fileList.map(item2 => {
          if (item.id === item2.id) {
            return { ...item2, isDeleted: 1 };
          }
          return { ...item2 };
        })
      });
    } else {
      handleDeleteFiles(item.uploadId, item.topic);
    }
  };

  return [
    {
      ...customColumnProps,
      title: '文件名称',
      dataIndex: 'fileName',
      width: 150
      // render: text => {
      //   return (
      //     <Tag style={{ maxWidth: 150 }}>
      //       <Tooltip title={text}>
      //         <span
      //           style={{
      //             maxWidth: 120,
      //             overflow: 'hidden',
      //             textOverflow: ' ellipsis',
      //             whiteSpace: 'nowrap',
      //             display: 'inline-block'
      //           }}
      //         >
      //           {text}
      //         </span>
      //       </Tooltip>
      //     </Tag>
      //   );
      // }
    },
    {
      ...customColumnProps,
      title: '文件类型',
      dataIndex: 'fileType',
      render: text => {
        return <span>{text === 1 ? '数据' : '脚本'}</span>;
      }
    },
    {
      ...customColumnProps,
      title: '文件数据量（条）',
      dataIndex: 'uploadedData'
    },
    {
      ...customColumnProps,
      title: (
        <span>
          是否拆分
          <Tooltip
            title="拆分时不同施压机将使用不同的数据；不拆分则所有施压机使用相同的数据。"
            placement="bottom"
          >
            <Icon style={{ marginLeft: 4 }} type="question-circle" />
          </Tooltip>
        </span>
      ),
      dataIndex: 'isSplit',
      render: (text, row) => {
        return row.fileType === 1 ? (
          <Checkbox
            checked={text === 1 ? true : false}
            onChange={() => handleSplit(row.id, row.uploadId, text)}
          />
        ) : (
          '-'
        );
      }
    },
    {
      ...customColumnProps,
      title: '更新时间',
      dataIndex: 'uploadTime'
    },
    {
      ...customColumnProps,
      title: '操作',
      dataIndex: 'action',
      align: 'right',
      render: (text, row) => {
        return (
          <Fragment>
            {row.fileType === 0 && (
              <Fragment>
                <EditCodeModal
                  state={state}
                  setState={setState}
                  btnText="编辑"
                  fileId={row.downloadUrl}
                />
                <Divider type="vertical" />
              </Fragment>
            )}
            <a
              style={{
                color: '#29C7D7',
                marginLeft: 8
              }}
              onClick={() => handleDelete(row)}
            >
              删除
            </a>
          </Fragment>
        );
      }
    }
  ];
};

export default getUploadFileColumns;
