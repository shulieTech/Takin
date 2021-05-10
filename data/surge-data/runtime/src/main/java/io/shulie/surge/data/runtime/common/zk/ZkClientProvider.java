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
