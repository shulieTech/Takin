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
import { CommonForm, CommonModal, CommonTable, useStateReducer } from 'racc';
import styles from './../index.less';
import BusinessActivityService from '../service';
import { Collapse, Icon, message } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import getDataSourceFormData from '../components/DataSourceFormData';

interface Props {
  btnText?: string | React.ReactNode;
  dataSourceId?: number | string;
  onSccuess?: () => void;
  action: string;
  businessActivityId?: string;
}

interface State {
  isReload?: boolean;
  form: any;
  dataSourceList: any[];
  dataSourceDetail: any;
  loading: boolean;
  info: string;
  debugStatus: boolean;
  debugPassed: boolean;
}
const AddDataSourceModal: React.FC<Props> = props => {
  const { Panel } = Collapse;
  const [state, setState] = useStateReducer<State>({
    isReload: false,
    form: null as WrappedFormUtils,
    dataSourceList: null,
    dataSourceDetail: null,
    loading: false,
    info: '保存前请先进行调试',
    debugStatus: null,
    debugPassed: null
  });
  const { dataSourceId, action, businessActivityId } = props;

  const handleClick = () => {
    queryDataSourceList();
    if (action === 'edit') {
      queryDataSourceDetail({
        businessActivityId,
        datasourceId: dataSourceId
      });
    }
  };

  /**
   * @name 获取数据源详情
   */
  const queryDataSourceDetail = async value => {
    const {
      data: { data, success }
    } = await BusinessActivityService.queryDataSourceDetail({ ...value });
    if (success) {
      setState({
        dataSourceDetail: data
      });
    }
  };

  /**
   * @name 获取数据源列表
   */
  const queryDataSourceList = async () => {
    const {
      data: { data, success }
    } = await BusinessActivityService.queryAllDataSourceList({});
    if (success) {
      setState({
        dataSourceList:
          data &&
          data.map((item, k) => {
            return { label: item.datasourceName, value: item.datasourceId };
          })
      });
    }
  };

  const handleCancle = () => {
    setState({
      debugStatus: false,
      debugPassed: null,
      loading: false,
      info: '保存前请先进行调试'
    });
  };

  /**
   * @name 新增数据源
   */
  const handleSubmit = async () => {
    return await new Promise(resolve => {
      state.form.validateFields(async (err, values) => {
        if (err) {
          message.error('请检查表单必填项');
          resolve(false);
          return false;
        }

        if (!state.debugStatus || !state.debugPassed) {
          message.error('保存前请先调试成功！');
          resolve(false);
          return false;
        }

        if (action === 'edit') {
          const {
            // tslint:disable-next-line:no-shadowed-variable
            data: { success, data }
          } = await BusinessActivityService.editDataSource({
            businessActivityId,
            datasourceId: dataSourceId,
            ...values,
            sqls: values.sqls.split('\n')
          });
          if (success) {
            message.success('操作成功');
            props.onSccuess();
            resolve(true);
          }
          return;
        }

        const {
          data: { success, data }
        } = await BusinessActivityService.addDataSource({
          businessActivityId,
          datasourceId: dataSourceId,
          ...values,
          sqls: values.sqls.split('\n')
        });
        if (success) {
          message.success('操作成功');
          props.onSccuess();
          resolve(true);
        }
        resolve(false);
      });
    });
  };

  return (
    <CommonModal
      modalProps={{
        width: 960,
        title: '添加数据源',
        maskClosable: false,
        centered: true
      }}
      btnProps={{
        type: props.action === 'edit' ? 'link' : null
      }}
      btnText={props.btnText}
      onClick={() => handleClick()}
      beforeOk={handleSubmit}
      afterCancel={handleCancle}
    >
      <div>
        <CommonForm
          getForm={form => setState({ form })}
          formData={getDataSourceFormData(
            state,
            setState,
            action,
            dataSourceId
          )}
          btnProps={{
            isResetBtn: false,
            isSubmitBtn: false
          }}
          rowNum={1}
          formItemProps={{ labelCol: { span: 6 }, wrapperCol: { span: 14 } }}
        />
      </div>
    </CommonModal>
  );
};
export default AddDataSourceModal;
