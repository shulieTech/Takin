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
import { Button, Icon } from 'antd';
import React, { useContext } from 'react';
import AuthorityBtn from 'src/common/authority-btn/AuthorityBtn';
import { MapUserAuthority } from 'src/utils/utils';
import { router } from 'umi';
import { BusinessActivityDetailsContext } from '../detailsPage';
import styles from '../index.less';
import FlowVerificateModal from '../modals/FlowVerificateModal';

declare var window: any;

interface Props {}
const ActionNode: React.FC<Props> = props => {
  const { state, setState } = useContext(BusinessActivityDetailsContext);
  return (
    <div
      className={styles.actions}
      style={{ left: state.siderVisible ? 300 : 8 }}
    >
      <span
        className={styles.siderIcon}
        onClick={() => setState({ siderVisible: !state.siderVisible })}
      >
        <Icon
          style={{ color: '#fff' }}
          type={state.siderVisible ? 'left' : 'right'}
        />
      </span>
      <AuthorityBtn isShow={MapUserAuthority('scriptManage')}>
        <FlowVerificateModal id={state.details.activityId} />
      </AuthorityBtn>
      <AuthorityBtn isShow={MapUserAuthority('debugTool_linkDebug')}>
        <Button
          type="primary"
          className="mg-l1x"
          onClick={() => {
            window.g_app._store.dispatch({
              type: 'app/updateState',
              payload: {
                debugToolId: state.details.activityId.toString()
              }
            });
            router.push(`/debugTool/linkDebug`);
          }}
        >
          去调试
        </Button>
      </AuthorityBtn>
    </div>
  );
};
export default ActionNode;
