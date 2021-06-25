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
import React, { useContext } from 'react';
import { BusinessActivityDetailsContext } from '../detailsPage';
import styles from '../index.less';
import SiderBasicInfo from './SiderBasicInfo';
import SiderCollectInfo from './SiderCollectInfo';
interface Props {}
const SiderNode: React.FC<Props> = props => {
  const { state, setState } = useContext(BusinessActivityDetailsContext);
  const isShow = state.details.topology.exceptions.length;
  return (
    <div
      style={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        boxShadow: '0px 0px 12px 0px rgba(177, 192, 192, 0.45)'
      }}
    >
      <div className={styles.siderBg} style={{ marginBottom: isShow && 8 }}>
        <SiderCollectInfo />
      </div>
      <div className={styles.siderBg} style={{ flex: 1, padding: 16 }}>
        <SiderBasicInfo />
      </div>
    </div>
  );
};
export default SiderNode;
