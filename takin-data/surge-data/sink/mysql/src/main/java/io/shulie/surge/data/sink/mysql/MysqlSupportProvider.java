
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

package io.shulie.surge.data.sink.mysql;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.shulie.surge.data.runtime.common.DataRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 返回单例的 {@link MysqlSupportProvider} 对象
 *
 * @author pamirs
 */

@Singleton
public class MysqlSupportProvider implements Provider<MysqlSupport> {
    private static final Logger logger = LoggerFactory.getLogger(MysqlSupportProvider.class);

    private MysqlSupportFactory factory;
    private MysqlSupportSpec spec;

    @Inject
    public MysqlSupportProvider(DataRuntime runtime,
                                @Named("config.mysql.url") String url,
                                @Named("config.mysql.userName") String username,
                                @Named("config.mysql.password") String password,
                                @Named("config.mysql.initialSize") Integer initialSize,
                                @Named("config.mysql.minIdle") Integer minIdle,
                                @Named("config.mysql.maxActive") Integer maxActive) {
        super();
        try {
            factory = runtime.getInstance(MysqlSupportFactory.class);
            spec = new MysqlSupportSpec();
            spec.setUrl(url);
            spec.setUsername(username);
            spec.setPassword(password);
            spec.setInitialSize(initialSize);
            spec.setMinIdle(minIdle);
            spec.setMaxActive(maxActive);
        } catch (Exception e) {
            logger.warn("ClickHouseSupportProvider init fail.url :{}" + url, e);
            throw e;
        }
    }

    @Override
    public MysqlSupport get() {
        try {
            return factory.create(spec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
