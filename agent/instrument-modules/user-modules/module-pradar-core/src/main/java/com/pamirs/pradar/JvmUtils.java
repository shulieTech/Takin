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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import static com.pamirs.pradar.AppNameUtils.appName;

/**
 * @author vincent
 * @version v0.1 2017/5/3 16:53
 */
public class JvmUtils {

    public static int getPid() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName(); // format: "pid@hostname"
        try {
            return Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Throwable e) {
            return -1;
        }
    }

    public static boolean writePidFile() {
        FileWriter fileWriter = null;
        try {

            String pidFileName = appName() + ".pid";
            String path = Pradar.PRADAR_LOG_DIR;
            File pidFile = new File(path + File.separatorChar + pidFileName);
            fileWriter = new FileWriter(pidFile);
            fileWriter.write(getPid());
            return true;
        } catch (Throwable e) {
            //ignore
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                //ignore
            }
        }
        return false;
    }
}
