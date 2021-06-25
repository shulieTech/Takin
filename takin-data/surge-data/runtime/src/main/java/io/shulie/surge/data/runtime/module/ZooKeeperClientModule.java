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
