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
import { Row, Input, Icon } from 'antd';
import { useStateReducer } from 'racc';
interface Props {
  value?: any;
  onChange?: (value: any) => void;
  action?: string;
}
interface State {
  list: string[];
}
const UrlAddress: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    list: []
  });

  useEffect(() => {
    if (!props.value) {
      return;
    }
    setState({
      list: props.value
    });
  }, [props.value]);

  const handleChange = (type, value, k) => {
    if (type === 'change') {
      state.list.splice(k, 1, value);
    } else if (type === 'plus') {
      state.list.push('');
    } else {
      state.list.splice(k, 1);
    }

    if (props.onChange) {
      props.onChange(state.list);
    }
  };

  return state.list.map((item, k) => {
    return (
      <Row key={k} style={{ width: '120%' }}>
        <Input
          style={{ width: '84%' }}
          placeholder="请输入接口地址"
          value={state.list[k]}
          onChange={e => handleChange('change', e.target.value, k)}
        />
        {k <= state.list.length - 1 && state.list.length !== 1 && (
          <Icon
            type="minus-circle"
            style={{ color: '#11BBD5', marginLeft: 5 }}
            onClick={() => handleChange('minus', '', k)}
          />
        )}
        {props.action === 'add' && k === state.list.length - 1 && (
          <Icon
            type="plus-circle"
            style={{ color: '#11BBD5', marginLeft: 5 }}
            onClick={() => handleChange('plus', '', k)}
          />
        )}
      </Row>
    );
  });
};
export default UrlAddress;
