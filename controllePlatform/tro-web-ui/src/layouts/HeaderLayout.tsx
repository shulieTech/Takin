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
 * @name 基础布局Layout
 * @author MingShined
 */
import React, { useEffect, useRef } from 'react';
import { Basic } from 'src/types';
import { Layout, ConfigProvider } from 'antd';
import HeaderNode from './components/HeaderNode';
import ContentNode from './components/ContentNode';
import FooterNode from './components/FooterNode';
declare var window: any;

interface HeaderLayoutProps extends Basic.BaseProps {}

const HeaderLayout: React.FC<HeaderLayoutProps> = props => {
  const pathname: string | any = props.location.pathname;
  const popupDom = useRef(null);
  useEffect(() => {
    handleDispatch({
      type: 'app/filterBreadCrumbs',
      payload: pathname
    });
  }, [pathname]);
  const handleDispatch = payload => {
    window.g_app._store.dispatch(payload);
  };
  const { children } = props;
  return (
    <Layout className="flex flex-1">
      <HeaderNode />
      <Layout className="flex">
        <ConfigProvider getPopupContainer={() => popupDom.current}>
          <div className="h-100p" ref={popupDom}>
            <ContentNode children={children} />
            <FooterNode />
          </div>
        </ConfigProvider>
      </Layout>
    </Layout>
  );
};

export default HeaderLayout;
