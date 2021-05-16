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

import { VenomBasicConfig } from './types/venom';

interface VenomConfigBean extends VenomBasicConfig {
  isTagRouter: 'product' | 'dev';
  defaultPageProps: { title: string; path: string; query: any };
}

const venomBasicConfig: VenomConfigBean = {
  title: '全链路压测平台',
  headerHeight: 50,
  siderWidth: 160,
  contentBg: '#1D2530',
  footerBg: '#fff',
  theme: 'dark',
  layout: 'sider',
  siderMultiple: true,
  fixHeader: true,
  fixSider: true,
  headerBg: '#354153',
  contentWidthMode: 'fluid',
  headerColor: '#7d8da6'
};

export default venomBasicConfig;
