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

package io.shulie.amdb.adaptors;


import io.shulie.amdb.adaptors.common.Closeable;
import io.shulie.amdb.adaptors.connector.Processor;

import java.util.Map;

/**
 * 适配器
 *
 * @author vincent
 */
public interface Adaptor extends Processor, Closeable {

    /**
     * 添加拦截器支持
     */
    void addConnector();

    /**
     * 注册适配器
     *
     * @return
     */
    void registor();


    /**
     * 适配器操作
     *
     * @param adaptorTemplate
     */
    void setAdaptorTemplate(AdaptorTemplate adaptorTemplate);

    /**
     * @param config
     */
    void addConfig(Map<String, Object> config);
}
