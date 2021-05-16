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
import { connect } from 'dva';
import { ColumnProps } from 'antd/lib/table';
import { Select, Divider, Row, Col, Form, Input, Descriptions, Card, Progress, Statistic } from 'antd';
import { CommonTable, useStateReducer } from 'racc';
import React, { useEffect } from 'react';
import { CommonModelState } from 'src/models/common';
import ReactEcharts from 'echarts-for-react';
import MissionManageService from '../service';
import moment from 'moment';
import styles from '../index.less';

const InputGroup = Input.Group;
const { Option } = Select;
interface Props extends CommonModelState {
  btnText: string;
  onSuccess: () => void;
  location: any;
}
const indexPage: React.FC<Props> = props => {
  const [state, setState] = useStateReducer({
    dataSource: [],
    total: 0,
    detial: {
      valueType1Level1: 0,
      valueType1Level2: 0,
      valueType2Level1: 0,
      valueType2Level2: 0
    },
    count: [],
    details: {
      checkPoint: {
        items: []
      },
      metrics: {
        items: []
      },
      rt: '',
      statistics: {
        e2eCount: 0,
        businessCount: 0,
        businessErrorCount: 0,
        e2eErrorCount: 0,
        successRate: 0
      },
      name: '',
      exceptionType: 1,
      exceptionLevel: 1,
      inlet: '',
      application: '',
      duration: '',
      startTime: '2020-01-01',
      endTime: '2021-01-01'
    },
    searchParams: {
      current: 0,
      pageSize: 10
    },
  });

  useEffect(() => {
    queryTable();
    queryPatrolSceneAndDashbordList();
  }, []);

  const queryPatrolSceneAndDashbordList = async () => {
    const {
      data: { data, success }
    } = await MissionManageService.read({});
    if (success) {
      setState({
        detial: data
      });
    }
  };

  const { location } = props;
  const queryTable = async () => {
    const {
      data: { data, success }
    } = await MissionManageService.exceptionDetail(location.query.id);
    if (success) {
      const datas = data.statistics && data.statistics.items && data.statistics.items.map((ite, ind) => {
        ite.name = ite.name;
        ite.value = ite.value;
        return ite;
      });
      setState({ details: data, count: datas });
    }
  };

  const sortArr = arr => {
    if (arr) {
      arr.sort((a, b) => {
        return b.rt - a.rt;
      });
      arr.map((ite, index) => {
        ite.key = index + 1;
        return ite;
      });
    }
    return arr;
  };

  const getColumns = (): ColumnProps<any>[] => {
    return [
      {
        title: '检查点（断言）',
        dataIndex: 'name',
      },
      {
        title: '服务',
        dataIndex: 'service'
      },
      {
        title: '应用',
        dataIndex: 'application',
      },
      {
        title: '上游应用',
        dataIndex: 'parentApplications',
        render: text => text.join(',')
      },
      {
        title: '占比',
        dataIndex: 'percentage',
        render: text => `${text.toFixed(2)}%`
      },
    ];
  };

  const getColumn = (): ColumnProps<any>[] => {
    return [
      {
        title: '排名',
        dataIndex: 'key',
      },
      {
        title: '服务',
        dataIndex: 'service'
      },
      {
        title: '应用',
        dataIndex: 'application',
      },
      {
        title: '上游应用',
        dataIndex: 'parentApplications',
        render: text => text.join(',')
      },
      {
        title: 'RT',
        dataIndex: 'rt',
        render: text => `${text.toFixed(2)}ms`
      },
    ];
  };

  const calcTimeDiff = (startTime, endTime) => {
    const diff = moment.duration(moment(endTime).diff(moment(startTime)));
    const tempData = [
      { key: 'years', value: 0, desc: 'year' },
      { key: 'months', value: 0, desc: 'mouth' },
      { key: 'days', value: 0, desc: 'day' },
      { key: 'hours', value: 0, desc: 'h' },
      { key: 'minutes', value: 0, desc: '\'' },
      { key: 'seconds', value: 0, desc: '\'\'' },
      // { key: 'milliseconds', value: 0, desc: 'ms' },
    ];
    tempData.forEach(t => { if (diff._data[t.key]) { t.value = diff._data[t.key]; } });
    let firstIndex = tempData.findIndex(t => t.value);
    const outCount = 3;
    const minOut = tempData.length - 3;
    firstIndex = firstIndex > minOut ? minOut : firstIndex;
    let out = '';
    for (let i = 0; i < outCount; i += 1) {
      const item = tempData[firstIndex + i];
      out += item.value + item.desc;
    }
    return out;
  };

  const { details, detial } = state;
  const num1 =
    Number(details.statistics && details.statistics.businessErrorCount) /
    Number(details.statistics && details.statistics.businessCount) * 100;
  const num2 =
    Number(details.statistics && details.statistics.e2eErrorCount) /
    Number(details.statistics && details.statistics.e2eCount) * 100;
  return (
    <Card bordered={false} className={styles.font}>
      <div style={{ width: '100%' }}>
        <Descriptions title={details.name} column={3}>
          <Descriptions.Item label="瓶颈类型">
            {details.exceptionType === 1 ? '卡慢' : details.exceptionType === 2 ? '接口异常' : '巡检异常'}
          </Descriptions.Item>
          <Descriptions.Item label="瓶颈程度">
            {details.exceptionLevel === 1 ? '一般' : '严重'}
          </Descriptions.Item>
          <Descriptions.Item label="业务活动入口">{details.inlet}</Descriptions.Item>
          <Descriptions.Item label="所属应用">{details.application}</Descriptions.Item>
          <Descriptions.Item label="持续时间">{calcTimeDiff(details.startTime, details.endTime)}</Descriptions.Item>
          <Descriptions.Item label="瓶颈开始时间">
            {moment(details.startTime).format('YYYY-MM-DD HH:mm:ss')}
          </Descriptions.Item>
        </Descriptions>
      </div>
      <Row style={{ display: location.query.type !== '1' ? 'block' : 'none' }}>
        <Col span={15}>
          <Card style={{ height: '370px' }}>
            <h3>请求概览</h3>
            <Row style={{ marginTop: 35 }}>
              <Col span={8}>
                <Statistic title="业务总请求" value={details.statistics && details.statistics.businessCount} />
                <p style={{ margin: '20px 0', fontSize: '14px', color: 'rgba(0, 0, 0, 0.45)' }}>业务请求错误率</p>
                <Progress type="circle" percent={num1} strokeColor="red" format={percent => `${percent} %`} />
              </Col>
              <Col span={8}>
                <Statistic title="巡检总请求" value={details.statistics && details.statistics.e2eCount} />
                <p style={{ margin: '20px 0', fontSize: '14px', color: 'rgba(0, 0, 0, 0.45)' }}>巡检请求错误率</p>
                <Progress type="circle" percent={num2} strokeColor="red" format={percent => `${percent} %`} />
              </Col>
              <Col span={8}>
                <Statistic title="当前成功率" value={details.statistics && details.statistics.successRate} />
                <Descriptions column={1} style={{ marginTop: 20 }}>
                  <Descriptions.Item label={`${details.exceptionLevel === 1 ? '一般' : '严重'}瓶颈基线`}>
                    {detial[`valueType${details.exceptionType}Level${details.exceptionLevel}`]}
                  </Descriptions.Item>
                </Descriptions>
              </Col>
            </Row>
          </Card>
        </Col>
        <Col span={8} offset={1} >
          <Card style={{ height: '370px' }}>
            <h3>业务检查点分布</h3>
            <ReactEcharts
              option={{
                color: ['#5470c6', '#91cc75', ' #fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', ' #9a60b4', '#ea7ccc'],
                tooltip: {
                  trigger: 'item'
                },
                legend: {
                  orient: 'vertical',
                  right: 'right',
                },
                series: [
                  {
                    type: 'pie',
                    top: '-50px',
                    left: '-80px',
                    radius: ['30%', '60%'],
                    avoidLabelOverlap: false,
                    label: {
                      show: false,
                      position: 'center'
                    },
                    emphasis: {
                      label: {
                        show: true,
                        fontSize: '20',
                        fontWeight: 'bold'
                      }
                    },
                    labelLine: {
                      show: false
                    },
                    data: state.count
                  }
                ]
              }}
            />
          </Card>
        </Col>
      </Row>
      <Row style={{ display: location.query.type === '1' ? 'block' : 'none' }}>
        <Col span={24}>
          <Card style={{ height: '300px' }}>
            <h3>请求概览</h3>
            <Row style={{ marginTop: 20 }}>
              <Col span={8}>
                <Row style={{ marginTop: 15 }}>
                  <Col span={12}>
                    <p style={{ marginBottom: 20, fontSize: '14px', color: 'rgba(0, 0, 0, 0.45)' }}>业务请求错误率</p>
                    <Progress type="circle" percent={num1} strokeColor="red" format={percent => `${percent} %`} />
                  </Col>
                  <Col span={12}>
                    <Statistic title="业务总请求" value={details.statistics && details.statistics.businessCount} />
                  </Col>
                </Row>
              </Col>
              <Col span={8}>
                <Row style={{ marginTop: 15 }}>
                  <Col span={12}>
                    <p style={{ marginBottom: 20, fontSize: '14px', color: 'rgba(0, 0, 0, 0.45)' }}>巡检请求错误率</p>
                    <Progress type="circle" percent={num2} strokeColor="red" format={percent => `${percent} %`} />
                  </Col>
                  <Col span={12}>
                    <Statistic title="巡检总请求" value={details.statistics && details.statistics.e2eCount} />
                  </Col>
                </Row>
              </Col>
              <Col span={8}>
                <Descriptions column={1} style={{ marginTop: 40 }}>
                  <Descriptions.Item label="当前RT">{details.rt}ms</Descriptions.Item>
                  <Descriptions.Item label={`${details.exceptionLevel === 1 ? '一般' : '严重'}基线`}>
                    {detial[`valueType${details.exceptionType}Level${details.exceptionLevel}`]}ms
                  </Descriptions.Item>
                </Descriptions>
              </Col>
            </Row>
          </Card>
        </Col>
      </Row>

      <Card style={{ marginTop: 20 }} style={{ display: location.query.type !== '1' ? 'block' : 'none' }}>
        <CommonTable
          columns={getColumns()}
          dataSource={details.checkPoint && details.checkPoint.items}
          style={{ background: 'none' }}
        />
      </Card>
      <Card style={{ marginTop: 20 }} style={{ display: location.query.type === '1' ? 'block' : 'none' }}>
        <CommonTable
          columns={getColumn()}
          dataSource={sortArr(details.metrics && details.metrics.items)}
          style={{ background: 'none' }}
        />
      </Card>
    </Card >
  );
};

export default Form.create()(indexPage);
