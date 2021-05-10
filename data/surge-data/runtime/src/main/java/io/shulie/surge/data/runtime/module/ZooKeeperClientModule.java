package io.shulie.surge.data.runtime.module;

import com.google.inject.Scopes;
import io.shulie.surge.data.common.zk.ZkClient;
import io.shulie.surge.data.common.zk.ZkClientSpec;
import io.shulie.surge.data.runtime.common.zk.CuratorClientProvider;
import io.shulie.surge.data.runtime.common.zk.NetflixCuratorZkClientFactory;
import io.shulie.surge.data.runtime.common.zk.ZkClientProvider;
import io.shulie.surge.data.runtime.common.zk.ZookeeperClientProvider;
import org.apache.zookeeper.ZooKeeper;

/**
 * ZooKeeper 实现注入
 *
 * @author pamirs
 */
public class ZooKeeperClientModule extends BaseDataModule {

	@Override
	protected void configure() {
		bindGeneric(ZkClient.class, NetflixCuratorZkClientFactory.class, ZkClientSpec.class);

		// 使用默认的 zookeeper 配置
		bind(ZooKeeper.class).toProvider(ZookeeperClientProvider.class).in(Scopes.SINGLETON);

		bind(org.I0Itec.zkclient.ZkClient.class).toProvider(ZkClientProvider.class);
		bind(ZkClient.class).toProvider(CuratorClientProvider.class);
	}
}
