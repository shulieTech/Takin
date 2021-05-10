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
package com.shulie.instrument.simulator.agent.spi.impl.utils;

import java.io.File;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/17 10:59 下午
 */
public class FileUtils {
    /**
     * 删除文件
     *
     * @param file 文件
     */
    public static void delete(File file) {
        clean(file);
    }

    /**
     * 清空目录
     *
     * @param dir 目录
     */
    public static void cleanDirectory(File dir) {
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles();
        for (File f : files) {
            clean(f);
        }
    }

    private static void clean(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        File[] files = file.listFiles();
        for (File f : files) {
            clean(f);
            f.delete();
        }
        file.delete();
    }
}
