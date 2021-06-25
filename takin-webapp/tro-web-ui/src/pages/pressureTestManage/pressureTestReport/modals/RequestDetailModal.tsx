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

import {
  Badge,
  Button,
  Divider,
  Icon,
  message,
  Row,
  Tooltip,
  Typography
} from 'antd';
import { ColumnProps } from 'antd/lib/table';
import copy from 'copy-to-clipboard';
import { CommonModal, renderToolTipItem, useStateReducer } from 'racc';
import React, { Fragment } from 'react';
import ColorCircle from 'src/common/color-circle/ColorCircle';
import Loading from 'src/common/loading';
import CustomTable from 'src/components/custom-table';
import { customColumnProps } from 'src/components/custom-table/utils';
import { router } from 'umi';
import Header from '../components/Header';
import PressureTestReportService from '../service';
import styles from './../index.less';

interface Props {
  btnText: string | React.ReactNode;
  traceId?: string;
  isLive?: boolean;
  reportId?: string;
}

interface State {
  isReload?: boolean;
  data: any;
  totalCost: number;
  startTime: string;
  entryHostIp: string;
  clusterTest: boolean;
  traceId: string;
  loading: boolean;
  originData: any[];
}
const RequestDetailModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    isReload: false,
    data: null,
    originData: null,
    totalCost: null,
    startTime: null,
    entryHostIp: null,
    clusterTest: null,
    traceId: null,
    loading: false
  });
  const { Paragraph } = Typography;
  const { traceId } = props;

  const timestampToTime = timestamp => {
    const date = new Date(timestamp);
    const Y = `${date.getFullYear()}-`;
    const M =
      date.getMonth() + 1 < 10
        ? `0${date.getMonth() + 1}-`
        : `${date.getMonth() + 1}-`;
    const D =
      date.getDate() < 10 ? `0${date.getDate()} ` : `${date.getDate()} `;
    const h = `${date.getHours()}:`;
    const m = `${date.getMinutes()}:`;
    const s = date.getSeconds();
    return Y + M + D + h + m + s;
  };

  const handleClick = () => {
    setState({
      traceId
    });
    queryRequestDetail({
      traceId
    });
  };

  /**
   * @name 获取请求详情列表
   */
  const queryRequestDetail = async value => {
    setState({
      data: null,
      loading: true
    });
    const {
      data: { success, data }
    } = await PressureTestReportService.queryRequestDetail({
      ...value
    });
    if (success) {
      if (data) {
        setState({
          originData: data.traces,
          data: data.traces,
          totalCost: data.totalCost,
          startTime: data.startTime,
          entryHostIp: data.entryHostIp,
          clusterTest: data.clusterTest,
          loading: false
        });
      } else {
        setState({
          data: null,
          loading: false
        });
      }
    }
  };

  const handleCopy = async value => {
    if (copy(value)) {
      message.success('复制成功');
    } else {
      message.error('复制失败');
    }
  };

  const getColumns = (): ColumnProps<any>[] => {
    const columns: ColumnProps<any>[] = [
      {
        ...customColumnProps,
        title: '接口(服务)',
        dataIndex: 'interfaceName',
        ellipsis: true,
        width: 300,
        render: text => {
          return (
            <span>
              <Tooltip placement="bottomLeft" title={text}>
                <span>{text}</span>
              </Tooltip>
            </span>
          );
        }
      },
      {
        ...customColumnProps,
        title: '节点IP',
        dataIndex: 'nodeIp',
        width: 150
      },
      {
        ...customColumnProps,
        title: '应用',
        dataIndex: 'applicationName',
        ellipsis: true,
        render: text => {
          return (
            <Tooltip placement="bottomLeft" title={text}>
              <span>{text}</span>
            </Tooltip>
          );
        }
      },
      {
        ...customColumnProps,
        title: '请求参数',
        dataIndex: 'params',
        ellipsis: true,
        width: 200,
        render: text => {
          return text ? (
            <Tooltip
              placement="bottomLeft"
              title={
                <div style={{ maxHeight: 300, overflow: 'scroll' }}>{text}</div>}
            >
              <span>{text}</span>
            </Tooltip>
          ) : (
            <span>-</span>
          );
        }
      },
      {
        ...customColumnProps,
        title: '状态',
        dataIndex: 'succeeded',
        width: 70,
        render: (text, row) => {
          return (
            <Fragment>
              <Row type="flex" align="middle">
                <ColorCircle color={text === true ? '#11DFB2' : '#ED5F47'} />
                {row.nodeSuccess === false && (
                  <Fragment>
                    <Divider type="vertical" />
                    <Tooltip title="子节点有异常" trigger="click">
                      <img
                        style={{ width: 14, cursor: 'pointer' }}
                        src={require('./../../../../assets/tooltip_error.png')}
                      />
                    </Tooltip>
                  </Fragment>
                )}
              </Row>
            </Fragment>
          );
        }
      },
      {
        ...customColumnProps,
        title: '响应参数',
        dataIndex: 'response',
        ellipsis: true,
        width: 200,
        render: text => {
          return text ? (
            <Tooltip
              placement="bottomLeft"
              title={
                <div style={{ maxHeight: 300, overflow: 'scroll' }}>{text}</div>}
            >
              <span>{text}</span>
            </Tooltip>
          ) : (
            <span>-</span>
          );
        }
      },
      {
        ...customColumnProps,
        title: '时间轴（平均RT）',
        dataIndex: 'costTime',
        width: 220,
        render: (text, row) => {
          return (
            <div style={{ width: 150, position: 'relative' }}>
              <span className={styles.timeLineBg} />
              {state.totalCost && (
                <div
                  className={styles.timeLineWrap}
                  style={{
                    left: (150 / state.totalCost) * row.offsetStartTime
                  }}
                >
                  <span
                    className={styles.timeLine}
                    style={{
                      width: (150 / state.totalCost) * text,
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
    const actions = {
      title: '操作',
      dataIndex: 'actions',
      render: (text, row) => {
        if (row.entryHostIp && row.agentId) {
          return (
            <Button
              onClick={() =>
                router.push(
                  `/analysisManage?tab=method&appName=${row.applicationName}&processName=${row.entryHostIp}|${row.agentId}&reportId=${props.reportId}&type=actually`
                )
              }
              style={{ marginLeft: 16 }}
              type="primary"
            >
              开启方法追踪
            </Button>
          );
        }
      }
    };
    if (props.isLive) {
      columns.push(actions);
    }
    return columns;
  };

  const handleExpand = async (expanded, record) => {
    if (expanded === false) {
      setState({
        data: changeNodes(state.data, record.id, [])
      });
      return;
    }
    const {
      data: { success, data }
    } = await PressureTestReportService.queryRequestDetail({
      traceId,
      id: record.id
    });
    if (success) {
      if (data) {
        setState({
          data: changeNodes(state.data, record.id, data.traces)
        });
      }
    }
  };

  /**
   * @name 替换子节点
   */
  function changeNodes(data, id, node) {
    data.map(item => {
      if (item.id === id) {
        item.nextNodes = node;
      }
      if (item.nextNodes) {
        changeNodes(item.nextNodes, id, node);
      }
    });

    return data;
  }

  const requestHeadList = [
    {
      label: '调用链路入口',
      value: state.entryHostIp
    },
    {
      label: '开始时间',
      value: state.startTime && timestampToTime(state.startTime)
    },
    {
      label: '调用链路总时长',
      value: state.totalCost ? `${state.totalCost}ms` : null
    }
  ];

  return (
    <CommonModal
      modalProps={{
        width: 'calc(100% - 40px)',
        footer: null,
        maskClosable: false,
        centered: true,
        title: (
          <p style={{ fontSize: 16 }}>
            请求详情
            <span style={{ color: '#a2a6b1', fontSize: 12, marginLeft: 16 }}>
              (请求日志数据仅保留3天，3天后将无法查看，请知晓。)
            </span>
          </p>
        )
      }}
      btnProps={{ type: 'link' }}
      btnText={props.btnText}
      onClick={() => handleClick()}
    >
      <div
        style={{ height: document.body.clientHeight - 200, overflow: 'scroll' }}
      >
        <div style={{ marginBottom: 26 }}>
          <div style={{ lineHeight: '32px', marginBottom: 8 }}>
            <span className={styles.requestTitle}>{state.traceId}</span>
            {state.clusterTest !== null && (
              <span className={styles.requestTag}>
                {state.clusterTest ? '压测流量' : '业务流量'}
              </span>
            )}
          </div>
          <Header list={requestHeadList} isExtra={false} />
        </div>
        {state.data && state.data[0] && !state.loading ? (
          <div className={styles.detailTable}>
            <CustomTable
              rowKey="id"
              columns={getColumns()}
              size="small"
              dataSource={state.data}
              defaultExpandAllRows={false}
              defaultExpandedRowKeys={[
                state.data && state.data[0] && state.data[0].id
              ]}
              childrenColumnName="nextNodes"
              onExpand={(expanded, record) => {
                if (record.id !== 0) {
                  handleExpand(expanded, record);
                }
              }}
            />
          </div>
        ) : state.data === null && state.loading ? (
          <Loading />
        ) : (
          <div className={styles.defaultWrap}>
            <div className={styles.circle} />
            <p className={styles.defaultTxt}>
              请求日志数据已清理，请查看其他报告信息。
            </p>
          </div>
        )}
      </div>
    </CommonModal>
  );
};
export default RequestDetailModal;

RequestDetailModal.defaultProps = {
  isLive: false
};
