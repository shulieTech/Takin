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

package com.pamirs.tro.common.redis;

/**
 * 一个辅助类，key是缓存的Key，timeout是缓存的失效时间。
 *
 * @author Administrator
 */
public class RedisKey {

    private String key;

    private long timeout;

    public RedisKey(String key, long timeout) {
        super();
        this.key = key;
        this.timeout = timeout;
    }

    public String getKey() {
        return key;
    }

    public long getTimeout() {
        return timeout;
    }

}
