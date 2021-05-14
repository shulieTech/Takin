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
 * @author MingShined
 */
import { Form, Icon, message, Tooltip } from 'antd';
import { CommonModal, CommonSelect, useStateReducer } from 'racc';
import React, { Fragment, useContext, useEffect, useRef } from 'react';
import { Link } from 'umi';
import { BusinessActivityDetailsContext } from '../detailsPage';
import { VerifyStatus } from '../enum';
import BusinessActivityService from '../service';
interface Props {
  id: string;
}
const FlowVerificateModal: React.FC<Props> = props => {
  const { state: rootState, setState: rootSetState } = useContext(
    BusinessActivityDetailsContext
  );
  const [state, setState] = useStateReducer({
    script: undefined,
    scriptList: [],
    loading: false
  });
  const timer = useRef<NodeJS.Timer>();
  useEffect(() => {
    if (rootState.details.verifyStatus === VerifyStatus.验证完成) {
      return;
    }
    pollStatus();
    return () => {
      clearInterval(timer.current);
      message.destroy();
    };
  }, []);
  let init = false;
  const pollStatus = () => {
    timer.current = setInterval(async () => {
      const {
        data: { data, success }
      } = await BusinessActivityService.verificateFlowStatus({
        activityId: props.id
      });
      if (success) {
        if (
          data.verifyStatus === null ||
          data.verifyStatus === VerifyStatus.未验证
        ) {
          clearInterval(timer.current);
          return;
        }
        if (data.verifyStatus === VerifyStatus.验证中 && !init) {
          init = true;
          setState({ loading: true });
          message.success('正在流量验证中，请勿重复发起', 0);
          return;
        }
        if (data.verifyStatus === VerifyStatus.验证完成) {
          clearInterval(timer.current);
          message.destroy();
          setState({ loading: false });
          rootSetState({ reload: !rootState.reload });
        }
      }
    }, 1000);
  };
  const modalTitle: React.ReactNode = (
    <div style={{ display: 'flex', alignItems: 'center' }}>
      <div
        style={{
          marginRight: 8,
          fontSize: 16,
          fontWeight: 600,
          color: '#434343'
        }}
      >
        流量验证
      </div>
      <Tooltip title="选择脚本，可向当前链路发起流量，验证流量走向与链路完整性，流量验证默认采用1并发施加流量，最多发送1000条。由于流量验证会产生业务数据，本功能建议仅在测试环境使用。">
        <Icon theme="filled" type="question-circle" />
      </Tooltip>
    </div>
  );
  const handleSubmit = () => {
    return new Promise(async resolve => {
      if (!state.script) {
        message.info('请选择脚本');
        resolve(false);
        return;
      }
      const {
        data: { success }
      } = await BusinessActivityService.verificateFlow({
        activityId: props.id,
        scriptId: state.script
      });
      if (success) {
        pollStatus();
        resolve(true);
      }
    });
  };
  const queryScriptList = async () => {
    const {
      data: { data, success }
    } = await BusinessActivityService.queryScriptList({
      current: 0,
      pageSize: 100000,
      businessActivity: props.id
    });
    if (success) {
      setState({
        scriptList: data.map(item => ({
          label: item.scriptName,
          value: item.id
        })),
        script: undefined
      });
    }
  };
  return (
    <Fragment>
      <CommonModal
        btnProps={{ style: { marginLeft: 16 }, loading: state.loading }}
        btnText={`流量验证${state.loading ? '中' : ''}`}
        modalProps={{
          title: modalTitle,
          width: 720,
          okText: '开始验证',
          destroyOnClose: true
        }}
        beforeOk={handleSubmit}
        onClick={queryScriptList}
      >
        <div
          style={{
            color: '#595959',
            fontSize: 13,
            marginBottom: 36,
            padding: '13px 16px',
            background: '#F5F5F5',
            border: '1px solid #E2E2E2'
          }}
        >
          <Icon style={{ marginRight: 4 }} type="info-circle" theme="filled" />
          流量验证需要向当前链路发起流量进行节点检测，您需要先进行脚本选择
        </div>
        <Form.Item
          labelCol={{ span: 2 }}
          wrapperCol={{ span: 19, push: 2 }}
          required={true}
          label="脚本"
        >
          <CommonSelect
            dataSource={state.scriptList}
            value={state.script}
            onChange={script => setState({ script })}
            style={{ width: 400 }}
            placeholder="请选择脚本"
          />
          <div style={{ color: '#666666', fontSize: 13 }}>
            暂无脚本？可点击前往
            <Link to="/scriptManage">
              <span
                style={{
                  color: '#11BBD5',
                  textDecoration: 'underline',
                  fontWeight: 600,
                  margin: '0 4px'
                }}
              >
                新增脚本
              </span>
            </Link>
            页面进行脚本关联
          </div>
        </Form.Item>
      </CommonModal>
    </Fragment>
  );
};
export default FlowVerificateModal;
