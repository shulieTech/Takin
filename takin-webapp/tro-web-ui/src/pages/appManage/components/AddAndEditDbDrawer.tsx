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
import { CommonDrawer, CommonForm, useStateReducer } from 'racc';
import React from 'react';
import { openNotification } from 'src/common/custom-notification/CustomNotification';
import AppManageService from '../service';
import getDbFormDataOld from './DbFormDataOld';
interface Props {
  action?: string;
  id?: string | Number;
  titles?: string | React.ReactNode;
  onSccuess?: () => void;
  detailData?: any;
  disabled?: boolean;
}
interface State {
  form: any;
  appData: any;
  dbType: number;
  dsType: number;
  dbTableDetail: any;
  dbConfig: any;
  config: string;
  dbTempValue: string;
  cacheTempValue: string;
  esTempValue: string;
  hbaseTempValue: string;
  kafkaTempValue: string;
}
const AddAndEditDbDrawer: React.FC<Props> = props => {
  const { action, id, titles, detailData, disabled } = props;
  const [state, setState] = useStateReducer<State>({
    form: null as WrappedFormUtils,
    appData: null,
    dbType: undefined,
    dsType: null, // 方案类型
    dbTableDetail: {} as any,
    dbConfig: undefined,
    config: undefined,
    dbTempValue: null,
    cacheTempValue: null,
    esTempValue: null,
    hbaseTempValue: null,
    kafkaTempValue: null
  });

  const handleClick = async () => {
    queryDbTemp();
    queryCacheTemp();
    queryESTemp();
    queryHBaseTemp();
    queryKafkaTemp();
    if (action === 'edit') {
      queryDbTableDetail();
    }
  };

  const handleCancle = async () => {
    setState({
      dbConfig: undefined,
      dsType: null,
      dbType: undefined
    });
  };

  /**
   * @name 获取影子库表详情
   */
  const queryDbTableDetail = async () => {
    const {
      data: { success, data }
    } = await AppManageService.queryDbTableDetailOld({ id });
    if (success) {
      setState({
        dbTableDetail: data,
        dbType: data.dbType,
        dsType: data.dsType,
        dbConfig: data && data.config,
        config: data && data.config
      });
    }
  };

  /**
   * @name 获取影子库表模板
   */
  const queryDbTemp = async () => {
    const {
      data: { success, data }
    } = await AppManageService.queryConfig({
      configCode: 'SHADOW_DB'
    });
    if (success) {
      setState({
        dbTempValue: data
      });
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

  /**
   * @name 获取ES模板
   */
  const queryESTemp = async () => {
    const {
      data: { success, data }
    } = await AppManageService.queryConfig({
      configCode: 'ES_SERVER'
    });
    if (success) {
      setState({
        esTempValue: data
      });
    }
  };

  /**
   * @name 获取HBase模板
   */
  const queryHBaseTemp = async () => {
    const {
      data: { success, data }
    } = await AppManageService.queryConfig({
      configCode: 'Hbase_SERVER'
    });
    if (success) {
      setState({
        hbaseTempValue: data
      });
    }
  };

  /**
   * @name 获取Kafka模板
   */
  const queryKafkaTemp = async () => {
    const {
      data: { success, data }
    } = await AppManageService.queryConfig({
      configCode: 'KAFKA_CLUSTER'
    });
    if (success) {
      setState({
        kafkaTempValue: data
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
          ...values,
          config:
            values.dsType === 0 ||
            values.dsType === 2 ||
            values.dsType === 3 ||
            values.dsType === 4 ||
            values.dsType === 5
              ? values.dbConfig
              : values.tableConfig
        };
        delete result.dbConfig;
        delete result.tableConfig;
        /**
         * @name 增加影子库表
         */
        if (action === 'add') {
          const {
            data: { success, data }
          } = await AppManageService.addDbTableOld({
            ...result,
            applicationId: id
          });
          if (success) {
            openNotification('增加影子库表成功');
            setState({
              dsType: null,
              dbConfig: undefined,
              dbType: undefined
            });
            props.onSccuess();
            resolve(true);
          }
        }

        /**
         * @name 编辑影子库表
         */
        if (action === 'edit') {
          const {
            data: { success, data }
          } = await AppManageService.editDbTableOld({
            ...result,
            id
          });
          if (success) {
            openNotification('修改影子库表成功');
            setState({
              dsType: null,
              dbConfig: undefined,
              dbType: undefined
            });
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
        title: '影子库表配置',
        maskClosable: false
      }}
      drawerFooterProps={{
        okText: '确认配置',
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
      afterCancel={() => handleCancle()}
    >
      <CommonForm
        getForm={form => setState({ form })}
        formData={getDbFormDataOld(state, action, setState, detailData)}
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
export default AddAndEditDbDrawer;
