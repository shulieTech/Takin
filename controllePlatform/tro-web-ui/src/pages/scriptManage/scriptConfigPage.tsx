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

import { Button, message } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import { connect } from 'dva';
import { useStateReducer } from 'racc';
import React, { useEffect } from 'react';
import CustomSkeleton from 'src/common/custom-skeleton';
import FormCardMultiple from 'src/components/form-card-multiple';
import { BasePageLayout } from 'src/components/page-layout';
import { router } from 'umi';
import LinkMarkService from '../linkMark/service';
import PressureTestSceneService from '../pressureTestManage/pressureTestScene/service';
import BaseInfo from './components/BaseInfo';
import ScriptFileUpload from './components/ScriptFileUpload';
import ScriptManageService from './service';

interface Props {
  location?: { query?: any };
  dictionaryMap?: any;
}
interface ScriptConfigPageState {
  form: any;
  fileList: any[];
  attachmentList: any[];
  bussinessActiveList: any[];
  businessFlowList: any[];
  selectedBussinessActiveList: any[];
  uploadFileNum: number;
  detailData: any;
  downloadUrl: string;
  loading: boolean;
  scriptCode: string;
  required: boolean;
}

const ScriptConfigPage: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<ScriptConfigPageState>({
    form: null as WrappedFormUtils,
    fileList: [],
    attachmentList: [],
    bussinessActiveList: null, // 所有业务活动
    businessFlowList: null, // 所有业务流程
    selectedBussinessActiveList: null,
    uploadFileNum: 0,
    detailData: {} as any,
    downloadUrl: undefined,
    loading: false,
    scriptCode: null,
    required: false
  });

  const { location, dictionaryMap } = props;
  const { query } = location;
  const { action, id } = query;

  useEffect(() => {
    queryBussinessActive();
    queryBigFileDownload();
    if (action === 'edit') {
      queryPressureTestSceneDetail(id);
    }
  }, []);

  const formDataSource = [
    BaseInfo(state, setState, props),
    ScriptFileUpload(state, setState, props)
  ];

  /**
   * @name 获取所有业务活动
   */
  const queryBussinessActive = async () => {
    const {
      data: { success, data }
    } = await PressureTestSceneService.queryBussinessActive({});
    if (success) {
      setState({
        bussinessActiveList:
          data &&
          data.map(item => {
            return { label: item.businessActiveName, value: item.id };
          })
      });
    }
  };

  /**
   * @name 获取下载大文件插件地址
   */
  const queryBigFileDownload = async () => {
    const {
      data: { success, data }
    } = await PressureTestSceneService.getBigFileDownload({});
    if (success) {
      setState({
        downloadUrl: data.url
      });
    }
  };

  /**
   * @name 获取脚本配置详情
   */
  const queryPressureTestSceneDetail = async value => {
    const {
      data: { success, data }
    } = await ScriptManageService.queryScript({
      scriptId: value
    });
    if (success && data) {
      setState({
        detailData: data,
        fileList: data.relatedFiles,
        attachmentList: data.relatedAttachments
      });
    }
  };

  const handleSubmit = async () => {
    state.form.validateFields(async (err, values) => {
      if (err) {
        message.error('请检查表单必填项');
        return false;
      }

      if (
        state.required &&
        (!values.pluginConfigs || !values.pluginConfigs.length)
      ) {
        message.error('请选择关联插件!');
        return;
      }

      if (state.fileList.length === 0) {
        message.error('请上传压测脚本文件！');
        return;
      }

      let result;

      result = {
        ...values,
        relatedType: values.relatedObj.relatedType,
        relatedId: values.relatedObj.relatedId,
        uploadFiles: state.fileList,
        pluginConfigs: state.required ? values.pluginConfigs : null,
        uploadAttachments: state.attachmentList
      };
      delete result.relatedObj;

      setState({
        loading: true
      });
      /**
       * @name 增加脚本
       */
      if (action === 'add') {
        const {
          data: { success, data }
        } = await ScriptManageService.addScript(result);
        if (success) {
          setState({
            loading: false
          });
          message.success('增加脚本成功');
          router.push('/scriptManage');
        }
        setState({
          loading: false
        });
      }

      /**
       * @name 编辑脚本
       */
      if (action === 'edit') {
        const {
          data: { success, data }
        } = await ScriptManageService.editScript({
          ...result,
          scriptId: id
        });
        if (success) {
          setState({
            loading: false
          });
          message.success('编辑脚本配置成功');
          router.push('/scriptManage');
        }
        setState({
          loading: false
        });
      }
    });
  };

  return (action !== 'add' && JSON.stringify(state.detailData) !== '{}') ||
    action === 'add' ? (
    <BasePageLayout title={'脚本配置'}>
      <FormCardMultiple
        commonFormProps={{
          rowNum: 1
        }}
        dataSource={formDataSource}
        getForm={form => setState({ form })}
      />

      <Button
        type="primary"
        onClick={() => handleSubmit()}
        loading={state.loading}
      >
        保存
      </Button>
    </BasePageLayout>
  ) : (
    <CustomSkeleton />
  );
};
export default connect(({ common }) => ({ ...common }))(ScriptConfigPage);
