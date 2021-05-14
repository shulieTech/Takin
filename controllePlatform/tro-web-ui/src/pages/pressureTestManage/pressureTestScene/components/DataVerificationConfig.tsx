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
 * @name 步骤1-基本信息
 */

import React, { Fragment, useEffect } from 'react';

import { FormCardMultipleDataSourceBean } from 'src/components/form-card-multiple/type';
import { FormDataType } from 'racc/dist/common-form/type';
import {
  Input,
  InputNumber,
  Radio,
  Statistic,
  Icon,
  Tooltip,
  Collapse,
  Empty
} from 'antd';
import styles from './../index.less';
import CustomTable from 'src/components/custom-table';
import { ColumnProps } from 'antd/lib/table';
import { customColumnProps } from 'src/components/custom-table/utils';
import InputNumberPro from 'src/common/inputNumber-pro';
import EmptyNode from 'src/common/empty-node';

interface Props {}

const DataVerificationConfig = (
  state,
  setState,
  props
): FormCardMultipleDataSourceBean => {
  /** @name 漏数验证设置 */
  const getDataVerificationConfigFormData = (): FormDataType[] => {
    const { location } = props;
    const { query } = location;
    const { action } = query;

    const { detailData, pressureMode } = state;

    const { Panel } = Collapse;

    // useEffect(() => {}, []);

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
          dataIndex: 'sql'
        }
      ];
    };

    const formData = [
      {
        key: 'scheduleInterval',
        label: (
          <span style={{ fontSize: 14 }}>
            时间间隔
            <Tooltip
              title={
                <div>
                  <p>
                    时间间隔指数据验证命令循环执行的时间，时间间隔越短，对数据库性能损耗越高，最大不得超过压测总时长。
                  </p>
                  <p>
                    根据验证命令实际执行情况，实际间隔时间可能会有少许出入，属于正常情况。
                  </p>
                </div>
              }
              placement="right"
              trigger="click"
            >
              <Icon type="question-circle" style={{ marginLeft: 4 }} />
            </Tooltip>
          </span>
        ),
        options: {
          initialValue: action !== 'add' ? detailData.scheduleInterval : null,
          rules: [
            {
              required: true,
              message: '请输入验证时间间隔'
            }
          ]
        },
        formItemProps: { labelCol: { span: 3 }, wrapperCol: { span: 16 } },
        node: (
          <InputNumberPro
            style={{ width: 300 }}
            precision={0}
            min={1}
            max={59}
            placeholder="请输入1~59的整数"
            addonAfter="分"
          />
        )
      },
      {
        key: 'order',
        label: '验证命令',
        options: {
          initialValue: action !== 'add' ? detailData.timeInterval : null,
          rules: [
            {
              required: false,
              message: '请输入验证时间间隔'
            }
          ]
        },
        formItemProps: { labelCol: { span: 3 }, wrapperCol: { span: 20 } },
        node:
          state.missingDataScriptList &&
          state.missingDataScriptList.length > 0 ? (
            <div className={styles.missingDataWrap} style={{ marginTop: 16 }}>
              <Collapse
                defaultActiveKey={['0']}
                expandIconPosition="right"
                bordered={false}
              >
                {state.missingDataScriptList.map((item, k) => {
                  return (
                    <Panel
                      style={customPanelStyle}
                      header={
                        <div style={{ position: 'relative' }}>
                          <div className={styles.title}>
                            {item.datasourceName}
                          </div>
                          <p className={styles.subTitle}>url: {item.jdbcUrl}</p>
                        </div>}
                      key={k}
                    >
                      <div>
                        <CustomTable
                          bordered={false}
                          defaultExpandAllRows={true}
                          columns={getMissingDataScriptColumns()}
                          dataSource={item.sqlResponseList}
                        />
                      </div>
                    </Panel>
                  );
                })}
              </Collapse>
            </div>
          ) : state.selectedBussinessActivityIds ? (
            <span style={{ textAlign: 'left', display: 'inline-block' }}>
              <EmptyNode
                desc={
                  '当前业务活动/业务流程暂未配置数据验证脚本，请先前往业务活动配置数据验证脚本'
                }
              />
            </span>
          ) : (
            <span style={{ textAlign: 'left', display: 'inline-block' }}>
              <EmptyNode desc={'请先选择业务活动或业务流程'} />
            </span>
          )
      }
    ];

    return formData;
  };

  return {
    title: (
      <span style={{ fontSize: 16 }}>
        数据验证设置
        <Tooltip
          title="数据验证可在压测的同时检测数据库表是否有数据发生，但会产生额外的性能消耗，建议仅在试跑时开启；默认关闭，如需启用请在每次压测启动手动开启。"
          placement="right"
          trigger="click"
        >
          <Icon type="question-circle" style={{ marginLeft: 4 }} />
        </Tooltip>
      </span>
    ),
    rowNum: 1,
    span: 24,
    formData: getDataVerificationConfigFormData()
  };
};

export default DataVerificationConfig;
