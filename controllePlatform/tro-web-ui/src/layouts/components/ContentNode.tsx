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
 * @name 主体main
 */
import React, { useLayoutEffect, useState } from 'react';
import { Layout } from 'antd';
import venomBasicConfig from 'src/venom.config';
import styles from '../index.less';
const { Content } = Layout;

const ContentNode: React.FC = props => {
  // const [footerHeight, setFooterHeight] = useState(0);
  // useLayoutEffect(() => {
  //   const footerEl = document.getElementById('footer');
  //   setFooterHeight(footerEl.offsetHeight);
  // }, []);
  return (
    <div
      style={{
        // paddingTop: venomBasicConfig.fixHeader
        //   ? venomBasicConfig.headerHeight
        //   : 0,
        background: venomBasicConfig.contentBg,
        // minHeight: `calc(100% - ${footerHeight}px)`
        minHeight: `100%`,
        height: '100%'
      }}
      className="flex flex-1 of-x-hd of-y-at"
    >
      <Content
        className={
          venomBasicConfig.layout === 'header' &&
          venomBasicConfig.contentWidthMode === 'fixed'
            ? styles.wrap
            : 'flex'
        }
        style={{
          flexDirection: 'column',
          // padding: '16px',
          backgroundColor: '#fff',
          marginLeft: '8px',
          marginRight: '8px',
          marginTop: '8px',
          borderRadius: '4px 4px 0 0',
          overflow: 'scroll'
        }}
      >
        {props.children}
      </Content>
    </div>
  );
};

export default ContentNode;
