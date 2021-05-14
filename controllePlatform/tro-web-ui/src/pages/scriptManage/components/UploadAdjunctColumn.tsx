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
import { message } from 'antd';
import { ColumnProps } from 'antd/lib/table';
import React, { Fragment } from 'react';
import { customColumnProps } from 'src/components/custom-table/utils';
import PressureTestSceneService from '../service';

const UploadAdjunctColumn = (
  state,
  setState,
  dictionaryMap
): ColumnProps<any>[] => {

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
  const handleDelete = async row => {
    const attachmentList = state.attachmentList.filter(item => item.id !== row.id);
    setState({ attachmentList });
    message.success('删除成功');
    // if (item.id) {
    //   setState({
    //     fileList: state.fileList.map(item2 => {
    //       if (item.id === item2.id) {
    //         return { ...item2, isDeleted: 1 };
    //       }
    //       return { ...item2 };
    //     })
    //   });
    // } else {
    //   handleDeleteFiles(item.uploadId, item.topic);
    // }
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

export default UploadAdjunctColumn;
