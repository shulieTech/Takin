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
package com.shulie.instrument.simulator.module.logger;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.api.util.ParameterUtils;
import com.shulie.instrument.simulator.module.ParamSupported;
import com.shulie.instrument.simulator.module.logger.common.Log4j2Helper;
import com.shulie.instrument.simulator.module.logger.common.Log4jHelper;
import com.shulie.instrument.simulator.module.logger.common.LogbackHelper;
import com.shulie.instrument.simulator.module.logger.common.LoggerHelper;
import com.shulie.instrument.simulator.module.util.AsmRenameUtil;
import com.shulie.instrument.simulator.module.util.ClassLoaderUtils;
import com.shulie.instrument.simulator.module.util.ReflectUtils;
import com.shulie.instrument.simulator.module.util.StringUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/6 7:54 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "logger", version = "1.0.0", author = "xiaobin@shulie.io", description = "日志模块")
public class LoggerModule extends ParamSupported implements ExtensionModule {
    private final static Logger logger = LoggerFactory.getLogger(LoggerModule.class);
    @Resource
    private SimulatorConfig config;

    private static byte[] LoggerHelperBytes;
    private static byte[] Log4jHelperBytes;
    private static byte[] LogbackHelperBytes;
    private static byte[] Log4j2HelperBytes;

    private static Map<Class<?>, byte[]> classToBytesMap = new HashMap<Class<?>, byte[]>();

    private static String simulatorClassLoaderHash = ClassLoaderUtils
            .classLoaderHash(LoggerModule.class.getClassLoader());

    static {
        LoggerHelperBytes = loadClassBytes(LoggerHelper.class);
        Log4jHelperBytes = loadClassBytes(Log4jHelper.class);
        LogbackHelperBytes = loadClassBytes(LogbackHelper.class);
        Log4j2HelperBytes = loadClassBytes(Log4j2Helper.class);

        classToBytesMap.put(LoggerHelper.class, LoggerHelperBytes);
        classToBytesMap.put(Log4jHelper.class, Log4jHelperBytes);
        classToBytesMap.put(LogbackHelper.class, LogbackHelperBytes);
        classToBytesMap.put(Log4j2Helper.class, Log4j2HelperBytes);
    }

    @Command(value = "level", description = "变更日志级别")
    public CommandResponse level(final Map<String, String> args) {
        try {
            final String name = args.get("name");
            final String hashCode = args.get("hashCode");
            final String level = args.get("level");
            if (name == null || level == null) {
                return CommandResponse.failure("name or level must not be null!");
            }
            return level(name, level, hashCode);
        } catch (Throwable e) {
            return CommandResponse.failure(e);
        }
    }

    @Command(value = "info", description = "日志配置信息")
    public CommandResponse info(final Map<String, String> args) {
        try {
            final String name = args.get("name");
            final String hashCode = args.get("hashCode");
            final boolean includeNoAppender = ParameterUtils.getBoolean(args, "includeNoAppender", true);
            return loggers(hashCode, name, includeNoAppender);
        } catch (Throwable e) {
            return CommandResponse.failure(e);
        }
    }

    public CommandResponse level(String name, String level, String hashCode) {
        Instrumentation inst = config.getInstrumentation();
        boolean result = false;
        try {
            Boolean updateResult = this.updateLevel(inst, Log4jHelper.class, hashCode, name, level);
            if (Boolean.TRUE.equals(updateResult)) {
                result = true;
            }
        } catch (Throwable e) {
            logger.error("logger command update log4j level error", e);
        }

        try {
            Boolean updateResult = this.updateLevel(inst, LogbackHelper.class, hashCode, name, level);
            if (Boolean.TRUE.equals(updateResult)) {
                result = true;
            }
        } catch (Throwable e) {
            logger.error("logger command update logback level error", e);
        }

        try {
            Boolean updateResult = this.updateLevel(inst, Log4j2Helper.class, hashCode, name, level);
            if (Boolean.TRUE.equals(updateResult)) {
                result = true;
            }
        } catch (Throwable e) {
            logger.error("logger command update log4j2 level error", e);
        }

        if (result) {
            return CommandResponse.success(true);
        } else {
            return CommandResponse.failure("Update logger level fail. Try to specify the classloader with the -c option. Use `sc -d CLASSNAME` to find out the classloader hashcode.");
        }
    }

    public CommandResponse loggers(String hashCode, String name, boolean includeNoAppender) {
        Map<ClassLoader, LoggerTypes> classLoaderLoggerMap = new LinkedHashMap<ClassLoader, LoggerTypes>();

        for (Class<?> clazz : config.getInstrumentation().getAllLoadedClasses()) {
            String className = clazz.getName();
            ClassLoader classLoader = clazz.getClassLoader();

            // if special classloader
            if (hashCode != null && !hashCode.equals(StringUtils.classLoaderHash(clazz))) {
                continue;
            }

            if (classLoader != null) {
                LoggerTypes loggerTypes = classLoaderLoggerMap.get(classLoader);
                if (loggerTypes == null) {
                    loggerTypes = new LoggerTypes();
                    classLoaderLoggerMap.put(classLoader, loggerTypes);
                }
                if ("org.apache.log4j.Logger".equals(className)) {
                    loggerTypes.addType(LoggerType.LOG4J);
                } else if ("ch.qos.logback.classic.Logger".equals(className)) {
                    loggerTypes.addType(LoggerType.LOGBACK);
                } else if ("org.apache.logging.log4j.Logger".equals(className)) {
                    loggerTypes.addType(LoggerType.LOG4J2);
                }
            }
        }

        List<Map<String, Map<String, Object>>> list = new ArrayList<Map<String, Map<String, Object>>>();
        for (Map.Entry<ClassLoader, LoggerTypes> entry : classLoaderLoggerMap.entrySet()) {
            ClassLoader classLoader = entry.getKey();
            LoggerTypes loggerTypes = entry.getValue();

            if (loggerTypes.contains(LoggerType.LOG4J)) {
                Map<String, Map<String, Object>> loggerInfoMap = loggerInfo(classLoader, Log4jHelper.class, name, includeNoAppender);
                list.add(loggerInfoMap);
            }

            if (loggerTypes.contains(LoggerType.LOGBACK)) {
                Map<String, Map<String, Object>> loggerInfoMap = loggerInfo(classLoader, LogbackHelper.class, name, includeNoAppender);
                list.add(loggerInfoMap);
            }

            if (loggerTypes.contains(LoggerType.LOG4J2)) {
                Map<String, Map<String, Object>> loggerInfoMap = loggerInfo(classLoader, Log4j2Helper.class, name, includeNoAppender);
                list.add(loggerInfoMap);
            }
        }

        return CommandResponse.success(list);
    }

    private static String helperClassNameWithClassLoader(ClassLoader classLoader, Class<?> helperClass) {
        String classLoaderHash = ClassLoaderUtils.classLoaderHash(classLoader);
        String className = helperClass.getName();
        // if want to debug, change to return className
        return className + simulatorClassLoaderHash + classLoaderHash;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, Object>> loggerInfo(ClassLoader classLoader, Class<?> helperClass, String name, boolean includeNoAppender) {
        Map<String, Map<String, Object>> loggers = Collections.emptyMap();

        String helperClassName = helperClassNameWithClassLoader(classLoader, helperClass);
        try {
            classLoader.loadClass(helperClassName);
        } catch (ClassNotFoundException e) {
            try {
                byte[] helperClassBytes = AsmRenameUtil.renameClass(classToBytesMap.get(helperClass),
                        helperClass.getName(), helperClassName);
                ReflectUtils.defineClass(helperClassName, helperClassBytes, classLoader);
            } catch (Throwable e1) {
                logger.error("simulator loggger command try to define helper class error: " + helperClassName,
                        e1);
            }
        }

        try {
            Class<?> clazz = classLoader.loadClass(helperClassName);
            Method getLoggersMethod = clazz.getMethod("getLoggers", new Class<?>[]{String.class, boolean.class});
            loggers = (Map<String, Map<String, Object>>) getLoggersMethod.invoke(null,
                    new Object[]{name, includeNoAppender});
        } catch (Throwable e) {
            // ignore
        }

        //expose attributes to json: classloader, classloaderHash
        for (Map<String, Object> loggerInfo : loggers.values()) {
            Class clazz = (Class) loggerInfo.get(LoggerHelper.clazz);
            loggerInfo.put(LoggerHelper.classLoader, getClassLoaderName(clazz.getClassLoader()));
            loggerInfo.put(LoggerHelper.classLoaderHash, StringUtils.classLoaderHash(clazz));

            List<Map<String, Object>> appenders = (List<Map<String, Object>>) loggerInfo.get(LoggerHelper.appenders);
            for (Map<String, Object> appenderInfo : appenders) {
                Class appenderClass = (Class) appenderInfo.get(LoggerHelper.clazz);
                if (appenderClass != null) {
                    appenderInfo.put(LoggerHelper.classLoader, getClassLoaderName(appenderClass.getClassLoader()));
                    appenderInfo.put(LoggerHelper.classLoaderHash, StringUtils.classLoaderHash(appenderClass));
                }
            }
        }

        return loggers;
    }

    private String getClassLoaderName(ClassLoader classLoader) {
        return classLoader == null ? null : classLoader.toString();
    }

    private Boolean updateLevel(Instrumentation inst, Class<?> helperClass, String hashCode, String name, String level) throws Exception {
        ClassLoader classLoader = null;
        if (hashCode == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        } else {
            classLoader = ClassLoaderUtils.getClassLoader(inst, hashCode);
        }

        Class<?> clazz = classLoader.loadClass(helperClassNameWithClassLoader(classLoader, helperClass));
        Method updateLevelMethod = clazz.getMethod("updateLevel", new Class<?>[]{String.class, String.class});
        return (Boolean) updateLevelMethod.invoke(null, new Object[]{name, level});

    }

    enum LoggerType {
        LOG4J, LOGBACK, LOG4J2
    }

    static class LoggerTypes {
        Set<LoggerType> types = new HashSet<LoggerType>();

        public Collection<LoggerType> types() {
            return types;
        }

        public void addType(LoggerType type) {
            types.add(type);
        }

        public boolean contains(LoggerType type) {
            return types.contains(type);
        }
    }

    private static byte[] loadClassBytes(Class<?> clazz) {
        try {
            InputStream stream = LoggerModule.class.getClassLoader()
                    .getResourceAsStream(clazz.getName().replace('.', '/') + ".class");

            return getBytes(stream);
        } catch (IOException e) {
            // ignore
            return null;
        }
    }

    /**
     * @return a byte[] containing the information contained in the specified
     * InputStream.
     * @throws java.io.IOException
     */
    private static byte[] getBytes(InputStream input) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        copy(input, result);
        result.close();
        return result.toByteArray();
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
    }

}
