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
import { Input } from 'antd';
import { connect } from 'dva';
import { CommonSelect } from 'racc';
import React, { Fragment, useContext, useEffect } from 'react';
import BusinessSelect from 'src/common/business-select';
import { CommonModelState } from 'src/models/common';
import Header from '../../linkMark/components/Header';
import { AddEditSystemPageContext } from '../addEditPage';
import styles from '../index.less';
import BusinessActivityService from '../service';

interface Props extends CommonModelState {}
const BasicInfo: React.FC<Props> = props => {
  const { state, setState } = useContext(AddEditSystemPageContext);
  useEffect(() => {
    if (state.app && state.serviceType) {
      queryServiceList();
    }
  }, [state.app, state.serviceType]);
  const queryServiceList = async () => {
    const {
      data: { data, success }
    } = await BusinessActivityService.queryServiceList({
      applicationName: state.app,
      type: state.serviceType
    });
    if (success) {
      setState({ serviceList: data });
    }
  };
  return (
    <Fragment>
      <Header title="基础信息" />
      <div style={{ margin: '16px 0 0' }}>
        <div className="flex">
          <div>
            <span style={labelStyle}>*</span>业务活动名称：
            <Input
              value={state.systemName}
              onChange={e => setState({ systemName: e.target.value })}
              maxLength={50}
              style={{ width: 300 }}
              placeholder="请输入业务活动名称"
            />
          </div>
          <div className="mg-l2x">
            <span style={labelStyle}>*</span>业务活动类型：
            <CommonSelect
              value={state.isCore}
              style={{ width: 300 }}
              onChange={(isCore: any) => setState({ isCore })}
              placeholder="请选择业务活动类型"
              showSearch
              dataSource={props.dictionaryMap && props.dictionaryMap.isCore}
              optionFilterProp="children"
            />
          </div>
        </div>
        <div className="flex mg-t2x">
          <div>
            <span style={labelStyle}>*</span>业务活动级别：
            <CommonSelect
              value={state.link_level}
              style={{ width: 300 }}
              onChange={(value: any) => setState({ link_level: value })}
              placeholder="请选择业务活动级别"
              showSearch
              dataSource={props.dictionaryMap && props.dictionaryMap.link_level}
              optionFilterProp="children"
            />
          </div>
          <div style={{ marginLeft: 52 }}>
            <span style={labelStyle}>*</span>业务域：
            <CommonSelect
              value={state.businessDomain}
              style={{ width: 300 }}
              onChange={(businessDomain: any) => setState({ businessDomain })}
              placeholder="请选择业务域"
              showSearch
              dataSource={props.dictionaryMap && props.dictionaryMap.domain}
              optionFilterProp="children"
            />
          </div>
        </div>
        <div className="mg-t2x">
          <span style={{ marginLeft: 24 }}>
            <span style={labelStyle}>*</span>
            选择服务：
          </span>
          <BusinessSelect
            value={state.app}
            style={{ width: 300 }}
            onChange={(value: any, options: any) =>
              setState({
                app: value,
                appName: options && options.props.children,
                service: undefined
              })
            }
            url="/application/names"
            placeholder="请选择应用"
            showSearch
            optionFilterProp="children"
            onLoad={() => setState({ loading: false })}
          />
          <BusinessSelect
            value={state.serviceType}
            className="mg-l2x mg-r2x"
            url="/application/entrances/types"
            style={{ width: 300 }}
            onChange={serviceType =>
              setState({ serviceType, service: undefined })
            }
            placeholder="请选择服务类型"
          />
          <CommonSelect
            value={state.service}
            dataSource={state.serviceList}
            style={{ width: 300 }}
            onChange={(service, options: any) =>
              setState({
                service,
                serviceName: options && options.props.children
              })
            }
            placeholder="请选择服务"
            showSearch
            optionFilterProp="children"
            dropdownClassName={styles.select}
            // onRender={item => (
            //   <Select.Option key={item.value}>{renderToolTipItem(item.label, 30)}</Select.Option>
            // )}
          />
        </div>
      </div>
    </Fragment>
  );
};
export default connect(({ common }) => ({ ...common }))(BasicInfo);

const labelStyle: React.CSSProperties = {
  color: '#f5222d',
  marginRight: 8
};
