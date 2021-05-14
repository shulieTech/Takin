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
 * @name 步骤1-基本信息
 */

import { Icon, message, Radio } from 'antd';
import { ImportFile } from 'racc';
import { FormDataType } from 'racc/dist/common-form/type';
import React from 'react';
import CustomTable from 'src/components/custom-table';
import { FormCardMultipleDataSourceBean } from 'src/components/form-card-multiple/type';
import {
  default as PressureTestSceneService,
  default as ScriptManageService
} from '../service';
import UploadAdjunctColumn from './UploadAdjunctColumn';
import getUploadFileColumns from './UploadFileColumn';

interface Props {
  dictionaryMap?: any;
}

const ScriptFileUpload = (
  state,
  setState,
  props
): FormCardMultipleDataSourceBean => {
  /** @name 基本信息 */
  const getScriptFileUploadData = (): FormDataType[] => {
    const { location, dictionaryMap } = props;
    const { query } = location;
    const { action } = query;

    const { SCRIPT_TYPE } = dictionaryMap;
    const { detailData } = state;

    const handleChange = info => {
      /**
       * @name 已上传的文件列表名
       */
      const fileListName =
        state.fileList &&
        state.fileList.map(item => {
          return item.fileName;
        });

      /**
       * @name 准备上传的文件列表名
       */
      const readyToUploadFileName =
        info.fileList &&
        info.fileList.slice(state.uploadFileNum).map(item => {
          return item.name;
        });

      /**
       * @name 准备上传的文件列表
       */
      const readyToUploadFileList =
        info.fileList && info.fileList.slice(state.uploadFileNum);

      /**
       * @name 判断是否是可接受类型
       */
      function isAcceptType(ext) {
        return ['jar', 'csv', 'jmx'].indexOf(ext.toLowerCase()) !== -1;
      }

      setState({
        uploadFileNum: info.fileList.length
      });

      /**
       * @name 待上传的元素含有不可接受类型
       */
      if (
        readyToUploadFileName.find(item => {
          return !isAcceptType(item.substr(item.lastIndexOf('.') + 1));
        })
      ) {
        message.error('上传的文件含有不可接受类型，请检查后上传');
        return;
      }

      /**
       * @name 待上传的元素超过200M大小
       */
      if (
        readyToUploadFileList.find(item => {
          return item.size / 1024 / 1024 > 200;
        })
      ) {
        message.error('上传的文件大小超过200M，请检查后上传');
        return;
      }

      /**
       * @name 待上传的元素含有重名文件列表
       */
      const equalList =
        readyToUploadFileName &&
        readyToUploadFileName.filter((item, index) => {
          if (
            fileListName &&
            fileListName
              .filter(item2 => {
                if (item2.isDeleted) {
                  return item2;
                }
              })
              .includes(item)
          ) {
            return item;
          }
        });

      if (equalList.length) {
        if (info.file.uid === info.fileList.slice(-1)[0].uid) {
          message.error('不能重复上传文件');
        }
        return;
      }
      // console.log('info.fileList', info.fileList);
      const newUploadFileList = info.fileList.slice(state.uploadFileNum);
      // console.log('newUploadFileList', newUploadFileList);

      const formData = new FormData();
      info.fileList.slice(state.uploadFileNum).map(item => {
        formData.append('file', item.originFileObj);
      });

      setState({
        fileList: state.fileList && state.fileList.concat(newUploadFileList)
      });

      if (info.file.uid === info.fileList.slice(-1)[0].uid && formData) {
        uploadFiles(formData);
      }
    };

    /**
     * @name 上传文件
     */
    const uploadFiles = async files => {
      const {
        data: { data, success }
      } = await PressureTestSceneService.uploadFiles(files);
      if (success) {
        setState({
          fileList: state.fileList ? state.fileList.concat(data) : data
        });
      }
    };

    /**
     * @name 删除新上传文件
     */
    const handleDeleteFiles = async uploadId => {
      const {
        data: { data, success }
      } = await PressureTestSceneService.deleteFiles({ uploadId });
      if (success) {
        message.success('删除文件成功！');
        setState({
          fileList:
            state.fileList &&
            state.fileList.filter(item => {
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
        handleDeleteFiles(item.uploadId);
      }
    };

    const handleUpload = async file => {
      const {
        data: { success, data }
      } = await ScriptManageService.uploadAttachments(file);
      if (success) {
        message.success('上传成功');
        setState({ attachmentList: [...state.attachmentList, ...data] });
      }
    };

    return [
      {
        key: 'scriptType',
        label: '脚本类型',
        options: {
          initialValue: action !== 'add' ? String(detailData.scriptType) : '0',
          rules: [{ required: true, message: '请选择脚本类型' }]
        },
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 16 } },
        node: (
          <Radio.Group>
            {SCRIPT_TYPE &&
              SCRIPT_TYPE.map((item, k) => {
                return (
                  <Radio key={k} value={item.value}>
                    {item.label}
                  </Radio>
                );
              })}
          </Radio.Group>
        )
      },
      {
        key: 'uploadFiles',
        label: '上传文件',
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 16 } },
        options: {
          initialValue: action !== 'add' ? detailData.uploadFiles : '',
          rules: [{ required: false, message: '请上传文件' }]
        },
        node: (
          <ImportFile
            style={{ marginLeft: 100 }}
            UploadProps={{
              type: 'drag',
              multiple: true,
              onChange: info => handleChange(info)
            }}
            fileName="file"
            onImport={file => true}
          >
            <Icon type="inbox" />
            <p
              style={{
                display: 'flex',
                padding: '0px 10px',
                justifyContent: 'center'
              }}
            >
              <span>
                <span
                  style={{
                    color: '#474C50',
                    display: 'block',
                    fontSize: '16px'
                  }}
                >
                  点击或将文件拖拽到此处上传
                </span>
                <span
                  className={`ant-upload-hint ft-12`}
                  style={{ color: 'rgba(0,0,0,0.43)', display: 'block' }}
                >
                  支持格式：.jar | .csv | .jmx
                </span>
                <span style={{ color: 'rgba(0,0,0,0.43)', display: 'block' }}>
                  上传的文件必须包含一个压测脚本
                </span>
                {/* <span style={{ color: 'rgba(0,0,0,0.43)', display: 'block' }}>
                  超过200M的数据文件，请保存场景后，请使用插件上传,
                  <a
                    onClick={e => e.stopPropagation()}
                    href={state.downloadUrl}
                    download
                  >
                    点击下载插件
                  </a>
                </span> */}
              </span>
            </p>
          </ImportFile>
        ),
        extra: (
          <div style={{ marginTop: 8, width: '160%' }}>
            <CustomTable
              columns={getUploadFileColumns(state, setState, props)}
              dataSource={
                state.fileList
                  ? state.fileList.filter(item => {
                    return item.isDeleted !== 1;
                  })
                  : []
              }
            />
          </div>
        )
      },
      {
        key: 'uploadAttachments',
        label: '上传附件',
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 16 } },
        options: {
          initialValue: action !== 'add' ? detailData.uploadAttachments : '',
          rules: [{ required: false, message: '请上传附件' }]
        },
        node: (
          <ImportFile
            style={{ marginLeft: 100 }}
            UploadProps={{
              type: 'drag',
              multiple: true
            }}
            accept={null}
            fileName="file"
            onImport={handleUpload}
          >
            <Icon type="inbox" />
            <p
              style={{
                display: 'flex',
                padding: '0px 10px',
                justifyContent: 'center'
              }}
            >
              <span>
                <span
                  style={{
                    color: '#474C50',
                    display: 'block',
                    fontSize: '16px'
                  }}
                >
                  点击或将文件拖拽到此处上传
                </span>
                <span
                  className={`ant-upload-hint ft-12`}
                  style={{ color: 'rgba(0,0,0,0.43)', display: 'block' }}
                >
                  脚本中需要调用的附件在此处上传，
                </span>
                <span style={{ color: 'rgba(0,0,0,0.43)', display: 'block' }}>
                  脚本中的调用路径请使用相对路径。
                </span>
              </span>
            </p>
          </ImportFile>
        ),
        extra: (
          <div style={{ marginTop: 8, width: '160%' }}>
            <CustomTable
              columns={UploadAdjunctColumn(state, setState, props)}
              dataSource={
                state.attachmentList
                  ? state.attachmentList.filter(item => {
                    return item.isDeleted !== 1;
                  })
                  : []
              }
            />
          </div>
        )
      }
    ];
  };

  return {
    title: '压测脚本/文件',
    rowNum: 1,
    span: 14,
    formData: getScriptFileUploadData()
  };
};

export default ScriptFileUpload;
