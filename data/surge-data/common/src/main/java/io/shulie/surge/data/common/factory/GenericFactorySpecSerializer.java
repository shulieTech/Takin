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

package io.shulie.surge.data.common.factory;

/**
 * 将 {@link GenericFactorySpec} 序列化 / 反序列化为 JSON 字符串
 *
 * @author pamirs
 */
public interface GenericFactorySpecSerializer {

    /**
     * 序列化为 JSON
     *
     * @param spec
     * @return
     */
    <T> String toJSONString(GenericFactorySpec<T> spec);

    /**
     * 从 JSON 反序列化为 GenericSpec 实例
     *
     * @param productInterface
     * @param name
     * @param jsonString
     * @return
     * @throws Exception
     */
    <T> GenericFactorySpec<T> fromJSONString(Class<T> productInterface,
                                             String name, String jsonString) throws Exception;
}
