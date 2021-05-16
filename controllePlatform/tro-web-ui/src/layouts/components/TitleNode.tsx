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
import React, { useState } from 'react';
import styles from '../index.less';
import venomBasicConfig from 'src/venom.config';
import Title from 'antd/lib/typography/Title';
import { Icon, Button } from 'antd';
import Link from 'umi/link';

interface Props {
  onCollapsed?: () => void;
  collapsedStatus?: boolean;
}
const TitleNode: React.FC<Props> = props => {
  let url = '/';
  if (localStorage.getItem('trowebUserResource')) {
    const data = JSON.parse(localStorage.getItem('trowebUserResource'));
    const urlString =
      Object.keys(data) && Object.keys(data).sort((a: any, b: any) => a - b)[0];
    const urls = urlString && urlString.replace(/_/, '/');
    url = urls ? `/${urls}` : '/';
  }

  return (
    <Link to={url}>
      <Title
        className={styles.logo}
        style={{
          width:
            venomBasicConfig.theme === 'dark'
              ? !props.collapsedStatus
                ? venomBasicConfig.siderWidth
                : '80px'
              : !props.collapsedStatus
              ? +venomBasicConfig.siderWidth - 1
              : '79px',

          height: venomBasicConfig.headerHeight,
          marginBottom: 0,
          background: venomBasicConfig.theme === 'dark' ? '#11BBD5' : '#fff',
          color: venomBasicConfig.theme === 'dark' ? '#fff' : '#1890ff',
          boxShadow:
            venomBasicConfig.theme === 'light' && '1px 1px 0 0 #e8e8e8',
          borderBottom: '1px solid rgba(25, 205, 232, 1)'
        }}
      >
        <div className={styles.titleName}>
          <span className={styles.logoImg}>SL</span>
          {!props.collapsedStatus && (
            <span className={styles.logoName}>全链路压测</span>
          )}
        </div>

        <Button
          type="link"
          onClick={props.onCollapsed}
          className={props.collapsedStatus ? styles.menuClose : styles.menuOpen}
        >
          <Icon
            type={props.collapsedStatus ? 'menu-unfold' : 'menu-fold'}
            style={{ color: '#fff' }}
          />
        </Button>
      </Title>
    </Link>
  );
};
export default TitleNode;
