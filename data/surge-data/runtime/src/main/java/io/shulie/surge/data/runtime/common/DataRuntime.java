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

package io.shulie.surge.data.runtime.common;


import io.shulie.surge.data.common.factory.GenericFactory;
import io.shulie.surge.data.common.factory.GenericFactorySpec;
import io.shulie.surge.data.common.lifecycle.StopLevel;
import io.shulie.surge.data.common.lifecycle.Stoppable;

/**
 * 返回 Log 服务化 API 的实现
 *
 * @author pamirs
 */
public interface DataRuntime {

    /**
     * 获取默认的实现
     *
     * @param type
     * @return
     */
    <T> T getInstance(Class<T> type);

    /**
     * 获取名为 <code>name</code> 的实现
     *
     * @param type
     * @param name
     * @return
     */
    <T> T getInstance(Class<T> type, String name);

    /**
     * 获取用于创建 T 的，名为 name 的 GenericFactory
     *
     * @param type
     * @param name
     * @return
     */
    <T> GenericFactory<T, GenericFactorySpec<T>> getGenericFactory(Class<T> type, String name);

    /**
     * 获取 T 的配置描述对象，返回的配置没有经过设置
     *
     * @param type
     * @param name
     * @return
     */
    <T> GenericFactorySpec<T> getGenericFactorySpec(Class<T> type, String name);

    /**
     * 返回 T 名为 name 的实例，创建参数由 jsonString 指定。
     * 使用这个方法的前提是已经为 T 绑定了工厂方法 {@link GenericFactory} 和
     * 配置对象 {@link GenericFactorySpec}
     *
     * @param type
     * @param name
     * @param json
     * @return
     * @throws Exception 创建过程出现任何异常
     */
    <T> T createGenericInstance(Class<T> type, String name, String json) throws Exception;

    /**
     * 根据 spec 创建实例 T，name 由 spec 指定。
     *
     * @param spec
     * @return
     * @throws Exception
     */
    <T> T createGenericInstance(GenericFactorySpec<T> spec) throws Exception;

    /**
     * 向 <code>instance</code> 注入依赖
     *
     * @param instance
     */
    void inject(Object instance);

    /**
     * 停止 {@link DataRuntime} 的执行，所有在
     * 注册的 Stoppable 回调都会被<b>先进先出</b>调用 <code>stop()</code>
     */
    void shutdown();

    /**
     * 注册在 {@link #shutdown()} 时进行的回调
     *
     * @param stoppable
     * @param stopLevel
     */
    void registShutdownCall(Stoppable stoppable, StopLevel stopLevel);

    /**
     * 取消注册在 {@link #shutdown()} 时进行的回调
     *
     * @param stoppable
     * @param stopLevel
     * @return 取消成功返回 <code>true</code>
     */
    boolean unregistShutdownCall(Stoppable stoppable, StopLevel stopLevel);

    @Deprecated
    public Object getValue(String key);

    @Deprecated
    public void putValue(String key, Object value);

}
