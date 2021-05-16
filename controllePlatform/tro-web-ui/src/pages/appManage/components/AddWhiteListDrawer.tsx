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

import { Input, message } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import { CommonDrawer, CommonForm, CommonSelect, useStateReducer } from 'racc';
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
  details?: any;
}

interface State {
  form: any;
  whiteListDetail: any;
}

const AddWhiteListDrawer: React.FC<Props> = props => {
  const { title, action, id, detailData } = props;
  const [state, setState] = useStateReducer<State>({
    form: null as WrappedFormUtils,
    whiteListDetail: null
  });

  const { whiteListDetail } = state;

  const handleClick = async () => {
    if (action === 'edit') {
      // queryWhiteListDetail();
      setState({ whiteListDetail: props.details });
    }
  };

  /**
   * @name 获取白名单详情
   */
  const queryWhiteListDetail = async () => {
    const {
      data: { success, data }
    } = await AppManageService.queryWhiteListDetail({ id: props.id });
    if (success) {
      setState({
        whiteListDetail: data
      });
    }
  };

  const handleEnterList = e => {
    // console.log(e.target.value.split('\n'));
  };
  const handleChangeList = e => {
    // console.log(e.target.value);
  };
  const getWhiteListFormData = (): FormDataType[] => [
    {
      key: 'applicationName',
      label: '应用',
      options: {
        initialValue: detailData && detailData.applicationName,
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
      key: 'interfaceType',
      label: '白名单类型',
      options: {
        initialValue:
          action !== 'add'
            ? whiteListDetail && whiteListDetail.interfaceType
            : undefined,
        rules: [
          {
            required: true,
            message: '请选择白名单类型'
          }
        ]
      },
      node: (
        <CommonSelect
          dataSource={[
            { label: 'HTTP', value: 1 },
            { label: 'DUBBO', value: 2 },
            // { label: 'RABBITMQ', value: 3 }
          ]}
          onRender={item => (
            <CommonSelect.Option key={item.value} value={item.value}>
              {item.label}
            </CommonSelect.Option>
          )}
        />
      )
    },
    {
      key: 'interfaceList',
      label: '接口地址',
      options: {
        initialValue:
          action !== 'add'
            ? whiteListDetail && whiteListDetail.interfaceName
            : // : undefined,
              [''],
        rules: [
          {
            required: true,
            message: '请至少输入一个接口地址'
          }
        ]
      },
      // node: <UrlAddress action={action} />
      node:
        action === 'add' ? (
          <Input.TextArea
            placeholder="输入接口地址，多个接口换行区分"
            style={{ height: 260 }}
            onPressEnter={e => handleEnterList(e)}
            onChange={e => handleChangeList(e)}
          />
        ) : (
          <Input placeholder="输入接口地址" />
        )
      // node: (
      //    <Input.TextArea
      //      placeholder="输入接口地址，多个接口换行区分"
      //      style={{ height: 260 }}
      //      onPressEnter={e => handleEnterList(e)}
      //      onChange={e => handleChangeList(e)}
      //    />
      //   <div className={styles.tagWrap}>
      //     <Select
      //       placeholder="输入接口地址，多个接口换行区分"
      //       mode="tags"
      //       // open={false}
      //       // onSelect={console.log('1')}
      //     />
      //   </div>
      // )
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
          ...values,
          interfaceList:
            values.interfaceList && values.interfaceList.split('\n'),
          interfaceName: values.interfaceList
        };

        /**
         * @name 增加白名单
         */
        if (action === 'add') {
          const {
            data: { success, data }
          } = await AppManageService.addWhiteList({
            ...result,
            applicationId: id
          });
          if (success) {
            openNotification('增加白名单成功', '');
            props.onSccuess();
            resolve(true);
          }
        }

        /**
         * @name 编辑白名单
         */
        if (action === 'edit') {
          const {
            data: { success, data }
          } = await AppManageService.editWhiteList({
            ...result,
            dbId: id
          });
          if (success) {
            openNotification('修改白名单成功', '');
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
        title: action === 'add' ? '新增白名单' : '编辑白名单',
        maskClosable: false
      }}
      drawerFooterProps={{
        okText: action === 'add' ? '确认新增' : '确认编辑',
        style: { zIndex: 10 }
      }}
      btnProps={
        action === 'add'
          ? {
            disabled: props.disabled,
            type: 'default'
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
        formData={getWhiteListFormData()}
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
export default AddWhiteListDrawer;
