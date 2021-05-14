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
 *
 * @name 处理中
 * @author Xunhuan
 *
 */

import React, { Fragment } from 'react';
import { Icon } from 'antd';
import styles from './index.less';

interface Props {}

const ProcessComponent: React.FC<Props> = props => {
  return (
    <Fragment>
      <div className={styles.process}>
        <div className={styles.processItem}>
          <Icon type="audit" />
          <div className={styles.processTit}>消息校验中</div>
        </div>
        <div className={styles.processItem}>
          <Icon type="folder" />
          <div className={styles.processTit}>注册商业能力</div>
        </div>
        <div className={styles.processItem}>
          <Icon type="github" />
          <div className={styles.processTit}>创建GIT</div>
        </div>
      </div>
    </Fragment>
  );
};

export default ProcessComponent;
