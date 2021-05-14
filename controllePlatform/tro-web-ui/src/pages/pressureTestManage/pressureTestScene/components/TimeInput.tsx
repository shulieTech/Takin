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
import { useStateReducer } from 'racc';
import styles from './../index.less';
interface Props {
  value?: any;
  onChange?: (value: any) => void;
}
interface State {
  timeObj: { h: number; m: number; s: number };
}
const TimeInput: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    timeObj: {
      h: 0,
      m: 0,
      s: 0
    }
  });

  useEffect(() => {
    if (!props.value) {
      return;
    }
    setState({
      timeObj: props.value
    });
  }, []);
  const handleChange = (key, value) => {
    setState({ timeObj: { ...state.timeObj, [key]: value } });
    if (props.onChange) {
      props.onChange({ ...state.timeObj, [key]: value });
    }
  };

  return (
    <Row type="flex">
      <Col>
        <InputNumber
          value={state.timeObj.h}
          style={{ width: 100 }}
          min={0}
          precision={0}
          onChange={value => {
            handleChange('h', value);
          }}
        />
        <span className={styles.suffix}>时</span>
      </Col>
      <Col>
        <InputNumber
          value={state.timeObj.m}
          style={{ width: 100 }}
          min={0}
          max={59}
          precision={0}
          onChange={value => {
            handleChange('m', value);
          }}
        />
        <span className={styles.suffix}>分</span>
      </Col>
      <Col>
        <InputNumber
          value={state.timeObj.s}
          style={{ width: 100 }}
          min={0}
          max={59}
          precision={0}
          onChange={value => {
            handleChange('s', value);
          }}
        />
        <span className={styles.suffix}>秒</span>
      </Col>
    </Row>
  );
};
export default TimeInput;
