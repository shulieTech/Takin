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
package com.shulie.instrument.module.config.fetcher.config.resolver;


import com.shulie.instrument.module.config.fetcher.config.AbstractConfig;
import com.shulie.instrument.module.config.fetcher.config.event.FIELDS;

/**
 * @author shiyajian
 * @since 2020-08-11
 */
public interface ConfigResolver<T extends AbstractConfig<T>> {
    String APP_COLUMN_APPLICATION_NAME = "applicationName";
    String APP_COLUMN_DDL_PATH = "ddlScriptPath";
    String APP_COLUMN_CLEAN_PATH = "cleanScriptPath";
    String APP_COLUMN_READY_PATH = "readyScriptPath";
    String APP_COLUMN_BASIC_PATH = "basicScriptPath";
    String APP_COLUMN_CACHE_PATH = "cacheScriptPath";

    /**
     * 解析配置
     *
     * @param t
     */
    void resolve(T t);

    /**
     * 触发获取配置
     *
     * @param t
     */
    void triggerFetch(T t);

    /**
     * 拉取配置
     *
     * @return
     */
    T fetch();

    /**
     * 拉取某个配置
     *
     * @param fields
     * @return
     */
    T fetch(FIELDS... fields);

    /**
     * 销毁
     */
    void destroy();
}
