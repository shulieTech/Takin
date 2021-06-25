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

import { Button, Col, Modal, Statistic } from 'antd';
import { useStateReducer } from 'racc';
import React, { Fragment, useEffect } from 'react';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';
import CustomSkeleton from 'src/common/custom-skeleton';
import CustomPopconfirm from 'src/components/custom-popconfirm/CustomPopconfirm';
import { BasePageLayout } from 'src/components/page-layout';
import router from 'umi/router';
import { TestMode } from '../pressureTestScene/enum';
import Header from './components/Header';
import LinkCharts from './components/LinkCharts';
import LinkOverview from './components/LinkOverview';
import RequestDetailList from './components/RequestDetailList';
import Summary from './components/Summary';
import styles from './index.less';
import PressureTestReportService from './service';
interface State {
  isReload?: boolean;
  detailData: any;
  visible: boolean;
  tabList: any;
  chartsInfo: any;
  tabKey: 0;
  flag: boolean;
  requestList: any;
  startTime: any;
}
interface Props {
  location?: { query?: any };
}

const btnAuthority: any =
  localStorage.getItem('trowebBtnResource') &&
  JSON.parse(localStorage.getItem('trowebBtnResource'));
const menuAuthority: any =
  localStorage.getItem('trowebUserResource') &&
  JSON.parse(localStorage.getItem('trowebUserResource'));

const PressureTestLive: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    isReload: false,
    detailData: {},
    visible: false,
    tabList: [{ label: '全局趋势', value: 0 }],
    chartsInfo: {},
    tabKey: 0,
    flag: false,
    requestList: null,
    startTime: null
  });

  const { location } = props;
  const { query } = location;
  const { id } = query;
  const { detailData, chartsInfo } = state;

  useEffect(() => {
    queryLiveBusinessActivity(id);
  }, []);
  useEffect(() => {
    reFresh();
    queryLiveDetail(id);
    queryLiveChartsInfo(id, state.tabKey);
    queryRequestList({
      startTime:
        state.startTime &&
        Date.parse(
          new Date(state.startTime && state.startTime.replace(/-/g, '/'))
        ) !== 0 &&
        !isNaN(
          Date.parse(
            new Date(state.startTime && state.startTime.replace(/-/g, '/'))
          )
        )
          ? Date.parse(state.startTime && state.startTime.replace(/-/g, '/'))
          : null,
      sceneId: id
    });
  }, [state.isReload]);

  /**
   * @name 5s刷新页面
   */
  const reFresh = () => {
    if (!state.flag) {
      setTimeout(() => {
        setState({
          isReload: !state.isReload
        });
      }, 5000);
    }
  };

  /**
   * @name 获取压测实况详情
   */
  const queryLiveDetail = async value => {
    const {
      data: { data, success }
    } = await PressureTestReportService.queryLiveDetail({
      sceneId: value
    });
    if (success) {
      setState({
        detailData: data,
        startTime: data.startTime
      });
      if (data.taskStatus !== 0) {
        setState({
          flag: true,
          visible: true
        });
      }
    }
  };

  /**
   * @name 获取实况业务活动列表
   */
  const queryLiveBusinessActivity = async value => {
    const {
      data: { data, success }
    } = await PressureTestReportService.queryLiveBusinessActivity({
      sceneId: value
    });
    if (success) {
      setState({
        tabList: state.tabList.concat(
          data &&
            data.map(item => {
              return {
                label: item.businessActivityName,
                value: item.businessActivityId
              };
            })
        )
      });
    }
  };

  /**
   * @name 获取压测实况趋势信息
   */
  const queryLiveChartsInfo = async (sceneId, businessActivityId) => {
    const {
      data: { data, success }
    } = await PressureTestReportService.queryLiveLinkChartsInfo({
      sceneId,
      businessActivityId
    });
    if (success) {
      setState({
        chartsInfo: data
      });
    }
  };

  /**
   * @name 获取压测实况请求流量列表
   */
  const queryRequestList = async value => {
    const {
      data: { success, data }
    } = await PressureTestReportService.queryRequestList({
      ...value,
      current: 0,
      pageSize: 50
    });
    if (success) {
      setState({
        requestList: data
      });
    }
  };
  const headList = [
    {
      label: '开始时间',
      value: detailData.startTime
    },
    {
      label: '压测模式',
      value: TestMode[detailData.pressureType]
    },
    {
      label: '最大并发',
      value: detailData.concurrent
    },
    {
      label: '操作人',
      value: detailData.operateName
    }
  ];

  const summaryList = [
    {
      label: '警告',
      value: detailData.totalWarn,
      precision: 0,
      color: '#FE7D61',
      suffix: '次'
    },
    {
      label: '实际并发数',
      value: detailData.avgConcurrent
      // precision: 2
    },
    {
      label: '实际/目标TPS',
      precision: 2,
      render: () => (
        <Fragment>
          <Statistic
            style={{ display: 'inline-block' }}
            value={detailData.avgTps || 0}
            precision={0}
          />
          /
          <Statistic
            style={{ display: 'inline-block' }}
            value={detailData.tps || 0}
            precision={0}
          />
        </Fragment>
      )
    },
    {
      label: '平均RT',
      value: detailData.avgRt,
      precision: 2,
      suffix: 'ms'
    },
    {
      label: '成功率',
      value: detailData.successRate,
      precision: 2,
      suffix: '%'
    },
    {
      label: 'SA',
      value: detailData.sa,
      precision: 2,
      suffix: '%'
    }
  ];

  /**
   * @name 跳转到报告详情
   */
  const handleOk = () => {
    router.push(
      `/pressureTestManage/pressureTestReport/details?id=${detailData.id}`
    );
  };

  /**
   * @name 跳转到压测场景列表
   */
  const handleCancel = () => {
    router.push(`/pressureTestManage/pressureTestScene`);
  };

  /**
   * @name 停止压测
   */
  const handleConfirm = async () => {
    const {
      data: { data, success }
    } = await PressureTestReportService.stopPressureTest({
      sceneId: id
    });
    if (success) {
      setState({
        flag: true,
        visible: true
      });
    }
  };

  return JSON.stringify(state.detailData) !== '{}' ? (
    <BasePageLayout
      title={detailData.sceneName ? detailData.sceneName : '-'}
      extra={
        <Fragment>
          <AuthorityBtn
            isShow={
              btnAuthority &&
              btnAuthority.pressureTestManage_pressureTestScene_5_start_stop &&
              detailData &&
              detailData.canStartStop
            }
          >
            <CustomPopconfirm
              title="是否确认停止？"
              okColor="#FE7D61"
              okText="确认停止"
              onConfirm={() => handleConfirm()}
            >
              <Button
                type="danger"
                style={{
                  position: 'absolute',
                  top: -20,
                  right: 20,
                  background: '#FE7D61',
                  color: '#fff',
                  border: 'none'
                }}
              >
                停止压测
              </Button>
            </CustomPopconfirm>
          </AuthorityBtn>
        </Fragment>
      }
      extraPosition="top"
    >
      <Header list={headList} isExtra={true} />
      <Summary
        detailData={detailData}
        list={summaryList}
        style={{ marginTop: 24, marginBottom: 24 }}
        leftWrap={
          <Col span={3}>
            <p className={styles.leftTitle}>计时 / 压测时间</p>
            <p className={styles.timeWrap}>
              <span className={styles.time}>{detailData.testTime}</span>/
              {detailData.testTotalTime}
            </p>
          </Col>}
      />
      <LinkOverview dataSource={detailData.businessActivity} />
      <LinkCharts
        tabList={state.tabList}
        chartsInfo={chartsInfo}
        setState={setState}
        state={state}
        isLive={true}
      />
      <RequestDetailList
        dataSource={state.requestList ? state.requestList : []}
      />
      <Modal
        title="压测已结束"
        // visible={false}
        visible={state.visible}
        // okText="查看压测报告"
        // cancelText="稍后查看"
        footer={[
          // 定义右下角 按钮的地方 可根据需要使用 一个或者 2个按钮
          <Button key="back" onClick={handleCancel}>
            稍后查看
          </Button>,
          <AuthorityBtn
            key="submit"
            isShow={
              menuAuthority &&
              menuAuthority.pressureTestManage_pressureTestReport
            }
          >
            <Button key="submit" type="primary" onClick={handleOk}>
              查看压测报告
            </Button>
          </AuthorityBtn>
        ]}
        // onOk={() => handleOk()}
        // onCancel={() => handleCancel()}
      >
        <div>
          <span>
            <span>报告生成中···</span>
          </span>
        </div>
      </Modal>
    </BasePageLayout>
  ) : (
    <CustomSkeleton />
  );
};
export default PressureTestLive;
