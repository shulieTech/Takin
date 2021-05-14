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

package io.shulie.tro.cloud.biz.service.schedule;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: mubai
 * @Date: 2020-10-29 17:21
 * @Description:
 */

@Component
public class CopyFileExecutors implements DisposableBean {

    public static ExecutorService poll = null;

    @PostConstruct
    public void init() {
        Executors.newFixedThreadPool(2);
    }

    @Override
    public void destroy() throws Exception {
        poll.shutdown();
    }
}
