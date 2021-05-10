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
package com.shulie.instrument.simulator.agent.module;

/**
 * 专门用于 Module 的Logger
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 8:23 下午
 */
public final class ModuleLogger {

    private final String loggerName;
    private final int prefixSize;

    public static ModuleLogger getLogger(String loggerName) {
        return new ModuleLogger(loggerName);
    }

    private ModuleLogger(String loggerName) {
        this.loggerName = loggerName;
        this.prefixSize = getLength(loggerName) + 3;
    }

    public void info(String log) {
        StringBuilder sb = new StringBuilder(getLength(log) + prefixSize);
        sb.append('[');
        sb.append(loggerName);
        sb.append("] ");
        sb.append(log);
        System.out.println(sb.toString());
    }

    private int getLength(String log) {
        if (log == null) {
            return 4;
        }
        return log.length();
    }

}
