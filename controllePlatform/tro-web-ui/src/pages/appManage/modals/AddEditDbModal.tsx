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
import { Icon, Input, InputNumber, message, Tooltip } from 'antd';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import { connect } from 'dva';
import { CommonForm, CommonModal, CommonSelect, useStateReducer } from 'racc';
import { FormDataType } from 'racc/dist/common-form/type';
import React from 'react';
import CustomFormCard from 'src/components/custom-form-card';
import { CommonModelState } from 'src/models/common';
import copy from 'copy-to-clipboard';
import { DbBean, ShadowConsumerBean } from '../enum';
import AppManageService from '../service';

interface Props extends CommonModelState {
  id?: string;
  btnText: string;
  onSuccess: () => void;
  detailData?: any;
  applicationId: string;
}
const AddEditDbModal: React.FC<Props> = props => {
  const { detailData } = props;
  const [state, setState] = useStateReducer({
    form: null as WrappedFormUtils,
    details: {},
    dbType: null,
    dsType: null,
    cacheTempValue: null
  });
  const text = props.id ? '编辑影子库表' : '新增影子库表';
  const getDetails = async () => {
    queryCacheTemp();
    if (!props.id) {
      return;
    }
    const {
      data: { data, success }
    } = await AppManageService.queryDbTableDetail({ id: props.id });
    if (success) {
      setState({
        details: data,
        dbType: data.dbType,
        dsType: data.dsType
      });
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
   * @name 获取缓存模板
   */
  const queryCacheTemp = async () => {
    const {
      data: { success, data }
    } = await AppManageService.queryConfig({
      configCode: 'SHADOW_SERVER'
    });
    if (success) {
      setState({
        cacheTempValue: data
      });
    }
  };

  const handleCancle = () => {
    setState({
      form: null as WrappedFormUtils,
      details: {},
      dbType: null,
      dsType: null
    });
  };

  /**
   * @name 切换方案类型
   */
  const handleChange = async value => {
    setState({
      dsType: value
    });
  };
  /**
   * @name 切换类型
   */
  const handleChangeDbType = async value => {
    state.form.setFieldsValue({
      dsType: null,
      dbType: value
    });
    setState({
      dbType: value,
      dsType: null
      //   dbConfig: undefined
    });
  };

  const getBaseInfoFormData = (): FormDataType[] => {
    return [
      {
        key: DbBean.应用,
        label: '应用',
        options: {
          initialValue: detailData && detailData.applicationName,
          rules: [
            {
              required: true,
              message: '请选择应用'
            }
          ]
        },
        node: <Input disabled={true} />
      },
      {
        key: DbBean.类型,
        label: '类型',
        options: {
          initialValue: state.details[DbBean.类型],
          rules: [
            {
              required: true,
              message: '请选择类型'
            }
          ]
        },
        node: (
          <CommonSelect
            placeholder="请选择类型"
            disabled={props.id ? true : false}
            dataSource={[
              { label: '数据库', value: 0 },
              { label: '缓存(redis)', value: 1 }
            ]}
            onChange={handleChangeDbType}
            onRender={item => (
              <CommonSelect.Option key={item.value} value={item.value}>
                {item.label}
              </CommonSelect.Option>
            )}
          />
        )
      }
    ];
  };

  const getBusinessFormData = (): FormDataType[] => {
    return [
      {
        key: DbBean.业务数据源地址,
        label: '业务数据源地址',
        options: {
          initialValue: state.details[DbBean.业务数据源地址],
          rules: [
            {
              required: true,
              message: '请输入业务数据源地址'
            }
          ]
        },
        node: <Input placeholder="请输入业务数据源地址" />
      }
    ];
  };

  const getUserNameFormData = (): FormDataType[] => {
    return [
      {
        key: DbBean.用户名,
        label: '用户名',
        options: {
          initialValue: state.details[DbBean.用户名],
          rules: [
            {
              required: true,
              message: '请输入用户名'
            }
          ]
        },
        node: <Input placeholder="请输入用户名" />
      }
    ];
  };

  const getSessionFormData = (): FormDataType[] => {
    return [
      {
        key: DbBean.方案类型,
        label: '方案类型',
        options: {
          initialValue: state.details[DbBean.方案类型],
          rules: [
            {
              required: true,
              message: '请选择方案类型'
            }
          ]
        },
        node: (
          <CommonSelect
            placeholder="请选择方案类型"
            disabled={props.id ? true : false}
            dataSource={[{ label: '影子server', value: 2 }]}
            onRender={item => (
              <CommonSelect.Option key={item.value} value={item.value}>
                {item.label}
              </CommonSelect.Option>
            )}
          />
        )
      },
      {
        key: DbBean.配置代码,
        label: (
          <span>
            配置代码
            <Tooltip
              trigger="click"
              title={() => {
                return (
                  <div>
                    <div style={{ textAlign: 'right' }}>
                      <a onClick={() => handleCopy(state.cacheTempValue)}>
                        复制
                      </a>
                    </div>
                    <div
                      style={{
                        maxHeight: 400,
                        minHeight: 200,
                        overflow: 'scroll'
                      }}
                    >
                      {state.cacheTempValue}
                    </div>
                  </div>
                );
              }}
            >
              <Icon style={{ marginLeft: 4 }} type="question-circle" />
            </Tooltip>
          </span>
        ),
        options: {
          initialValue: state.details[DbBean.配置代码],
          rules: [
            {
              required: true,
              message: '请输入配置代码'
            }
          ]
        },
        node: (
          <Input.TextArea
            placeholder="请输入配置代码"
            style={{ height: 350 }}
          />
        )
      }
    ];
  };

  const getDbDetailFormData = (): FormDataType[] => {
    return [
      {
        key: DbBean.方案类型,
        label: '方案类型',
        options: {
          initialValue: state.details[DbBean.方案类型],
          rules: [
            {
              required: true,
              message: '请选择方案类型'
            }
          ]
        },
        node: (
          <CommonSelect
            placeholder="请选择方案类型"
            disabled={props.id ? true : false}
            dataSource={[
              { label: '影子库', value: 0 },
              { label: '影子表', value: 1 }
            ]}
            onChange={handleChange}
            onRender={item => (
              <CommonSelect.Option key={item.value} value={item.value}>
                {item.label}
              </CommonSelect.Option>
            )}
          />
        )
      }
    ];
  };

  const getBaseFormData = (): FormDataType[] => {
    return [
      {
        key: DbBean.数据源地址,
        label: '数据源地址',
        options: {
          initialValue: state.details[DbBean.数据源地址],
          rules: [
            {
              required: true,
              message: '请输入数据源地址'
            }
          ]
        },
        node: <Input placeholder="请输入数据源地址" />
      },
      {
        key: DbBean.数据源用户名,
        label: '用户名',
        options: {
          initialValue: state.details[DbBean.数据源用户名],
          rules: [
            {
              required: true,
              message: '请输入用户名'
            }
          ]
        },
        node: <Input placeholder="请输入用户名" />
      },
      {
        key: DbBean.密码,
        label: '密码',
        options: {
          //   initialValue: state.details[DbBean.密码],
          rules: [
            {
              required: true,
              message: '请输入密码'
            }
          ]
        },
        node: <Input placeholder="请输入密码" type="password" />
      },
      {
        key: DbBean.minldle,
        label: 'minldle',
        options: {
          initialValue: state.details[DbBean.minldle],
          rules: [
            {
              required: false,
              message: '请输入minldle'
            }
          ]
        },
        node: (
          <InputNumber
            placeholder="建议值:1~100,如果不填,将以业务设置值为准。"
            min={1}
            max={100}
            precision={0}
          />
        )
      },
      {
        key: DbBean.maxActive,
        label: 'maxActive',
        options: {
          initialValue: state.details[DbBean.maxActive],
          rules: [
            {
              required: false,
              message: '请输入maxActive'
            }
          ]
        },
        node: (
          <InputNumber
            placeholder="建议值:1~100,如果不填,将以业务设置值为准。"
            min={1}
            max={100}
            precision={0}
          />
        )
      }
    ];
  };

  const getTableFormData = (): FormDataType[] => {
    return [
      {
        key: DbBean.配置代码,
        label: '表名称',
        options: {
          initialValue: state.details[DbBean.配置代码],
          rules: [
            {
              required: true,
              message: '请输入表名称'
            }
          ]
        },
        node: (
          <Input.TextArea
            placeholder="请输入表名称，以逗号隔开"
            style={{ height: 300 }}
          />
        )
      }
    ];
  };
  const handleSubmit = () => {
    return new Promise(async resolve => {
      state.form.validateFields(async (err, values) => {
        if (err) {
          message.info('请检查表单必填项');
          resolve(false);
          return;
        }

        const result = {
          applicationId: props.applicationId,
          ...values
        };

        const ajaxEvent = props.id
          ? AppManageService.editDbTable({
            ...state.details,
            ...result
          })
          : AppManageService.addDbTable(result);
        const {
          data: { success }
        } = await ajaxEvent;
        if (success) {
          message.success(`${text}成功`);
          resolve(true);
          props.onSuccess();
          return;
        }
        resolve(false);
      });
    });
  };

  let dataSource = [];

  // dbType :0 数据库 1 缓存
  // dsType 0:影子库 1：影子表
  if (state.dbType === null || state.dbType === undefined) {
    dataSource = [
      {
        title: '基本信息',
        formData: getBaseInfoFormData(),
        span: 24
      }
    ];
  }

  if (state.dbType === 0) {
    dataSource = [
      {
        title: '基本信息',
        formData: getBaseInfoFormData().concat(getDbDetailFormData()),
        span: 24
      },
      {
        title: '业务源数据',
        formData: getBusinessFormData(),
        span: 24
      }
    ];
    if (state.dsType === 0) {
      dataSource = [
        {
          title: '基本信息',
          formData: getBaseInfoFormData().concat(getDbDetailFormData()),
          span: 24
        },
        {
          title: '业务源数据',
          formData: getBusinessFormData().concat(getUserNameFormData()),
          span: 24
        },
        {
          title: '影子库表信息',
          formData: getBaseFormData(),
          span: 24
        }
      ];
    }
    if (state.dsType === 1) {
      dataSource = [
        {
          title: '基本信息',
          formData: getBaseInfoFormData().concat(getDbDetailFormData()),
          span: 24
        },
        {
          title: '业务源数据',
          formData: getBusinessFormData(),
          span: 24
        },
        {
          title: '影子库表信息',
          formData: getTableFormData(),
          span: 24
        }
      ];
    }
  }

  if (state.dbType === 1) {
    dataSource = [
      {
        title: '基本信息',
        formData: getBaseInfoFormData().concat(getSessionFormData()),
        span: 24
      }
    ];
  }

  return (
    <CommonModal
      beforeOk={handleSubmit}
      modalProps={{ title: text, width: 560, destroyOnClose: true }}
      btnText={props.btnText}
      btnProps={{ type: props.id ? 'link' : 'primary' }}
      onClick={getDetails}
      afterCancel={handleCancle}
    >
      <CustomFormCard
        getForm={form => setState({ form })}
        dataSource={dataSource}
        commonFormProps={{
          rowNum: 1,
          formItemProps: {
            labelCol: { span: 6 },
            wrapperCol: { span: 14 }
          }
        }}
      />
    </CommonModal>
  );
};
export default connect(({ common }) => ({ ...common }))(AddEditDbModal);
