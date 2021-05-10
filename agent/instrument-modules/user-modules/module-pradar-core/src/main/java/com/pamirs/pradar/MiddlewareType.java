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

/**
 * @author xiaobin.zfb
 * @since 2020/7/8 2:34 下午
 */
public final class MiddlewareType {
    /**
     * web server类型
     */
    static public final int TYPE_WEB_SERVER = 0;
    /**
     * rpc
     */
    static public final int TYPE_RPC = 1;

    /**
     * 消息
     */
    static public final int TYPE_MQ = 3;

    /**
     * db
     */
    static public final int TYPE_DB = 4;

    /**
     * 缓存
     */
    static public final int TYPE_CACHE = 5;

    /**
     * 索引
     */
    static public final int TYPE_SEARCH = 6;
    /**
     * job
     */
    static public final int TYPE_JOB = 7;

    /**
     * 文件
     */
    static public final int TYPE_FS = 8;
    /**
     * 本地方法
     */
    static public final int TYPE_LOCAL = 9;

    /**
     * 未知
     */
    static public final int TYPE_UNKNOW = -1;

}
