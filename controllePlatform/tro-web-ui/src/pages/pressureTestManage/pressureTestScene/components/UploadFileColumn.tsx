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
import { message, Checkbox, Icon, Tooltip } from 'antd';
import PressureTestSceneService from '../service';

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
          if (item.fileId === fileId) {
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
      title: 'ID',
      dataIndex: 'id'
    },
    {
      ...customColumnProps,
      title: '文件名称',
      dataIndex: 'fileName'
    },
    {
      ...customColumnProps,
      title: '上传时间',
      dataIndex: 'uploadTime'
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
      title: '已上传数据量',
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
      title: '操作',
      dataIndex: 'action',
      align: 'right',
      render: (text, row) => {
        return (
          <Fragment>
            <Icon
              style={{
                color: '#29C7D7'
              }}
              type="delete"
              onClick={() => handleDelete(row)}
            />
          </Fragment>
        );
      }
    }
  ];
};

export default getUploadFileColumns;
