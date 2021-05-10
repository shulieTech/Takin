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
package com.shulie.instrument.simulator.module.profiler.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/9 2:59 下午
 */
public class LibUtils {
    public static String getLibPath() {
        String profierSoPath = null;
        if (OSUtils.isMac()) {
            profierSoPath = "async-profiler/libasyncProfiler-mac-x64.so";
        } else if (OSUtils.isLinux()) {
            profierSoPath = "async-profiler/libasyncProfiler-linux-x64.so";
            if (OSUtils.isArm32()) {
                profierSoPath = "async-profiler/libasyncProfiler-linux-arm.so";
            } else if (OSUtils.isArm64()) {
                profierSoPath = "async-profiler/libasyncProfiler-linux-aarch64.so";
            }
        } else {
            throw new UnsupportedOperationException("Unsupported operation system. " + OSUtils.platform.toString());
        }

        if (profierSoPath == null) {
            throw new UnsupportedOperationException("Unsupported operation system. " + OSUtils.platform.toString());
        }

        String filePath = System.getProperty("java.io.tmpdir") + File.separator + profierSoPath;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        copy(LibUtils.class.getResourceAsStream("/" + profierSoPath), file);
        file.deleteOnExit();
        return filePath;
    }

    private static void copy(InputStream in, File file) {
        FileOutputStream fos = null;
        byte[] data = new byte[1024];
        int length = 0;
        try {
            fos = new FileOutputStream(file);
            while ((length = in.read(data)) != -1) {
                fos.write(data, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException("拷贝库文件出现错误", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
