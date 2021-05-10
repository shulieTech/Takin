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
package com.shulie.instrument.module.register.register;


import com.shulie.instrument.simulator.api.resource.SimulatorConfig;

import java.lang.instrument.Instrumentation;

/**
 * @author xiaobin.zfb
 * @since 2020/8/20 10:22 上午
 */
public class RegisterOptions {
    private String md5;
    /**
     * 注册器名称
     */
    private String registerName;
    /**
     * 注册的路径
     */
    private String registerBasePath;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * zk地址
     */
    private String zkServers;
    /**
     * 连接超时时间
     */
    private int connectionTimeoutMillis = 30000;
    /**
     * session超时时间
     */
    private int sessionTimeoutMillis = 60000;

    /**
     * 仿真器配置
     */
    private SimulatorConfig simulatorConfig;

    public SimulatorConfig getSimulatorConfig() {
        return simulatorConfig;
    }

    public void setSimulatorConfig(SimulatorConfig simulatorConfig) {
        this.simulatorConfig = simulatorConfig;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public String getRegisterBasePath() {
        return registerBasePath;
    }

    public void setRegisterBasePath(String registerBasePath) {
        this.registerBasePath = registerBasePath;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getZkServers() {
        return zkServers;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }

    public int getConnectionTimeoutMillis() {
        return connectionTimeoutMillis;
    }

    public void setConnectionTimeoutMillis(int connectionTimeoutMillis) {
        this.connectionTimeoutMillis = connectionTimeoutMillis;
    }

    public int getSessionTimeoutMillis() {
        return sessionTimeoutMillis;
    }

    public void setSessionTimeoutMillis(int sessionTimeoutMillis) {
        this.sessionTimeoutMillis = sessionTimeoutMillis;
    }
}
