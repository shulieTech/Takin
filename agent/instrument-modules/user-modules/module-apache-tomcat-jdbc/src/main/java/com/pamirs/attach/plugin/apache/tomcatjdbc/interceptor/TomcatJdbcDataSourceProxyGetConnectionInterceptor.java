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
package com.pamirs.attach.plugin.apache.tomcatjdbc.interceptor;

import com.pamirs.attach.plugin.apache.tomcatjdbc.obj.TomcatJdbcMediatorDataSource;
import com.pamirs.attach.plugin.apache.tomcatjdbc.util.DataSourceWrapUtil;
import com.pamirs.pradar.CutOffResult;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.CutoffInterceptorAdaptor;
import com.pamirs.pradar.internal.config.ShadowDatabaseConfig;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.pamirs.pradar.pressurement.agent.event.IEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.ClusterTestSwitchOffEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.ShadowDataSourceConfigModifyEvent;
import com.pamirs.pradar.pressurement.agent.listener.EventResult;
import com.pamirs.pradar.pressurement.agent.listener.PradarEventListener;
import com.pamirs.pradar.pressurement.agent.shared.service.DataSourceMeta;
import com.pamirs.pradar.pressurement.agent.shared.service.EventRouter;
import com.pamirs.pradar.pressurement.datasource.util.DbUrlUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author angju
 * @date 2020/8/11 20:27
 */
public class TomcatJdbcDataSourceProxyGetConnectionInterceptor extends CutoffInterceptorAdaptor {
    private final static Logger logger = LoggerFactory.getLogger(TomcatJdbcDataSourceProxyGetConnectionInterceptor.class.getName());

    private static AtomicBoolean isInited = new AtomicBoolean(false);

    public TomcatJdbcDataSourceProxyGetConnectionInterceptor() {
        addListener();
    }

    @Override
    public CutOffResult cutoff0(Advice advice) {
        Object target = advice.getTarget();
        DataSource dataSource = (DataSource) target;
        DataSourceMeta<DataSource> dataSourceMeta = new DataSourceMeta<DataSource>(dataSource.getUrl(), dataSource.getUsername(), dataSource);
        /**
         * 压测状态为关闭,如果当前为压测流量则直接报错
         */
        ClusterTestUtils.validateClusterTest();
        DataSourceWrapUtil.doWrap(dataSourceMeta);
        Connection connection = null;

        /**
         * 所有的流量均切换到此逻辑上,防止业务有连接缓存后无法进入
         * 如果未找到配置情况下则当前流量为压测流量时返回null,非压测流量则执行业务连接池正常逻辑,此种情况可能由于数据源未配置的情况
         * 如果获取连接出错时如果流量为压测流量则返回null，非压测流量则执行业务连接池正常逻辑
         */
        if (DataSourceWrapUtil.pressureDataSources.containsKey(dataSourceMeta)) {
            TomcatJdbcMediatorDataSource mediatorDataSource = DataSourceWrapUtil.pressureDataSources.get(dataSourceMeta);
            if (mediatorDataSource != null) {
                try {
                    connection = mediatorDataSource.getConnection();
                } catch (SQLException e) {
                    throw new PressureMeasureError(e);
                }
            } else {
                if (!Pradar.isClusterTest()) {
                    return CutOffResult.passed();
                }
            }
            return CutOffResult.cutoff(connection);
        } else {
            if (!Pradar.isClusterTest()) {
                return CutOffResult.passed();
            }
            return CutOffResult.cutoff(null);
        }
    }

    private void addListener() {
        if (!isInited.compareAndSet(false, true)) {
            return;
        }
        EventRouter.router().addListener(new PradarEventListener() {
            @Override
            public EventResult onEvent(IEvent event) {
                if (!(event instanceof ClusterTestSwitchOffEvent)) {
                    return EventResult.IGNORE;
                }
                //关闭压测数据源
                DataSourceWrapUtil.destroy();
                return EventResult.success("tomcat-jdbc-plugin");
            }

            @Override
            public int order() {
                return 8;
            }
        }).addListener(new PradarEventListener() {
            @Override
            public EventResult onEvent(IEvent event) {
                if (!(event instanceof ShadowDataSourceConfigModifyEvent)) {
                    return EventResult.IGNORE;
                }
                ShadowDataSourceConfigModifyEvent shadowDataSourceConfigModifyEvent = (ShadowDataSourceConfigModifyEvent) event;
                Set<ShadowDatabaseConfig> target = shadowDataSourceConfigModifyEvent.getTarget();
                if (null == target || target.size() == 0) {
                    return EventResult.IGNORE;
                }
                for (ShadowDatabaseConfig config : target) {
                    Iterator<Map.Entry<DataSourceMeta, TomcatJdbcMediatorDataSource>> it = DataSourceWrapUtil.pressureDataSources.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<DataSourceMeta, TomcatJdbcMediatorDataSource> entry = it.next();
                        if (StringUtils.equalsIgnoreCase(DbUrlUtils.getKey(config.getUrl(), config.getUsername()),
                                DbUrlUtils.getKey(entry.getKey().getUrl(), entry.getKey().getUsername()))) {
                            TomcatJdbcMediatorDataSource value = entry.getValue();
                            it.remove();
                            try {
                                value.close();
                                logger.info("module-tomcat-jdbc: destroyed shadow table datasource success. url:{} ,username:{}", entry.getKey().getUrl(), entry.getKey().getUsername());
                            } catch (Throwable e) {
                                logger.error("module-tomcat-jdbc: closed datasource err! target:{}, url:{} username:{}", entry.getKey().getDataSource().hashCode(), entry.getKey().getUrl(), entry.getKey().getUsername(), e);
                            }
                            break;
                        }
                    }

                }
                return EventResult.success("module-tomcat-jdbc: destroyed shadow table datasource success.");
            }

            @Override
            public int order() {
                return 2;
            }
        });
    }
}
