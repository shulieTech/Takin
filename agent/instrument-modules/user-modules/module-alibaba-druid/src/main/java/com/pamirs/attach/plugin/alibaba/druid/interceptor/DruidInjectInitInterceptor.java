/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.attach.plugin.alibaba.druid.interceptor;

import com.alibaba.druid.pool.DruidDataSource;
import com.pamirs.attach.plugin.alibaba.druid.util.DataSourceWrapUtil;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.pamirs.pradar.pressurement.agent.shared.service.DataSourceMeta;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

//删掉mybatis适配、jdbctemplate切点
public class DruidInjectInitInterceptor extends AroundInterceptor {

    @Override
    public void doAfter(Advice advice) {
        /**
         * 压测状态判断
         */
        DruidDataSource target1 = (DruidDataSource) advice.getTarget();
        DataSourceMeta<DruidDataSource> dataSourceMeta = new DataSourceMeta<DruidDataSource>(
                target1.getUrl(),
                target1.getUsername(),
                target1);
        DataSourceWrapUtil.doWrap(dataSourceMeta);
    }
}
