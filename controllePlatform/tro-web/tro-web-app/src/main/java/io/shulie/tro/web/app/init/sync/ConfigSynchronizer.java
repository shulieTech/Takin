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

package io.shulie.tro.web.app.init.sync;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.web.app.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-09-17
 */
@Component
@Slf4j
public class ConfigSynchronizer {

    @Autowired
    private ConfigSyncService configSyncService;

    @Autowired
    private ApplicationService applicationService;

    @Resource
    private TUserMapper userMapper;

    /**
     * 项目启动后，去更新下配置
     */
    public void initSyncAgentConfig() {
        log.info("项目启动，重新同步信息去配置中心");
        List<User> userList = userMapper.selectDistinctUserAppKey();
        List<String> userAppKeyList = userList.stream().map(User::getKey).distinct().collect(Collectors.toList());
        for (String userAppKey : userAppKeyList) {
            List<Long> userIdList = userList.stream().filter(user -> user.getKey().equals(userAppKey)).map(User::getId)
                .collect(Collectors.toList());
            List<TApplicationMnt> applications = applicationService.getApplicationsByUserIdList(userIdList);
            if (CollectionUtils.isEmpty(applications)) {
                continue;
            } else {
                for (TApplicationMnt application : applications) {
                    configSyncService.syncGuard(userAppKey, application.getApplicationId(),
                        application.getApplicationName());
                    sleep();
                    configSyncService.syncShadowDB(userAppKey, application.getApplicationId(),
                        application.getApplicationName());
                    sleep();
                    configSyncService.syncAllowList(userAppKey, application.getApplicationId(),
                        application.getApplicationName());
                    sleep();
                    configSyncService.syncShadowJob(userAppKey, application.getApplicationId(),
                        application.getApplicationName());
                    sleep();
                    configSyncService.syncShadowConsumer(userAppKey, application.getApplicationId(),
                        application.getApplicationName());
                }
            }
            configSyncService.syncClusterTestSwitch(userAppKey);
            sleep();
            configSyncService.syncAllowListSwitch(userAppKey);
            sleep();
            configSyncService.syncBlockList(userAppKey);
        }
        log.info("所有配置同步到配置中心成功");
    }

    /**
     * 给zk写的压力不要太大，慢慢写
     */
    private void sleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            //ignore
        }
    }
}
