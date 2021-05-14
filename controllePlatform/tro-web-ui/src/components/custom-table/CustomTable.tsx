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
import React, { Fragment } from 'react';
import { CommonTable } from 'racc';
import { CommonTableProps } from 'racc/dist/common-table/CommonTable';
import styles from './index.less';
interface Props extends CommonTableProps {}
const CustomTable: React.FC<Props> = props => {
  return (
    <CommonTable
      bordered={false}
      size="small"
      {...props}
      className={styles.customTable}
    />
  );
};
export default CustomTable;
