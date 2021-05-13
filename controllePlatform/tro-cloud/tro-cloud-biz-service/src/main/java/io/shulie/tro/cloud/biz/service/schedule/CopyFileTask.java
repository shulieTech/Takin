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

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * @Author: mubai
 * @Date: 2020-10-29 17:15
 * @Description:
 */
@Slf4j
public class CopyFileTask implements Runnable {
    private String source;
    private String dest;

    public CopyFileTask(String source, String dest) {
        this.source = source;
        this.dest = dest;
    }


    @Override
    public void run() {

        String cmd = "cmd /c copy" + source + " " + dest;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
