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

import { Alert, Icon, message, Modal } from 'antd';
import { CommonModal, ImportFile, useStateReducer } from 'racc';
import React, { Fragment } from 'react';
import AppManageService from '../service';
interface Props {
  btnText?: string;
  id?: any;
  onSuccess?: () => void;
  visible?: boolean;
  state?: any;
  setState?: (value) => void;
}

/**
 * @name 导入配置
 */
const ImportFileModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer({
    errorInfo: null
  });
  const handleChange = async info => {
    const formData = new FormData();
    info.fileList.map(item => {
      formData.set('file', item.originFileObj);
      formData.set('id', props.id);
    });
    const {
      data: { data, success }
    } = await AppManageService.importAppConfig(formData);
    if (success) {
      message.success('导入成功');
      props.setState({
        visible: false
      });
      props.onSuccess();
      return;
    }
    message.error('导入失败');
    setState({
      errorInfo: data && data.msg
    });
  };

  return (
    <Modal
      title={state.errorInfo ? '导入失败' : '导入'}
      width={560}
      footer={null}
      visible={props.visible}
      onCancel={() => {
        props.setState({
          visible: false
        });
        setState({
          errorInfo: null
        });
      }}
    >
      {state.errorInfo ? (
        <div>
          <div style={{ color: '#8C8C8C', fontSize: '14px', marginBottom: 16 }}>
            失败原因：
          </div>
          {state.errorInfo.map((item, k) => {
            return (
              <p
                style={{
                  color: '#646464',
                  lineHeight: '21px',
                  fontSize: '13px'
                }}
                key={k}
              >
                {item}
              </p>
            );
          })}
        </div>
      ) : (
        <Fragment>
          <Alert
            message="导入规范：请使用在控制台应用配置导出的Excel直接进行导入，请勿自行更改，避免导入异常。若导入失败，将全部失败。若出现重复数据，将直接覆盖。"
            type="warning"
            showIcon
          />
          <div style={{ marginTop: 12 }}>
            <ImportFile
              accept={['xlsx']}
              UploadProps={{
                type: 'drag',
                multiple: false,
                onChange: info => handleChange(info)
              }}
              fileName="file"
              onImport={file => true}
            >
              <img
                src={require('./../../../assets/box.png')}
                style={{ width: 48 }}
              />
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
                      fontSize: '16px',
                      marginTop: 8
                    }}
                  >
                    点击或将文件拖拽到此处上传
                  </span>
                  <span
                    className={`ant-upload-hint ft-12`}
                    style={{
                      color: 'rgba(0,0,0,0.43)',
                      display: 'block',
                      marginBottom: 40,
                      marginTop: 8
                    }}
                  >
                    支持格式：.xlsx
                  </span>
                </span>
              </p>
            </ImportFile>
          </div>
        </Fragment>
      )}
    </Modal>
  );
};
export default ImportFileModal;
