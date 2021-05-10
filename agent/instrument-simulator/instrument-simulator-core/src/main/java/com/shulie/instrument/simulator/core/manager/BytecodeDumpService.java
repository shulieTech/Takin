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
package com.shulie.instrument.simulator.core.manager;

import java.io.File;

/**
 * Created by xiaobin on 2017/1/19.
 */
public interface BytecodeDumpService {
    /**
     * dump字节码
     *
     * @param dumpMessage  dump信息
     * @param dumpPath     dump 路径
     * @param jvmClassName className
     * @param bytes        字节码数组
     * @param classLoader  类加载器
     * @param isDumpFile   是否dump文件
     */
    void dumpBytecode(String dumpMessage, String dumpPath, String jvmClassName, boolean isDumpFile, byte[] bytes, ClassLoader classLoader);

    /**
     * 获取dump的文件
     *
     * @return 返回dump的文件
     */
    File getDumpFile();
}
