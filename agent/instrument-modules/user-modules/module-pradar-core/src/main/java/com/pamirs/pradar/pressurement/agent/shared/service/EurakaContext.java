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
package com.pamirs.pradar.pressurement.agent.shared.service;

import org.apache.commons.lang.StringUtils;

public class EurakaContext {
    /**
     * 是否经过eruaka代理发送
     */
    private static ThreadLocal<String> eurakaArchonThreadLocal = new ThreadLocal<String>();

    /**
     * 经过euraka
     */
    public static void overWhelmingThread() {
        eurakaArchonThreadLocal.set("Y");
    }

    /**
     * 去掉进过euraka标记
     */
    public static void eradicateThread() {
        eurakaArchonThreadLocal.remove();
    }


    /**
     * 是否经过了euraka spring cloud2
     *
     * @return
     */
    public static boolean isEurakaCloud() {
        return StringUtils.equals("Y", eurakaArchonThreadLocal.get());
    }
}
