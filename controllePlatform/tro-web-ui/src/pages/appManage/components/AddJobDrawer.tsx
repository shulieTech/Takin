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
import { CommonDrawer, CommonForm, CommonSelect, useStateReducer } from 'racc';
import { FormDataType } from 'racc/dist/common-form/type';
import { Input, message, Icon, Tooltip } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import AppManageService from '../service';
import copy from 'copy-to-clipboard';
import { openNotification } from 'src/common/custom-notification/CustomNotification';
interface Props {
  id?: string;
  title?: string;
  action?: string;
  state?: any;
  setState?: (value) => void;
  onSccuess?: () => void;
  detailData?: any;
  disabled?: boolean;
}

interface State {
  form: any;
  appData: any;
  jobDetail: any;
}

const AddJobDrawer: React.FC<Props> = props => {
  const { title, action, id, detailData } = props;
  const [state, setState] = useStateReducer<State>({
    form: null as WrappedFormUtils,
    appData: null,
    jobDetail: null
  });

  const { jobDetail } = state;

  const handleClick = async () => {
    if (action === 'edit') {
      queryJobDetail();
    }
  };

  const handleCopy = async value => {
    if (copy(value)) {
      message.success('复制成功');
    } else {
      message.error('复制失败');
    }
  };

  /**
   * @name 获取job详情
   */
  const queryJobDetail = async () => {
    const {
      data: { success, data }
    } = await AppManageService.queryJobDetail({ id: props.id });
    if (success) {
      setState({
        jobDetail: data
      });
    }
  };
  const getJobFormData = (): FormDataType[] => [
    {
      key: 'applicationName',
      label: '应用',
      options: {
        initialValue:
          action !== 'add'
            ? jobDetail && jobDetail.applicationName
            : detailData && detailData.applicationName,
        rules: [
          {
            required: true,
            message: '请输入应用名'
          }
        ]
      },
      node: <Input disabled={true} />
    },
    {
      key: 'configCode',
      label: (
        <span>
          配置代码
          <Tooltip
            title={() => {
              const tempValue = `
            <xml>
              <className>com.pradar.xx.TestJob</className>
              <cron>0 * * * * *</cron>
              <jobType>elastic-job</jobType>
              <jobDataType>simple</jobDataType>
              <listener>com.pradar.listener.TestListener</listener>
            </xml>
            `;
              return (
                <div>
                  <div style={{ textAlign: 'right' }}>
                    <a onClick={() => handleCopy(tempValue)}>复制</a>
                  </div>
                  {tempValue}
                </div>
              );
            }}
          >
            <Icon style={{ marginLeft: 4 }} type="question-circle" />
          </Tooltip>
        </span>
      ),
      options: {
        initialValue:
          action !== 'add' ? jobDetail && jobDetail.configCode : undefined,
        rules: [
          {
            required: true,
            message: '请输入配置代码'
          }
        ]
      },
      node: <Input.TextArea placeholder="请输入" style={{ height: 440 }} />
    },
    {
      key: 'remark',
      label: '备注',
      options: {
        initialValue:
          action !== 'add' ? jobDetail && jobDetail.remark : undefined,
        rules: [
          {
            required: false,
            message: '请输入正确备注',
            max: 200
          }
        ]
      },
      node: (
        <Input.TextArea
          placeholder="请输入（200字以内）"
          style={{ height: 67 }}
        />
      )
    }
  ];

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
         * @name 增加Job
         */
        if (action === 'add') {
          const {
            data: { success, data }
          } = await AppManageService.addJob({
            ...result,
            applicationId: id
          });
          if (success) {
            openNotification('增加Job成功', '');
            props.onSccuess();
            resolve(true);
          }
        }

        /**
         * @name 编辑Job
         */
        if (action === 'edit') {
          const {
            data: { success, data }
          } = await AppManageService.editJob({
            ...result,
            id
          });
          if (success) {
            openNotification('修改Job成功', '');
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
      btnText={title}
      drawerProps={{
        width: 650,
        title: action === 'add' ? '新增Job' : '编辑Job',
        maskClosable: false
      }}
      drawerFooterProps={{
        okText: action === 'add' ? '确认新增' : '确认编辑',
        style: { zIndex: 10 }
      }}
      btnProps={
        action === 'add'
          ? {
            disabled: props.disabled
          }
          : {
            type: 'link',
            disabled: props.disabled
          }
      }
      beforeOk={() => handleSubmit()}
      onClick={() => handleClick()}
    >
      <CommonForm
        getForm={form => setState({ form })}
        formData={getJobFormData()}
        btnProps={{
          isResetBtn: false,
          isSubmitBtn: false
        }}
        rowNum={1}
        formItemProps={{ labelCol: { span: 6 }, wrapperCol: { span: 14 } }}
      />
    </CommonDrawer>
  );
};
export default AddJobDrawer;
