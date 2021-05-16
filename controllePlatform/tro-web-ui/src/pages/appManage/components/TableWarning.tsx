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
import React, { Fragment } from 'react';
import AddAppDrawer from './AddAppDrawer';
import { Alert } from 'antd';

interface Props {
  state?: any;
  setState?: (value) => void;
}
const TableWarning: React.FC<Props> = props => {
  const { state, setState } = props;
  const { switchStatus } = state;
  let messageTxt;

  if (switchStatus === 'OPENING' || switchStatus === 'CLOSING') {
    messageTxt = '压测总开关操作中，暂时不能对应用配置进行修改';
  } else {
    messageTxt = '压测总开关关闭，所有应用配置不生效，如有需要请联系管理员配置';
  }

  return (
    <Fragment>
      <Alert
        type="warning"
        message={<p style={{ color: '#646676' }}>{messageTxt}</p>}
        showIcon
        style={{ marginTop: 16, marginBottom: 8 }}
      />
    </Fragment>
  );
};
export default TableWarning;
