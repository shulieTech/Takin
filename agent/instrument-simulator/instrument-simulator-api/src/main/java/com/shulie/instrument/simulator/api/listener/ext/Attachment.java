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

/**
 * 可附件的
 * <p>
 * 继承类拥有线程不安全的携带附件操作
 * </p>
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface Attachment {

    /**
     * 添加附件
     *
     * @param attachment 消息附件
     */
    void attach(Object attachment);

    /**
     * 获取所携带的附件
     *
     * @param <T> 附件类型(自动强制转换)，请使用者自行保障类型不会转换失败!
     * @return 消息所携带的附件
     */
    <T> T attachment();

}
