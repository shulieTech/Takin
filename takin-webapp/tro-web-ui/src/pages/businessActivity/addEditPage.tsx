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
import { Spin } from 'antd';
import { useCreateContext, useStateReducer } from 'racc';
import React, { useEffect } from 'react';
import { MainPageLayout } from 'src/components/page-layout';
import { Basic } from 'src/types';
import AddEditExtra from './components/AddEditExtra';
import BasicInfo from './components/BasicInfo';
import ChainInfo from './components/ChainInfo';
import BusinessActivityService from './service';

const getInitState = () => ({
  systemName: undefined,
  service: undefined,
  serviceName: undefined,
  serviceType: undefined,
  app: undefined,
  appName: undefined,
  serviceList: [],
  loading: true,
  isCore: undefined,
  link_level: undefined,
  businessDomain: undefined
});
type State = ReturnType<typeof getInitState>;
export const AddEditSystemPageContext = useCreateContext<State>();

interface Props extends Basic.BaseProps {}
const AddEditSystemPage: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<State>(getInitState());
  const id = props.location.query.id;
  useEffect(() => {
    if (id) {
      queryDetails();
    }
  }, []);
  const queryDetails = async () => {
    const {
      data: { data, success }
    } = await BusinessActivityService.querySystemProcess({
      id: props.location.query.id
    });
    if (success) {
      setState({
        systemName: data.activityName,
        app: data.applicationName,
        serviceType: data.type,
        service: data.linkId,
        isCore: data.isCore,
        link_level: data.link_level,
        businessDomain: data.businessDomain
      });
    }
  };
  return (
    <AddEditSystemPageContext.Provider value={{ state, setState }}>
      <MainPageLayout title="业务活动配置" extra={<AddEditExtra id={id} />}>
        <Spin spinning={state.loading}>
          <BasicInfo />
          {/* <ChainProblem /> */}
          <ChainInfo />
        </Spin>
      </MainPageLayout>
    </AddEditSystemPageContext.Provider>
  );
};
export default AddEditSystemPage;

export const getEntranceInfo = (dataSource, value) => {
  return dataSource.find(item => item.value === value);
};
