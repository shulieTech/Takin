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

import { InputNumber, Row } from 'antd';
import { CommonSelect, useStateReducer } from 'racc';
import React, { useEffect, useRef } from 'react';
interface Props {
  value?: any;
  onChange?: (value: any) => void;
  onBlur?: (value: any) => void;
  selectDisabled?: boolean;
  isReload?: boolean;
}
interface State {
  time: number;
  unit: string;
}
const TimeInputWithUnit: React.FC<Props> = props => {
  // console.log('组件里的props:props', props);
  const [state, setState] = useStateReducer<State>({
    time: null,
    unit: null
  });

  useEffect(() => {
    if (!props.value) {
      return;
    }
    setState({
      time: props.value.time,
      unit: props.value.unit
    });
  }, [props.value, props.isReload]);

  const handleChange = (key, value) => {
    if (key === 'time' && value && isNaN(+value)) {
      return;
    }
    setState({
      ...state,
      [key]: value
    });
    if (props.onChange) {
      props.onChange({ ...state, [key]: value });
    }

    if (key === 'unit' || props.onBlur) {
      props.onBlur({
        ...state,
        [key]: value
      });
    }
  };

  const handleBlur = () => {
    if (props.onBlur) {
      props.onBlur({ ...state });
      // props.onChange({ ...state });
    }
  };

  const inputRef = useRef(null);

  return (
    <Row type="flex" style={{ width: '100%' }}>
      <InputNumber
        style={{ width: 'calc(100% - 50px)', display: 'inline-block' }}
        precision={0}
        min={1}
        ref={inputRef}
        value={state.time}
        onBlur={() => handleBlur()}
        onChange={value => {
          handleChange('time', value);
        }}
      />
      <CommonSelect
        allowClear={false}
        style={{ width: 50, border: 'none', display: 'inline-block' }}
        value={state.unit}
        disabled={props.selectDisabled}
        onChange={value => handleChange('unit', value)}
        dataSource={[
          { label: '分', value: 'm' },
          { label: '秒', value: 's' }
        ]}
      />
    </Row>
  );
};
export default TimeInputWithUnit;
