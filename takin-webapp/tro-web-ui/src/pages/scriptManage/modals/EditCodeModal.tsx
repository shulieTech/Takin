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

import React, { Fragment, useEffect } from 'react';
import { CommonForm, CommonModal, CommonSelect, useStateReducer } from 'racc';
import ScriptManageService from '../service';
import { message } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import { Controlled as CodeMirror } from 'react-codemirror2';
import styles from './../index.less';
require('codemirror/lib/codemirror.css');
require('codemirror/theme/material.css');
require('codemirror/theme/neat.css');
require('codemirror/mode/xml/xml.js');
require('codemirror/mode/javascript/javascript.js');

interface Props {
  btnText?: string | React.ReactNode;
  fileId?: number | string;
  state?: any;
  setState?: (value) => void;
}

interface State {
  isReload?: boolean;
  tagList: any;
  tags: any;
  form: any;
  scriptCode: any;
}
const EditCodeModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    isReload: false,
    tagList: [],
    tags: undefined,
    form: null as WrappedFormUtils,
    scriptCode: null
  });

  const { fileId } = props;

  const handleClick = () => {
    if (!props.state.scriptCode) {
      queryScriptCode();
    } else {
      setState({
        scriptCode: props.state.scriptCode
      });
    }
  };

  const handleCancle = () => {
    setState({
      tags: undefined,
      scriptCode: null
    });
  };

  /**
   * @name 获取文件脚本代码
   */
  const queryScriptCode = async () => {
    const {
      data: { success, data }
    } = await ScriptManageService.queryScriptCode({
      scriptFileUploadPath: fileId
    });
    if (success) {
      setState({
        scriptCode: data.content
      });
    }
  };

  /**
   * @name 修改文件脚本代码
   */
  const handleChangeCode = value => {
    setState({
      scriptCode: value
    });
    props.setState({
      scriptCode: value
    });
  };

  /**
   * @name 保存文件
   */

  const handleSubmit = async () => {
    props.setState({
      scriptCode: state.scriptCode
    });
    return await new Promise(async resolve => {
      props.setState({
        fileList: props.state.fileList.map((item, k) => {
          if (item.isDeleted === 0 && item.fileType === 0) {
            return { ...item, scriptContent: state.scriptCode };
          }
          return item;
        })
      });
      resolve(true);
    });
  };

  return (
    <CommonModal
      modalProps={{
        width: 'calc(100% - 40px)',
        title: '编辑代码',
        centered: true
      }}
      btnProps={{ type: 'link' }}
      btnText={props.btnText}
      onClick={() => handleClick()}
      beforeOk={handleSubmit}
      afterCancel={handleCancle}
    >
      <div style={{ height: 700 }}>
        <CodeMirror
          className={styles.codeMirror}
          value={state.scriptCode}
          options={{
            mode: 'xml',
            theme: 'material',
            lineNumbers: true
          }}
          onBeforeChange={(editor, data, value) => {
            // this.setState({value});
            handleChangeCode(value);
          }}
          onChange={(editor, data, value) => {
            handleChangeCode(value);
          }}
        />
      </div>
    </CommonModal>
  );
};
export default EditCodeModal;
