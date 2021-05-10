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
package com.shulie.instrument.simulator.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模块信息
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    /**
     * 未知版本
     */
    String UNKNOW_VERSION = "UNKNOW_VERSION";

    /**
     * 未知作者
     */
    String UNKNOW_AUTHOR = "UNKNOW_AUTHOR";

    /**
     * 模块ID
     * 全JVM唯一标记了一个模块，所有基于模块的操作都将基于此ID来完成
     *
     * @return 模块ID
     */
    String id();

    /**
     * 模块期待仿真器的加载模式
     * <p>
     * 一些模块对仿真器的启动方式有特殊要求，比如必须要求仿真器以{@code AGENT}的方式启动才能正常工作
     * 通过设置这个标记位可以让仿真器加载模块的时候做一个判断，不符合模块期待的，当前模块将不会被加载
     * </p>
     *
     * @return 期待仿真器的加载模式
     */
    LoadMode[] supportedModes() default {LoadMode.AGENT, LoadMode.ATTACH};

    /**
     * 是否在加载时候就激活模块。
     *
     * @return 当值为TRUE时，模块加载完成后会主动激活模块；
     * 当值为FALSE时，模块加载完成后状态为冻结
     */
    boolean isActiveOnLoad() default true;

    /**
     * 定义模块版本号
     *
     * @return 模块版本号
     */
    String version() default UNKNOW_VERSION;

    /**
     * 定义模块作者
     *
     * @return 模块作者
     */
    String author() default UNKNOW_AUTHOR;

    /**
     * 模块的描述信息
     *
     * @return
     */
    String description() default "";

    /**
     * 优先级，可以调整同一模块内部的多个扩展加载的优先级，默认为100
     *
     * @return
     */
    int priority() default 100;

}
