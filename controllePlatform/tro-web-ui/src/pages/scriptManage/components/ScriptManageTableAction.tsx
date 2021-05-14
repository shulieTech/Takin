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
import { Button, Statistic, Tooltip } from 'antd';
import Link from 'umi/link';
import styles from './../index.less';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';

interface Props {
  state?: any;
  setState?: (value) => void;
}
const ScriptManageTableAction: React.FC<Props> = props => {
  const btnAuthority: any =
    localStorage.getItem('trowebBtnResource') &&
    JSON.parse(localStorage.getItem('trowebBtnResource'));
  return (
    <Fragment>
      <AuthorityBtn isShow={btnAuthority && btnAuthority.scriptManage_2_create}>
        <Link to="/scriptManage/scriptConfig?action=add">
          <Button type="primary">新增脚本</Button>
        </Link>
      </AuthorityBtn>
    </Fragment>
  );
};
export default ScriptManageTableAction;
