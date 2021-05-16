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

import { message } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import { connect } from 'dva';
import { CommonDrawer, CommonForm, useStateReducer } from 'racc';
import React from 'react';
import BusinessActivityService from 'src/pages/businessActivity/service';
import EntryRuleService from '../service';
import getEntryRuleFormData from './EntryRuleFormData';

interface Props {
  action?: string;
  id?: string | Number;
  titles?: string | React.ReactNode;
  disabled?: boolean;
  onSccuess?: () => void;
  location?: { query?: any };
  dictionaryMap?: any;
}
interface State {
  form: any;
  EntryRuleDetail: any;
  allAppList: any;
}
const AddAndEditEntryRuleDrawer: React.FC<Props> = props => {
  const { action, id, titles, dictionaryMap } = props;
  const [state, setState] = useStateReducer<State>({
    form: null as WrappedFormUtils,
    EntryRuleDetail: {} as any,
    allAppList: null
  });

  const handleClick = async () => {
    queryAllAppList();
    if (action === 'edit') {
      queryEntryRuleDetail();
    }
  };

  /**
   * @name 获取入口规则详情
   */
  const queryEntryRuleDetail = async () => {
    const {
      data: { success, data }
    } = await EntryRuleService.queryEntryRuleDetail({ id });
    if (success) {
      setState({
        EntryRuleDetail: data
      });
    }
  };

  /**
   * @name 获取应用列表数据
   */
  const queryAllAppList = async () => {
    const {
      total,
      data: { success, data }
    } = await BusinessActivityService.queryAllApp({ current: 0, pageSize: -1 });
    if (success) {
      setState({
        allAppList:
          data &&
          data.map((item, k) => {
            return { label: item.applicationName, value: item.applicationName };
          })
      });
    }
  };

  /**
   * @name 提交
   */
  const handleSubmit = async () => {
    return await new Promise(resolve => {
      state.form.validateFields(async (err, values) => {
        if (err) {
          message.error('请检查表单必填项');
          resolve(false);
          return false;
        }

        const result = {
          ...values
        };

        /**
         * @name 增加入口规则
         */
        if (action === 'add') {
          const {
            data: { success, data }
          } = await EntryRuleService.addEntryRule(result);
          if (success) {
            message.success('增加入口规则成功');
            props.onSccuess();
            resolve(true);
          }
        }

        /**
         * @name 编辑入口规则
         */
        if (action === 'edit') {
          const {
            data: { success, data }
          } = await EntryRuleService.editEntryRule({
            ...result,
            id
          });

          if (success) {
            message.success('编辑入口规则成功');
            props.onSccuess();
            resolve(true);
          }
        }
        resolve(false);
      });
    });
  };

  return (
    <CommonDrawer
      btnText={titles}
      drawerProps={{
        width: 650,
        title: action === 'add' ? '新增入口规则' : '编辑入口规则',
        maskClosable: false
      }}
      drawerFooterProps={{
        okText: action === 'add' ? '确认新增' : '确认编辑',
        style: { zIndex: 10 }
      }}
      btnProps={
        action === 'edit' && {
          type: 'link'
        }
      }
      beforeOk={() => handleSubmit()}
      onClick={() => handleClick()}
    >
      <CommonForm
        getForm={form => setState({ form })}
        formData={getEntryRuleFormData(state, action, setState, dictionaryMap)}
        btnProps={{
          isResetBtn: false,
          isSubmitBtn: false
        }}
        rowNum={1}
        formItemProps={{ labelCol: { span: 7 }, wrapperCol: { span: 13 } }}
      />
    </CommonDrawer>
  );
};
export default connect(({ common }) => ({ ...common }))(
  AddAndEditEntryRuleDrawer
);
