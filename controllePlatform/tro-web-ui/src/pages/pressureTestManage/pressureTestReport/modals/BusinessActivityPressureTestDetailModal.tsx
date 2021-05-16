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
import { Typography, Tag, Tooltip, message, Icon, Row } from 'antd';
import copy from 'copy-to-clipboard';
import styles from './../index.less';
import CustomStatistic from 'src/components/custom-statistic/CustomStatistic';
import RequestDetailModal from './RequestDetailModal';
import DefaultModal from './DefaultModal';

interface Props {
  btnText: string | React.ReactNode;
  reportId: string;
  businessActivityId: number;
  businessActivityName: string;
  detailData?: any;
}

interface State {
  isReload?: boolean;
  data: any[];
  totalRT: number;
  loading: boolean;
  detailData?: any;
}
const BusinessActivityPressureTestDetailModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    isReload: false,
    data: null,
    totalRT: null,
    loading: false
  });
  const { Paragraph } = Typography;
  const {
    reportId,
    businessActivityId,
    businessActivityName,
    detailData
  } = props;

  const startTimeDate = new Date(detailData && detailData.startTime);

  const handleClick = () => {
    queryBusinessActivityPressureTestDetail({
      reportId,
      businessActivityId,
      startTime: startTimeDate.getTime(),
      endTime: detailData && detailData.endTime
    });
  };

  /**
   * @name 获取业务活动压测明细列表
   */
  const queryBusinessActivityPressureTestDetail = async value => {
    setState({
      data: null,
      loading: true
    });
    const {
      data: { success, data }
    } = await PressureTestReportService.queryBusinessActivityPressureTestDetail(
      {
        ...value
      }
    );
    if (success) {
      setState({
        data: data.details,
        totalRT: data.totalRT,
        loading: false
      });
      return;
    }
    setState({
      loading: false
    });
  };

  const handleCopy = async value => {
    if (copy(value)) {
      message.success('复制成功');
    } else {
      message.error('复制失败');
    }
  };

  const getColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...customColumnProps,
        title: '接口(服务)',
        dataIndex: 'serviceName',
        width: 300,
        render: (text, row) => {
          return (
            <div className={styles.serviceName}>
              <Row type="flex">
                {row.bottleneckFlag === 1 && (
                  <Tooltip title="瓶颈接口">
                    <Icon
                      type="info-circle"
                      style={{ marginRight: 8, width: 16 }}
                      theme="twoTone"
                      twoToneColor="#FF846A"
                    />
                  </Tooltip>
                )}
                <Tooltip
                  style={{ display: 'inline-block' }}
                  title={
                    <Fragment>
                      <p>{text}</p>
                      <p style={{ textAlign: 'right' }}>
                        <a onClick={() => handleCopy(text)}>复制</a>
                      </p>
                    </Fragment>
                  }
                >
                  <Paragraph
                    style={{
                      maxWidth: 200,
                      color: row.totalCount === 0 ? '#ddd' : null
                    }}
                    ellipsis
                  >
                    {text}
                  </Paragraph>
                </Tooltip>
              </Row>
            </div>
          );
        }
      },
      {
        ...customColumnProps,
        title: '类型',
        dataIndex: 'eventType',
        render: (text, row) => {
          return <Tag color={row.totalCount === 0 ? '#ddd' : null}>{text}</Tag>;
        }
      },
      {
        ...customColumnProps,
        title: '应用',
        dataIndex: 'appName',
        render: (text, row) => {
          return (
            <span style={{ color: row.totalCount === 0 ? '#ddd' : null }}>
              {text}
            </span>
          );
        }
      },
      {
        ...customColumnProps,
        title: '请求数',
        dataIndex: 'totalCount',
        align: 'right',
        render: (text, row) => {
          return (
            <CustomStatistic
              valueStyle={{ color: row.totalCount === 0 ? '#ddd' : null }}
              value={text}
            />
          );
        }
      },
      {
        ...customColumnProps,
        title: '请求比',
        dataIndex: 'requestRate',
        align: 'right',
        render: (text, row) => {
          return (
            <CustomStatistic
              valueStyle={{ color: row.totalCount === 0 ? '#ddd' : null }}
              value={text}
              precision={2}
            />
          );
        }
      },
      {
        ...customColumnProps,
        title: 'TPS',
        dataIndex: 'tps',
        align: 'right',
        render: (text, row) => {
          if (text === 0) {
            return (
              <span style={{ color: row.totalCount === 0 ? '#ddd' : null }}>
                {`<1`}
              </span>
            );
          }
          return (
            <CustomStatistic
              valueStyle={{ color: row.totalCount === 0 ? '#ddd' : null }}
              value={text}
            />
          );
        }
      },
      {
        ...customColumnProps,
        title: '最大RT',
        dataIndex: 'maxRt',
        // width: 100,
        align: 'center',
        render: (text, row) => {
          if (row.totalCount === 0) {
            return <span style={{ color: '#ddd' }}>{text}</span>;
          }
          if (row.traceId === null || row.traceId === '') {
            return (
              <DefaultModal
                content={<div>TraceId为空，暂时无法查看接口详情</div>}
                btnText={
                  <div className={styles.maxRtWrap}>
                    <CustomStatistic value={text} />
                    <Icon style={{ color: '#11BBD5' }} type="right" />
                  </div>}
              />
            );
          }
          return (
            <RequestDetailModal
              traceId={row.traceId}
              btnText={
                <div className={styles.maxRtWrap}>
                  {/* <CustomStatistic value={text} suffix="ms" />
                   */}
                  <span style={{ color: '#595959' }}>{text} </span>
                  <Icon
                    style={{ marginLeft: 12, color: '#11BBD5' }}
                    type="right"
                  />
                </div>}
            />
          );
        }
      },
      {
        ...customColumnProps,
        title: '最小RT',
        dataIndex: 'minRt',
        align: 'right',
        render: (text, row) => {
          return (
            <CustomStatistic
              valueStyle={{ color: row.totalCount === 0 ? '#ddd' : null }}
              value={text}
            />
          );
        }
      },
      {
        ...customColumnProps,
        title: '时间轴（平均RT）',
        dataIndex: 'avgRt',
        width: 220,
        render: (text, row) => {
          if (row.totalCount === 0) {
            return <Fragment />;
          }
          return (
            <div style={{ width: 150, position: 'relative' }}>
              <span className={styles.timeLineBg} />
              {state.totalRT && (
                <div
                  className={styles.timeLineWrap}
                  style={{
                    left: (150 / state.totalRT) * row.offset
                  }}
                >
                  <span
                    className={styles.timeLine}
                    style={{
                      width: (150 / state.totalRT) * text,
                      marginRight: 2
                    }}
                  />
                  <span>{text}ms</span>
                </div>
              )}
            </div>
          );
        }
      }
    ];
  };

  return (
    <CommonModal
      modalProps={{
        width: 1234,
        footer: null,
        title: `业务活动压测明细(业务活动：${businessActivityName})`
      }}
      btnProps={{ type: 'link' }}
      btnText={props.btnText}
      onClick={() => handleClick()}
    >
      <div style={{ height: 700, overflow: 'scroll' }}>
        {state.data && (
          <CustomTable
            rowKey="uuid"
            loading={state.loading}
            columns={getColumns()}
            size="small"
            dataSource={state.data}
            defaultExpandAllRows={true}
          />
        )}
      </div>
    </CommonModal>
  );
};
export default BusinessActivityPressureTestDetailModal;
