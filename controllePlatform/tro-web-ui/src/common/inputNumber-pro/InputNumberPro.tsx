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
import React, { Fragment } from 'react';
import InputNumber, { InputNumberProps } from 'antd/lib/input-number';
import styles from './index.less';
interface InputNumberProProps extends InputNumberProps {
  addonAfter?: string | React.ReactNode;
  addonBefore?: string | React.ReactNode;
}
const InputNumberPro: React.FC<InputNumberProProps> = props => {
  return (
    <Fragment>
      {props.addonBefore && <span className={styles.addon}>{props.addonBefore}</span>}
      <InputNumber {...props} />
      {props.addonAfter && <span className={styles.addon}>{props.addonAfter}</span>}
    </Fragment>
  );
};
export default InputNumberPro;
