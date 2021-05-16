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
import { BasePageLayout } from 'src/components/page-layout';

import { useStateReducer } from 'racc';

import CustomSkeleton from 'src/common/custom-skeleton';
import PressureMeasurementRecordService from './service';
import ReportDetailHeader from './components/ReportDetailHeader';
import ReportDetailTabs from './components/ReportDetailTabs';

interface Props {
  location?: { query?: any };
}
const DomainDetail: React.FC<Props> = props => {
  const [state, setState] = useStateReducer({
    detailData: {} as any
  });

  const { location } = props;
  const { query } = location;
  const { id } = query;

  useEffect(() => {
    // queryReportDetail(id);
  }, []);

  /**
   * @name 获取压测报告详情
   */
  const queryReportDetail = async value => {
    const {
      data: { data, success }
    } = await PressureMeasurementRecordService.queryReportDetail({
      id: value
    });
    if (success) {
      setState({
        detailData: data
      });
    }
  };
  return (
    <Fragment>
      {/* {JSON.stringify(state.detailData) !== '{}' ? ( */}
      <BasePageLayout>
        <ReportDetailHeader detailData={state.detailData} />
        <ReportDetailTabs detailData={state.detailData} />
      </BasePageLayout>
      {/* ) : (
        <CustomSkeleton />
      )} */}
    </Fragment>
  );
};
export default DomainDetail;
