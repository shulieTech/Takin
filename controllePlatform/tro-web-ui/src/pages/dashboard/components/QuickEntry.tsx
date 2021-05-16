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
import styles from './../index.less';
import { Tooltip, Icon, Row, Statistic, Col, Button } from 'antd';
import Link from 'umi/link';
import Loading from 'src/common/loading';
interface Props {
  data?: any;
}
const QuickEntry: React.FC<Props> = props => {
  const { data } = props;
  if (data) {
    return (
      <div className={styles.border}>
        <Row type="flex" align="middle" justify="space-between">
          <Col>
            <span className={styles.blueline} />
            <span className={`${styles.boldTitle}`}>快捷入口</span>
          </Col>
        </Row>
        <Row type="flex" className={styles.entryWrap}>
          {data.length > 0 &&
            data.map((item, key) => {
              return (
                <Col key={key} span={12} className={styles.iconWrap}>
                  <Link to={item.urlAddress}>
                    <img
                      className={styles.icon}
                      src={require(`./../../../assets/quick_icon${item.id}.png`)}
                    />
                    <p className={styles.iconText}>{item.quickName}</p>
                  </Link>
                </Col>
              );
            })}
        </Row>
      </div>
    );
  }
  return <Loading />;
};
export default QuickEntry;
