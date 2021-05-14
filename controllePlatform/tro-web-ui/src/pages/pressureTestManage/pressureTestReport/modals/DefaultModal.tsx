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

import React, { Fragment, useEffect } from 'react';
import { CommonModal, CommonTable, useStateReducer } from 'racc';
import { ColumnProps } from 'antd/lib/table';
import { customColumnProps } from 'src/components/custom-table/utils';
import PressureTestReportService from '../service';
import CustomTable from 'src/components/custom-table';
import { Typography, Tag, Tooltip, message, Icon, Row, Badge } from 'antd';
import copy from 'copy-to-clipboard';
import styles from './../index.less';
import CustomStatistic from 'src/components/custom-statistic/CustomStatistic';
import Header from '../components/Header';

interface Props {
  btnText: string | React.ReactNode;
  content: string | React.ReactNode;
}

const DefaultModal: React.FC<Props> = props => {
  return (
    <CommonModal
      modalProps={{
        footer: null
      }}
      btnProps={{ type: 'link' }}
      btnText={props.btnText}
    >
      <div>{props.content}</div>
    </CommonModal>
  );
};
export default DefaultModal;
