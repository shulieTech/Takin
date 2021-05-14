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
 * @author chuxu
 */
import React, { Fragment } from 'react';
import { Row, Col, Divider, Badge } from 'antd';

import styles from './../index.less';
import DetailHeader from 'src/common/detail-header';
interface Props {
  detailData?: any;
}
const ReportDetailHeader: React.FC<Props> = props => {
  const { detailData } = props;
  const leftData = {
    label: detailData && (
      <Badge
        className={styles.popMsg}
        color={
          detailData.status === 1
            ? '#11BBD5'
            : detailData.status === 0
            ? '#f50'
            : '-'
        }
        text={
          detailData.status === 1
            ? '压测通过'
            : detailData.status === 0
            ? '压测不通过'
            : '-'
        }
      />
    ),
    value: detailData && detailData.extName
  };
  const rightData = [
    { label: '任务名称', value: detailData && detailData.a },
    { label: '压测时长', value: detailData && detailData.a },
    { label: '压测开始时间', value: detailData && detailData.a },
    { label: '压测结束时间', value: detailData && detailData.a }
  ];
  return <DetailHeader leftWrapData={leftData} rightWrapData={rightData} />;
};
export default ReportDetailHeader;
