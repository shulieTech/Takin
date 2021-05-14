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

import React, { Fragment } from 'react';
import { Button, Tooltip, message, Checkbox } from 'antd';
import CustomPopconfirm from 'src/components/custom-popconfirm/CustomPopconfirm';
import BlacklistService from '../service';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';
interface Props {
  state: any;
  setState: (value: any) => void;
}
const BlacklistFooterAction: React.FC<Props> = props => {
  const { state, setState } = props;
  const { checkedKeys } = state;

  const btnAuthority: any =
    localStorage.getItem('trowebBtnResource') &&
    JSON.parse(localStorage.getItem('trowebBtnResource'));
  /**
   * @name 批量删除黑名单
   */
  const handleDestroy = async () => {
    const {
      data: { data, success }
    } = await BlacklistService.deleteBlacklist({
      blistIds: checkedKeys
    });
    if (success) {
      message.success(`删除成功`);
      setState({
        isReload: !state.isReload
      });
    }
  };

  return (
    <Fragment>
      <AuthorityBtn
        isShow={btnAuthority && btnAuthority.configCenter_blacklist_4_delete}
      >
        <CustomPopconfirm
          disabled={checkedKeys.length === 0 ? true : false}
          title="是否确定删除？"
          okColor="#FE7D61"
          onConfirm={() => handleDestroy()}
        >
          <Button
            style={{ marginLeft: 10 }}
            disabled={checkedKeys.length === 0 ? true : false}
            type="link"
          >
            批量删除
          </Button>
        </CustomPopconfirm>
      </AuthorityBtn>
    </Fragment>
  );
};
export default BlacklistFooterAction;
