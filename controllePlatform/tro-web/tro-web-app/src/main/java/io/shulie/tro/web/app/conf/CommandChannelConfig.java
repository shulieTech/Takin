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

package io.shulie.tro.web.app.conf;

import io.shulie.tro.channel.ServerChannel;
import io.shulie.tro.channel.router.zk.DefaultServerChannel;
import io.shulie.tro.channel.router.zk.ZkClientConfig;
import io.shulie.tro.web.app.utils.JsonChannelProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: mubai
 * @Date: 2021-01-04 17:38
 * @Description:
 */

@Configuration
@Slf4j
public class CommandChannelConfig {

    @Value("${tro.config.zk.addr}")
    private String zkPath;

    @Bean
    public ServerChannel registerChannel() {

        ServerChannel channel = null;
        try {
            ZkClientConfig config = new ZkClientConfig();
            config.setZkServers(zkPath);
            channel = new DefaultServerChannel()
                .build(config)
                .setChannelProtocol(new JsonChannelProtocol());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return channel;
    }

}
