package io.shulie.surge.data.runtime.common.zk;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import io.shulie.surge.data.common.factory.GenericFactory;
import io.shulie.surge.data.common.lifecycle.StopLevel;
import io.shulie.surge.data.common.pool.NamedThreadFactory;
import io.shulie.surge.data.common.zk.ZkClient;
import io.shulie.surge.data.common.zk.ZkClientSpec;
import io.shulie.surge.data.common.zk.impl.NetflixCuratorZkClient;
import io.shulie.surge.data.runtime.common.DataRuntime;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.Callable;

@Singleton
public class NetflixCuratorZkClientFactory implements GenericFactory<ZkClient, ZkClientSpec> {

    private static final Logger logger = LoggerFactory.getLogger(NetflixCuratorZkClientFactory.class);

    private static Cache<String, ZkClient> cache = CacheBuilder.newBuilder().build();

    @Inject
    private DataRuntime runtime;

    @Override
    public ZkClient create(ZkClientSpec spec) throws Exception {
        final ZkClientSpec specF = spec;
        return cache.get(spec.getZkServers(), new Callable<ZkClient>() {
            public ZkClient call() throws Exception {
                String path = ZooKeeper.class.getProtectionDomain().getCodeSource().getLocation().toString();
                logger.info("Load ZooKeeper from {}", path);

                CuratorFramework client = CuratorFrameworkFactory.builder()
                        .connectString(specF.getZkServers())
                        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                        .connectionTimeoutMs(specF.getConnectionTimeoutMillis())
                        .sessionTimeoutMs(specF.getSessionTimeoutMillis())
                        .threadFactory(new NamedThreadFactory("curator", true))
                        .build();
                client.start();
                logger.info("ZkClient started: {}", specF.getZkServers());

                NetflixCuratorZkClient theClient = new NetflixCuratorZkClient(client, specF.getZkServers());
                runtime.registShutdownCall(theClient, StopLevel.BASIC);

                return theClient;
            }
        });
    }
}
