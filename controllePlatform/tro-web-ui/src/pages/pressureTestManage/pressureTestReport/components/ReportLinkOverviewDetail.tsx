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

import { Icon, Popover } from 'antd';
import { ColumnProps } from 'antd/lib/table';
import { CommonTable, useStateReducer } from 'racc';
import React, { Fragment, useEffect } from 'react';
import { customColumnProps } from 'src/components/custom-table/utils';
import PressureTestReportService from '../service';
import styles from './../index.less';
interface Props {
  id?: string;
  detailData?: any;
}

interface State {
  data: any[];
  loading: boolean;
}
const ReportLinkOverviewDetail: React.FC<Props> = props => {
  const { id, detailData } = props;

  const [state, setState] = useStateReducer<State>({
    data: null,
    loading: false
  });

  useEffect(() => {
    queryPressureTestDetailList({ reportId: id });
  }, []);

  /**
   * @name 获取压测明细列表
   */
  const queryPressureTestDetailList = async value => {
    setState({
      loading: true
    });
    const {
      data: { success, data }
    } = await PressureTestReportService.queryPressureTestDetailList({
      ...value
    });
    if (success) {
      setState({
        data
      });
    }
    setState({
      loading: false
    });
  };

  const getReportLinkOverviewColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...customColumnProps,
        title: '业务活动',
        dataIndex: 'businessActivityName'
      },
      {
        ...customColumnProps,
        title: '请求数',
        dataIndex: 'totalRequest'
      },
      {
        ...customColumnProps,
        title: '平均TPS（实际/目标）',
        dataIndex: 'tps',
        render: (text, row) => {
          return (
            <Fragment>
              <span
                style={{
                  color:
                    Number(text.result) < Number(text.value) ? '#FE7D61' : ''
                }}
              >
                {text.result}
              </span>
              <span style={{ margin: '0 8px' }}>/</span>
              <span>{text.value}</span>
            </Fragment>
          );
        }
      },
      {
        ...customColumnProps,
        title: '平均RT（实际/目标）',
        dataIndex: 'avgRT',
        render: (text, row) => {
          return (
            <Fragment>
              <div style={{ position: 'relative' }}>
                <span
                  style={{
                    color:
                      Number(text.result) > Number(text.value) ? '#FE7D61' : ''
                  }}
                >
                  {text.result}ms
                </span>
                <span style={{ margin: '0 8px' }}>/</span>
                <span>{text.value}ms</span>
                <Popover
                  placement="bottomLeft"
                  content={
                    <div className={styles.distributionWrap}>
                      <p className={styles.title}>分布</p>
                      {row.distribute &&
                        row.distribute.map((item, key) => {
                          return (
                            <p key={key} className={styles.distributionList}>
                              <span
                                style={{
                                  display: 'inline-block',
                                  width: '50%'
                                }}
                              >
                                {item.lable}
                              </span>
                              <span>{item.value}</span>
                            </p>
                          );
                        })}
                    </div>}
                >
                  <Icon
                    style={{ position: 'absolute', right: 6, top: 2 }}
                    type="pie-chart"
                    theme="filled"
                  />
                </Popover>
              </div>
            </Fragment>
          );
        }
      },
      {
        ...customColumnProps,
        title: '请求成功率（实际/目标）',
        dataIndex: 'sucessRate',
        render: (text, row) => {
          return (
            <Fragment>
              <span
                style={{
                  color:
                    Number(text.result) < Number(text.value) ? '#FE7D61' : ''
                }}
              >
                {text.result}%
              </span>
              <span style={{ margin: '0 8px' }}>/</span>
              <span>{text.value}%</span>
            </Fragment>
          );
        }
      },
      {
        ...customColumnProps,
        title: 'SA（实际/目标）',
        dataIndex: 'sa',
        render: (text, row) => {
          return (
            <Fragment>
              <span
                style={{
                  color:
                    Number(text.result) < Number(text.value) ? '#FE7D61' : ''
                }}
              >
                {text.result}%
              </span>
              <span style={{ margin: '0 8px' }}>/</span>
              <span>{text.value}%</span>
            </Fragment>
          );
        }
      },
      {
        ...customColumnProps,
        title: '峰值情况',
        align: 'center',
        children: [
          {
            title: '最大TPS',
            dataIndex: 'maxTps'
          },
          {
            title: '最大RT',
            dataIndex: 'maxRt'
          },
          {
            title: '最小RT',
            dataIndex: 'minRt'
          }
        ]
      },
      // {
      //   ...customColumnProps,
      //   title: '操作',
      //   align: 'right',
      //   render: (text, row) => {
      //     return (
      //       <BusinessActivityPressureTestDetailModal
      //         btnText="链路明细"
      //         businessActivityId={row.businessActivityId}
      //         businessActivityName={row.businessActivityName}
      //         reportId={id}
      //         detailData={detailData}
      //       />
      //     );
      //   }
      // }
    ];
  };
  return (
    <Fragment>
      <CommonTable
        loading={state.loading}
        bordered
        size="small"
        style={{ marginTop: 8 }}
        columns={getReportLinkOverviewColumns()}
        dataSource={state.data ? state.data : []}
      />
    </Fragment>
  );
};
export default ReportLinkOverviewDetail;
