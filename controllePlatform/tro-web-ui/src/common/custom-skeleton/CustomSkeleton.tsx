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
import { Skeleton } from 'antd';
interface Props {}
const CustomSkeleton: React.FC<Props> = props => {
  return (
    <BasePageLayout>
      <Skeleton active />
    </BasePageLayout>
  );
};
export default CustomSkeleton;
