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

package io.shulie.surge.data.runtime.common.remote.impl;

import com.google.inject.Inject;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * 从 Zookeeper 同步配置数据
 * @author pamirs
 */
public class ZkDataImpl<T> extends BaseRemoteData<T> {

	private static final Logger logger = Logger.getLogger(ZkDataImpl.class);

	@Inject
	private ZkClient zkClient;

	@Override
	public T get() {
		return data;
	}

	@Override
	public void set(T value) {
		zkClient.writeData(dataId, value);
		updateData(value);
	}

	@Override
	protected void init(Field field, Type fieldType, Object instance) throws Exception {
		Object data;
		try {
			data = zkClient.readData(dataId);
		} catch (ZkNoNodeException e) {
			try {
				// 如果 node 不存在，需要先创建出来，否则无法监听
				zkClient.create(dataId, defalutData, CreateMode.PERSISTENT);
				logger.info("create new node for listen, dataId=" + dataId);
			} catch (Exception e2) {
				logger.warn("fail to create new node for listen, dataId=" + dataId + ": " + e2.getMessage());
			}
			data = defalutData;
		} catch (NullPointerException e) {
			//zk挂了
			logger.warn("init zk exception", e);
			return;
		}
		updateData(data);
		notifyUpdate();

		logger.info("SubscribeDataChanges: " + dataId);

		zkClient.subscribeDataChanges(dataId, new IZkDataListener() {
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				updateData(null);
				notifyUpdate();
			}

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				updateData(data);
				notifyUpdate();
			}
		});
	}
}
