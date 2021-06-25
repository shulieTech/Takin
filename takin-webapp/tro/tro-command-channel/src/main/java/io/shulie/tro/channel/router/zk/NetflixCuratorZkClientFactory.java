package io.shulie.tro.channel.router.zk;

import java.util.concurrent.ThreadFactory;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import io.shulie.tro.channel.exception.ChannelException;
import io.shulie.tro.channel.router.zk.impl.NetflixCuratorZkClient;
import io.shulie.tro.channel.utils.StringUtils;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description CuratorZkClientFactory
 * @author: HengYu
 * @mail guohaozhu@shulie.io
 * @Date 2020/12/29 20:11
 */
public class NetflixCuratorZkClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(NetflixCuratorZkClientFactory.class);

    private static final class NetflixCuratorZkClientFactoryHolder {
        public final static NetflixCuratorZkClientFactory INSTANCE = new NetflixCuratorZkClientFactory();
    }

    public static NetflixCuratorZkClientFactory getInstance() {
        return NetflixCuratorZkClientFactoryHolder.INSTANCE;
    }

    private NetflixCuratorZkClientFactory() {
    }

    public ZkClient create(final ZkClientConfig spec) throws Exception {
        if (StringUtils.isBlank(spec.getZkServers())) {
            throw new ChannelException("zookeeper servers is empty.");
        }
        String path = ZooKeeper.class.getProtectionDomain().getCodeSource().getLocation().toString();
        logger.info("Load ZooKeeper from {}", path);

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(spec.getZkServers())
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(spec.getConnectionTimeoutMillis())
                .sessionTimeoutMs(spec.getSessionTimeoutMillis())
                .threadFactory(new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, spec.getThreadName());
                    }
                })
                .build();
        client.start();
        logger.info("ZkClient started: {}", spec.getZkServers());
        NetflixCuratorZkClient theClient = new NetflixCuratorZkClient(client, spec.getZkServers());
        return theClient;

    }
}
