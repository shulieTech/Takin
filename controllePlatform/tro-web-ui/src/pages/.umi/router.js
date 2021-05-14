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

import React from 'react';
import {
  Router as DefaultRouter,
  Route,
  Switch,
  StaticRouter,
} from 'react-router-dom';
import dynamic from 'umi/dynamic';
import renderRoutes from 'umi/lib/renderRoutes';
import history from '@@/history';

const Router = require('dva/router').routerRedux.ConnectedRouter;

const routes = [
  {
    path: '/',
    component: __IS_BROWSER
      ? dynamic({
          loader: () => import('../../layouts/index.tsx'),
          loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
            .default,
        })
      : require('../../layouts/index.tsx').default,
    routes: [
      {
        path: '/404',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../404.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../404.tsx').default,
      },
      {
        path: '/appManage/details',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../appManage/detailsPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../appManage/detailsPage.tsx').default,
      },
      {
        path: '/appManage/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../appManage/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../appManage/indexPage.tsx').default,
      },
      {
        path: '/bottleneckTable/bottleneckDetails/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () =>
                import('../bottleneckTable/bottleneckDetails/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../bottleneckTable/bottleneckDetails/indexPage.tsx')
              .default,
      },
      {
        path: '/bottleneckTable/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../bottleneckTable/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../bottleneckTable/indexPage.tsx').default,
      },
      {
        path: '/businessActivity/addEdit',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../businessActivity/addEditPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../businessActivity/addEditPage.tsx').default,
      },
      {
        path: '/businessActivity/details',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../businessActivity/detailsPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../businessActivity/detailsPage.tsx').default,
      },
      {
        path: '/businessActivity/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../businessActivity/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../businessActivity/indexPage.tsx').default,
      },
      {
        path: '/capacityManage/pressureMeasurementConfig/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () =>
                import('../capacityManage/pressureMeasurementConfig/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../capacityManage/pressureMeasurementConfig/indexPage.tsx')
              .default,
      },
      {
        path: '/capacityManage/pressureMeasurementRecord/details',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () =>
                import('../capacityManage/pressureMeasurementRecord/detailsPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../capacityManage/pressureMeasurementRecord/detailsPage.tsx')
              .default,
      },
      {
        path: '/capacityManage/pressureMeasurementRecord/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () =>
                import('../capacityManage/pressureMeasurementRecord/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../capacityManage/pressureMeasurementRecord/indexPage.tsx')
              .default,
      },
      {
        path: '/configCenter/bigDataConfig/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () =>
                import('../configCenter/bigDataConfig/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../configCenter/bigDataConfig/indexPage.tsx').default,
      },
      {
        path: '/configCenter/blacklist/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../configCenter/blacklist/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../configCenter/blacklist/indexPage.tsx').default,
      },
      {
        path: '/configCenter/entryRule/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../configCenter/entryRule/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../configCenter/entryRule/indexPage.tsx').default,
      },
      {
        path: '/configCenter/keyConfig/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../configCenter/keyConfig/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../configCenter/keyConfig/indexPage.tsx').default,
      },
      {
        path: '/configCenter/operationLog/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () =>
                import('../configCenter/operationLog/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../configCenter/operationLog/indexPage.tsx').default,
      },
      {
        path: '/configCenter/pressureMeasureSwitch/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () =>
                import('../configCenter/pressureMeasureSwitch/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../configCenter/pressureMeasureSwitch/indexPage.tsx')
              .default,
      },
      {
        path: '/configCenter/whitelistSwitch/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () =>
                import('../configCenter/whitelistSwitch/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../configCenter/whitelistSwitch/indexPage.tsx').default,
      },
      {
        path: '/dashboard/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../dashboard/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../dashboard/indexPage.tsx').default,
      },
      {
        path: '/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../index/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../index/indexPage.tsx').default,
      },
      {
        path: '/linkMark/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../linkMark/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../linkMark/indexPage.tsx').default,
      },
      {
        path: '/pressureTestManage/pressureTestReport/details',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () =>
                import('../pressureTestManage/pressureTestReport/detailsPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../pressureTestManage/pressureTestReport/detailsPage.tsx')
              .default,
      },
      {
        path: '/pressureTestManage/pressureTestReport/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () =>
                import('../pressureTestManage/pressureTestReport/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../pressureTestManage/pressureTestReport/indexPage.tsx')
              .default,
      },
      {
        path: '/pressureTestManage/pressureTestReport/pressureTestLive',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () =>
                import('../pressureTestManage/pressureTestReport/pressureTestLivePage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../pressureTestManage/pressureTestReport/pressureTestLivePage.tsx')
              .default,
      },
      {
        path: '/pressureTestManage/pressureTestScene/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () =>
                import('../pressureTestManage/pressureTestScene/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../pressureTestManage/pressureTestScene/indexPage.tsx')
              .default,
      },
      {
        path: '/pressureTestManage/pressureTestScene/pressureTestSceneConfig',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () =>
                import('../pressureTestManage/pressureTestScene/pressureTestSceneConfigPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../pressureTestManage/pressureTestScene/pressureTestSceneConfigPage.tsx')
              .default,
      },
      {
        path: '/scriptManage/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../scriptManage/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../scriptManage/indexPage.tsx').default,
      },
      {
        path: '/scriptManage/scriptConfig',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../scriptManage/scriptConfigPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../scriptManage/scriptConfigPage.tsx').default,
      },
      {
        path: '/user/login',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../user/loginPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../user/loginPage.tsx').default,
      },
      {
        path: '/userManage/',
        exact: true,
        component: __IS_BROWSER
          ? dynamic({
              loader: () => import('../userManage/indexPage.tsx'),
              loading: require('/Users/chuxu/shulie_project/full-link/src/common/loading')
                .default,
            })
          : require('../userManage/indexPage.tsx').default,
      },
    ],
  },
  {
    component: () =>
      React.createElement(
        require('/Users/chuxu/shulie_project/full-link/node_modules/umi-build-dev/lib/plugins/404/NotFound.js')
          .default,
        { pagesPath: 'src/pages', hasRoutesInConfig: false },
      ),
  },
];
window.g_routes = routes;
const plugins = require('umi/_runtimePlugin');
plugins.applyForEach('patchRoutes', { initialValue: routes });

export { routes };

export default class RouterWrapper extends React.Component {
  unListen() {}

  constructor(props) {
    super(props);

    // route change handler
    function routeChangeHandler(location, action) {
      plugins.applyForEach('onRouteChange', {
        initialValue: {
          routes,
          location,
          action,
        },
      });
    }
    this.unListen = history.listen(routeChangeHandler);
    // dva 中 history.listen 会初始执行一次
    // 这里排除掉 dva 的场景，可以避免 onRouteChange 在启用 dva 后的初始加载时被多执行一次
    const isDva =
      history.listen
        .toString()
        .indexOf('callback(history.location, history.action)') > -1;
    if (!isDva) {
      routeChangeHandler(history.location);
    }
  }

  componentWillUnmount() {
    this.unListen();
  }

  render() {
    const props = this.props || {};
    return <Router history={history}>{renderRoutes(routes, props)}</Router>;
  }
}
