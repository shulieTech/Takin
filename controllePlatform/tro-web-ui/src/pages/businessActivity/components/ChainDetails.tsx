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
import { Icon, Popover } from 'antd';
import { ColumnProps } from 'antd/lib/table';
import { CommonTable, defaultColumnProps } from 'racc';
import React, { Fragment, useContext, useEffect, useState } from 'react';
import { AddEditSystemPageContext, getEntranceInfo } from '../addEditPage';
import { SystemFlowEnum } from '../enum';
import BusinessActivityService from '../service';
interface Props {}
const ChainDetails: React.FC<Props> = props => {
  const { state } = useContext(AddEditSystemPageContext);
  const [dataSource, setDataSource] = useState([]);
  useEffect(() => {
    if (state.service) {
      queryChainList();
    }
  }, [state.service]);
  const queryChainList = async () => {
    const {
      data: { data, success }
    } = await BusinessActivityService.queryChainList({
      ...getEntranceInfo(state.serviceList, state.service),
      applicationName: state.app,
      type: state.serviceType,
      linkId: state.service
    });
    if (success) {
      setDataSource(data);
    }
  };
  const getColumns = (): ColumnProps<any>[] => {
    return [
      { ...defaultColumnProps, title: '应用', dataIndex: SystemFlowEnum.应用 },
      {
        ...defaultColumnProps,
        title: '节点',
        dataIndex: SystemFlowEnum.节点,
        render: data => data.map(item => <div key={item.ip}>{item.ip}</div>)
      },
      // {
      //   ...defaultColumnProps,
      //   title: '服务类型',
      //   dataIndex: SystemFlowEnum.服务类型
      // },
      {
        ...defaultColumnProps,
        title: '服务',
        dataIndex: SystemFlowEnum.服务,
        render: data =>
          data.map(item => (
            <div key={item.serviceType}>
              {item.serviceType}
              <Popover
                content={item.extendInfo.map((row, i) => (
                  <div key={i}>{row}</div>
                ))}
                title=""
              >
                <Icon style={{ cursor: 'pointer' }} type="right" />
              </Popover>
            </div>
          ))
      },
      {
        ...defaultColumnProps,
        title: '调用服务',
        dataIndex: SystemFlowEnum.调用服务,
        render: data =>
          data.map(item => (
            <div key={item.serviceType}>
              {item.serviceType}
              <Popover
                content={
                  <Fragment>
                    {item.items.map((row, i) => (
                      <div key={i}>
                        {row.title}
                        <ul>
                          {row.info.map((info, i2) => (
                            <li key={i2}>
                              {info}
                            </li>
                          ))}
                        </ul>
                      </div>
                    ))}
                  </Fragment>
                }
              >
                <Icon style={{ cursor: 'pointer' }} type="right" />
              </Popover>
            </div>
          ))
      },
      {
        ...defaultColumnProps,
        title: '下游应用',
        dataIndex: SystemFlowEnum.下游应用,
        render: data =>
          data.map(item => (
            <div key={item.applicationName}>{item.applicationName}</div>
          ))
      }
    ];
  };
  return (
    <Fragment>
      <CommonTable
        columns={getColumns()}
        dataSource={dataSource}
        rowKey={(row, index) => index.toString()}
      />
    </Fragment>
  );
};
export default ChainDetails;
