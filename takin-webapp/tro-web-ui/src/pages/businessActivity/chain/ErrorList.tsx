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
import { Icon } from 'antd';
import { ColumnProps } from 'antd/lib/table';
import { CommonTable, defaultColumnProps } from 'racc';
import React, { Fragment, useContext } from 'react';
import { BusinessActivityDetailsContext } from '../detailsPage';
import { ActivityBean } from '../enum';
import styles from '../index.less';
import { PopoverCard } from './SiderCollectInfo';

const icon = (
  <Icon
    theme="filled"
    type="exclamation-circle"
    className={styles.icon}
    style={{ color: '#EE6077' }}
  />
);

interface Props {}
const ErrorList: React.FC<Props> = props => {
  const { state, setState } = useContext(BusinessActivityDetailsContext);
  const dataSource = state.details.topology.exceptions;
  if (!dataSource || !dataSource.length) {
    return null;
  }
  const title: React.ReactNode = (
    <div className="pd-2x">
      <span>{icon}</span>
      <span style={{ color: '#434343', fontSize: 16 }} className="mg-l1x">
        异常（{dataSource.length}个）
      </span>
    </div>
  );
  const getColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...defaultColumnProps,
        title: '序号',
        dataIndex: 'index',
        align: 'left',
        width: 60,
        render: (text, row, index) => (
          <span style={{ color: '#434343', fontWeight: 'bold' }}>
            {index + 1 < 10 ? `0${index + 1}` : index + 1}
          </span>
        )
      },
      {
        ...defaultColumnProps,
        title: '问题描述',
        align: 'left',
        dataIndex: ActivityBean.问题描述
      },
      {
        ...defaultColumnProps,
        title: '问题类型',
        width: 100,
        align: 'left',
        dataIndex: ActivityBean.问题类型
      },
      {
        ...defaultColumnProps,
        title: '建议解决方案',
        align: 'left',
        dataIndex: ActivityBean.建议解决方案
      }
    ];
  };
  const renderContent = () => {
    return (
      <CommonTable
        columns={getColumns()}
        dataSource={dataSource}
        style={{ background: 'none' }}
        className={styles.table}
        rowKey={(row, index) => index.toString()}
      />
    );
  };
  return (
    <Fragment>
      <PopoverCard
        overlayStyle={{ width: 720 }}
        title={title}
        content={renderContent()}
      >
        <span style={{ fontSize: 14, fontWeight: 500, color: '#353535' }}>
          {icon}
          <span
            style={{
              fontWeight: 'bold',
              fontSize: 16,
              marginLeft: 24,
              marginRight: 8
            }}
          >
            {dataSource.length}
          </span>
          个异常
        </span>
      </PopoverCard>
    </Fragment>
  );
};
export default ErrorList;
