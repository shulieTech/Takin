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

import { Col, Row } from 'antd';
import { CommonSelect, useStateReducer } from 'racc';
import React, { useEffect } from 'react';
interface Props {
  value?: any;
  onChange?: (value: any) => void;
  businessActivityList: any;
  businessFlowList: any;
}
interface State {
  relatedObj: { relatedType: string; relatedId: string };
}
const TwoSelect: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    relatedObj: {
      relatedType: undefined,
      relatedId: undefined
    }
  });

  useEffect(() => {
    if (!props.value) {
      return;
    }
    setState({
      relatedObj: props.value
    });
  }, [props.value]);

  const handleChange = (key, value) => {
    if (key === 'relatedType' && state.relatedObj.relatedType !== value) {
      setState({ relatedObj: { relatedType: value, relatedId: undefined } });
      if (props.onChange) {
        props.onChange({ relatedType: value, relatedId: undefined });
      }
      return;
    }
    setState({ relatedObj: { ...state.relatedObj, [key]: value } });
    if (props.onChange) {
      props.onChange({ ...state.relatedObj, [key]: value });
    }
  };

  const relatedTypeList = [{ label: '业务活动', value: '1' }];

  return (
    <Row type="flex">
      <Col span={9}>
        <CommonSelect
          allowClear={false}
          value={state.relatedObj.relatedType}
          placeholder="请选择类型"
          dataSource={relatedTypeList}
          onChange={value => handleChange('relatedType', value)}
          showSearch={true}
        />
      </Col>
      <Col span={1} />
      <Col span={9}>
        <CommonSelect
          allowClear={false}
          // style={{ width: 200 }}
          placeholder={
            state.relatedObj.relatedType === undefined
              ? '请先选择类型'
              : state.relatedObj.relatedType === '1'
              ? '请选择业务活动'
              : '请选择业务流程'
          }
          value={state.relatedObj.relatedId}
          dataSource={
            state.relatedObj.relatedType === undefined
              ? []
              : state.relatedObj.relatedType === '1'
              ? props.businessActivityList
              : props.businessFlowList
          }
          onChange={value => handleChange('relatedId', value)}
          showSearch={true}
          filterOption={(input, option) =>
            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >=
            0
          }
          dropdownMatchSelectWidth={false}
          onRender={item => (
            <CommonSelect.Option
              key={item.value}
              value={item.value}
              style={{ fontSize: '12px' }}
            >
              {item.label}
            </CommonSelect.Option>
          )}
        />
      </Col>
    </Row>
  );
};
export default TwoSelect;
