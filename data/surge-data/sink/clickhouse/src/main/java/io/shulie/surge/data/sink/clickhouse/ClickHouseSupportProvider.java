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

package io.shulie.surge.data.sink.clickhouse;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.shulie.surge.data.runtime.common.DataRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 返回单例的 {@link ClickHouseSupportProvider} 对象
 *
 * @author pamirs
 */
@Singleton
public class ClickHouseSupportProvider implements Provider<ClickHouseSupport> {
    private static final Logger logger = LoggerFactory.getLogger(ClickHouseSupportProvider.class);

    private ClickHouseSupportFactory factory;
    private ClickHouseSupportSpec spec;

    @Inject
    public ClickHouseSupportProvider(DataRuntime runtime,
                                     @Named("config.clickhouse.url") String url,
                                     @Named("config.clickhouse.userName") String username,
                                     @Named("config.clickhouse.password") String password,
                                     @Named("config.clickhouse.batchCount") int batchCount,
                                     @Named("config.clickhouse.enableRound") boolean enableRound) {
        super();
        try {
            factory = runtime.getInstance(ClickHouseSupportFactory.class);
            spec = new ClickHouseSupportSpec();
            spec.setUrl(url);
            spec.setUsername(username);
            spec.setPassword(password);
            spec.setBatchCount(batchCount);
            spec.setEnableRound(enableRound);
        } catch (Exception e) {
            logger.warn("ClickHouseSupportProvider init fail.url :{}" + url, e);
            throw e;
        }
    }

    @Override
    public ClickHouseSupport get() {
        try {
            return factory.create(spec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
