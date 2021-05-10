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


import com.shulie.instrument.simulator.api.event.EventType;
import com.shulie.instrument.simulator.api.listener.Listeners;

/**
 * 行为匹配构造器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:40 下午
 */
public interface IBehaviorMatchBuilder {

    /**
     * 指定参数访问参数
     *
     * @param access
     * @return
     */
    IBehaviorMatchBuilder withAccess(int access);

    /**
     * 匹配所有参数
     *
     * @return this
     */
    IBehaviorMatchBuilder withAnyParameterTypes();

    /**
     * 指定参数为空参数
     *
     * @return this
     */
    IBehaviorMatchBuilder withEmptyParameterTypes();

    /**
     * 指定方法参数类型
     *
     * @param patterns
     * @return this
     */
    IBehaviorMatchBuilder withParameterTypes(String... patterns);

    /**
     * 指定方法参数类型
     * 如果class是jdk的类则可以直接使用，如果是业务中使用的自定义类则
     * 使用{@link #withParameterTypes(String...)}
     *
     * @param classes
     * @return
     */
    IBehaviorMatchBuilder withParameterTypes(Class<?>... classes);

    /**
     * 匹配指定的参数下标
     *
     * @param index   参数下标
     * @param pattern 参数类型匹配规则，不为能空
     * @return
     */
    IBehaviorMatchBuilder withParameterType(int index, String pattern);

    /**
     * 匹配指定的参数下标
     *
     * @param index 参数下标
     * @param clazz 参数类型，不能为空
     * @return
     */
    IBehaviorMatchBuilder withParameterType(int index, Class<?> clazz);

    /**
     * 指定方法包含异常类型
     *
     * @param patterns
     * @return
     */
    IBehaviorMatchBuilder hasExceptionTypes(String... patterns);

    /**
     * 指定参数包含异常类型
     * 如果class是jdk的类则可以直接使用，如果是业务中使用的自定义类则
     * 使用{@link #hasExceptionTypes(String...)}
     *
     * @param classes
     * @return
     */
    IBehaviorMatchBuilder hasExceptionTypes(Class<?>... classes);

    /**
     * 指定方法包含注解
     *
     * @param patterns
     * @return
     */
    IBehaviorMatchBuilder hasAnnotationTypes(String... patterns);

    /**
     * 指定方法包含注解类型
     * 如果class是jdk的类则可以直接使用，如果是业务中使用的自定义类则
     * 使用{@link #hasAnnotationTypes(String...)}
     *
     * @param classes
     * @return
     */
    IBehaviorMatchBuilder hasAnnotationTypes(Class<?>... classes);

    /**
     * 指定方法名
     *
     * @param pattern
     * @return
     */
    IBehaviorMatchBuilder onBehavior(String pattern);

    /**
     * 反匹配
     *
     * @return
     */
    IBehaviorMatchBuilder withNot();

    /**
     * 观察行为调用
     * 调用之后，
     * <ul>
     * <li>{@link AdviceListener#before(Advice)} </li>
     * <li>{@link AdviceListener#afterReturning(Advice)} </li>
     * <li>{@link AdviceListener#afterThrowing(Advice)}</li>
     * </ul>
     * <p>
     * 将会被触发
     *
     * @return IBuildingForWatching
     */
    IBehaviorMatchBuilder withInvoke();

    /**
     * 观察行为内部的方法调用
     * 调用之后，
     * <ul>
     * <li>{@link AdviceListener#beforeCall(Advice, int, boolean, String, String, String)}</li>
     * <li>{@link AdviceListener#afterCallReturning(Advice, int, boolean, String, String, String)}</li>
     * <li>{@link AdviceListener#afterCallThrowing(Advice, int, boolean, String, String, String, Throwable)}</li>
     * </ul>
     * <p>
     * 将会被触发
     *
     * @return IBuildingForWatching
     */
    IBehaviorMatchBuilder withCall();

    /**
     * 观察行为内部的行调用
     * 调用之后，
     * <ul>
     * <li>{@link AdviceListener#beforeLine(Advice, int)}</li>
     * </ul>
     * 将会被触发
     *
     * @return IBuildingForWatching
     */
    IBehaviorMatchBuilder withLine();

    /**
     * 观察器
     *
     * @param listeners advice监听器定义
     * @return this
     */
    IBehaviorMatchBuilder onListener(Listeners listeners);

    /**
     * 返回class
     *
     * @return
     */
    IClassMatchBuilder onClass();

    /**
     * 观察器
     *
     * @param listeners           监听器回调
     * @param eventEventTypeArray 需要被监听的事件列表（参数废弃）
     * @return this
     */
    IBehaviorMatchBuilder onListener(Listeners listeners, EventType... eventEventTypeArray);


    /**
     * 观察器
     * 与 onWatch 的区别是执行完 WatchCallback#watchCompleted则会删除Watch
     *
     * @param watchCallback
     * @throws Throwable 如果 watching 不成功会抛出异常
     * @see IClassMatchBuilder#onWatch()
     */
    void onWatching(Listeners listeners, WatchCallback watchCallback, EventType... eventEventTypeArray) throws Throwable;
}
