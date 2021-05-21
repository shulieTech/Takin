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
package com.shulie.instrument.simulator.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.shulie.instrument.simulator.api.LoadMode;
import com.shulie.instrument.simulator.api.ModuleRepositoryMode;
import com.shulie.instrument.simulator.core.util.FeatureCodec;
import com.shulie.instrument.simulator.core.util.HttpUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 内核启动配置
 */
public class CoreConfigure {
    private final static Logger LOGGER = LoggerFactory.getLogger(CoreConfigure.class);

    private static final String KEY_NAMESPACE = "namespace";
    private static final String DEFAULT_VAL_NAMESPACE = "default";

    private static final String KEY_SIMULATOR_HOME = "simulator_home";
    private static final String KEY_LAUNCH_MODE = "mode";
    private static final String KEY_MODULE_REPOSITORY_MODE = "module_repository_mode";
    private static final String KEY_MODULE_REPOSITORY_ADDR = "module_repository_addr";
    private static final String KEY_SERVER_IP = "server.ip";
    private static final String KEY_SERVER_PORT = "server.port";
    private static final String KEY_SERVER_CHARSET = "server.charset";

    private static final String KEY_SYSTEM_MODULE_LIB_PATH = "system_module";
    private static final String KEY_USER_MODULE_LIB_PATH = "user_module";
    private static final String KEY_CLASSLOADER_JARS = "classloader_jars";
    private static final String KEY_LOG_PATH = "log_path";
    private static final String KEY_LOG_LEVEL = "log_level";
    private static final String KEY_ZK_SERVERS = "zk_servers";
    private static final String KEY_REGISTER_PATH = "register_path";
    private static final String KEY_ZK_CONNECTION_TIMEOUT = "zk_connection_timeout";
    private static final String KEY_ZK_SESSION_TIMEOUT = "zk_session_timeout";
    private static final String KEY_PROVIDER_LIB_PATH = "provider";
    private static final String KEY_CONFIG_LIB_PATH = "config";
    private static final String KEY_APP_NAME = "app_name";
    private static final String KEY_AGENT_VERSION = "agent_version";
    private static final String KEY1_APP_NAME = "simulator.app.name";
    private static final String KEY2_APP_NAME = "app.name";
    private static final String KEY_AGENT_ID = "agentId";
    private static final String VAL_LAUNCH_MODE_AGENT = "agent";
    private static final String VAL_LAUNCH_MODE_ATTACH = "attach";

    private static final String PROP_KEY_APP_NAME = "simulator.app.name";
    private static final String PROP_KEY_AGENT_ID = "simulator.agentId";
    private static final String PROP_KEY_SIMULATOR_HOME = "simulator.home";
    private static final String PROP_KEY_MD5 = "simulator.md5";
    private static final String PROP_KEY_AGENT_VERSION = "agent.version";
    private static final String PROP_KEY_SIMULATOR_VERSION = "simulator.version";

    //模块仓库模块
    private static final String VAL_MODULE_REPOSITORY_MODE_LOCAL = "local";
    private static final String VAL_MODULE_REPOSITORY_MODE_REMOTE = "remote";

    //默认的模块仓库地址
    private static final String VAL_MODULE_REPOSITORY_MODE_DEFAULT = "http://127.0.0.1:9888";

    private static final String KEY_UNSAFE_ENABLE = "unsafe.enable";

    /**
     * 模块禁用配置
     */
    private static final String KEY_DISABLED_MODULES = "simulator.modules.disabled";

    private static final String KEY_LICENSE_CODE = "license.code";

    /**
     * 写入 system property 的 key
     */
    private static final String PROP_KEY_LOG_PATH = "SIMULATOR_LOG_PATH";
    private static final String PROP_KEY_LOG_LEVEL = "SIMULATOR_LOG_LEVEL";

    /**
     * license 文件名称
     */
    private static final String LICENSE_FILE_NAME = "license.lic";

    // 受保护key数组，在保护key范围之内，以用户传递的配置为准，系统配置不允许覆盖
    private static final String[] PROTECT_KEY_ARRAY = {KEY_NAMESPACE, KEY_SIMULATOR_HOME, KEY_LAUNCH_MODE, KEY_SERVER_IP, KEY_SERVER_PORT, KEY_SERVER_CHARSET};

    // 用户配置和系统默认配置都可以，需要进行合并的key，例如user_module
    private static final String[] MULTI_KEY_ARRAY = {KEY_USER_MODULE_LIB_PATH};

    private static final FeatureCodec codec = new FeatureCodec(';', '=');

    private final Map<String, String> featureMap = new LinkedHashMap<String, String>();

    private Map<String, List<File>> bizClassLoaderInjectFiles;
    private Map<String, List<String>> bizClassLoaderInjectURLs;

    private final Instrumentation instrumentation;

    /**
     * md5值
     */
    private final String md5;

    /**
     * 当前框架的版本号
     */
    private final String simulatorVersion;

    private CoreConfigure(final String featureString,
                          final String propertiesFilePath,
                          final Instrumentation instrumentation) {
        final Map<String, String> featureMap = toFeatureMap(featureString);
        String appName = getProperty(featureMap, KEY_APP_NAME, KEY1_APP_NAME, KEY2_APP_NAME);
        if (appName == null) {
            appName = getAppName();
        }
        if (StringUtils.isBlank(appName)) {
            throw new RuntimeException("SIMULATOR: appName can't not be empty.");
        }
        final Map<String, String> propertiesMap = getPropertiesMap(propertiesFilePath, appName);
        this.featureMap.putAll(merge(featureMap, propertiesMap));

        this.simulatorVersion = getVersion0();
        this.instrumentation = instrumentation;
        this.md5 = readMd5(getSimulatorHome() + File.separator + "simulator.md5");
        this.dumpToSystemProperty(this.featureMap);
    }

    /**
     * 获取 log path
     *
     * @return
     */
    public String getLogPath() {
        String path = featureMap.get(KEY_LOG_PATH);
        if (StringUtils.isNotBlank(path)) {
            return path;
        }
        path = featureMap.get("simulator.log.path");
        if (StringUtils.isNotBlank(path)) {
            return path;
        }
        return System.getProperty("user.home") + File.separator + "logs" + File.separator + getAppName() + File.separator + "simulator";
    }

    /**
     * 获取 log level
     *
     * @return
     */
    public String getLogLevel() {
        return featureMap.get(KEY_LOG_LEVEL);
    }

    /**
     * 根据配置文件获取md5文件的内容
     *
     * @param md5FilePath md5文件路径
     * @return
     */
    private static String readMd5(String md5FilePath) {
        File md5File = new File(md5FilePath);
        if (!md5File.exists()) {
            return "";
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(md5File));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(StringUtils.trim(line)).append("\n");
            }
            return builder.toString();
        } catch (Exception e) {
            LOGGER.warn("read agent md5 file err:" + md5File.getAbsolutePath());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return "";
    }

    /**
     * 将当前的配置全部导入到系统
     *
     * @param featureMap
     */
    private void dumpToSystemProperty(Map<String, String> featureMap) {
        for (Map.Entry<String, String> entry : featureMap.entrySet()) {
            /**
             * 如果值不为空，则将值写入 System property 中
             */
            if (entry.getValue() != null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        }
        System.setProperty(PROP_KEY_APP_NAME, StringUtils.defaultIfBlank(getAppName(), ""));
        System.setProperty(PROP_KEY_AGENT_ID, StringUtils.defaultIfBlank(getAgentId(), ""));
        System.setProperty(PROP_KEY_MD5, StringUtils.defaultIfBlank(getSimulatorMd5(), ""));
        System.setProperty(PROP_KEY_LOG_PATH, StringUtils.defaultIfBlank(getLogPath(), ""));
        System.setProperty(PROP_KEY_LOG_LEVEL, StringUtils.defaultIfBlank(getLogLevel(), "info"));
        System.setProperty(PROP_KEY_SIMULATOR_HOME, StringUtils.defaultIfBlank(getSimulatorHome(), ""));
        System.setProperty(PROP_KEY_SIMULATOR_VERSION, simulatorVersion);
        String agentVersion = getAgentVersion();
        if (agentVersion != null) {
            System.setProperty(PROP_KEY_AGENT_VERSION, agentVersion);
        }
    }

    private Map<String, String> toFeatureMap(String featureString) {
        return codec.toMap(featureString);
    }

    private Map<String, String> getPropertiesMap(String propertiesFilePath, String appName) {
        if (StringUtils.startsWith(propertiesFilePath, "http://") || StringUtils.startsWith(propertiesFilePath, "https://")) {
            return toExternalPropertiesMap(propertiesFilePath, appName);
        } else {
            return toFilePropertiesMap(propertiesFilePath);
        }
    }

    /**
     * 获取仿真器的版本号
     *
     * @return
     */
    public String getSimulatorVersion() {
        return simulatorVersion;
    }

    /**
     * 读取 仿真器版本号
     *
     * @return
     */
    private String getVersion0() {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("com/shulie/instrument/simulator/version");
        try {
            return IOUtils.toString(is);
        } catch (IOException e) {
            // impossible
            return "1.0.0.1";
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 从外部获取配置
     *
     * @param propertiesUrl 配置获取的地址
     * @param appName       应用名称
     * @return 返回读取的配置结果
     */
    private Map<String, String> toExternalPropertiesMap(String propertiesUrl, String appName) {
        if (StringUtils.indexOf(propertiesUrl, '?') != -1) {
            propertiesUrl += "&appName=" + appName;
        } else {
            propertiesUrl += "?appName=" + appName;
        }
        String result = HttpUtils.doGet(propertiesUrl);
        if (StringUtils.isBlank(result)) {
            throw new RuntimeException("SIMULATOR: can't get app properties config from url:" + propertiesUrl);
        }
        return JSON.parseObject(result, new TypeReference<Map<String, String>>() {
        }.getType());
    }

    private Map<String, String> toFilePropertiesMap(String propertiesFilePath) {
        final Map<String, String> propertiesMap = new LinkedHashMap<String, String>();

        if (null == propertiesFilePath) {
            return propertiesMap;
        }

        final File propertiesFile = new File(propertiesFilePath);
        if (!propertiesFile.exists()
                || !propertiesFile.canRead()) {
            return propertiesMap;
        }


        // 从指定配置文件路径中获取配置信息
        final Properties properties = new Properties();
        InputStream is = null;
        try {
            is = openInputStream(propertiesFile);
            properties.load(is);
        } catch (Throwable cause) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }

        // 转换为Map
        for (String key : properties.stringPropertyNames()) {
            propertiesMap.put(key, properties.getProperty(key));
        }

        return propertiesMap;
    }

    private static FileInputStream openInputStream(final File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    private Map<String, String> merge(Map<String, String> featureMap, Map<String, String> propertiesMap) {

        // 以featureMap配置为准
        final Map<String, String> mergeMap = new LinkedHashMap<String, String>(featureMap);

        // 合并propertiesMap
        for (final Map.Entry<String, String> propertiesEntry : propertiesMap.entrySet()) {

            // 如果是受保护的KEY，则以featureMap中的非空值为准
            if (mergeMap.containsKey(propertiesEntry.getKey())
                    && ArrayUtils.contains(PROTECT_KEY_ARRAY, propertiesEntry.getKey())) {
                continue;
            }

            // 如果是多值合并的KEY，则不进行覆盖，转为合并
            else if (ArrayUtils.contains(MULTI_KEY_ARRAY, propertiesEntry.getKey())
                    && mergeMap.containsKey(propertiesEntry.getKey())) {
                mergeMap.put(
                        propertiesEntry.getKey(),
                        mergeMap.get(propertiesEntry.getKey()) + ";" + propertiesEntry.getValue()
                );
                continue;
            }

            // 合并K,V
            else {
                mergeMap.put(propertiesEntry.getKey(), propertiesEntry.getValue());
            }

        }

        return mergeMap;

    }

    private static volatile CoreConfigure instance;

    public static CoreConfigure toConfigure(final String featureString, final String propertiesFilePath, final Instrumentation inst) {
        return instance = new CoreConfigure(featureString, propertiesFilePath, inst);
    }

    public static CoreConfigure getInstance() {
        return instance;
    }

    /**
     * 获取容器的命名空间
     *
     * @return 容器的命名空间
     */
    public String getNamespace() {
        final String namespace = featureMap.get(KEY_NAMESPACE);
        return StringUtils.isNotBlank(namespace)
                ? namespace
                : DEFAULT_VAL_NAMESPACE;
    }

    /**
     * 获取系统模块加载路径
     *
     * @return 模块加载路径
     */
    public String getSystemModuleLibPath() {
        return featureMap.get(KEY_SYSTEM_MODULE_LIB_PATH);
    }


    /**
     * 获取用户模块加载路径
     *
     * @return 用户模块加载路径
     */
    public String getUserModuleLibPath() {
        return featureMap.get(KEY_USER_MODULE_LIB_PATH);
    }

    /**
     * 获取 zk 连接超时时间
     *
     * @return
     */
    public int getZkConnectionTimeout() {
        String value = featureMap.get(KEY_ZK_CONNECTION_TIMEOUT);
        if (NumberUtils.isDigits(value)) {
            return Integer.valueOf(value);
        }
        return 30000;
    }

    /**
     * 获取 zk sesison 超时时间
     *
     * @return
     */
    public int getZkSessionTimeout() {
        String value = featureMap.get(KEY_ZK_SESSION_TIMEOUT);
        if (NumberUtils.isDigits(value)) {
            return Integer.valueOf(value);
        }
        return 60000;
    }

    /**
     * 获取 zk 地址
     *
     * @return
     */
    public String getZkServers() {
        return featureMap.get(KEY_ZK_SERVERS);
    }

    /**
     * 获取心跳节点路径
     *
     * @return
     */
    public String getHeartbeatPath() {
        return featureMap.get(KEY_REGISTER_PATH);
    }

    /**
     * 获取所有需要业务Classloader加载的jar url
     *
     * @return Map<String, List < String>>
     */
    public Map<String, List<String>> getBizClassLoaderURLExternalForms() {
        if (bizClassLoaderInjectURLs != null) {
            return Collections.unmodifiableMap(bizClassLoaderInjectURLs);
        }
        synchronized (this) {
            if (bizClassLoaderInjectURLs != null) {
                return Collections.unmodifiableMap(bizClassLoaderInjectURLs);
            }
            String path = featureMap.get(KEY_CLASSLOADER_JARS);
            File file = new File("biz-classloader-inject.properties");
            Properties properties = new Properties();
            try {
                properties.load(new FileReader(file));
            } catch (IOException e) {
                return Collections.EMPTY_MAP;
            }
            Enumeration enumeration = properties.propertyNames();
            Map<String, List<String>> result = new HashMap<String, List<String>>();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = properties.getProperty(key);
                String[] values = StringUtils.split(value, ";");
                if (values == null || values.length == 0) {
                    continue;
                }
                List<String> jarFiles = new ArrayList<String>();
                for (String val : values) {
                    if (StringUtils.isBlank(val)) {
                        continue;
                    }
                    File f = new File(path, val);
                    if (f.exists()) {
                        try {
                            jarFiles.add(f.toURL().toExternalForm());
                        } catch (IOException e) {
                            LOGGER.error("SIMULATOR: classloader-inject.properties config invalid. jar file is invalid! {} = {} , {}", key, value, val);
                        }
                    } else {
                        LOGGER.error("SIMULATOR: classloader-inject.properties config invalid. jar is not exists! {} = {} , {}", key, value, val);
                    }
                }
                result.put(key, jarFiles);

            }
            bizClassLoaderInjectURLs = result;
        }

        return bizClassLoaderInjectURLs;
    }

    /**
     * 获取所有需要业务Classloader加载的jar包
     *
     * @return Map<String, List < JarFile>>
     */
    public Map<String, List<File>> getBizClassLoaderInjectFiles() {
        if (bizClassLoaderInjectFiles != null) {
            return Collections.unmodifiableMap(bizClassLoaderInjectFiles);
        }
        synchronized (this) {
            if (bizClassLoaderInjectFiles != null) {
                return Collections.unmodifiableMap(bizClassLoaderInjectFiles);
            }
            String path = featureMap.get(KEY_CLASSLOADER_JARS);
            File file = new File(path, "biz-classloader-inject.properties");
            Properties properties = new Properties();
            try {
                properties.load(new FileReader(file));
            } catch (IOException e) {
                return Collections.EMPTY_MAP;
            }
            Enumeration enumeration = properties.propertyNames();
            Map<String, List<File>> result = new HashMap<String, List<File>>();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = properties.getProperty(key);
                String[] values = StringUtils.split(value, ";");
                if (values == null || values.length == 0) {
                    continue;
                }
                List<File> jarFiles = new ArrayList<File>();
                for (String val : values) {
                    if (StringUtils.isBlank(val)) {
                        continue;
                    }
                    File f = new File(path, val);
                    if (f.exists()) {
                        jarFiles.add(f);
                    } else {
                        LOGGER.error("SIMULATOR: classloader-inject.properties config invalid. jar is not exists! {} = {} , {}", key, value, val);
                    }
                }
                result.put(key, jarFiles);

            }
            bizClassLoaderInjectFiles = result;
        }

        return Collections.unmodifiableMap(bizClassLoaderInjectFiles);
    }

    /**
     * 获取用户模块加载路径(集合)
     *
     * @return 用户模块加载路径(集合)
     */
    public String[] getUserModuleLibPaths() {
        return replaceWithSysPropUserHome(codec.toCollection(featureMap.get(KEY_USER_MODULE_LIB_PATH)).toArray(new String[]{}));
    }

    private static String[] replaceWithSysPropUserHome(final String[] pathArray) {
        if (ArrayUtils.isEmpty(pathArray)) {
            return pathArray;
        }
        final String SYS_PROP_USER_HOME = System.getProperty("user.home");
        for (int index = 0; index < pathArray.length; index++) {
            if (StringUtils.startsWith(pathArray[index], "~")) {
                pathArray[index] = StringUtils.replaceOnce(pathArray[index], "~", SYS_PROP_USER_HOME);
            }
        }
        return pathArray;
    }

    public String[] getUserModulePaths() {
        if (isModuleRepositoryLocal()) {
            return getUserModuleLibPaths();
        } else if (isModuleRepositoryRemote()) {
            return new String[]{getRemoteModuleRepositoryAddr()};
        }
        return null;
    }

    /**
     * 获取配置文件加载路径
     *
     * @return 配置文件加载路径
     */
    public String getConfigLibPath() {
        return featureMap.get(KEY_CONFIG_LIB_PATH);
    }

    @Override
    public String toString() {
        return codec.toString(featureMap);
    }

    /**
     * 是否是本地模块仓库
     *
     * @return true/false
     */
    private boolean isModuleRepositoryLocal() {
        return StringUtils.equals(featureMap.get(KEY_MODULE_REPOSITORY_MODE), VAL_MODULE_REPOSITORY_MODE_LOCAL);
    }

    /**
     * 是否是远程模块仓库
     *
     * @return true/false
     */
    private boolean isModuleRepositoryRemote() {
        return StringUtils.equals(featureMap.get(KEY_MODULE_REPOSITORY_MODE), VAL_MODULE_REPOSITORY_MODE_REMOTE);
    }

    /**
     * 是否以Agent模式启动
     *
     * @return true/false
     */
    private boolean isLaunchByAgentMode() {
        return StringUtils.equals(featureMap.get(KEY_LAUNCH_MODE), VAL_LAUNCH_MODE_AGENT);
    }

    /**
     * 是否以Attach模式启动
     *
     * @return true/false
     */
    private boolean isLaunchByAttachMode() {
        return StringUtils.equals(featureMap.get(KEY_LAUNCH_MODE), VAL_LAUNCH_MODE_ATTACH);
    }

    /**
     * 获取仿真器的启动模式
     * 默认按照ATTACH模式启动
     *
     * @return 仿真器的启动模式
     */
    public LoadMode getLaunchMode() {
        if (isLaunchByAgentMode()) {
            return LoadMode.AGENT;
        }
        if (isLaunchByAttachMode()) {
            return LoadMode.ATTACH;
        }
        return LoadMode.ATTACH;
    }

    /**
     * 获取模块仓库模式，默认为本地仓库模式
     *
     * @return 模块仓库模式
     */
    public ModuleRepositoryMode getModuleRepositoryMode() {
        if (isModuleRepositoryLocal()) {
            return ModuleRepositoryMode.LOCAL;
        }

        if (isModuleRepositoryRemote()) {
            return ModuleRepositoryMode.REMOTE;
        }
        return ModuleRepositoryMode.LOCAL;
    }

    /**
     * 获取远程模块仓库地址
     *
     * @return 模块仓库地址
     */
    public String getRemoteModuleRepositoryAddr() {
        return StringUtils.defaultIfBlank(featureMap.get(KEY_MODULE_REPOSITORY_ADDR), VAL_MODULE_REPOSITORY_MODE_DEFAULT);
    }

    /**
     * 是否启用Unsafe功能
     *
     * @return unsafe.enable
     */
    public boolean isEnableUnsafe() {
        return BooleanUtils.toBoolean(featureMap.get(KEY_UNSAFE_ENABLE));
    }

    /**
     * 获取仿真器安装目录
     *
     * @return 仿真器安装目录
     */
    public String getSimulatorHome() {
        return featureMap.get(KEY_SIMULATOR_HOME);
    }

    /**
     * 获取服务器绑定IP
     *
     * @return 服务器绑定IP
     */
    public String getServerIp() {
        return StringUtils.isNotBlank(featureMap.get(KEY_SERVER_IP))
                ? featureMap.get(KEY_SERVER_IP)
                : "127.0.0.1";
    }

    /**
     * 获取服务器端口
     *
     * @return 服务器端口
     */
    public int getServerPort() {
        return NumberUtils.toInt(featureMap.get(KEY_SERVER_PORT), 0);
    }

    /**
     * 返回应用名称
     *
     * @return 返回应用名称
     */
    public String getAppName() {
        return getProperty(KEY_APP_NAME, getProperty(KEY1_APP_NAME, getProperty(KEY2_APP_NAME)));
    }

    /**
     * 从指定 Map中获取指定的 key
     *
     * @param properties properties
     * @param keys       key
     * @return 返回 property 值
     */
    private String getProperty(Map<String, String> properties, String... keys) {
        if (properties == null || keys == null || keys.length == 0) {
            return null;
        }
        for (String key : keys) {
            String value = properties.get(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取配置的agentId
     *
     * @return 返回agentId
     */
    public String getAgentId() {
        return getProperty(KEY_AGENT_ID);
    }

    /**
     * 获取仿真器内部服务提供库目录
     *
     * @return 仿真器内部服务提供库目录
     */
    public String getProviderLibPath() {
        return featureMap.get(KEY_PROVIDER_LIB_PATH);
    }

    /**
     * 获取 agent 版本号
     *
     * @return
     */
    public String getAgentVersion() {
        return featureMap.get(KEY_AGENT_VERSION);
    }

    /**
     * 获取服务器编码
     *
     * @return 服务器编码
     */
    public Charset getServerCharset() {
        try {
            return Charset.forName(featureMap.get(KEY_SERVER_CHARSET));
        } catch (Exception cause) {
            return Charset.defaultCharset();
        }
    }

    /**
     * 获取服务配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    public String getProperty(String key) {
        return getProperty(key, null);
    }

    /**
     * 获取服务配置
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 返回配置值
     */
    public String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null) {
            value = featureMap.get(key);
        }
        if (value == null) {
            value = System.getenv(key);
        }
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取int配置
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 返回配置值
     */
    public Integer getIntProperty(String key, Integer defaultValue) {
        String value = getProperty(key);
        if (NumberUtils.isDigits(value)) {
            return Integer.valueOf(value);
        }
        return defaultValue;
    }

    /**
     * 获取int配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    public Integer getIntProperty(String key) {
        return getIntProperty(key, null);
    }

    /**
     * 获取long配置
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 返回配置值
     */
    public Long getLongProperty(String key, Long defaultValue) {
        String value = getProperty(key);
        if (NumberUtils.isDigits(value)) {
            return Long.valueOf(value);
        }
        return defaultValue;
    }

    /**
     * 获取int配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    public Long getLongProperty(String key) {
        return getLongProperty(key, null);
    }

    /**
     * 获取long配置
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 返回配置值
     */
    public Boolean getBooleanProperty(String key, Boolean defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            return Boolean.valueOf(value);
        }
        return defaultValue;
    }

    /**
     * 获取禁用的模块列表
     *
     * @return
     */
    public List<String> getDisabledModules() {
        return getListProperty(KEY_DISABLED_MODULES);
    }

    /**
     * 根据 key 获取 List 配置，值的多个配置中间以逗号分隔
     *
     * @param key       键
     * @param separator 分隔符
     * @return List
     */
    public List<String> getListProperty(String key, String separator) {
        return getListProperty(key, separator, Collections.EMPTY_LIST);
    }

    /**
     * 根据 key 获取 List 配置，值的多个配置中间以逗号分隔
     *
     * @param key          键
     * @param separator    分隔符
     * @param defaultValue 默认值，如果不存在此 key 则返回此默认值
     * @return List
     */
    public List<String> getListProperty(String key, String separator, List<String> defaultValue) {
        String property = getProperty(key, null);
        if (property == null) {
            return defaultValue;
        }
        String[] arr = StringUtils.split(property, separator);
        List<String> result = new ArrayList<String>();
        for (String str : arr) {
            if (StringUtils.isBlank(str)) {
                continue;
            }
            result.add(StringUtils.trim(str));
        }
        return result;
    }

    /**
     * 获取某个 List 的配置，多个值中间以逗号分隔
     *
     * @param key 配置 Key
     * @return 返回的配置 List
     */
    public List<String> getListProperty(String key) {
        return getListProperty(key, Collections.EMPTY_LIST);
    }

    /**
     * 获取某个 List 的配置，多个值中间以逗号分隔
     *
     * @param key          配置 Key
     * @param defaultValue 默认值，如果此 key 不存在则返回此默认值
     * @return 返回的配置 List
     */
    public List<String> getListProperty(String key, List<String> defaultValue) {
        return getListProperty(key, ",", defaultValue);
    }

    /**
     * 获取int配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    public Boolean getBooleanProperty(String key) {
        return getBooleanProperty(key, null);
    }

    /**
     * 获取Instrumentation
     *
     * @return Instrumentation
     */
    public Instrumentation getInstrumentation() {
        return instrumentation;
    }

    /**
     * 获取所有的key
     *
     * @return 返回所有的配置key
     */
    public Set<String> keys() {
        return featureMap.keySet();
    }

    /**
     * 获取仿真器 md5值
     *
     * @return
     */
    public String getSimulatorMd5() {
        return md5;
    }
}
