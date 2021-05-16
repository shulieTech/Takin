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

import { Col, Row, Statistic } from 'antd';
import React from 'react';
import Loading from 'src/common/loading';
import Link from 'umi/link';
import styles from './../index.less';
interface Props {
  data?: any;
}
const AppAndFlow: React.FC<Props> = props => {
  const { data } = props;
  if (data) {
    return (
      <Row type="flex" align="middle" style={{ marginLeft: 16 }}>
        {(data.applicationNum || data.applicationNum === 0) && (
          <Col span={12} className={styles.borderWithPadding}>
            <Link to="/appManage">
              <div className={styles.border}>
                <span className={`${styles.title}`}>接入应用</span>
                <p className={styles.number}>
                  <Statistic value={data.applicationNum} precision={0} />
                </p>
              </div>
            </Link>
          </Col>
        )}
        {(data.accessErrorNum || data.accessErrorNum === 0) && (
          <Col span={12} className={styles.borderWithPadding}>
            <Link to="/appManage?accessStatus=3">
              <div className={styles.border}>
                <span className={`${styles.title}`}>异常应用</span>
                <p className={styles.number}>
                  <Statistic
                    valueStyle={{ color: '#FE7D61' }}
                    value={data.accessErrorNum}
                    precision={0}
                  />
                </p>
              </div>
            </Link>
          </Col>
        )}
        {/* {(data.systemProcessNum || data.systemProcessNum === 0) && (
          <Col span={6} className={styles.borderWithPadding}>
            <Link to="/systemFlow">
              <div className={styles.border}>
                <span className={`${styles.title}`}>系统流程</span>
                <p className={styles.number}>
                  <Statistic value={data.systemProcessNum} precision={0} />
                </p>
              </div>
            </Link>
          </Col>
        )}
        {(data.changedProcessNum || data.changedProcessNum === 0) && (
          <Col span={6} className={styles.borderWithPadding}>
            <Link to="/systemFlow?ischange=1">
              <div className={styles.border}>
                <span className={`${styles.title}`}>变更流程</span>
                <p className={styles.number}>
                  <Statistic
                    valueStyle={{ color: '#FE7D61' }}
                    value={data.changedProcessNum}
                    precision={0}
                  />
                </p>
              </div>
            </Link>
          </Col>
        )} */}
      </Row>
    );
  }
  return <Loading />;
};
export default AppAndFlow;
