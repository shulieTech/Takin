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
package com.shulie.instrument.simulator.api.listener;

import com.shulie.instrument.simulator.api.listener.ext.AdviceListener;
import com.shulie.instrument.simulator.api.scope.ExecutionPolicy;

/**
 * AdviceListener 回调，此类是为了让目标的 Listener 延迟实例化，这样可以最大化的减少在多个业务类加载器的情况下，
 * 最大化的限度减少默认模块类加载器加载到的类，因为在多业务类加载实例的情况下，默认类加载器一般只会负责加载 Module 类，
 * 而其他定义的 Listener 则是由业务类加载器实例对应的模块类加载器实例加载
 * <p>
 * 业务类加载器与模块类加载器是一一对应关系，一个业务类加载器会对应一个模块类加载器，这样的设计主要是因为模块中会引用到
 * 业务类加载器加载的类，而多业务类加载器则会产生一个引用的 Class 对象可能会由多个业务类加载器加载，而如果模块类加载器
 * 只有一个会导致模块中引用的类始终都是由同一个业务类加载器加载，导致类转换异常
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/21 9:32 下午
 */
public interface AdviceListenerCallback {
    /**
     * 回调函数，回调时产生对应的通知监听器实例，当实例化通知监听器失败时会抛出异常(如使用了构造传参但是参数类型与个数不匹配)
     * 该异常会被框架所捕获，如果监听器实现了 Interruptable 或者是添加了 @Interrupt 注解，则此异常会显式抛出至业务层，
     * 否则异常则在内部直接消化掉，只记录到日志中，不会显示抛出至业务上层
     * <p>
     * 此方法支持作用域的加载，此作用域会与作用于同线程的多级方法调用中
     *
     * @param listener  监听器
     * @param scopeName 作用域
     * @param policy    策略
     * @return 通知监听器
     */
    AdviceListener onCall(AdviceListener listener, String scopeName, ExecutionPolicy policy);
}
