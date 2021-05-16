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
 * @author Xunhuan
 */
import React, { Fragment } from 'react';
import styles from '../index.less';

import { Menu, Icon, Button } from 'antd';
import Link from 'umi/link';

interface Props {
  itemData: {
    title: string;
    path: string;
  };
  itemIndex: number;
  navTabChecked: string;
  handlerClose?: (arg: string, idx: number) => void;
}

// const TabItem = (props): React.ReactNode => {
const TabItem: React.FC<Props> = props => {
  return (
    <div
      className={
        props.itemData && props.navTabChecked === props.itemData.path
          ? styles.tabItemSelected
          : styles.tabItem
      }
    >
      <Link to={props.itemData && props.itemData.path}>
        {props.itemData && props.itemData.title}
      </Link>
      {props.itemIndex !== 0 && (
        <Button
          className={styles.tabItemClose}
          type="link"
          onClick={() => {
            props.handlerClose(
              props.itemData && props.itemData.path,
              props.itemIndex
            );
          }}
        >
          <Icon type="close" />
        </Button>
      )}
    </div>
  );
};
export default TabItem;
