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
package com.pamirs.pradar.upload.info;

import com.alibaba.fastjson.JSON;
import com.pamirs.pradar.AppNameUtils;
import com.pamirs.pradar.PradarCoreUtils;
import com.pamirs.pradar.common.ServerDetector;
import com.shulie.druid.util.Utils;
import org.apache.commons.lang.StringUtils;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shiyajian
 * create: 2020-07-20
 */
public class AgentInfo {

    /**
     * agentId 由 IP+应用名+应用包的绝对路径的 MD5 组成
     * FIXME 如果没有传入应用包的绝对路径，同一台机器下面有多个应用会重复，如果只有一个，那么就不会重复；
     * 强制传入应用的绝对路径是不是就可以解决这个问题了？？
     */
    private String agentId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用存放的绝对路径
     * IP + 绝对路径，可以定位到一个具体的应用
     */
    private String appAbsPath;

    /**
     * Jar / War
     */
    private AppJarInfo.PackageType packageType;

    /**
     * 编程语言, 默认是 Java
     */
    private String programLanguage;

    /**
     * Agent版本
     */
    private String agentVersion;

    /**
     * 运行进程ID
     */
    private Long pid;

    /**
     * 主机名
     */
    private String hostName;

    /**
     * JDK版本
     */
    private String jdkVersion;

    /**
     * 主机IP
     */
    private String hostIp;

    /**
     * 应用启动时间
     */
    private Long startTime;

    /**
     * 启动时候的参数
     */
    private List<String> inputArgs;

    /**
     * 垃圾回收器
     */
    private List<String> garbageCollectors;

    /**
     * 应用引用的Jar包对应的名字
     */
    private List<String> dependenciesJarNames;

    /**
     * 容器类型
     * 如果为 Jar 包，类型为： Spring Boot [tomcat]、Spring Boot [jetty]、Spring Boot [undertow]
     * 如果为 War 包，类型为： 外部容器 [ tomcat ]、jboss、weblogic、webshpere、mule
     */
    private String containerType;

    /**
     * maven 的 groupId
     */
    private String groupId;

    /**
     * maven 的 artifactId
     */
    private String artifactId;

    private AgentInfo() {
        this.appName = initAppName();
        this.packageType = initPackageType();
        this.agentVersion = initAgentVersion();
        this.pid = initPid();
        this.hostName = initHostName();
        this.jdkVersion = initJdkVersion();
        this.hostIp = initHostIp();
        this.startTime = initStartTime();
        this.inputArgs = initInputArgs();
        this.containerType = initContainerType();
        this.dependenciesJarNames = initDependenciesJarNames();
        this.garbageCollectors = initGCs();
        this.artifactId = initArtifactId();
        this.groupId = initGroupId();
        this.appAbsPath = initAppAbsPath();
        this.agentId = initAgentId();
        this.programLanguage = "Java";
    }

    private final static transient AgentInfo INSTANCE = new AgentInfo();

    public static AgentInfo getInstance() {
        return INSTANCE;
    }

    private String initAgentId() {
        return Utils.md5(this.appName + "@" + this.hostIp + ":/" + this.appAbsPath);
    }

    private String initAppName() {
        return AppNameUtils.appName();
    }

    private String initAgentVersion() {
        return this.getClass().getPackage().getImplementationVersion();
    }

    private Long initPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String[] split = StringUtils.split(name,'@');
        if (split.length == 2) {
            return Long.valueOf(split[0]);
        }
        return -1L;
    }

    private String initHostName() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String[] split = StringUtils.split(name,'@');
        if (split.length == 2) {
            return split[1];
        }
        return "unknown";
    }

    private String initJdkVersion() {
        return ManagementFactory.getRuntimeMXBean().getVmName() + " [" + System.getProperty("java.version") + "]";
    }

    private String initHostIp() {
        return PradarCoreUtils.getLocalAddress();
    }

    private Long initStartTime() {
        return ManagementFactory.getRuntimeMXBean().getStartTime();
    }

    private List<String> initGCs() {
        List<String> gcList = new ArrayList<String>();
        List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        if (garbageCollectorMXBeans.size() > 0) {
            for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
                gcList.add(garbageCollectorMXBean.getName());
            }
        }
        return gcList;
    }

    private List<String> initInputArgs() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments();
    }

    private AppJarInfo.PackageType initPackageType() {
        return AppJarInfo.INSTANCE.getPackageType();
    }

    private List<String> initDependenciesJarNames() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String libPath : AppJarInfo.INSTANCE.getLibs()) {
            arrayList.add(getJarName(libPath));
        }
        return arrayList;
    }

    private String initContainerType() {
        if (AppJarInfo.INSTANCE.isBoot()) {
            String prefix = "Spring Boot ";
            for (String dependenciesJarName : initDependenciesJarNames()) {
                if (dependenciesJarName.startsWith("spring-boot-starter-tomcat")) {
                    return prefix + "[ Tomcat ]";
                }
                if (dependenciesJarName.startsWith("spring-boot-starter-jetty")) {
                    return prefix + "[ Jetty ]";
                }
                if (dependenciesJarName.startsWith("spring-boot-starter-undertow")) {
                    return prefix + "[ Undertow ]";
                }
            }
            return prefix;
        }
        return "外部容器 [ " + ServerDetector.getServerType().getDesc() + " ]";
    }

    private String initGroupId() {
        Object groupId = AppJarInfo.INSTANCE.getPomProperties().get("groupId");
        return groupId == null ? null : (String) groupId;
    }

    private String initArtifactId() {
        Object artifactId = AppJarInfo.INSTANCE.getPomProperties().get("artifactId");
        return artifactId == null ? null : (String) artifactId;
    }

    private String initAppAbsPath() {
        return AppJarInfo.INSTANCE.getAbsPath();
    }

    /**
     * 将应用依赖的的 Jar 包全路径切割成只有文件名
     */
    private String getJarName(String libPath) {
        return libPath.substring(libPath.lastIndexOf("/") + 1);
    }

    public String toJson() {
        return JSON.toJSONString(AgentInfo.INSTANCE);
    }

    // ================== getter ============================

    public String getAgentId() {
        return agentId;
    }

    public String getAppName() {
        return appName;
    }

    public AppJarInfo.PackageType getPackageType() {
        return packageType;
    }

    public String getAgentVersion() {
        return agentVersion;
    }

    public Long getPid() {
        return pid;
    }

    public String getHostName() {
        return hostName;
    }

    public String getJdkVersion() {
        return jdkVersion;
    }

    public String getHostIp() {
        return hostIp;
    }

    public Long getStartTime() {
        return startTime;
    }

    public List<String> getInputArgs() {
        return inputArgs;
    }

    public List<String> getGarbageCollectors() {
        return garbageCollectors;
    }

    public List<String> getDependenciesJarNames() {
        return dependenciesJarNames;
    }

    public String getContainerType() {
        return containerType;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getAppAbsPath() {
        return appAbsPath;
    }

    public String getProgramLanguage() {
        return programLanguage;
    }
}
