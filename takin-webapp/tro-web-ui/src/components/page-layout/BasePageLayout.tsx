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
 * @name 全局路由页组件
 * @author Xunhuan
 */
import React, { Component } from 'react';
import { Affix, Row, Col } from 'antd';
import { connect } from 'dva';
import styles from './index.less';

interface BasePageLayoutProps {
  extra?: React.ReactNode | any;
  extraPosition?: string | any; // default[ 1=bottom ],  [2= top]
  title?: string | any;
}

@connect(({ app }) => ({ ...app }))
export default class BasePageLayout extends Component<BasePageLayoutProps> {
  render() {
    const { children, extra, title, extraPosition } = this.props;
    return (
      <div className={styles.baseLayConent}>
        <Affix
          offsetTop={64}
          target={() => document.getElementById('contentLayout')}
        >
          <div className="pd-r1x">
            <h1 className="ft-20 mg-b0">{title}</h1>
            {(extraPosition === 'top' || extraPosition === '2') && (
              <Row type="flex" align="middle" justify="end">
                <Col>{extra}</Col>
              </Row>
            )}
          </div>
        </Affix>

        <div className="of-x-hd ofy-at pd-t3x pd-b3x">{children}</div>
        {(extraPosition === 'bottom' || !extraPosition) && (
          <div className={styles.baseLayFoot}>
            <div className={styles.baseLayFootControl}>
              <Row type="flex" align="middle" justify="center">
                <Col>{extra}</Col>
              </Row>
            </div>
          </div>
        )}
      </div>
    );
  }
}
