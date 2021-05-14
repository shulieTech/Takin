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

package io.shulie.tro.web.app.init;

import io.shulie.tro.web.app.init.fix.BlacklistDataFixer;
import io.shulie.tro.web.app.init.fix.LinkManageFixer;
import io.shulie.tro.web.app.init.fix.WhitelistEffectAppNameDataFixer;
import io.shulie.tro.web.app.init.sync.ConfigSynchronizer;
import io.shulie.tro.web.app.service.pradar.PradarConfigService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-10-10
 */
@Component
public class Initializer implements InitializingBean {

    @Autowired
    private ConfigSynchronizer configSynchronizer;

    @Autowired
    private LinkManageFixer linkManageFixer;

    @Autowired
    private PradarConfigService pradarConfigService;

    @Autowired
    private BlacklistDataFixer blacklistDataFixer;

    @Autowired
    private WhitelistEffectAppNameDataFixer whitelistEffectAppNameDataFixer;

    /**
     * 所有项目启动需要做的事情都统一注册在这里
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 将agent需要的配置同步到文件、redis、zk等
        new Thread(() -> configSynchronizer.initSyncAgentConfig()).start();
        new Thread(() -> linkManageFixer.fix()).start();
        // 黑名单数据补全
        new Thread(() -> blacklistDataFixer.fix()).start();
        // 白名单生效应用数据订正
        new Thread(() -> whitelistEffectAppNameDataFixer.fix()).start();
        new Thread(() -> pradarConfigService.initZKData()).start();

    }
}
