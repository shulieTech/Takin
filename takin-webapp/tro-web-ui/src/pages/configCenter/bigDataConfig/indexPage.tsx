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
import { Icon, Tooltip } from 'antd';
import { useStateReducer } from 'racc';
import React, { Fragment } from 'react';
import SearchTable from 'src/components/search-table';
import getColumns from './components/TableNode';
interface Props {}
const getInitState = () => ({
  reload: false
});
export type BigDataConfigState = ReturnType<typeof getInitState>;
const BigDataConfig: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<BigDataConfigState>(getInitState());
  const title: React.ReactNode = (
    <Fragment>
      开关配置
      <Tooltip title="开关配置可控制大数据中心的相关配置信息，如采集率、插件开关等">
        <Icon className="mg-l1x" type="question-circle" />
      </Tooltip>
    </Fragment>
  );
  return (
    <Fragment>
      <SearchTable
        commonTableProps={{
          columns: getColumns(state, setState),
          size: 'small'
        }}
        title={title}
        // commonFormProps={{ formData: getFormData(state, setState), rowNum: 6 }}
        ajaxProps={{ url: '/pradar/switch/list', method: 'GET' }}
        toggleRoload={state.reload}
      />
    </Fragment>
  );
};
export default BigDataConfig;
