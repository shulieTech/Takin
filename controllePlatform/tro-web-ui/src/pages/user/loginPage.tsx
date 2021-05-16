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

import { Icon, Input, message, notification, Popover } from 'antd';
import { connect } from 'dva';
import { CommonForm } from 'racc';
import { FormDataType } from 'racc/dist/common-form/type';
import React, { Fragment } from 'react';
import DvaComponent from 'src/components/basic-component/DvaComponent';
import UserService from 'src/services/user';
import request from 'src/utils/request';
import router from 'umi/router';
import styles from './indexPage.less';
interface Props {}

const state = {
  nums: null,
  color: null,
  rotate: null,
  fz: null,
  imgSrc: ''
};
type State = Partial<typeof state>;
const getFormData = (that: Login): FormDataType[] => {
  return [
    {
      key: 'username',
      label: '',
      options: {
        rules: [
          {
            required: true,
            message: '请输入账号'
          }
        ]
      },
      node: (
        <Input
          className={styles.inputStyle}
          prefix={<Icon type="user" className={styles.prefixIcon} />}
          placeholder="账号"
        />
      )
    },
    {
      key: 'password',
      label: '',
      options: {
        rules: [
          {
            required: true,
            message: '请输入密码'
          }
        ]
      },
      node: (
        <Input
          className={styles.inputStyle}
          prefix={<Icon type="lock" className={styles.prefixIcon} />}
          placeholder="密码"
          type="password"
        />
      )
    },
    {
      key: 'code',
      label: '',
      options: {
        rules: [
          {
            required: true,
            message: '请输入验证码'
          }
        ]
      },
      node: (
        <Input
          style={{ width: 205 }}
          className={styles.inputStyle}
          prefix={<Icon type="safety" className={styles.prefixIcon} />}
          placeholder="验证码"
        />
      ),
      extra: (
        <div style={{ display: 'inline-block' }}>
          <img style={{ marginLeft: 16 }} src={that.state.imgSrc} />
          <span
            style={{ marginLeft: 8, cursor: 'pointer' }}
            onClick={that.refresh}
          >
            <Icon type="redo" />
          </span>
        </div>
      )
    }
  ];
};
declare var serverUrl: string;
@connect()
export default class Login extends DvaComponent<Props, State> {
  namespace = 'user';
  state = state;

  componentDidMount = () => {
    // this.setState(this.initState());
    this.queryCode();
  };

  getRandom = (max, min, num?) => {
    const asciiNum = Math.random() * (max - min + 1) + min;
    if (!Boolean(num)) {
      return Math.floor(asciiNum);
    }
    const arr = [];
    // tslint:disable-next-line:no-increment-decrement
    for (let i = 0; i < num; i++) {
      arr.push(this.getRandom(max, min));
    }
    return arr;
  };

  color16 = () => {
    const r = Math.floor(Math.random() * 256);
    const g = Math.floor(Math.random() * 256);
    const b = Math.floor(Math.random() * 256);
    // tslint:disable-next-line:prefer-template
    const color = '#' + r.toString(16) + g.toString(16) + b.toString(16);
    return color;
  };

  initState = () => {
    return {
      nums: this.getRandom(9, 0, 4),
      rotate: this.getRandom(30, -30, 4),
      fz: this.getRandom(16, 20, 4),
      color: [this.color16(), this.color16(), this.color16(), this.color16()]
    };
  };

  refresh = () => {
    this.queryCode();
  };

  queryCode = async () => {
    const { data, status, headers } = await request({
      url: `${serverUrl}/verification/code`,
      responseType: 'blob',
      headers: {
        'Access-Token': localStorage.getItem('Access-Token')
      }
    });

    const url = URL.createObjectURL(data);
    this.setState({
      imgSrc: url
    });
    localStorage.setItem('Access-Token', headers['access-token']);
  };

  handleSubmit = async (err, value) => {
    if (err) {
      return;
    }
    // this.handleDispatch('troLogin', value);

    const {
      data: { success, data }
    } = await UserService.troLogin(value);
    if (success) {
      notification.success({
        message: '通知',
        description: '登录成功',
        duration: 1.5
      });
      localStorage.setItem('troweb-userName', data.name);
      localStorage.setItem('troweb-userId', data.id);
      localStorage.setItem('troweb-role', data.userType);

      localStorage.setItem('full-link-token', data.xToken);
      localStorage.setItem('troweb-expire', data.expire);
      localStorage.removeItem('Access-Token');
      // if (data.role === 1) {
      //   router.push('/appTrialManage');
      //   return;
      // }
      router.push('/');
    }
  };

  content = () => {
    return (
      <div style={{ position: 'relative', zIndex: 100000 }}>
        <p className={styles.wechat}>微信扫码联系</p>
        <img
          style={{ width: 100 }}
          src={require('./../../assets/wechat.png')}
        />
      </div>
    );
  };

  render() {
    return (
      <div className={styles.mainWrap}>
        <img
          className={styles.bg1}
          src={require('./../../assets/login_bg.png')}
        />
        <img
          className={styles.bg2}
          src={require('./../../assets/login_bg2.png')}
        />
        <img
          className={styles.bg3}
          src={require('./../../assets/login_img.png')}
        />
        <img className={styles.bg4} src={require('./../../assets/logo.png')} />
        <div className={styles.main}>
          <div className={styles.login}>
            <p className={styles.sysName}>全链路压测</p>
            <CommonForm
              formData={getFormData(this)}
              rowNum={1}
              onSubmit={this.handleSubmit}
              btnProps={{
                isResetBtn: false,
                isSubmitBtn: true,
                submitText: '登录',
                submitBtnProps: {
                  style: { width: 329, marginTop: 20 },
                  type: 'primary'
                }
              }}
            />
            <p className={styles.applyAccount}>
              <Popover
                content={this.content()}
                trigger="click"
                placement="bottom"
              >
                <a>申请账号</a>
              </Popover>
            </p>
          </div>
        </div>
      </div>
    );
  }
}
