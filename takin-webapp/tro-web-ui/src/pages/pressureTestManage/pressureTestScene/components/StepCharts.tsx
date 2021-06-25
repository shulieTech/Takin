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
import ReactEcharts from 'echarts-for-react';
interface Props {
  chartsInfo: any;
}
const StepCharts: React.FC<Props> = props => {
  const { chartsInfo } = props;

  return (
    <Fragment>
      <ReactEcharts
        style={{ width: 500, height: 190 }}
        option={{
          color: ['#FFB64A'],
          tooltip: {
            trigger: 'axis'
          },
          xAxis: [
            {
              type: 'value',
              splitNumber: 10,
              boundaryGap: false
            }
          ],
          yAxis: [
            {
              type: 'value'
            }
          ],
          series: [
            {
              symbolSize: 0,
              type: 'line',
              step: 'start',
              data: props.chartsInfo
            }
          ]
        }}
      />
    </Fragment>
  );
};
export default StepCharts;
