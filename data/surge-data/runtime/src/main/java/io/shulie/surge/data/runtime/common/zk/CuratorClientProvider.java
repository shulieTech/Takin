package io.shulie.surge.data.runtime.common.zk;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import io.shulie.surge.data.common.pool.NamedThreadFactory;
import io.shulie.surge.data.common.zk.ZkClient;
import io.shulie.surge.data.common.zk.impl.NetflixCuratorZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fabing.zhaofb
 */
@Singleton
public class CuratorClientProvider implements Provider<ZkClient> {
    private static final Logger logger = LoggerFactory.getLogger(CuratorClientProvider.class);
    private ZkClient singleton;

    @Inject
    public CuratorClientProvider(@Named("config.data.zk.servers") String zkServers,
                                 @Named("config.data.zk.sessionTimeoutMillis") int sessionTimeout,
                                 @Named("config.data.zk.connTimeoutMillis") int connectionTimeout) {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zkServers)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(sessionTimeout)
                .sessionTimeoutMs(connectionTimeout)
                .threadFactory(new NamedThreadFactory("curator", true))
                .build();
        client.start();
        logger.info("ZkClient started: {}", zkServers);

        this.singleton = new NetflixCuratorZkClient(client, zkServers);
    }

    @Override
    public ZkClient get() {
        return singleton;
    }
}
