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
package com.shulie.instrument.simulator.api.resource;

/**
 * 动态属性管理器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/4/24 1:46 下午
 */
public interface DynamicFieldManager {

    /**
     * 获取某个对象的动态属性值
     *
     * @param target
     * @param fieldName
     * @param <T>
     * @return
     */
    <T> T getDynamicField(Object target, String fieldName);

    /**
     * 获取某个对象的动态属性值，如果没有会返回默认值
     *
     * @param target
     * @param fieldName
     * @param defaultValue
     * @param <T>
     * @return
     */
    <T> T getDynamicField(Object target, String fieldName, T defaultValue);

    /**
     * 销毁某个对象上的所有动态属性值
     *
     * @param target
     */
    void removeAll(Object target);

    /**
     * 给某个对象设置动态字段
     *
     * @param target    目标对象
     * @param fieldName 属性名称
     * @param value     属性值
     */
    void setDynamicField(Object target, String fieldName, Object value);

    /**
     * 移除某个对象上的动态属性值
     *
     * @param target
     * @param fieldName
     * @param <T>
     * @return
     */
    <T> T removeField(Object target, String fieldName);

    /**
     * 销毁当前的动态属性管理器
     */
    void destroy();
}
