
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
import io.shulie.surge.data.common.factory.GenericFactory;
import io.shulie.surge.data.common.lifecycle.StopLevel;
import io.shulie.surge.data.runtime.common.DataRuntime;

public class MysqlSupportFactory implements GenericFactory<MysqlSupport, MysqlSupportSpec> {
    @Inject
    private DataRuntime runtime;


    /**
     * 按照参数配置来创建 T
     *
     * @param spec
     * @return
     * @throws Exception 参数不正确，或创建失败时抛出异常
     */

    @Override
    public MysqlSupport create(MysqlSupportSpec spec) throws Exception {
        synchronized (MysqlSupportFactory.class) {
            MysqlSupport clickHouseSupport = new MysqlSupport(
                    spec.getUrl(),
                    spec.getUsername(),
                    spec.getPassword(),
                    spec.getInitialSize(),
                    spec.getMinIdle(),
                    spec.getMaxActive());
            runtime.inject(clickHouseSupport);
            runtime.registShutdownCall(clickHouseSupport, StopLevel.SUPPORT);
            return clickHouseSupport;
        }
    }
}
