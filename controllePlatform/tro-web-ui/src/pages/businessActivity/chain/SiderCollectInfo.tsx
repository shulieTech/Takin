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
import { Col, Icon, Popover, Row } from 'antd';
import React, { Fragment } from 'react';
import styles from '../index.less';
import ErrorList from './ErrorList';
interface Props {}
const SiderCollectInfo: React.FC<Props> = props => {
  return (
    <Fragment>
      <ErrorList />
    </Fragment>
  );
};
export default SiderCollectInfo;

export const PopoverCard: React.FC<{
  title: string | React.ReactNode;
  content: React.ReactNode;
  overlayStyle?: React.CSSProperties;
}> = props => {
  return (
    <Popover
      trigger="click"
      placement="rightTop"
      content={props.content}
      title={props.title}
      overlayStyle={props.overlayStyle}
      overlayClassName={styles.popover}
    >
      <Row
        align="middle"
        type="flex"
        justify="space-between"
        className="pd-2x pointer"
      >
        <Col>{props.children}</Col>
        <Col>
          <Icon style={{ color: '#D9D9D9' }} type="caret-right" />
        </Col>
      </Row>
    </Popover>
  );
};
