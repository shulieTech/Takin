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
package com.shulie.instrument.module.config.fetcher.config;

import com.shulie.instrument.module.config.fetcher.config.event.FIELDS;

/**
 * @author shiyajian
 * @since 2020-08-11
 */
public interface IConfig<T> {

    /**
     * 初始化
     */
    void init();

    /**
     * 是否初始化
     *
     * @return
     */
    boolean isInit();

    /**
     * 手动触发拉取某个配置
     */
    void trigger(FIELDS... fields);

    /**
     * 触发获取配置
     */
    void triggerFetch();

    /**
     * 刷新配置
     *
     * @param t
     */
    void refresh(T t);

    /**
     * 销毁
     */
    void destroy();
}
