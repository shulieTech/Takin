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
import { Col, Row } from 'antd';
import { CommonSelect, useStateReducer } from 'racc';
import React, { Fragment, useEffect } from 'react';
import ScriptManageService from '../service';
interface Props {
  value?: any[];
  onChange?: (value: any[] | any) => void;
  relatedId: string;
  relatedType: string;
  getRequired: (required: boolean) => void;
}
const RelatePlugin: React.FC<Props> = props => {
  const [state, setState] = useStateReducer({
    pluginList: [],
    value: []
  });
  useEffect(() => {
    if (props.relatedId && props.relatedType) {
      queryPluginList();
    }
  }, [props.relatedId, props.relatedType]);
  useEffect(() => {
    setState({ value: props.value || [] });
  }, [props.value]);
  const queryPluginList = async () => {
    const {
      data: { data, success }
    } = await ScriptManageService.queryPluginList({
      relatedType: props.relatedType,
      relatedId: props.relatedId
    });
    if (success) {
      setState({ pluginList: data || [] });
      props.getRequired(data && data.length);
    }
  };
  const handleChange = (value, index) => {
    const result = [...state.value];
    result[index] = value;
    setState({ value: result });
    const isEmpty = !result.length || result.find(item => !item);
    if (isEmpty) {
      props.onChange([]);
      return;
    }
    props.onChange(result);
  };
  // if (!state.pluginList.length) {
  //   return <Fragment>暂无插件列表</Fragment>;
  // }
  return (
    <Fragment>
      {state.pluginList.map((item, index) => (
        <PluginSelectItem
          key={index}
          value={state.value[index]}
          onChange={value => handleChange(value, index)}
          {...item}
        />
      ))}
    </Fragment>
  );
};
export default RelatePlugin;

interface PluginSelectItemProps {
  type: string;
  singlePluginRenderResponseList: any[];
  onChange?: (value: any) => void;
  value?: any;
}
const PluginSelectItem: React.FC<PluginSelectItemProps> = props => {
  const [state, setState] = useStateReducer({
    name: undefined,
    version: undefined,
    versionList: []
  });
  useEffect(() => {
    if (state.name) {
      queryVersionList();
    }
  }, [state.name]);
  useEffect(() => {
    if (props.value) {
      setState({ name: props.value.name, version: props.value.version });
      return;
    }
    setState({ name: undefined, version: undefined, versionList: [] });
  }, [props.value]);
  const queryVersionList = async () => {
    const {
      data: { data, success }
    } = await ScriptManageService.queryPluginVersionList({
      pluginId: state.name
    });
    if (success) {
      setState({
        versionList: data.versionList.map(item => ({
          label: item,
          value: item
        }))
      });
    }
  };
  useEffect(() => {
    if (state.version) {
      handleChange();
    }
  }, [state.version]);
  const handleChange = () => {
    let result = null;
    if (state.name && state.version) {
      result = {
        name: state.name,
        version: state.version,
        type: props.type
      };
    }
    props.onChange(result);
  };
  return (
    <Row type="flex" className="mg-b1x">
      <Col span={9}>
        <CommonSelect
          placeholder="请选择类型"
          onChange={name => setState({ name, version: undefined })}
          value={state.name}
          dataSource={props.singlePluginRenderResponseList}
          optionFilterProp="children"
          showSearch
          allowClear={false}
        />
      </Col>
      <Col span={9} push={1}>
        <CommonSelect
          onChange={version => setState({ version })}
          value={state.version}
          placeholder="请选择版本"
          dataSource={state.versionList}
          optionFilterProp="children"
          showSearch
          allowClear={false}
        />
      </Col>
    </Row>
  );
};
