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
package com.shulie.instrument.simulator.api.listener.ext;

import com.shulie.instrument.simulator.api.filter.AccessFlags;

/**
 * 构建类匹配构造器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:47 下午
 */
public interface IClassMatchBuilder {

    /**
     * 是否包含被Bootstrap所加载的类
     * <p>
     * 类似如："java.lang.String"等，都是来自BootstrapClassLoader所加载的类。
     * 如果你需要增强他们则必须在{@code simulator.properties}文件，将UNSAFE开关打开{@code unsafe.enable=true}
     * </p>
     *
     * @return IBuildingForClass
     */
    IClassMatchBuilder includeBootstrap();

    /**
     * 是否包含被Bootstrap所加载的类
     *
     * @param isIncludeBootstrap TRUE:包含Bootstrap;FALSE:不包含Bootstrap;
     * @return IBuildingForClass
     * @see #includeBootstrap()
     */
    IClassMatchBuilder isIncludeBootstrap(boolean isIncludeBootstrap);

    /**
     * {@link EventWatchBuilder#onClass}所指定的类，检索路径是否包含子类（实现类）
     * <ul>
     * <li>如果onClass()了一个接口，则匹配时会搜索这个接口的所有实现类</li>
     * <li>如果onClass()了一个类，则匹配时会搜索这个类的所有子类</li>
     * </ul>
     *
     * @return IBuildingForClass
     */
    IClassMatchBuilder includeSubClasses();

    /**
     * 是否包含子类
     *
     * @param isIncludeSubClasses TRUE:包含子类（实现类）;FALSE:不包含子类（实现类）;
     * @return IBuildingForClass
     * @see #includeSubClasses()
     */
    IClassMatchBuilder isIncludeSubClasses(boolean isIncludeSubClasses);

    /**
     * 类修饰匹配
     *
     * @param access access flag
     * @return IBuildingForClass
     * @see AccessFlags
     */
    IClassMatchBuilder withAccess(int access);

    /**
     * 类是否声明实现了某一组接口
     *
     * @param classes 接口组类型数组
     * @return IBuildingForClass
     * @see #hasInterfaceTypes(String...)
     */
    IClassMatchBuilder hasInterfaceTypes(Class<?>... classes);

    /**
     * 类是否声明继承了某一父类
     * <p>
     * 父类组是一个可变参数组，匹配关系为"与"。即：当前类只要继承其中的一个父类则匹配通过
     * </p>
     *
     * @param patterns 父类匹配模版
     * @return IBuildingForClass
     */
    IClassMatchBuilder withSuperClass(String... patterns);

    /**
     * 类是否声明继承了某一父类
     * <p>
     * 父类组是一个可变参数组，匹配关系为"与"。即：当前类只要继承其中的一个父类则匹配通过
     * </p>
     *
     * @param classes 父类组匹配模版
     * @return IBuildingForClass
     */
    IClassMatchBuilder withSuperClass(Class... classes);

    /**
     * 类是否声明实现了某一组接口
     * <p>
     * 接口组是一个可变参数组，匹配关系为"与"。即：当前类必须同时实现接口模式匹配组的所有接口才能匹配通过
     * </p>
     *
     * @param patterns 接口组匹配模版
     * @return IBuildingForClass
     */
    IClassMatchBuilder hasInterfaceTypes(String... patterns);

    /**
     * 类是否拥有某一组标注
     *
     * @param classes 标注组类型数组
     * @return IBuildingForClass
     * @see #hasAnnotationTypes(String...)
     */
    IClassMatchBuilder hasAnnotationTypes(Class<?>... classes);

    /**
     * 添加渲染进度监听器，可以添加多个
     * <p>
     * 用于观察{@link #onWatch()}的渲染进度
     * </p>
     *
     * @param progress 渲染进度监听器
     * @return IBuildingForWatching
     */
    IClassMatchBuilder withProgress(Progress progress);

    /**
     * 类是否拥有某一组标注
     * <p>
     * 标注组是一个可变参数组，匹配关系为"与"。即：当前类必须同时满足所有标注匹配条件！
     * </p>
     *
     * @param patterns 标注组匹配模版
     * @return IBuildingForClass
     */
    IClassMatchBuilder hasAnnotationTypes(String... patterns);

    /**
     * 构建行为匹配器，匹配任意行为
     * <p>
     * 等同于{@code onBehavior("*")}
     * </p>
     *
     * @return IBuildingForBehavior
     */
    IBehaviorMatchBuilder onAnyBehavior();

    /**
     * 构建行为匹配器，匹配符合模版名称的行为，对于参数会进行精确匹配
     *
     * @param pattern 行为名称
     * @return IBuildingForBehavior
     */
    IBehaviorMatchBuilder onBehavior(String pattern);

    /**
     * 构建行为匹配器，匹配符合模版名称的行为
     *
     * @param patterns 行为名称
     * @return IBuildingForBehavior
     */
    IBehaviorMatchBuilder onBehavior(String... patterns);

    /**
     * 构建行为匹配器，匹配符合模版名称的行为，忽略参数匹配
     *
     * @param pattern 行为名称
     * @return IBuildingForBehavior
     */
    IBehaviorMatchBuilder onAnyBehavior(String pattern);

    /**
     * 构建行为匹配器，匹配符合模版名称的行为，忽略参数匹配
     *
     * @param patterns 行为名称
     * @return IBuildingForBehavior
     */
    IBehaviorMatchBuilder onAnyBehavior(String... patterns);

    /**
     * 观察器
     *
     * @return EventWatcher
     */
    EventWatcher onWatch();
}
