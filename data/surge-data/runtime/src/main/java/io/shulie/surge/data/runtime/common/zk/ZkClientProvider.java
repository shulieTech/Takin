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

package io.shulie.surge.data.runtime.common.zk;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.shulie.surge.data.common.zk.SimpleZkSerializer;
import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;

/**
 * 返回单例的 {@link ZkClient} 对象
 *
 * @author pamirs
 */
@Singleton
public class ZkClientProvider implements Provider<ZkClient> {

    private ZkClient singleton;
    private static final Logger logger = Logger.getLogger(ZkClientProvider.class);

    @Inject
    public ZkClientProvider(@Named("config.data.zk.servers") String zkServers,
                            @Named("config.data.zk.sessionTimeoutMillis") int sessionTimeout,
                            @Named("config.data.zk.connTimeoutMillis") int connectionTimeout) {
        try {
            this.singleton = new ZkClient(zkServers, sessionTimeout, connectionTimeout, new SimpleZkSerializer());
        } catch (Exception e) {
            //zk 挂了
            logger.warn("ZkClientProvider init fail, servers=" + zkServers, e);
            return;
        }
    }

    @Override
    public ZkClient get() {
        return singleton;
    }
}
