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
package com.shulie.instrument.simulator.api.scope;

/**
 * 拦截器作用域调用
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface InterceptorScopeInvocation {
    /**
     * 返回名称
     *
     * @return 名称
     */
    String getName();

    /**
     * 尝试进入
     *
     * @param policy
     * @return
     */
    boolean tryEnter(ExecutionPolicy policy);

    /**
     * 尝试离开
     *
     * @param policy
     * @return
     */
    boolean canLeave(ExecutionPolicy policy);

    /**
     * 离开
     *
     * @param policy
     */
    void leave(ExecutionPolicy policy);

    /**
     * 是否是激活状态
     *
     * @return
     */
    boolean isActive();

    /**
     * 设置附件
     *
     * @param attachment
     * @return
     */
    Object setAttachment(Object attachment);

    /**
     * 获取附件
     *
     * @return
     */
    Object getAttachment();

    /**
     * 使用附件创建工厂获取或者创建附件
     *
     * @param factory
     * @return
     */
    Object getOrCreateAttachment(AttachmentFactory factory);

    /**
     * 删除附件
     *
     * @return
     */
    Object removeAttachment();
}

