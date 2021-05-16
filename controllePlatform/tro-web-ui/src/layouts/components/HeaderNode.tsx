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
import React, { Fragment, useReducer, useEffect } from 'react';
import { Menu } from 'antd';
import { connect } from 'dva';
import { AppModelState } from 'src/models/app';
import styles from '../index.less';
import venomBasicConfig from 'src/venom.config';
import renderMenuNode from './MenuNode';
import TabItem from './TabItem';
import { useStateReducer } from 'racc';
import router from 'umi/router';
import _ from 'lodash';

interface Props extends AppModelState {
  navTabItem: any[];
  navTabChecked: string;
}

const HeaderMenu: React.FC<Props> = props => {
  const [state, setState] = useStateReducer({
    navTabItem: [
      {
        title: '',
        path: '/'
      }
    ],
    navTabChecked: null
  });

  const { navTabItem, navTabChecked } = state;

  const { breadCrumbs } = props;

  useEffect(() => {
    if (breadCrumbs.length <= 0) {
      return;
    }

    const breadCrumbsTemp = breadCrumbs;
    if (
      navTabItem.find(
        item =>
          item.path === _.replace(window.location.hash, '#', '').split('?')[0]
      )
    ) {
      return setState({
        navTabItem,
        navTabChecked: _.replace(window.location.hash, '#', '').split('?')[0]
      });
    }

    navTabItem.push(
      breadCrumbsTemp.filter(
        item =>
          item.path === _.replace(window.location.hash, '#', '').split('?')[0]
      )[0]
    );

    setState({
      navTabItem,
      navTabChecked: _.replace(window.location.hash, '#', '').split('?')[0]
    });

    // return () => {
    //   cleanup
    // }
  }, [breadCrumbs]);

  // TODO 关闭导航选项卡
  const handlerCloseNavTabItem = (tabPathData, closeTabIdx) => {
    // const navTabItemTemp = navTabItem;

    // navTabItem = navTabItemTemp.filter((item, index) => item.path !== tabPathData);

    setState({
      navTabItem: navTabItem.filter(item => item.path !== tabPathData),
      navTabChecked:
        closeTabIdx === 0
          ? navTabItem[closeTabIdx + 1].path
          : navTabItem[closeTabIdx - 1].path
    });
    router.push(
      closeTabIdx === 0
        ? navTabItem[closeTabIdx + 1].path
        : navTabItem[closeTabIdx - 1].path
    );
    // setState;
  };

  return (
    <Fragment>
      <div
        style={{
          display: 'flex',
          justifyContent: 'start',
          verticalAlign: 'top',
          backgroundColor: '#354153'
        }}
      >
        {navTabItem &&
          navTabItem.map((item, index) =>
            TabItem({
              navTabChecked,
              handlerClose: handlerCloseNavTabItem,
              itemData: item,
              itemIndex: index
            })
          )}
      </div>
    </Fragment>
  );
};

export default connect(({ app }) => ({ ...app }))(HeaderMenu);
