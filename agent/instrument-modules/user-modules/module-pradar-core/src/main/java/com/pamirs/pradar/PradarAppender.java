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


public abstract class PradarAppender {
    /**
     * 写日志
     *
     * @param log 追加的日志
     */
    public abstract void append(String log);

    /**
     * 刷新输出缓冲区
     */
    public abstract void flush();

    /**
     * 触发滚动
     */
    public abstract void rollOver();

    /**
     * 重新加载，这是为了防止在多进程环境并发写同一个文件导致问题
     */
    public abstract void reload();

    /**
     * 关闭输出，释放资源
     */
    public abstract void close();

    /**
     * 清理释放资源，一般用于清理滚动后要删除的文件，或者临时文件
     */
    public abstract void cleanup();
}
