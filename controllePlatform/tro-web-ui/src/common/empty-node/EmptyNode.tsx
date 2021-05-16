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
import styles from './index.less';
interface Props {
  title?: string | React.ReactNode;
  desc?: string | React.ReactNode;
  extra?: React.ReactNode;
}
const EmptyNode: React.FC<Props> = props => {
  const { title, desc, extra } = props;
  return (
    <div className={styles.emptyNodeWrap}>
      <div className={styles.circle} />
      <div className={styles.title}>{title ? title : '暂无数据'}</div>
      {desc && <p className={styles.desc}>{desc}</p>}
      {extra}
    </div>
  );
};
export default EmptyNode;
