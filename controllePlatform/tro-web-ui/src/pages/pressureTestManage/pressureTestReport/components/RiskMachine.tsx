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

import { Pagination, Tabs } from 'antd';
import { ColumnProps } from 'antd/lib/table';
import { renderToolTipItem, useStateReducer } from 'racc';
import React, { useEffect } from 'react';
import Loading from 'src/common/loading';
import CustomTable from 'src/components/custom-table';
import { customColumnProps } from 'src/components/custom-table/utils';
import MachinePerformanceDetailModal from '../modals/MachinePerformanceDetailModal';
import PressureTestReportService from '../service';
import styles from './../index.less';
interface Props {
  state?: any;
  setState?: (value) => void;
  id?: string;
}

interface RiskMachineState {
  searchParams: {
    current: number;
    pageSize: number;
  };
  riskMachineList: any[];
  total?: number;
  loading?: boolean;
  riskMachineAppList?: any;
}
const RiskMachine: React.FC<Props> = props => {
  const { state, setState, id } = props;
  const [riskMachineState, setRiskMachineState] = useStateReducer<
    RiskMachineState
    // tslint:disable-next-line:ter-func-call-spacing
  >({
    searchParams: {
      current: 0,
      pageSize: 10
    },
    riskMachineList: null,
    total: 0,
    loading: false,
    riskMachineAppList: null
  });

  const { TabPane } = Tabs;
  const { riskMachineAppList, riskMachineList } = riskMachineState;

  useEffect(() => {
    queryRiskMachineAppList({ reportId: id });
  }, []);

  useEffect(() => {
    if (state.riskAppName) {
      queryRiskMachineList({
        reportId: id,
        applicationName: state.riskAppName,
        ...riskMachineState.searchParams
      });
    }
  }, [
    state.riskAppName,
    riskMachineState.searchParams.current,
    riskMachineState.searchParams.pageSize
  ]);

  /**
   * @name 获取风险机器左侧应用列表
   *
   */
  const queryRiskMachineAppList = async value => {
    const {
      data: { success, data }
    } = await PressureTestReportService.queryRiskMachineAppList({
      ...value
    });
    if (success) {
      setRiskMachineState({
        riskMachineAppList: data
      });
      setState({
        riskAppName:
          data &&
          data.applicationList &&
          data.applicationList[0].applicationName
      });
    }
  };

  /**
   * @name 获取风险机器列表
   *
   */
  const queryRiskMachineList = async value => {
    setRiskMachineState({
      loading: true
    });
    const {
      total,
      data: { success, data }
    } = await PressureTestReportService.queryRiskMachineList({
      ...value
    });
    if (success) {
      setRiskMachineState({
        total,
        riskMachineList: data
      });
    }
    setRiskMachineState({
      loading: false
    });
  };

  const handleChangeApp = (value, applicationName) => {
    setState({
      riskAppKey: value,
      riskAppName: applicationName
    });
  };

  const handleChange = async (current, pageSize) => {
    setRiskMachineState({
      searchParams: {
        pageSize,
        current: current - 1
      }
    });
  };

  const handlePageSizeChange = async (current, pageSize) => {
    setRiskMachineState({
      searchParams: {
        pageSize,
        current: 0
      }
    });
  };

  const getRiskMachineListColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...customColumnProps,
        title: 'AgentID',
        dataIndex: 'agentId'
      },
      {
        ...customColumnProps,
        title: '机器',
        dataIndex: 'machineIp'
      },
      {
        ...customColumnProps,
        title: '风险',
        dataIndex: 'riskContent'
      },
      {
        ...customColumnProps,
        title: '操作',
        dataIndex: 'action',
        render: (text, row) => {
          return (
            <MachinePerformanceDetailModal
              btnText="性能详情"
              ip={row.machineIp}
              applicationName={state.riskAppName}
              reportId={id}
            />
          );
        }
      }
    ];
  };

  return riskMachineAppList ? (
    <div style={{ display: 'flex', height: 450 }}>
      <div className={styles.leftSelected}>
        <p className={styles.riskTip}>
          共{riskMachineAppList.applicationCount || 0}个应用的
          {riskMachineAppList.machineCount || 0}
          台机器存在风险
        </p>
        {riskMachineAppList.applicationList &&
          riskMachineAppList.applicationList.map((item, key) => {
            return (
              <p
                key={key}
                className={
                  state.riskAppKey === key
                    ? styles.appItemActive
                    : styles.appItem
                }
                onClick={() => handleChangeApp(key, item.applicationName)}
              >
                {renderToolTipItem(item.applicationName, 20)}
                <span className={styles.appNum}>{item.riskCount}</span>
              </p>
            );
          })}
      </div>
      <div className={styles.riskMachineList}>
        <CustomTable
          rowKey={(row, index) => index.toString()}
          loading={riskMachineState.loading}
          columns={getRiskMachineListColumns()}
          dataSource={riskMachineList ? riskMachineList : []}
        />
        <Pagination
          style={{ marginTop: 20, textAlign: 'right' }}
          total={riskMachineState.total}
          current={riskMachineState.searchParams.current + 1}
          pageSize={riskMachineState.searchParams.pageSize}
          showTotal={(t, range) =>
            `共 ${riskMachineState.total} 条数据 第${riskMachineState
              .searchParams.current + 1}页 / 共 ${Math.ceil(
              riskMachineState.total /
                (riskMachineState.searchParams.pageSize || 10)
            )}页`
          }
          showSizeChanger={true}
          onChange={(current, pageSize) => handleChange(current, pageSize)}
          onShowSizeChange={handlePageSizeChange}
        />
      </div>
    </div>
  ) : (
    <Loading />
  );
};
export default RiskMachine;
