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
import { Row, Input, Icon, Card } from 'antd';
import { useStateReducer, CommonTable, CommonSelect } from 'racc';
import { ColumnProps } from 'antd/lib/table';
import { customColumnProps } from 'src/components/custom-table/utils';
import Rule from './Rule';

interface Props {
  title: string | React.ReactNode;
  value?: any;
  onChange?: (value: any) => void;
  state?: any;
  dictionaryMap?: any;
}
interface State {
  list: any[];
}

const SLAConfigStopTable: React.FC<Props> = props => {
  const { dictionaryMap } = props;
  const [state, setState] = useStateReducer<State>({
    list: []
  });

  useEffect(() => {
    setState({
      list: props.value
    });
  }, []);

  /** @name 终止条件配置 */
  const getColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...customColumnProps,
        title: '名称',
        dataIndex: 'ruleName',
        render: (text, row, index) => {
          return (
            <Input
              placeholder="请输入规则名称"
              value={text}
              onChange={e =>
                handleChange('change', 'ruleName', e.target.value, index)
              }
            />
          );
        }
      },
      {
        ...customColumnProps,
        title: '对象',
        dataIndex: 'businessActivity',
        render: (text, row, index) => {
          return (
            <CommonSelect
              dropdownMatchSelectWidth={false}
              value={text}
              mode="multiple"
              dataSource={
                props.state.selectedBussinessActiveList
                  ? [
                    {
                      label: '全部',
                      value: '-1'
                    }
                  ].concat(
                      props.state.selectedBussinessActiveList.map(item => {
                        return {
                          label: item.businessActivityName,
                          value: String(item.businessActivityId)
                        };
                      })
                    )
                  : []
              }
              style={{ width: 150 }}
              onChange={value =>
                handleChange('change', 'businessActivity', value, index)
              }
              onRender={item => (
                <CommonSelect.Option key={item.value} value={item.value}>
                  {item.label}
                </CommonSelect.Option>
              )}
            />
          );
        }
      },
      {
        ...customColumnProps,
        title: '规则',
        dataIndex: 'rule',
        render: (text, row, index) => {
          return (
            <Rule
              dictionaryMap={dictionaryMap}
              value={text}
              onChange={value => handleChange('change', 'rule', value, index)}
            />
          );
        }
      },
      {
        ...customColumnProps,
        title: '操作',
        dataIndex: 'action',
        render: (text, row, index) => {
          return (
            <span>
              {index <= state.list.length - 1 && state.list.length !== 1 && (
                <Icon
                  type="minus-circle"
                  style={{ color: '#11BBD5', marginLeft: 5 }}
                  onClick={() => handleChange('minus', '', '', index)}
                />
              )}
              {index === state.list.length - 1 && (
                <Icon
                  type="plus-circle"
                  style={{ color: '#11BBD5', marginLeft: 5 }}
                  onClick={() => handleChange('plus', '', '', index)}
                />
              )}
            </span>
          );
        }
      }
    ];
  };

  const handleChange = (type, key, value, k) => {
    if (type === 'change') {
      state.list.splice(k, 1, { ...state.list[k], [key]: value });
    } else if (type === 'plus') {
      state.list.push({
        ruleName: '',
        businessActivity: undefined,
        rule: ''
      });
    } else {
      state.list.splice(k, 1);
    }

    if (props.onChange) {
      props.onChange(state.list);
    }
  };

  return (
    <Card title={props.title}>
      <CommonTable
        rowKey={(row, index) => index.toString()}
        columns={getColumns()}
        size="small"
        dataSource={state.list}
      />
    </Card>
  );
};
export default SLAConfigStopTable;
