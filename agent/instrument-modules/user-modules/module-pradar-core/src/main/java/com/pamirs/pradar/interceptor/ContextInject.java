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
package com.pamirs.pradar.interceptor;

import java.util.Map;

/**
 * 上下文注入接口
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/5/15 5:10 下午
 */
public interface ContextInject {
    /**
     * 注入上下文
     *
     * @param context 上下文对象
     */
    void injectContext(Map<String, String> context);
}
