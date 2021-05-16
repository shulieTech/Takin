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
 * 工厂 GenericFactory 创建 T 的实例的配置说明，实现类必须提供无参构造函数
 * @param <T>
 * @author pamirs
 * @author pamirs
 * @see GenericFactory
 */
public interface GenericFactorySpec<T> {

    /**
     * 返回工厂在注入时需要指定的名称
     * @return
     */
    String factoryName();

    /**
     * @return 被创建的对象的 interface
     */
    Class<T> productClass();
}
