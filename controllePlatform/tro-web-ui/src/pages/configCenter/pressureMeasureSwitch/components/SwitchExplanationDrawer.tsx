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
import { CommonDrawer } from 'racc';
import styles from './../index.less';

interface Props {
  action?: string;
  id?: string | Number;
  titles?: string | React.ReactNode;
  disabled?: boolean;
  onSccuess?: () => void;
}
interface State {}
const SwitchExplanationDrawer: React.FC<Props> = props => {
  return (
    <CommonDrawer
      btnText="「全局压测开关说明」"
      drawerProps={{
        width: 560,
        title: '全局压测开关说明',
        maskClosable: false
      }}
      footer={null}
      btnProps={{
        type: 'link',
        style: {
          border: 'none',
          padding: 0,
          height: 12
        }
      }}
    >
      <div>
        <h4 className={styles.h4}>概述：</h4>
        <p className={styles.txts}>
          压测开关是控制系统压测配置是否生效的工具。「开启状态」系统所有压测配置生效，可保障线上安全压测；「关闭状态」所有应用接入配置失效，
          <span style={{ color: '#FC6849' }}>当前状态不能做压测任务</span>
          ，以免出现压测数据写入生产等风险。
        </p>
        <h4 className={styles.h4}>关闭操作的影响：</h4>
        <p className={styles.txts}>
          <p>
            01.
            关闭过程可能需要20分钟，期间应用配置信息均无法修改，已录入信息不生效，不能执行压测任务；
          </p>
          <p>
            02.
            关闭「压测总开关」后，所有压测配置均会失效，同时也会释放这些配置所占用的机器性能，此时不能执行压测任务，以免出现压测数据写入生产等风险。
          </p>
        </p>
        <h4 className={styles.h4}>开启操作的影响：</h4>
        <p className={styles.txts}>
          <p>
            01.
            开启过程可能需要20分钟，期间应用配置信息不生效，也无法修改，此时不能执行压测任务；
          </p>
          <p>
            02.
            开启「压测总开关」后，所有压测配置均会生效，确认各项压测配置正常后可进行安全压测。同时这些配置也会占用部分机器性能。
          </p>
        </p>
        <h4 className={styles.h4}>风险/警告：</h4>
        <p className={styles.txts}>
          <p>
            01.
            压测过程中禁止操作「全局压测开关」，否则会导致一系列的异常，包括压测数据写入生产、挡板失效等。
          </p>
          <p>
            02.
            压测总开关关闭过程中可能出现异常问题，此时，压测配置不生效，配置占用的性能不会完全释放。请根据异常提示处理对应问题。
          </p>
          <p>
            03.
            压测总开关开启过程中可能出现异常问题，需根据异常提示处理对应问题，保证没有遗留异常后，「压测总开关开启」，压测配置生效。
          </p>
          <p>
            04.「压测总开关」在操作执行过程中，如出现的异常，必须将所有问题处理完毕后，才能进行后续操作。
          </p>
        </p>
        <h4 className={styles.h4}>如有疑问，请向数列人员咨询</h4>
      </div>
    </CommonDrawer>
  );
};
export default SwitchExplanationDrawer;
