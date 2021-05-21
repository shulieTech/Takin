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

package io.shulie.amdb.run;

import io.shulie.amdb.adaptors.base.AdaptorStarter;
import io.shulie.amdb.service.AppInstanceService;
import io.shulie.amdb.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@Order(value = 1000)
public class AdaptorStarterRunner implements ApplicationRunner {

    @Value("${zookeeper.server}")
    private String zkPath;

    @Value("${server.port}")
    private String serverPort;

    @Value("${config.adaptor.instance.open}")
    private boolean isOpen;

    @Autowired
    private AppService appService;

    @Autowired
    private AppInstanceService appInstanceService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!isOpen) {
            return;
        }
        try {
            Map<String, Object> config = new HashMap<String, Object>();
            config.put("appService", appService);
            config.put("appInstanceService", appInstanceService);
            System.setProperty("zookeeper.servers", zkPath);
            AdaptorStarter starter = new AdaptorStarter(config);
            starter.start();
        } catch (Exception e) {
            log.error("adaptor启动失败:{}", e);
        }
    }
}
