import dva from 'dva';
import { Component } from 'react';
import createLoading from 'dva-loading';
import history from '@tmp/history';

let app = null;

export function _onCreate() {
  const plugins = require('umi/_runtimePlugin');
  const runtimeDva = plugins.mergeConfig('dva');
  app = dva({
    history,
    
    ...(runtimeDva.config || {}),
    ...(window.g_useSSR ? { initialState: window.g_initialData } : {}),
  });
  
  app.use(createLoading());
  (runtimeDva.plugins || []).forEach(plugin => {
    app.use(plugin);
  });
  app.use(require('../../plugins/tagRoutersPlugin/index.ts').default);
app.use(require('../../plugins/tagRoutersPlugin/template/tagRouters.ts').default);
  app.model({ namespace: 'app', ...(require('/Users/chuxu/shulie_project/full-link/src/models/app.ts').default) });
app.model({ namespace: 'common', ...(require('/Users/chuxu/shulie_project/full-link/src/models/common.ts').default) });
app.model({ namespace: 'user', ...(require('/Users/chuxu/shulie_project/full-link/src/models/user.ts').default) });
  return app;
}

export function getApp() {
  return app;
}

export class _DvaContainer extends Component {
  render() {
    const app = getApp();
    app.router(() => this.props.children);
    return app.start()();
  }
}
