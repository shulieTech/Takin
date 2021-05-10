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
package com.pamirs.attach.plugin.catalina.common;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/3 5:38 下午
 */
public class CatalinaVersion {

    private static boolean isVersion9x;

    static {
        init();
    }

    private static void init() {
        try {
            Request.class.getDeclaredConstructor(Connector.class);
            isVersion9x = true;
        } catch (NoSuchMethodException e) {
            isVersion9x = false;
        }
    }

    public static Request getRequest(Request request) {
        if (isVersion9x) {
            return new BufferedServletRequestWrapper9x(request);
        } else {
            return new BufferedServletRequestWrapper(request);
        }
    }
}
