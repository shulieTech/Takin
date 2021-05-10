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
package com.pamirs.pradar;

import java.util.Map;

/**
 * Pradar 向外暴露的公共的服务
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/11 2:08 下午
 */
public interface IPradarService {

    /**
     * 判断当前流量是否是压测流量
     *
     * @return 返回当前流量是否是压测流量
     */
    boolean isClusterTest();

    /**
     * 向当前流量打标记
     * key 的长度不能超过16个字符
     * value 的长度不能超过48个字符
     *
     * @param key  标记key，长度不超过16个字符
     * @param mark 标记值，长度不超过48个字符
     * @return 返回成功还是失败
     */
    boolean mark(String key, String mark);

    /**
     * 当前流量取消标记
     *
     * @param key 标记key
     */
    void unmark(String key);

    /**
     * 判断当前流量是否有指定标记
     *
     * @param mark 标记值
     * @return 返回当前流量是否有指定标记
     */
    boolean hasMark(String mark);

    /**
     * 返回当前压测是否可用
     *
     * @return true|false
     */
    boolean isClusterTestEnabled();

    /**
     * 获取调用的上下文
     *
     * @return
     */
    Map<String, String> getInvokeContext();

}
