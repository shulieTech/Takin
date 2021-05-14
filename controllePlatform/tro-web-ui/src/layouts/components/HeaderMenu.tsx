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
 * @name 菜单栏
 */
import React, { Fragment } from 'react';
import { Menu } from 'antd';
import { connect } from 'dva';
import { AppModelState } from 'src/models/app';

import TabItem from './TabItem';
import { useStateReducer } from 'racc';
import router from 'umi/router';

interface Props extends AppModelState {
  navTabItem: any[];
}

const HeaderMenu: React.FC<Props> = props => {
  const [state, setState] = useStateReducer({
    navTabItem: []
  });

  const { breadCrumbs } = props;
  const keys = breadCrumbs.map(item => item.path);
  const title = breadCrumbs.map(item => item.title);
  // console.log(keys);

  // TODO 关闭导航选项卡
  const handlerCloseNavTabItem = e => {
    // console.log(e);
    if (state.navTabItem.length) {
      let closeTabIdx = 0;
      const tabNavListChange = state.navTabItem.filter((it, idx) => {
        closeTabIdx = idx;
        return it.keys !== e;
      });
      setState({
        navTabItem: tabNavListChange
      });
      router.push(state.navTabItem[closeTabIdx - 1]);
    } else {
      return;
    }
  };

  // TODO 添加导航选项卡
  const handlerAddNavTabItem = () => {
    if (keys.length) {
      const tabNavListTemp = state.navTabItem;
      let pushTabListAble = true;

      tabNavListTemp.map((item, idx) => {
        if (item.keys !== keys[keys.length - 1]) {
          tabNavListTemp[idx].active = false;
        } else {
          pushTabListAble = false;
          tabNavListTemp[idx].active = true;
          return;
        }
      });

      if (pushTabListAble) {
        tabNavListTemp.push({
          keys: keys[keys.length - 1],
          title: title[title.length - 1],
          active: true,
          handlerClose: handlerCloseNavTabItem
        });
      }

      setState({
        navTabItem: tabNavListTemp
      });

      // console.log(state.navTabItem);
    }
  };

  return (
    <Fragment>
      <div
        style={{
          display: 'flex',
          justifyContent: 'start',
          verticalAlign: 'top'
        }}
      >
        {state.navTabItem.map(item => TabItem(item))}
      </div>
    </Fragment>
  );
};

export default connect(({ app }) => ({ ...app }))(HeaderMenu);
