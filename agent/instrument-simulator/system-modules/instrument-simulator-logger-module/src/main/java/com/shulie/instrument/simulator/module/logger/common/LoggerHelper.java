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
package com.shulie.instrument.simulator.module.logger.common;

/**
 * 
 * @author hengyunabc 2019-09-06
 *
 */
public interface LoggerHelper {
    public static final String clazz = "class";
    public static final String classLoader = "classLoader";
    public static final String classLoaderHash = "classLoaderHash";
    public static final String codeSource = "codeSource";

    // logger info
    public static final String level = "level";
    public static final String effectiveLevel = "effectiveLevel";

    // log4j2 only
    public static final String config = "config";

    // type boolean
    public static final String additivity = "additivity";
    public static final String appenders = "appenders";

    // appender info
    public static final String name = "name";
    public static final String file = "file";
    public static final String blocking = "blocking";
    // type List<String>
    public static final String appenderRef = "appenderRef";
    public static final String target = "target";

}
