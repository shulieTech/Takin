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

import React, { Children, Fragment, useEffect } from 'react';
import {
  Row,
  Input,
  Icon,
  InputNumber,
  Col,
  Tooltip,
  Cascader,
  Button
} from 'antd';
import { useStateReducer, CommonTable, CommonSelect } from 'racc';
import { ColumnProps } from 'antd/lib/table';
import { customColumnProps } from 'src/components/custom-table/utils';
import InputNumberPro from 'src/common/inputNumber-pro';
import PressureTestSceneService from '../service';
declare var serverUrls: string;
interface Props {
  state?: any;
  setState?: (value) => void;
  value?: any;
  onChange?: (value: any) => void;
  dataSource?: any[];
  queryBussinessActivityAndScript?: void;
  queryBussinessFlowAndScript?: void;
}
interface State {
  list: any[];
}

const BusinessActivityConfigTable: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>({
    list: []
  });

  useEffect(() => {
    setState({
      list: props.value
    });
  }, [props.value]);

  function filter(inputValue, path) {
    return path.some(
      option => option.name.toLowerCase().indexOf(inputValue.toLowerCase()) > -1
    );
  }

  /** @name 业务活动配置 */
  const getColumns = (): ColumnProps<any>[] => {
    /**
     * @name 获取所有业务活动和脚本
     */
    const queryBussinessActivityAndScript = async () => {
      const {
        data: { success, data }
      } = await PressureTestSceneService.queryBussinessActivityAndScript({});
      if (success) {
        props.setState({
          bussinessActivityAndScriptList:
            data &&
            data.map((item, k) => {
              return {
                id: item.id,
                name: item.name,
                disabled: item.scriptList ? false : true,
                scriptList: item.scriptList
              };
            })
        });
      }
    };

    const displayRender = (labels, selectedOptions) =>
      labels.map((label, i) => {
        const option = selectedOptions[i];
        if (i === labels.length - 1) {
          return <span key={option.value}>{label}</span>;
        }
        return <span key={option.value}>{label} / </span>;
      });

    return [
      {
        ...customColumnProps,
        title: '业务活动名称',
        dataIndex: 'businessActivityId',
        render: (text, row, index) => {
          return props.state.configType === 2 ? (
            <CommonSelect
              disabled={true}
              dropdownMatchSelectWidth={false}
              allowClear={false}
              value={String(text)}
              style={{ width: 150 }}
              onChange={(value, options) => {
                handleChange('change', 'businessActivityId', value, index);
                handleChange(
                  'change',
                  'businessActivityName',
                  options.props.children,
                  index
                );
              }}
              dataSource={
                props.state.bussinessActiveList
                  ? props.state.bussinessActiveList
                  : []
              }
            />
          ) : (
            <Cascader
              allowClear={false}
              value={[
                row.businessActivityId && String(row.businessActivityId),
                row.scriptId
              ]}
              options={
                props.state.bussinessActivityAndScriptList
                  ? props.state.bussinessActivityAndScriptList
                  : []
              }
              fieldNames={{
                label: 'name',
                value: 'id',
                children: 'scriptList'
              }}
              changeOnSelect={false}
              onChange={(value, options) => {
                handleChange(
                  'change',
                  'businessActivityId',
                  value && value[0] && Number(value[0]),
                  index
                );
                handleChange(
                  'change',
                  'businessActivityName',
                  options[0].name,
                  index
                );
                handleChange('change', 'scriptId', value && value[1], index);
              }}
              // onFocus={() => queryBussinessActivityAndScript()}
              // displayRender={displayRender}
              showSearch={{ filter, matchInputWidth: false }}
            />
          );
        }
      },
      {
        ...customColumnProps,
        title: '目标TPS',
        dataIndex: 'targetTPS',
        render: (text, row, index) => {
          return (
            <InputNumber
              value={text}
              min={0}
              onChange={value =>
                handleChange('change', 'targetTPS', value, index)
              }
            />
          );
        }
      },
      {
        ...customColumnProps,
        title: '目标RT',
        dataIndex: 'targetRT',
        render: (text, row, index) => {
          return (
            <InputNumberPro
              style={{ width: 150 }}
              addonAfter="ms"
              value={text}
              min={0}
              onChange={value =>
                handleChange('change', 'targetRT', value, index)
              }
            />
          );
        }
      },
      {
        ...customColumnProps,
        title: '目标成功率',
        dataIndex: 'targetSuccessRate',
        render: (text, row, index) => {
          return (
            <InputNumberPro
              addonAfter="%"
              style={{ width: 150 }}
              value={text}
              min={0}
              max={100}
              precision={2}
              onChange={value =>
                handleChange('change', 'targetSuccessRate', value, index)
              }
            />
          );
        }
      },
      {
        ...customColumnProps,
        title: (
          <span>
            目标SA
            <Tooltip
              title="RT的达标率，请求中RT达到目标值的百分比"
              placement="bottom"
            >
              <Icon style={{ marginLeft: 8 }} type="question-circle" />
            </Tooltip>
          </span>
        ),
        dataIndex: 'targetSA',
        render: (text, row, index) => {
          return (
            <InputNumberPro
              addonAfter="%"
              style={{ width: 150 }}
              value={text}
              min={0}
              max={100}
              precision={2}
              onChange={value =>
                handleChange('change', 'targetSA', value, index)
              }
            />
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
        businessActivityId: '',
        businessActivityName: '',
        targetTPS: '',
        targetRT: '',
        targetSuccessRate: '',
        targetSA: ''
      });
    } else {
      state.list.splice(k, 1);
    }

    if (props.onChange) {
      props.onChange(state.list);
      props.setState({
        selectedBussinessActiveList: state.list,
        selectedBussinessActivityIds:
          state.list &&
          state.list.map((item, k1) => {
            return item.businessActivityId;
          })
      });
    }
  };

  return (
    <CommonTable
      rowKey={(row, index) => index.toString()}
      columns={getColumns()}
      size="small"
      dataSource={state.list}
    />
  );
};
export default BusinessActivityConfigTable;
