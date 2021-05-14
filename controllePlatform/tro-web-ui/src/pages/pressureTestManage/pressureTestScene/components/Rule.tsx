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
import { Row, Input, Icon, InputNumber, Col } from 'antd';
import { useStateReducer, CommonSelect } from 'racc';
import styles from './../index.less';
interface Props {
  value?: any;
  onChange?: (value: any) => void;
  dictionaryMap?: any;
}
interface State {
  ruleObj: {
    indexInfo: string | number;
    condition: string | number;
    during: number;
    times: number;
  };
}
const Rule: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    ruleObj: {
      indexInfo: undefined,
      condition: undefined,
      during: undefined,
      times: undefined
    }
  });

  const { dictionaryMap } = props;
  const { SLA_TARGER_TYPE, COMPARE_TYPE } = dictionaryMap;

  useEffect(() => {
    if (!props.value) {
      return;
    }
    setState({
      ruleObj: props.value
    });
  }, []);
  const handleChange = (key, value) => {
    setState({ ruleObj: { ...state.ruleObj, [key]: value } });
    if (props.onChange) {
      props.onChange({ ...state.ruleObj, [key]: value });
    }
  };

  return (
    <Row type="flex">
      <Col>
        <CommonSelect
          value={
            state.ruleObj.indexInfo || state.ruleObj.indexInfo === 0
              ? String(state.ruleObj.indexInfo)
              : state.ruleObj.indexInfo
          }
          placeholder="指标"
          onChange={value => {
            handleChange('indexInfo', value);
          }}
          style={{ width: 100 }}
          dataSource={SLA_TARGER_TYPE ? SLA_TARGER_TYPE : []}
        />
      </Col>
      <Col>
        <CommonSelect
          dataSource={COMPARE_TYPE ? COMPARE_TYPE : []}
          value={
            state.ruleObj.condition || state.ruleObj.condition === 0
              ? String(state.ruleObj.condition)
              : state.ruleObj.condition
          }
          style={{ width: 100, margin: '0 10px' }}
          placeholder="条件"
          onChange={value => {
            handleChange('condition', value);
          }}
        />
      </Col>
      <Col>
        <InputNumber
          value={state.ruleObj.during}
          style={{ width: 100 }}
          min={0}
          precision={0}
          onChange={value => {
            handleChange('during', value);
          }}
        />
        <span className={styles.suffix}>
          {String(state.ruleObj.indexInfo) === '0'
            ? 'ms'
            : String(state.ruleObj.indexInfo) === '1'
            ? ''
            : '%'}
        </span>
      </Col>
      <Col>
        <span className={styles.suffix}>连续出现</span>
        <InputNumber
          value={state.ruleObj.times}
          style={{ width: 100 }}
          min={0}
          precision={0}
          onChange={value => {
            handleChange('times', value);
          }}
        />
        <span className={styles.suffix}>次</span>
      </Col>
    </Row>
  );
};
export default Rule;
