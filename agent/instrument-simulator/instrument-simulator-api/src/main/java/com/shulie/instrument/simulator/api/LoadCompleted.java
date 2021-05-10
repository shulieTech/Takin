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

/**
 * 模块加载完成回调
 * <p>
 * 因{@link #loadCompleted()}方法比较常用，所以单独出来成为一个接口，
 * 原有方法语意、触发时机保持不变
 * </p>
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface LoadCompleted {

    /**
     * 模块加载完成，模块完成加载后调用！
     * <p>
     * 模块完成加载是在模块完成所有资源加载、分配之后的回调，在模块生命中期中有且只会调用一次。
     * 这里抛出异常不会影响模块被加载成功的结果。
     * </p>
     * <p>
     * 模块加载完成之后，所有的基于模块的操作都可以在这个回调中进行
     * </p>
     */
    void loadCompleted();

}
