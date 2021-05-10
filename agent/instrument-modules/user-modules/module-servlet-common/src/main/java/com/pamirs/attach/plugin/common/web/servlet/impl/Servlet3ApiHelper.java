/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.attach.plugin.common.web.servlet.impl;


import com.pamirs.attach.plugin.common.web.servlet.ServletApiHelper;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/13 3:41 下午
 */
public class Servlet3ApiHelper implements ServletApiHelper {
    @Override
    public boolean isAsyncDispatcherBefore(HttpServletRequest request) {
        return request.isAsyncStarted() || request.getDispatcherType() == DispatcherType.ASYNC;
    }

    @Override
    public boolean isAsyncDispatcherAfter(HttpServletRequest request) {
        return request.getDispatcherType() == DispatcherType.ASYNC;
    }

    @Override
    public int getStatus(HttpServletResponse response) {
        return response.getStatus();
    }
}
