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
import { Collapse, Pagination } from 'antd';
import { customColumnProps } from 'src/components/custom-table/utils';
import PressureTestReportService from '../service';
import CustomTable from 'src/components/custom-table';
import styles from './../index.less';
interface Props {
  btnText?: string | React.ReactNode;
  reportId?: number;
  hasMissingData: number;
}

interface State {
  isReload?: boolean;
  data: any[];
  status: any;
}
const MissingDataListModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    isReload: false,
    data: null,
    status: null
  });
  const { reportId, hasMissingData } = props;
  const { Panel } = Collapse;

  const handleClick = () => {
    queryMissingDataList({ reportId });
  };

  /**
   * @name 获取漏数验证列表
   */
  const queryMissingDataList = async value => {
    const {
      data: { success, data }
    } = await PressureTestReportService.queryMissingDataList({
      ...value
    });
    if (success) {
      setState({
        data: data && data.dsResultResponseList,
        status: data && data.statusResponse
      });
    }
  };

  const customPanelStyle = {
    background: '#ffffff',
    borderRadius: 2,
    marginBottom: 8,
    border: '1px solid #F0F0F0',
    overflow: 'hidden'
  };

  const getMissingDataScriptColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...customColumnProps,
        title: '序号',
        dataIndex: 'order',
        width: 80
      },
      {
        ...customColumnProps,
        title: '命令',
        dataIndex: 'sql',
        render: (text, row) => {
          return (
            <span style={{ color: row.status !== 0 ? '#EA5B3C' : '' }}>
              {text}
            </span>
          );
        }
      },
      {
        ...customColumnProps,
        title: '结果',
        dataIndex: 'statusResponse',
        render: (text, row) => {
          return (
            <span style={{ color: row.status !== 0 ? '#EA5B3C' : '' }}>
              {text && text.label}
            </span>
          );
        }
      }
    ];
  };

  return (
    <CommonModal
      modalProps={{
        width: 900,
        footer: null,
        title: `数据验证结果-${state.status && state.status.label}`
      }}
      btnProps={{
        type: 'link',
        style: { color: hasMissingData ? '#EA5B3C' : '#11BBD5' }
      }}
      btnText={props.btnText}
      onClick={() => handleClick()}
    >
      <div style={{ height: 600, overflowY: 'scroll' }}>
        <div
          className={styles.missingDataWrap}
          style={{ height: 600, overflowY: 'scroll', marginTop: 16 }}
        >
          <Collapse
            defaultActiveKey={['0']}
            expandIconPosition="right"
            bordered={false}
          >
            {state.data &&
              state.data.map((item, k) => {
                return (
                  <Panel
                    style={customPanelStyle}
                    header={
                      <div style={{ position: 'relative' }}>
                        <div className={styles.missingDataTitle}>
                          {item.status === 1 && (
                            <span className={styles.louIcon}>漏</span>
                          )}
                          {item.datasourceName}
                        </div>
                        <p className={styles.missingDataSubTitle}>
                          {item.jdbcUrl}
                        </p>
                      </div>}
                    key={k}
                  >
                    <div>
                      <CustomTable
                        bordered={false}
                        defaultExpandAllRows={true}
                        columns={getMissingDataScriptColumns()}
                        dataSource={item.detailResponseList}
                      />
                    </div>
                  </Panel>
                );
              })}
          </Collapse>
        </div>
      </div>
    </CommonModal>
  );
};
export default MissingDataListModal;
