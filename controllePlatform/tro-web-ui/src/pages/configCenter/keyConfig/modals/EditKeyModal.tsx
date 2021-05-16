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

import React, { Fragment } from 'react';
import { CommonModal } from 'racc';
import styles from './../index.less';
import { Input } from 'antd';
interface Props {
  btnText?: string | React.ReactNode;
}
const EditKeyModal: React.FC<Props> = props => {
  return (
    <div style={{ marginTop: 24 }}>
      <CommonModal
        modalProps={{
          width: 400,
          title: '修改密钥',
          okText: '提交'
        }}
        btnProps={{ type: 'primary' }}
        btnText={props.btnText}
        // onClick={() => handleClick()}
      >
        <p className={styles.tip}>危险操作，密钥涉及压测平台数据准确性</p>
        <Input value={1} />
      </CommonModal>
    </div>
  );
};
export default EditKeyModal;
