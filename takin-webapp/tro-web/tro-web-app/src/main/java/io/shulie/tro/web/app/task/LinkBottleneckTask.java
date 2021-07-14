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

package io.shulie.tro.web.app.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.shulie.tro.web.app.service.LinkBottleneckService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Author: xingchen
 * @ClassName: LinkBottleneck
 * @package: com.pamirs.tro.web.api.task
 * @Date: 2019/6/10下午9:30
 * @Description:
 */
@Component
public class LinkBottleneckTask implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger logger = LoggerFactory.getLogger(LinkBottleneckTask.class);

    @Autowired
    private LinkBottleneckService linkBottleneckService;

    @Value("${flag:true}")
    private boolean flag;

    /**
     * 每隔30秒查询一次数据
     */
    private int initialDelay = 30;

    /**
     * 定时查询应用负载及异步处理情况
     *
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
           /* if (flag) {
                ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
                service.scheduleAtFixedRate(() -> linkBottleneckService.handleLinkBottleneck(),
                    initialDelay, initialDelay, TimeUnit.SECONDS);
            }*/
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }
}
