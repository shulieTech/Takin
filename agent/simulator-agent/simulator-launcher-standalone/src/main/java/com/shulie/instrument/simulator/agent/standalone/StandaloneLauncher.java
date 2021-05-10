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
package com.shulie.instrument.simulator.agent.standalone;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * agent Main，agent 启动入口，整合与控制中心交互流程
 * 主要的职责负责agent 启动与停止、转发 agent 命令
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/16 6:00 下午
 */
public class StandaloneLauncher {
    private static final String DEFAULT_AGENT_HOME
            = new File(StandaloneLauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile())
            .getParent();

    private final static String ARGS_KEY_DELAY = "delay";
    private final static String ARGS_KEY_UNIT = "unit";

    public static void main(String[] args) throws Throwable {
        StandaloneLauncher launcher = new StandaloneLauncher();
        launcher.start();
    }

    private synchronized void start() throws Throwable {
        File file = new File(DEFAULT_AGENT_HOME + File.separator + "core", "simulator-agent-core.jar");
        AgentClassLoader agentClassLoader = new AgentClassLoader(new URL[]{file.toURI().toURL()});
        Class coreLauncherOfClass = agentClassLoader.loadClass("com.shulie.instrument.simulator.agent.core.CoreLauncher");
        Constructor constructor = coreLauncherOfClass.getConstructor(String.class);
        Object coreLauncherOfInstance = constructor.newInstance(DEFAULT_AGENT_HOME);

        Integer delay = getDelay();
        if (delay != null) {
            Method setDelayMethod = coreLauncherOfClass.getDeclaredMethod("setDelay", int.class);
            setDelayMethod.invoke(coreLauncherOfInstance, delay.intValue());
        }

        TimeUnit unit = getUnit();
        if (unit != null) {
            Method setUnitMethod = coreLauncherOfClass.getDeclaredMethod("setUnit", TimeUnit.class);
            setUnitMethod.invoke(coreLauncherOfInstance, unit);
        }

        Method startMethod = coreLauncherOfClass.getDeclaredMethod("start");
        startMethod.invoke(coreLauncherOfInstance);
        this.wait();
    }

    /**
     * 获取启动的延迟时间
     *
     * @return 启动延迟时间
     */
    private static Integer getDelay() {
        String durationStr = trim(System.getProperty(ARGS_KEY_DELAY));
        if (isNumeric(durationStr)) {
            return Integer.valueOf(durationStr);
        }
        durationStr = trim(System.getenv(ARGS_KEY_DELAY));
        if (isNumeric(durationStr)) {
            return Integer.valueOf(durationStr);
        }
        return null;
    }

    /**
     * 获取启动延迟时间单位
     *
     * @return 延迟时间单位
     */
    private static TimeUnit getUnit() {
        String unitStr = trim(System.getProperty(ARGS_KEY_UNIT));
        if (isNotBlank(unitStr)) {
            TimeUnit unit = TimeUnit.valueOf(unitStr);
            if (unit != null) {
                return unit;
            }
        }
        unitStr = trim(System.getenv(ARGS_KEY_UNIT));
        if (isNotBlank(unitStr)) {
            TimeUnit unit = TimeUnit.valueOf(unitStr);
            if (unit != null) {
                return unit;
            }
        }
        return null;
    }

    /**
     * trim 字符串
     *
     * @param str 字符串
     * @return 返回 trim后的字符串
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }


    /**
     * 是否是数字
     *
     * @param str 字符串
     * @return 返回 true|false
     */
    public static boolean isNumeric(final String str) {
        if (isBlank(str)) {
            return false;
        }
        final int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNotBlank(final String string) {
        return null != string
                && string.length() > 0
                && !string.matches("^\\s*$");
    }

    private static boolean isBlank(final String string) {
        return !isNotBlank(string);
    }
}
