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
import { BasePageLayout } from 'src/components/page-layout';
import { Button } from 'antd';
import EditKeyModal from './modals/EditKeyModal';
interface Props {}
const Demo: React.FC<Props> = props => {
  return (
    <Fragment>
      <BasePageLayout title="账号密钥">
        <p>
          密钥涉及压测平台数据准确性，请谨慎修改，若密钥遗忘或误修改请及时联系数列工作人员
        </p>
        <EditKeyModal btnText="修改密钥" />
      </BasePageLayout>
    </Fragment>
  );
};
export default Demo;
