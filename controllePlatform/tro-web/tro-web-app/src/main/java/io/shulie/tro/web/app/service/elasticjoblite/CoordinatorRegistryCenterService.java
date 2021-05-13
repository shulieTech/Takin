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

package io.shulie.tro.web.app.service.elasticjoblite;

import javax.annotation.PostConstruct;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2021/1/7 8:22 下午
 * @Description:
 */
@Component
@Slf4j
public class CoordinatorRegistryCenterService {

    @Autowired
    private Environment environment;

    private CoordinatorRegistryCenter registryCenter;

    @PostConstruct
    public void init() {
        String zkAddr = environment.getProperty("tro.config.zk.addr");
        if (StringUtils.isEmpty(zkAddr)) {
            throw new RuntimeException("配置中心zk地址没有填写，请核对校验`tro.config.zk.addr`");
        }
        registryCenter = new ZookeeperRegistryCenter(
            new ZookeeperConfiguration(zkAddr, "verify-job"));
        registryCenter.init();
    }

    public CoordinatorRegistryCenter getRegistryCenter() {
        return registryCenter;
    }
}
