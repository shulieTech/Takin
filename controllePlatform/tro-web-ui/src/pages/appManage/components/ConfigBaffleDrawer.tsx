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

import { Icon, Input, message, Tooltip } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import copy from 'copy-to-clipboard';
import { CommonDrawer, CommonForm, useStateReducer } from 'racc';
import { FormDataType } from 'racc/dist/common-form/type';
import React from 'react';
import { openNotification } from 'src/common/custom-notification/CustomNotification';
import AppManageService from '../service';
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
  baffleConfigDetail: any;
  configValue: String;
}

const ConfigBaffleDrawer: React.FC<Props> = props => {
  const { title, action, id, detailData } = props;
  const [state, setState] = useStateReducer<State>({
    form: null as WrappedFormUtils,
    baffleConfigDetail: null,
    configValue: null
  });

  const { baffleConfigDetail } = state;

  const handleClick = async () => {
    getBaffleConfig();
    if (action === 'edit') {
      queryBaffleDetail();
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
   * @name 获取配置挡板详情
   */
  const queryBaffleDetail = async () => {
    const {
      data: { success, data }
    } = await AppManageService.queryBaffleConfigDetail({ id: props.id });
    if (success) {
      setState({
        baffleConfigDetail: data
      });
    }
  };

  /**
   * @name 获取挡板模板
   */
  const getBaffleConfig = async () => {
    const {
      data: { success, data }
    } = await AppManageService.queryConfig({
      configCode: 'PRADAR_GUARD_TEMPLATE'
    });
    if (success) {
      setState({
        configValue: data
      });
    }
  };
  const getConfigBattleFormData = (): FormDataType[] => [
    {
      key: 'applicationName',
      label: '应用',
      options: {
        initialValue:
          action !== 'add'
            ? baffleConfigDetail && baffleConfigDetail.applicationName
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
      key: 'methodInfo',
      label: '类名#方法名',

      options: {
        initialValue:
          action !== 'add'
            ? baffleConfigDetail && baffleConfigDetail.methodInfo
            : undefined,
        rules: [
          {
            required: true,
            message: '请输入类名#方法名'
          }
        ]
      },
      node: <Input.TextArea placeholder="请输入" style={{ height: 67 }} />
    },
    {
      key: 'groovy',
      label: (
        <Tooltip
          title={() => {
            return (
              <div>
                <div style={{ textAlign: 'right' }}>
                  <a onClick={() => handleCopy(state.configValue)}>复制</a>
                </div>
                <div style={{ width: 250, height: 400, overflow: 'scroll' }}>
                  {state.configValue}
                </div>
              </div>
            );
          }}
        >
          返回结果java代码块
          <Icon style={{ marginLeft: 4 }} type="question-circle" />
        </Tooltip>
      ),
      options: {
        initialValue:
          action !== 'add'
            ? baffleConfigDetail && baffleConfigDetail.groovy
            : undefined,
        rules: [
          {
            required: false,
            message: '请输入groovy脚本'
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
          action !== 'add'
            ? baffleConfigDetail && baffleConfigDetail.remark
            : undefined,
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
         * @name 增加挡板配置
         */
        if (action === 'add') {
          const {
            data: { success, data }
          } = await AppManageService.addBaffleConfig({
            ...result,
            applicationId: id
          });
          if (success) {
            openNotification('增加挡板成功');
            props.onSccuess();
            resolve(true);
          }
        }

        /**
         * @name 编辑挡板配置
         */
        if (action === 'edit') {
          const {
            data: { success, data }
          } = await AppManageService.editBaffleConfig({
            ...result,
            id
          });
          if (success) {
            openNotification('修改挡板成功');
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
        title: action === 'add' ? '新增挡板配置' : '编辑挡板配置',
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
        formData={getConfigBattleFormData()}
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
export default ConfigBaffleDrawer;
