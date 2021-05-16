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
 * @author chuxu
 */
import React, { Fragment } from 'react';
import { ColumnProps } from 'antd/lib/table';
import _ from 'lodash';
import { customColumnProps } from 'src/components/custom-table/utils';
import { message, Button, Switch, Badge, Modal } from 'antd';
import CustomPopconfirm from 'src/components/custom-popconfirm/CustomPopconfirm';
import MissionManageService from '../service';
import NewKanbanModal from '../modals/NewKanbanModal';
import { router } from 'umi';
import moment from 'moment';

const getMissionManageColumns = (state, setState): ColumnProps<any>[] => {
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
  return [
    {
      ...customColumnProps,
      title: '瓶颈ID',
      dataIndex: 'id'
    },
    {
      ...customColumnProps,
      title: '巡检任务',
      dataIndex: 'businessName'
    },
    {
      ...customColumnProps,
      title: '巡检任务类型',
      dataIndex: 'businessType',
      render: text => text === 0 ? '业务' : '技术'
    },
    {
      ...customColumnProps,
      title: '巡检场景',
      dataIndex: 'sceneName'
    },
    {
      ...customColumnProps,
      title: '巡检看板',
      dataIndex: 'boardName'
    },
    {
      ...customColumnProps,
      title: '瓶颈类型',
      dataIndex: 'type',
      render: text => text === 1 ? '  卡慢' : text === 2 ? '接口异常' : '巡检异常'
    },
    {
      ...customColumnProps,
      title: '瓶颈程度',
      dataIndex: 'level',
      render: text => {
        return (
          <Badge
            color={text === 1 ? '#FFB64A' : text === 2 ? '#EA5B3C' : ''}
            text={text === 1 ? '  一般' : text === 2 ? '严重' : ''}
          />
        );
      }
    },
    {
      ...customColumnProps,
      title: '状态',
      dataIndex: 'status',
      render: text => text === 1 ? '  待处理' : text === 2 ? '已处理' : '判定为误报'
    },
    {
      ...customColumnProps,
      title: '瓶颈开始时间',
      dataIndex: 'startTime',
      render: text => moment(text).format('YYYY-MM-DD HH:mm:ss') || '--'
    },
    {
      ...customColumnProps,
      title: '持续时间',
      dataIndex: 'startTime',
      render: (text, row) => {
        return calcTimeDiff(text, row.endTime);
      }
    },
    {
      ...customColumnProps,
      title: '操作',
      dataIndex: 'action',
      align: 'right',
      render: (text, row) => {
        return (
          <Fragment>
            {row.status === 1 && (
              <NewKanbanModal
                btnText="误报"
                id={row.id}
                onSuccess={() => {
                  setState({
                    isReload: !state.isReload,
                    searchParams: {
                      current: 0
                    }
                  });
                }}
              />
            )}
            <span style={{ marginLeft: 5 }} />
            {row.status === 3 && (
              <NewKanbanModal
                btnText="查看误报"
                id={row.id}
                onSuccess={() => {
                  setState({
                    isReload: !state.isReload,
                    searchParams: {
                      current: 0
                    }
                  });
                }}
              />)}
            <span style={{ marginLeft: 5 }} />
            <a onClick={() => router.push(`/bottleneckTable/bottleneckDetails?id=${row.id}&type=${row.type}`)}>详情</a>
          </Fragment>
        );
      }
    }
  ];
};

export default getMissionManageColumns;
