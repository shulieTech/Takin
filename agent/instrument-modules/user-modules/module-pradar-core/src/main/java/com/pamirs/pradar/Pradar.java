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

import com.pamirs.pradar.common.PropertyPlaceholderHelper;
import com.pamirs.pradar.common.RuntimeUtils;
import com.pamirs.pradar.debug.DebugTestInfoPusher;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.pamirs.pradar.AppNameUtils.appName;
import static com.pamirs.pradar.InvokeContext.INVOKE_ID_LENGTH_LIMIT;

/**
 * 帕米尔雷达是一个监控庞大的分布式环境内一个外部请求进来开始递归调用各个center的调用关系的系统。<br> 帕米尔雷达有利于系统问题排查，性能优化，以及容量规划。 <p> </p>
 *
 * @see <a href="http://pradar.pamirs.top:9999">Pradar 系统</a>
 */
public final class Pradar {
    /**
     * web server名称
     */
    static public String WEB_SERVER_NAME;

    /**
     * user.app.key
     */
    static final public String USER_APP_KEY = "user.app.key";

    /**
     * 用来标识线程 ID
     */
    static final public String THREAD_ID_KEY = "threadId";
    static final public String HAS_CONTEXT = "hasContext";
    static final public String IDENTITY_CONTEXT_ID = "identity_context_id";

    /**
     * Pradar日志放置的位置 ~/pradarlogs/，统一使用 SIMULATOR_LOG_PATH 系统变量配置
     */
    static final public String PRADAR_LOG_DIR = locatePradarLogPath();

    /**
     * 强制指定写日志用的编码
     */
    static final public Charset DEFAULT_CHARSET = getDefaultOutputCharset();

    /**
     * trace日志版本号
     */
    static public final int PRADAR_TARCE_LOG_VERSION = getPradarTarceLogVersion();

    /**
     * monitor日志版本号
     */
    static public final int PRADAR_MONITOR_LOG_VERSION = getPradarMonitorLogVersion();

    /**
     * 清理 pradar 日志的间隔周期
     */
    static public final int PRADAR_LOG_DAEMON_INTERVAL = getPradarLogDaemonInterval();

    static public final int DEFAULT_PRADAR_TRACE_LOG_VERSION = 16;
    static public final int DEFAULT_PRADAR_MONITOR_LOG_VERSION = 11;
    /**
     * pradar log daemon 运行周期
     */
    static public final int DEFAULT_PRADAR_LOG_DAEMON_INTERVAL = 20;

    /**
     * pradar user key
     */
    static public final String PRADAR_USER_KEY = getPradarUserKey();

    /**
     * pradar user id
     */
    static public final String PRADAR_USER_ID = getPradarUserId();

    /**
     * 日志推送的大小
     */
    static public int PUSH_MAX_SIZE = getPushMaxSize();


    /**
     * Pradar Invoke 日志文件名
     */
    static final public String PRADAR_INVOKE_LOG_FILE = Pradar.PRADAR_LOG_DIR + "pradar_trace.log";

    /**
     * Pradar 服务器信息 日志文件名
     */
    static final public String PRADAR_MONITOR_LOG_FILE = Pradar.PRADAR_LOG_DIR + "pradar_monitor.log";

    static final long MAX_RPC_LOG_FILE_SIZE = getMaxRpcLogFileSize();
    static final long MAX_MONITOR_LOG_FILE_SIZE = getMaxMonitorLogFileSize();

    /**
     * 业务日志可接受最大长度
     */
    static public final int MAX_BIZ_LOG_SIZE = 4 * 1024;

    /**
     * localData 可接受单个数据的最大长度
     */
    static public final int MAX_LOCAL_DATA_VALUE_SIZE = 48;

    /**
     * localData 可接受单个键的最大长度
     */
    static public final int MAX_LOCAL_DATA_KEY_SIZE = 16;

    /**
     * userData 可接受单个数据的最大长度
     */
    static public final int MAX_USER_DATA_VALUE_SIZE = 48;

    /**
     * userData 可接受单个键的最大长度
     */
    static public final int MAX_USER_DATA_KEY_SIZE = 16;

    /**
     * Invoke 日志输出
     */
    static AsyncAppender rpcAppender;


    /**
     * 服务器指标信息 日志输出
     */
    static AsyncAppender serverMonitorAppender;

    /**
     * 正常 TRACE 开始的 InvokeId
     */
    static public final String ROOT_INVOKE_ID = "0";
    /**
     * 缺少 TraceId，重新开始的 InvokeId
     */
    static public final String MAL_ROOT_INVOKE_ID = "9";
    /**
     * 超长被调整后的 invokeId
     */
    static public final String ADJUST_ROOT_INVOKE_ID = "8";

    public final static String[] SPECIAL_CHARACTORS = new String[]{
            "\r\n", "\n", "\r", "\t", "|"
    };

    /**
     * InvokeContext 对应的日志类型
     */
    static final public int LOG_TYPE_TRACE = 1;
    static final public int LOG_TYPE_INVOKE_CLIENT = 2;
    static final public int LOG_TYPE_INVOKE_SERVER = 3;

    /**
     * 忽略不处理，用于防御某些不配对的调用埋点
     */
    static final int LOG_TYPE_EVENT_ILLEGAL = -255;

    /**
     * 全链路压测前缀
     */
    static public final String CLUSTER_TEST_PREFIX = getClusterTestPrefix();


    /**
     * 全链路压测前缀 非下划线后缀
     */
    static public final String CLUSTER_TEST_PREFIX_ROD = getClusterTestPrefixRod();
    /**
     * 全链路压测后缀
     */
    static public final String CLUSTER_TEST_SUFFIX = getClusterTestSuffix();

    /**
     * 全链路压测后缀
     */
    static public final String CLUSTER_TEST_SUFFIX_ROD = getClusterTestSuffixRod();

    /**
     * trace 日志的队列长度大小
     */
    static public final String TRACE_QUEUE_SIZE = "pradar.trace.queue.size";

    /**
     * monitor 日志的队列长度大小
     */
    static public final String MONITOR_QUEUE_SIZE = "pradar.monitor.queue.size";

    /**
     * 是否影子库里用影子表模式
     */
    static public final String SHADOW_DATABASE_WITH_SHADOW_TABLE = "shadow.database.with.shadow.table";

    /**
     * 默认 trace 缓存队列的大小
     */
    static public final int DEFAULT_TRACE_QUEUE_SIZE = 1024;
    /**
     * 默认 monitor 缓存队列的大小
     */
    static public final int DEFAULT_MONITOR_QUEUE_SIZE = 512;

    /**
     * 获取agent id
     */
    static public final String AGENT_ID = getAgentId();

    /**
     * 控制门开启
     */
    static public final String DOOR_OPENED = "Y";
    /**
     * 控制门关闭
     */
    static public final String DOOR_CLOSED = "N";

    /**
     * 全链路压测上下文标值
     */
    static public final String PRADAR_CLUSTER_TEST_ON = "1";
    static public final String PRADAR_CLUSTER_TEST_OFF = "0";

    static public final String PRADAR_DEBUG_ON = "1";
    static public final String PRADAR_DEBUG_OFF = "0";

    /**
     * http 请求时统一的压测后缀
     */
    public static final String PRADAR_CLUSTER_TEST_HTTP_USER_AGENT_SUFFIX = "PerfomanceTest";

    static public final char ENTRY_SEPARATOR = (char) 0x12;
    static public final char KV_SEPARATOR = (char) 0x1;   // METAQ 不允许使用的分隔符，不能在 UserData 中使用
    static public final char KV_SEPARATOR2 = (char) 0x14;

    static final ConcurrentHashMap<String, String> indexes = new ConcurrentHashMap<String, String>();

    public static final ThreadLocal<Boolean> isDebug = new ThreadLocal<Boolean>();

    /**
     * 用于记录当前时段内是否有压测流量请求
     */
    private final static AtomicBoolean hasPressureRequest = new AtomicBoolean(false);

    private final static Logger LOGGER = LoggerFactory.getLogger(Pradar.class);

    private final static List<String> RPC_TRANSFORM_KEYS = Arrays.asList(
            PradarService.PRADAR_TRACE_ID_KEY,
            PradarService.PRADAR_TRACE_APPNAME_KEY,
            PradarService.PRADAR_INVOKE_ID_KEY,
            PradarService.PRADAR_USER_DATA_KEY,
            PradarService.PRADAR_REMOTE_APPNAME_KEY,
            PradarService.PRADAR_UPSTREAM_APPNAME_KEY,
            PradarService.PRADAR_CLUSTER_TEST_KEY,
            PradarService.PRADAR_REMOTE_IP,
            PradarService.PRADAR_DEBUG_KEY,
            PradarService.PRADAR_TRACE_NODE_KEY
    );

    /**
     * 给指定值添加大写的压测前缀
     *
     * @param value 指定值
     * @return 添加大写的压测前缀后的值
     */
    public static String addClusterTestPrefixUpper(String value) {
        return CLUSTER_TEST_PREFIX.toUpperCase() + value;
    }

    /**
     * 给指定值添加小写的压测前缀
     *
     * @param value 指定值
     * @return 添加小写的压测前缀后的值
     */
    public static String addClusterTestPrefixLower(String value) {
        return CLUSTER_TEST_PREFIX.toLowerCase() + value;
    }


    /**
     * 给指定值添加小写的压测前缀
     *
     * @param value 指定值
     * @return 添加小写的压测前缀后的值
     */
    public static String addClusterTestPrefixRodLower(String value) {
        return CLUSTER_TEST_PREFIX_ROD.toLowerCase() + value;
    }

    /**
     * 给指定值添加压测前缀
     *
     * @param value 指定值
     * @return 添加压测前缀后的值
     */
    public static String addClusterTestPrefix(String value) {
        return CLUSTER_TEST_PREFIX + value;
    }

    /**
     * 给指定值添加大写的压测后缀
     *
     * @param value 指定值
     * @return 添加大写的压测前缀后的值
     */
    public static String addClusterTestSuffixUpper(String value) {
        return value + CLUSTER_TEST_SUFFIX.toUpperCase();
    }

    /**
     * 给指定值添加小写的压测后缀
     *
     * @param value 指定值
     * @return 添加小写的压测前缀后的值
     */
    public static String addClusterTestSuffixLower(String value) {
        return value + CLUSTER_TEST_SUFFIX.toLowerCase();
    }

    /**
     * 获取 trace 的列队的大小
     *
     * @return 默认返回 {@link #DEFAULT_TRACE_QUEUE_SIZE}
     */
    public static int getTraceQueueSize() {
        String value = System.getProperty(TRACE_QUEUE_SIZE);
        if (NumberUtils.isDigits(value)) {
            return Integer.valueOf(value);
        }
        return DEFAULT_TRACE_QUEUE_SIZE;
    }

    /**
     * 获取 monitor 的列队的大小
     *
     * @return 默认返回 {@link #DEFAULT_MONITOR_QUEUE_SIZE}
     */
    public static int getMonitorQueueSize() {
        String value = System.getProperty(MONITOR_QUEUE_SIZE);
        if (NumberUtils.isDigits(value)) {
            return Integer.valueOf(value);
        }
        return DEFAULT_MONITOR_QUEUE_SIZE;
    }

    /**
     * 是否影子库里用影子表模式
     *
     * @return 默认返回 true
     */
    public static boolean isShadowDatabaseWithShadowTable() {
        String value = System.getProperty(SHADOW_DATABASE_WITH_SHADOW_TABLE);
        if (StringUtils.isNotBlank(value)) {
            return Boolean.valueOf(value);
        }
        return true;
    }

    /**
     * 给指定值添加小写的压测后缀
     *
     * @param value 指定值
     * @return 添加小写的压测前缀后的值
     */
    public static String addClusterTestSuffixRodLower(String value) {
        return value + CLUSTER_TEST_SUFFIX_ROD.toLowerCase();
    }

    /**
     * 给指定值添加压测后缀
     *
     * @param value 指定值
     * @return 添加压测前缀后的值
     */
    public static String addClusterTestSuffix(String value) {
        return value + CLUSTER_TEST_SUFFIX;
    }

    /**
     * 判断当前值是否包含压测前缀
     *
     * @param value 当前值
     * @return 是否包含压测前缀
     */
    public static boolean isContainsClusterTest(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        String clusterTestPrefix = CLUSTER_TEST_PREFIX;
        return StringUtils.lowerCase(value).contains(StringUtils.lowerCase(clusterTestPrefix));
    }

    /**
     * 判断当前值是否包含压测前缀，匹配规则为 prefix+压测前缀, prefix 是全局匹配，压测前缀则忽略大小写匹配
     *
     * @param value
     * @param prefix
     * @return
     */
    public static boolean isContainsClusterTest(String value, String prefix) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        if (StringUtils.isBlank(prefix)) {
            prefix = "";
        }
        if (StringUtils.isBlank(prefix)) {
            return isContainsClusterTest(value);
        }

        while (true) {
            int idx = StringUtils.indexOf(value, prefix);
            if (idx == -1) {
                return false;
            }
            value = StringUtils.substring(value, idx + prefix.length());
            if (StringUtils.isBlank(value)) {
                return false;
            }
            boolean isClusterTestPrefix = isClusterTestPrefix(value);
            if (isClusterTestPrefix) {
                return true;
            }
        }
    }

    /**
     * 将当前值移除压测前缀
     *
     * @param value 当前值
     * @return 返回移除压测前缀后的值
     */
    public static String removeClusterTestPrefix(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        String clusterTestPrefix = CLUSTER_TEST_PREFIX;
        if (isClusterTestPrefix(value)) {
            value = value.substring(clusterTestPrefix.length());
        }
        return value;
    }

    /**
     * 判断当前值是否是压测前缀开头,匹配规则为 prefix 全匹配，压测前缀则忽略大小写匹配
     *
     * @param value  当前值
     * @param prefix 需要追加在压测前缀前的前缀
     * @return 是否是压测前缀
     */
    public static boolean isClusterTestPrefix(String value, String prefix) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        if (StringUtils.isBlank(prefix)) {
            prefix = "";
        }
        String clusterTestPrefix = CLUSTER_TEST_PREFIX;
        if (clusterTestPrefix.length() + prefix.length() > value.length()) {
            return false;
        }

        for (int i = 0, len = prefix.length(); i < len; i++) {
            if (value.charAt(i) != prefix.charAt(i)) {
                return false;
            }
        }

        value = value.substring(prefix.length());
        for (int i = 0, len = clusterTestPrefix.length(); i < len; i++) {
            char p = clusterTestPrefix.charAt(i);
            char c = value.charAt(i);
            if (Character.toLowerCase(p) != Character.toLowerCase(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断当前值是否是以 prefix 加压测前缀开头
     *
     * @param value  判断的值
     * @param prefix 前缀,判断是以前缀加压测前缀为判断开头前缀
     * @return
     */
    public static boolean isClusterTestGroupPrefix(String value, String prefix) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        String gprefix = prefix + CLUSTER_TEST_PREFIX;
        if (gprefix.length() > value.length()) {
            return false;
        }
        for (int i = 0, len = gprefix.length(); i < len; i++) {
            char p = gprefix.charAt(i);
            char c = value.charAt(i);
            if (Character.toLowerCase(c) != Character.toLowerCase(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断当前值是否是压测前缀开头
     *
     * @param value 当前值
     * @return 是否是压测前缀
     */
    public static boolean isClusterTestPrefix(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        String prefix = CLUSTER_TEST_PREFIX;
        if (prefix.length() > value.length()) {
            return false;
        }
        for (int i = 0, len = prefix.length(); i < len; i++) {
            char p = prefix.charAt(i);
            char c = value.charAt(i);
            if (Character.toLowerCase(c) != Character.toLowerCase(p)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 判断当前值是否是压测前缀开头
     *
     * @param value 当前值
     * @return 是否是压测前缀
     */
    public static boolean isClusterTestPrefixRod(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        String prefix = CLUSTER_TEST_PREFIX_ROD;
        if (prefix.length() > value.length()) {
            return false;
        }
        for (int i = 0, len = prefix.length(); i < len; i++) {
            char p = prefix.charAt(i);
            char c = value.charAt(i);
            if (Character.toLowerCase(c) != Character.toLowerCase(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断当前值是否是压测前缀结尾
     *
     * @param value 当前值
     * @return 是否是压测前缀
     */
    public static boolean isClusterTestSuffix(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        String clusterTestSuffix = CLUSTER_TEST_SUFFIX;
        if (clusterTestSuffix.length() > value.length()) {
            return false;
        }
        for (int i = 0, len = clusterTestSuffix.length(); i < len; i++) {
            char p = clusterTestSuffix.charAt(clusterTestSuffix.length() - i - 1);
            char c = value.charAt(value.length() - i - 1);
            if (Character.toLowerCase(c) != Character.toLowerCase(p)) {
                return false;
            }
        }
        return true;
    }

    private static long getMaxRpcLogFileSize() {
        String maxRpcLogFileSize = System.getProperty("pradar.trace.max.file.size");
        long maxSize = 200 * 1024 * 1024;
        if (NumberUtils.isDigits(maxRpcLogFileSize)) {
            maxSize = Long.valueOf(maxRpcLogFileSize);
        }
        return maxSize;
    }

    private static long getMaxMonitorLogFileSize() {
        String maxRpcLogFileSize = System.getProperty("pradar.monitor.max.file.size");
        long maxSize = 200 * 1024 * 1024;
        if (NumberUtils.isDigits(maxRpcLogFileSize)) {
            maxSize = Long.valueOf(maxRpcLogFileSize);
        }
        return maxSize;
    }

    /**
     * 获取monitor appender
     *
     * @return
     */
    public static AsyncAppender getServerMonitorAppender() {
        return serverMonitorAppender;
    }

    /**
     * 获取agentId
     *
     * @return
     */
    public static String getAgentId() {
        String agentId = System.getProperty("simulator.agentId");
        if (StringUtils.isBlank(agentId)) {
            return new StringBuilder(PradarCoreUtils.getLocalAddress()).append("-").append(RuntimeUtils.getPid()).toString();
        } else {
            Properties properties = new Properties();
            properties.setProperty("pid", String.valueOf(RuntimeUtils.getPid()));
            properties.setProperty("hostname", PradarCoreUtils.getHostName());
            properties.setProperty("ip", PradarCoreUtils.getLocalAddress());
            PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");
            return propertyPlaceholderHelper.replacePlaceholders(agentId, properties);
        }
    }

    /**
     * 获取压测前缀
     *
     * @return
     */
    public static String getClusterTestPrefix() {
        String prefix = System.getProperty("pradar.cluster.test.prefix");
        if (StringUtils.isBlank(prefix)) {
            prefix = "PT_";
        }
        System.out.println("SIMULATOR: cluster test prefix is:" + prefix);
        return prefix;
    }

    /**
     * 获取压测后缀非下划线方式，类似oss不得使用下划线
     *
     * @return
     */
    public static String getClusterTestSuffixRod() {
        String prefix = System.getProperty("pradar.cluster.test.suffix.rod");
        if (StringUtils.isBlank(prefix)) {
            prefix = "-PT";
        }
        System.out.println("SIMULATOR: cluster test suffix rod is:" + prefix);
        return prefix;
    }

    /**
     * 获取压测后缀非下划线方式，类似oss不得使用下划线
     *
     * @return
     */
    public static String getClusterTestPrefixRod() {
        String prefix = System.getProperty("pradar.cluster.test.prefix.rod");
        if (StringUtils.isBlank(prefix)) {
            prefix = "PT-";
        }
        System.out.println("SIMULATOR: cluster test prefix rod is:" + prefix);
        return prefix;
    }

    /**
     * 获取压测后缀
     *
     * @return
     */
    public static String getClusterTestSuffix() {
        String suffix = System.getProperty("pradar.cluster.test.suffix");
        if (StringUtils.isBlank(suffix)) {
            suffix = "_PT";
        }
        System.out.println("SIMULATOR: cluster test suffix is:" + suffix);
        return suffix;
    }

    /**
     * 返回 Pradar class 的加载位置
     */
    static final String getPradarLocation() {
        try {
            URL resource = Pradar.class.getProtectionDomain().getCodeSource().getLocation();
            if (resource != null) {
                return resource.toString();
            }
        } catch (Throwable t) {
            // ignore
        }
        return "unknown location";
    }

    /**
     * 返回pradar trace日志格式的协议版本号
     *
     * @return
     */
    static final int getPradarTarceLogVersion() {
        String version = getSystemProperty("pradar.trace.log.version");
        if (PradarCoreUtils.isNotBlank(version)) {
            try {
                return Integer.valueOf(PradarCoreUtils.trim(version));
            } catch (NumberFormatException e) {
                return DEFAULT_PRADAR_TRACE_LOG_VERSION;
            }
        }
        return DEFAULT_PRADAR_TRACE_LOG_VERSION;
    }

    /**
     * 返回pradar monitor日志格式的协议版本号
     *
     * @return
     */
    static final int getPradarMonitorLogVersion() {
        String version = getSystemProperty("pradar.monitor.log.version");
        if (PradarCoreUtils.isNotBlank(version)) {
            try {
                return Integer.valueOf(PradarCoreUtils.trim(version));
            } catch (NumberFormatException e) {
                return DEFAULT_PRADAR_MONITOR_LOG_VERSION;
            }
        }
        return DEFAULT_PRADAR_MONITOR_LOG_VERSION;
    }

    /**
     * @return
     */
    static public int getPradarLogDaemonInterval() {
        String version = getSystemProperty("pradar.log.daemon.interval");
        if (PradarCoreUtils.isNotBlank(version)) {
            try {
                return Integer.valueOf(PradarCoreUtils.trim(version));
            } catch (NumberFormatException e) {
                return DEFAULT_PRADAR_LOG_DAEMON_INTERVAL;
            }
        }
        return DEFAULT_PRADAR_LOG_DAEMON_INTERVAL;
    }

    /**
     * 返回pradar user key
     *
     * @return
     */
    static final String getPradarUserKey() {
        return getSystemProperty(Pradar.USER_APP_KEY);
    }

    /**
     * 返回pradar user id
     *
     * @return
     */
    static final String getPradarUserId() {
        return getSystemProperty("pradar.user.id");
    }

    /**
     * 获取推送日志的大小
     *
     * @return
     */
    static final int getPushMaxSize() {
        String pushMaxSize = getSystemProperty("pradar.log.push.maxsize");
        if (NumberUtils.isDigits(pushMaxSize)) {
            return Integer.valueOf(pushMaxSize);
        }
        /**
         * 默认为1M
         */
        return 1024 * 1024 * 1;
    }

    /**
     * 返回 Pradar 输出日志的字符编码，默认从 ${PRADAR.CHARSET} 加载， 如果未设置，按照 GB18030、GBK、UTF-8 的顺序依次尝试。
     */
    static final Charset getDefaultOutputCharset() {
        Charset cs;
        String charsetName = getSystemProperty("pradar.charset");
        if (PradarCoreUtils.isNotBlank(charsetName)) {
            charsetName = charsetName.trim();
            try {
                cs = Charset.forName(charsetName);
                if (cs != null) {
                    return cs;
                }
            } catch (Throwable e) {
                // quietly
            }
        }
        try {
            cs = Charset.forName("GB18030");
        } catch (Throwable e) {
            try {
                cs = Charset.forName("GBK");
            } catch (Throwable e2) {
                cs = Charset.forName("UTF-8");
            }
        }
        return cs;
    }

    /**
     * 根据系统参数，获取用户目录，获取失败时返回 /tmp/
     *
     * @return 返回路径，结尾包含“/”
     */
    static private final String locateUserHome() {
        String userHome = getSystemProperty("user.home");
        if (PradarCoreUtils.isNotBlank(userHome)) {
            if (!userHome.endsWith(File.separator)) {
                userHome += File.separator;
            }
        } else {
            userHome = "/tmp/";
        }
        return userHome;
    }

    /**
     * 根据系统参数，设置 Pradar 的日志fa目录。判断优先级： <ol> <li>如果设置了 ${PRADAR.LOG.PATH}，在 ${PRADAR.LOG.PATH}/ 下面输出日志。
     * <li>在 ${BASE_LOG_DIR}/pradar/ 下面输出日志。 </ol>
     *
     * @return 返回路径，结尾包含“/”
     */
    static private final String locatePradarLogPath() {
        String value = System.getProperty("SIMULATOR_LOG_PATH");
        if (StringUtils.isBlank(value)) {
            return "~/pradarlogs/";
        }
        if (!value.endsWith("/")) {
            return value + '/';
        }
        return value;
    }

    /**
     * 获取当前进程号
     *
     * @return
     */
    static private String getPid() {
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String pid = StringUtils.split(name, '@')[0];
            return pid;
        } catch (Throwable e) {
            int randomValue = (int) ((Math.random() + 1) * 1000);
            return randomValue + "";
        }
    }

    /**
     * 获取系统变量
     *
     * @param key
     * @return
     */
    static private final String getSystemProperty(String key) {
        try {
            return System.getProperty(key);
        } catch (Throwable e) {
            return null;
        }
    }

    static private final PradarRollingFileAppender createPradarLoggers() {
        // 配置日志输出
        rpcAppender = new AsyncAppender(getTraceQueueSize(), 0);

        PradarRollingFileAppender rpcLogger = new PradarRollingFileAppender(
                PRADAR_INVOKE_LOG_FILE, Pradar.MAX_RPC_LOG_FILE_SIZE, false);
        rpcAppender.start(rpcLogger, new TraceInvokeContextEncoder(), "RpcLog");
        PradarLogDaemon.watch(rpcAppender);
        return rpcLogger;

    }

    static private final PradarRollingFileAppender createMonitorLoggers() {

        // 配置日志输出
        serverMonitorAppender = new AsyncAppender(getMonitorQueueSize(), 0);

        PradarRollingFileAppender rpcLogger = new PradarRollingFileAppender(
                PRADAR_MONITOR_LOG_FILE, Pradar.MAX_MONITOR_LOG_FILE_SIZE, false);
        serverMonitorAppender.start(rpcLogger, new TraceInvokeContextEncoder(), "MonitorLog");
        PradarLogDaemon.watch(serverMonitorAppender);
        return rpcLogger;
    }

    private static PradarRollingFileAppender pradarAppender;
    private static PradarRollingFileAppender monitorAppender;

    /**
     * Pradar 初始化
     */
    static {
        LOGGER.info("Pradar started ({})", getPradarLocation());

        try {
            pradarAppender = createPradarLoggers();
            monitorAppender = createMonitorLoggers();
        } catch (Throwable e) {
            LOGGER.error("fail to create Pradar logger", e);
        }
        try {
            PradarLogDaemon.start();
        } catch (Throwable e) {
            LOGGER.error("fail to start PradarLogDaemon", e);
        }
    }

    public static void shutdown() {
        PradarLogDaemon.shutdown();
        if (pradarAppender != null) {
            pradarAppender.shutdown();
        }
        if (monitorAppender != null) {
            monitorAppender.shutdown();
        }
    }

    private Pradar() {
    }

    /**
     * 开启新的trace。该接口仅提供给最源头的前中间件或自己启动的定时程序调用，使用该接口时， 必须最后调用endTrace结束。
     *
     * @param traceId     全局唯一的id，如果传入的值为空或者null，系统会自动生成
     * @param serviceName 用户自定义的入口标识值，不能为 <code>null</code>， 建议传入能够唯一标识入口的数据，例如用户访问网络的 http url
     */
    static public void startTrace(String traceId, String serviceName, String methodName) {
        startTrace(traceId, null, serviceName, methodName);
    }

    /**
     * 开启新的trace，该接口仅提供给最源头的前中间件或自己启动的定时程序调用， 支持配置 invokeId 来开启一个嵌套的调用链。使用该接口时，必须最后调用endTrace结束。
     *
     * @param traceId     全局唯一的id，如果传入的值为空或者null，系统会自动生成
     * @param invokeId    额外指定 invokeId，如果为 <code>null</code>，使用 {@link #ROOT_INVOKE_ID}
     * @param serviceName 用户自定义的入口标识值，不能为 <code>null</code>， 建议传入能够唯一标识入口的数据，例如用户访问网络的 http url
     */
    static public void startTrace(String traceId, String invokeId, String serviceName, String methodName) {
        if (serviceName == null) {
            return;
        }
        InvokeContext ctx = InvokeContext.get();
        boolean isClusterTest = false;
        boolean isDebug = false;
        //这个地方可能是由于支持压测标和调试标的前置设置而导致设置了一个空的 InvokeContext
        //所以需要将流量标与调试标取出来重新设置到新的 trace 中
        if (ctx != null && ctx.traceId != null) {
            if (ctx.isEmpty()) {
                isClusterTest = ctx.isClusterTest();
                isDebug = ctx.isDebug();
            }
            // 重复 startTrace 的检测
            if (!ctx.traceId.equals(traceId) || !serviceName.equals(ctx.serviceName)) {
                // 说明有潜在的埋点问题，先把前面那个调用链结束掉
                LOGGER.warn("duplicated startTrace detected, overrided {} ({}) to {} ({})", ctx.traceId, ctx.serviceName, traceId, serviceName);
                endTrace();
            } else {
                // traceId 和 traceName 都相同，说明是 PradarFilter 和 tbsession 有重复埋点
                return;
            }
        }

        if (traceId == null || traceId.isEmpty()) {
            traceId = TraceIdGenerator.generate();
            invokeId = Pradar.ROOT_INVOKE_ID;
        } else if (invokeId == null || invokeId.length() > INVOKE_ID_LENGTH_LIMIT) {
            invokeId = Pradar.ROOT_INVOKE_ID;
        }
        try {
            /* ctx = new InvokeContext(traceId, appName(), invokeId);*/
            ctx = new InvokeContext(traceId, appName(), invokeId, methodName, serviceName);
            InvokeContext.set(ctx);
            ctx.startTrace(serviceName, methodName);
            ctx.setUpAppName(appName());
            if (isClusterTest) {
                Pradar.setClusterTest(true);
            }
            if (isDebug) {
                Pradar.setDebug(true);
            }

        } catch (Throwable re) {
            LOGGER.error("startTrace", re);
        } finally {
            if (Pradar.isDebug()) {
                DebugTestInfoPusher.doRecord("beforeFirst");
                DebugTestInfoPusher.doRecord("beforeLast");
            }
        }
    }

    /**
     * 标记压测流量
     * 加一个校验，防止因为没有上下文设置压测标导致不可预测的问题
     * 这个加一个判断，只有当设置当前为压测流量并且没有上下文时报错
     *
     * @param isClusterTest
     */
    static public void setClusterTest(boolean isClusterTest) {
        InvokeContext ctx = InvokeContext.get();
        if (ctx != null) {
            ctx.setClusterTest(isClusterTest);
        } else {
            if (isClusterTest) {
                InvokeContext emptyInvokeContext = InvokeContext.buildEmptyInvokeContext();
                emptyInvokeContext.setClusterTest(true);
                Pradar.setInvokeContext(emptyInvokeContext);
            }
        }
    }

    /**
     * 判断当前流量是否为压测流量
     *
     * @return
     */
    static public boolean isClusterTest() {
        InvokeContext ctx = InvokeContext.get();
        if (ctx != null) {
            return ctx.isClusterTest();
        }
        return false;
    }

    /**
     * 设置 debug 流量
     *
     * @param b
     */
    public static void setDebug(boolean b) {
        isDebug.set(b);
        InvokeContext ctx = InvokeContext.get();
        if (ctx != null) {
            ctx.setDebug(b);
        } else {
            if (b) {
                InvokeContext emptyInvokeContext = InvokeContext.buildEmptyInvokeContext();
                emptyInvokeContext.setDebug(b);
                Pradar.setInvokeContext(emptyInvokeContext);
            }
        }
    }

    /**
     * 判断当前流量是否为调试压测流量
     *
     * @return
     */
    static public boolean isDebug() {
        InvokeContext ctx = InvokeContext.get();
        if (ctx != null) {
            return ctx.isDebug() || (null != isDebug.get() && isDebug.get());
        }
        return null != isDebug.get() && isDebug.get();
    }

    /**
     * 结束一次跟踪，Threadlocal 变量会被清空，调用了startTrace及startTrace4Top的必须在finally或者最后调用该接口。
     */
    static public void endTrace() {
        endTrace(null, MiddlewareType.TYPE_WEB_SERVER);
    }

    /**
     * 结束一次跟踪，Threadlocal 变量会被清空，调用了 startTrace 及 startTrace4Top 的必须在finally或者最后调用该接口。
     */
    static public void endTrace(String resultCode, int type) {
        try {
            if (Pradar.isDebug()) {
                if (Pradar.isSuccessResult()) {
                    DebugTestInfoPusher.doRecord("afterFirst");
                } else {
                    DebugTestInfoPusher.doRecord("exceptionFirst");
                }
            }
            // find root context
            InvokeContext root = InvokeContext.get();
            if (null == root) {
                return;
            }
            // 取得根 trace
            while (null != root.parentInvokeContext) {
                root = root.parentInvokeContext;
            }
            root.endTrace(resultCode, type);
            commitInvokeContext(root);
            if (Pradar.isDebug()) {
                if (Pradar.isSuccessResult()) {
                    DebugTestInfoPusher.doRecord("afterLast");
                } else {
                    DebugTestInfoPusher.doRecord("exceptionLast");
                }
            }
        } catch (Throwable re) {
            LOGGER.error("endTrace", re);
        } finally {
            clearInvokeContext();
        }
    }

    static final InvokeContext createContextIfNotExists(final boolean setToThreadLocal) {
        final InvokeContext ctx = InvokeContext.get();
        if (null == ctx) {
            final InvokeContext newCtx = new InvokeContext(TraceIdGenerator.generate(), appName(), MAL_ROOT_INVOKE_ID
                    , ctx.traceMethod, ctx.traceServiceName);

            if (setToThreadLocal) {
                // 在这里设置的 ctx，有可能无法释放，例如在没有 startTrace 的上下文中直接
                // putUserData()，之后没办法释放 InvokeContext
                InvokeContext.set(newCtx);
            }
            return newCtx;
        }
        return ctx;
    }

    /**
     * 返回当前线程关联的调用上下文。仅供中间件调用。可能返回 NULL。 目前是以 Map 形式用来序列化 InvokeContext，以便网络传输时序列化可以兼容新老版本。
     *
     * @see #setInvokeContext(Object) 重新还原 InvokeContext
     */
    static public Object currentInvokeContext() {
        return getInvokeContextMap();
    }

    /**
     * 直接取得当前的 InvokeContext，用于备份调用上下文（不做 Map 转换）
     * 不再直接提供对外的获取上下文实例的方法 - 2021-04-22
     * 获取上下文请参考{@link #getInvokeContextMap()}
     * 需要使用{@link #hasInvokeContext(Map)} 来判断获取的值中是否包含上下文
     * 因为即使没有上下文时也会产生额外的数据如压测标和debug 标识
     *
     * 如果远程传输请使用 {@link #getInvokeContextTransformMap()}
     *
     * @see # setInvokeContext(InvokeContext) 还原 InvokeContext
     */
    static public InvokeContext getInvokeContext() {
        return InvokeContext.get();
    }

    /**
     * 从 Map 中构建一个 InvokeContext
     *
     * @param ctx
     * @return
     */
    static public InvokeContext fromMap(Map<String, String> ctx) {
        return InvokeContext.fromMap(ctx);
    }

    /**
     * 判断是否有上下文
     *
     * @return
     */
    static public boolean hasInvokeContext() {
        return getInvokeContext() != null;
    }

    /**
     * 判断指定的 Map 是否包含上下文信息
     *
     * @param ctx
     * @return
     */
    static public boolean hasInvokeContext(Map<String, String> ctx) {
        if (ctx == null) {
            return false;
        }

        String value = ctx.get(HAS_CONTEXT);
        return Boolean.TRUE.toString().equals(value);
    }

    /**
     * 获取当前的调用方法
     *
     * @return
     */
    static public String getMethod() {
        InvokeContext context = getInvokeContext();
        if (context == null) {
            return null;
        }
        return context.getMethodName();
    }

    /**
     * 获取当前的调用服务名
     *
     * @return
     */
    static public String getService() {
        InvokeContext context = getInvokeContext();
        if (context == null) {
            return null;
        }
        return context.getServiceName();
    }

    /**
     * 此方法只用于获取当前调用上下文的数据，不允许对其进行修改
     *
     * 如果远程传输请使用 {@link #getInvokeContextTransformMap()}
     *
     * @return 如果当前上下文中无值则返回 Collections.EMPTY_MAP
     */
    static public Map<String, String> getInvokeContextMap() {
        InvokeContext invokeContext = InvokeContext.get();
        if (invokeContext == null) {
            Map<String, String> ctx = new HashMap<String, String>();
            if (isClusterTest()) {
                ctx.put(PradarService.PRADAR_CLUSTER_TEST_KEY, String.valueOf(isClusterTest()));
            }
            if (isDebug()) {
                ctx.put(PradarService.PRADAR_DEBUG_KEY, String.valueOf(isDebug()));
            }
            if (!ctx.isEmpty()) {
                if (StringUtils.isNotBlank(AppNameUtils.appName())) {
                    ctx.put(PradarService.PRADAR_UPSTREAM_APPNAME_KEY, AppNameUtils.appName());
                    ctx.put(PradarService.PRADAR_REMOTE_APPNAME_KEY, AppNameUtils.appName());
                }
                ctx.put(PradarService.PRADAR_REMOTE_IP, PradarCoreUtils.getLocalAddress());
            }
            //标记一下序列化的上下文所属的线程 ID, 因为这个可能会用到后续的反序列上下文上
            ctx.put(THREAD_ID_KEY, String.valueOf(Thread.currentThread().getId()));
            ctx.put(HAS_CONTEXT, Boolean.FALSE.toString());
            return ctx;
        }
        Map<String, String> ctx = invokeContext.toMap();
        if (ctx != null) {
            if (StringUtils.isNotBlank(AppNameUtils.appName())) {
                ctx.put(PradarService.PRADAR_UPSTREAM_APPNAME_KEY, AppNameUtils.appName());
                ctx.put(PradarService.PRADAR_REMOTE_APPNAME_KEY, AppNameUtils.appName());
            }
            ctx.put(PradarService.PRADAR_REMOTE_IP, PradarCoreUtils.getLocalAddress());
            //标记一下序列化的上下文所属的线程 ID, 因为这个可能会用到后续的反序列上下文上
            ctx.put(THREAD_ID_KEY, String.valueOf(Thread.currentThread().getId()));
            ctx.put(HAS_CONTEXT, Boolean.TRUE.toString());
            ctx.put(IDENTITY_CONTEXT_ID, String.valueOf(invokeContext.getId()));
        }
        return ctx;
    }

    /**
     * 获取专门用于数据远程传输的上下文数据
     *
     * 该方法返回的传输字段在{@link #getInvokeContextTransformKeys()} 中定义，如果不在
     * 这个范围内定义的字段，则会被强行移除，因为{@link #getInvokeContextMap()} 方法会返回一些用于
     * 内部上下文传输时需要的冗余字段，而这些字段不在远程传输字段列表内
     *
     * @return
     */
    static public Map<String, String> getInvokeContextTransformMap() {
        Map<String, String> ctx = getInvokeContextMap();
        Iterator<Map.Entry<String, String>> it = ctx.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (!getInvokeContextTransformKeys().contains(entry.getKey())) {
                it.remove();
            }
        }
        return ctx;
    }

    /**
     * 获取数据传输上下文的key
     *
     * @return
     */
    static public List<String> getInvokeContextTransformKeys() {
        return RPC_TRANSFORM_KEYS;
    }

    /**
     * 设置rpc上下文，如果当前上下文中有父上下文则保持父上下文
     *
     * @param rpcCtx
     */
    static public void setInvokeContextWithParent(Object rpcCtx) {
        try {
            InvokeContext ctx = null;
            if (rpcCtx instanceof Map) {
                final Map<String, String> context = (Map<String, String>) rpcCtx;
                boolean isSampleThreadId = false;
                String threadId = context.get(THREAD_ID_KEY);
                /**
                 * 需要如果设置到上下文中的线程 ID与当前的线程 ID一致，并且上下文不为空，则忽略此次重构上下文
                 */
                if (threadId != null && threadId.equals(String.valueOf(Thread.currentThread().getId())) && getInvokeContext() != null) {
                    LOGGER.warn("setInvokeContext is sample thread. will append child context auto.");
                    String identityContextId = context.get(IDENTITY_CONTEXT_ID);
                    //如果需要设置的目标 context 与当前的 context 是同一个，则直接忽略此次的设置上下文
                    if (identityContextId != null && InvokeContext.get() != null && identityContextId.equals(String.valueOf(InvokeContext.get().getId()))) {
                        return;
                    }
                    isSampleThreadId = true;
                }
                ctx = InvokeContext.fromMap(context);
                //如果是相同线程，则将当前设置的上下文的父上下文设置成
                if (isSampleThreadId) {
                    if (InvokeContext.get() != null && ctx != null) {
                        ctx.parentInvokeContext = InvokeContext.get();
                    }
                }
            } else if (rpcCtx instanceof InvokeContext) {
                ctx = (InvokeContext) rpcCtx;
            }

            InvokeContext.set(ctx);
        } catch (Throwable re) {
            LOGGER.error("setInvokeContextWithParent", re);
        }
    }

    /**
     * 切换当前线程关联的RPC调用上下文。上下文对象可以是 {@link #getInvokeContext()} 的返回值， 也可以是 {@link #currentInvokeContext()}
     * 的返回值。
     *
     * @param invokeCtx 调用上下文，可以为null，表示清空当前Threadlocal变量， 该接口不允许业务方调用，只允许 rpc 层调用。
     * @see #getInvokeContext() 直接获取 InvokeContext 对象，不做 Map 转换
     * @see #currentInvokeContext() 获取用于序列化、网络传输的 InvokeContext 对象
     */
    @SuppressWarnings("unchecked")
    static public void setInvokeContext(Object invokeCtx) {
        try {
            InvokeContext ctx = null;
            Boolean isClusterTest = null;
            Boolean isDebug = null;
            boolean isSampleThreadId = false;
            if (invokeCtx instanceof Map) {
                Map<String, String> invokeContextMap = (Map<String, String>) invokeCtx;
                String threadId = invokeContextMap.get(THREAD_ID_KEY);
                /**
                 * 需要如果设置到上下文中的线程 ID与当前的线程 ID一致，并且上下文不为空，则忽略此次重构上下文
                 */
                if (threadId != null && threadId.equals(String.valueOf(Thread.currentThread().getId())) && getInvokeContext() != null) {
                    LOGGER.warn("setInvokeContext is sample thread. will append child context auto.");
                    String identityContextId = invokeContextMap.get(IDENTITY_CONTEXT_ID);
                    //如果需要设置的目标 context 与当前的 context 是同一个，则直接忽略此次的设置上下文
                    if (identityContextId != null && InvokeContext.get() != null && identityContextId.equals(String.valueOf(InvokeContext.get().getId()))) {
                        return;
                    }
                    isSampleThreadId = true;
                }
                ctx = InvokeContext.fromMap(invokeContextMap);
                //如果是相同线程，则将当前设置的上下文的父上下文设置成
                if (isSampleThreadId) {
                    if (InvokeContext.get() != null && ctx != null) {
                        ctx.parentInvokeContext = InvokeContext.get();
                    }
                }
                isClusterTest = ClusterTestUtils.isClusterTestRequest(((Map<String, String>) invokeCtx).get(PradarService.PRADAR_CLUSTER_TEST_KEY));
                isDebug = ClusterTestUtils.isDebugRequest(((Map<String, String>) invokeCtx).get(PradarService.PRADAR_DEBUG_KEY));
            } else if (invokeCtx instanceof InvokeContext) {
                ctx = (InvokeContext) invokeCtx;
                isClusterTest = ((InvokeContext) invokeCtx).isClusterTest;
                isDebug = ((InvokeContext) invokeCtx).isDebug;
            }
            if (ctx != null) {
                InvokeContext.set(ctx);
            }
            if (isClusterTest != null) {
                Pradar.setClusterTest(isClusterTest);
            }
            if (isDebug != null) {
                Pradar.setDebug(isDebug);
            }
        } catch (Throwable re) {
            LOGGER.error("setInvokeContext", re);
        }
    }

    /**
     * 如果InvokeContext为空则返回为空
     *
     * @param rpcCtx
     * @param parent
     * @return
     */
    static InvokeContext createServerInvokeContext(Object rpcCtx, InvokeContext parent) {
        InvokeContext ctx = null;
        if (rpcCtx instanceof Map) {
            ctx = InvokeContext.fromMap((Map<String, String>) rpcCtx, parent);
        } else if (rpcCtx instanceof InvokeContext) {
            ctx = (InvokeContext) rpcCtx;
        }
        return ctx;
    }

    static InvokeContext createInvokeContext(Object rpcCtx) {
        InvokeContext ctx = null;
        boolean isClusterTest = false;
        boolean isDebug = false;
        if (rpcCtx instanceof Map) {
            boolean isSampleThreadId = false;
            final Map<String, String> context = (Map<String, String>) rpcCtx;
            String threadId = context.get(THREAD_ID_KEY);
            /**
             * 需要如果设置到上下文中的线程 ID与当前的线程 ID一致，并且上下文不为空，则忽略此次重构上下文
             */
            if (threadId != null && threadId.equals(String.valueOf(Thread.currentThread().getId())) && getInvokeContext() != null) {
                LOGGER.warn("setInvokeContext is sample thread. will append child context auto.");
                String identityContextId = context.get(IDENTITY_CONTEXT_ID);
                //如果需要设置的目标 context 与当前的 context 是同一个，则直接返回当前的上下文
                if (identityContextId != null && InvokeContext.get() != null && identityContextId.equals(String.valueOf(InvokeContext.get().getId()))) {
                    return InvokeContext.get();
                }
                isSampleThreadId = true;
            }
            ctx = InvokeContext.fromMap(context);
            if (isSampleThreadId) {
                if (InvokeContext.get() != null && ctx != null) {
                    ctx.parentInvokeContext = InvokeContext.get();
                }
            }

            isClusterTest = Boolean.valueOf((context).get(PradarService.PRADAR_CLUSTER_TEST_KEY));
            isDebug = Boolean.valueOf((context).get(PradarService.PRADAR_DEBUG_KEY));
        } else if (rpcCtx instanceof InvokeContext) {
            ctx = (InvokeContext) rpcCtx;
        }

        if (ctx == null) {
            ctx = new InvokeContext(TraceIdGenerator.generate(), appName(), MAL_ROOT_INVOKE_ID
                    , StringUtils.EMPTY, StringUtils.EMPTY);
            ctx.setUpAppName(appName());
            ctx.setClusterTest(isClusterTest);
            ctx.setDebug(isDebug);
        }
        return ctx;
    }

    /**
     * @param context 通过传入context，设置threadlocal变量
     * @see #getInvokeContext() 直接获取 InvokeContext 对象
     */
    static public void setInvokeContext(InvokeContext context) {
        InvokeContext.set(context);
    }

    /**
     * 清理全部调用上下文信息
     */
    static public void clearInvokeContext() {
        InvokeContext.set(null);
        Pradar.setDebug(false);
    }

    /**
     * 从栈上弹出一层 InvokeContext，用于客户端 Send/Recv 异步时主逻辑 需要把 send 的子 InvokeContext 弹出的场景
     *
     * @return 弹出的当前子 InvokeContext
     * @see #endClientInvoke(String, int) 类似用法，但不记日志
     */
    static public Map<String, String> popInvokeContextMap() {
        InvokeContext ctx = InvokeContext.get();
        if (null == ctx) {
            return null;
        }
        InvokeContext.set(ctx.parentInvokeContext);
        return ctx.toMap();
    }

    /**
     * 从栈上弹出一层 InvokeContext，用于客户端 Send/Recv 异步时主逻辑 需要把 send 的子 InvokeContext 弹出的场景
     *
     * @return 弹出的当前子 InvokeContext
     * @see #endClientInvoke(String, int) 类似用法，但不记日志
     */
    static public InvokeContext popInvokeContext() {
        InvokeContext ctx = InvokeContext.get();
        if (null == ctx) {
            return null;
        }
        InvokeContext.set(ctx.parentInvokeContext);
        /**
         * 如果弹出上下文，则将上下文的父上下文置空
         * 否则继续引用父上下文可能会导致内存泄露隐患
         */
        ctx.parentInvokeContext = null;
        return ctx;
    }


    static public void upAppName(String upAppName) {
        InvokeContext ctx = getInvokeContext();
        if (ctx != null) {
            ctx.setUpAppName(upAppName);
        }
    }

    static public void setLogType(int logType) {
        if (logType != Pradar.LOG_TYPE_TRACE && logType != Pradar.LOG_TYPE_INVOKE_CLIENT && logType != Pradar.LOG_TYPE_INVOKE_SERVER) {
            return;
        }
        InvokeContext context = InvokeContext.get();
        if (context != null) {
            context.setLogType(logType);
        }
    }

    static public void middlewareName(String middlewareName) {
        InvokeContext ctx = getInvokeContext();
        if (ctx != null) {
            ctx.setMiddlewareName(middlewareName);
            /**
             * 重新生成节点 ID
             */
            ctx.setNodeId(ctx.generateNodeId(ctx.getTraceNode(), ctx.getServiceName(), ctx.getMethodName(), middlewareName));
        }
    }

    /**
     * 获取当前调用的中间件名称
     *
     * @return
     */
    static public String getMiddlewareName() {
        InvokeContext invokeContext = getInvokeContext();
        if (invokeContext == null) {
            return null;
        }
        return invokeContext.getMiddlewareName();
    }

    /**
     * 创建一次的调用
     */
    static private void startInvoke() {
        try {
            // find root context
            InvokeContext ctx = InvokeContext.get();
            InvokeContext childCtx;
            /**
             * 兼容一下特殊场景
             */
            boolean isClusterTest = false;
            boolean isDebug = false;
            if (ctx != null && ctx.isEmpty()) {
                if (ctx.isEmpty()) {
                    isClusterTest = ctx.isClusterTest();
                    isDebug = ctx.isDebug();
                    popInvokeContext();
                    ctx = InvokeContext.get();
                }
            }

            if (null == ctx) {
                childCtx = new InvokeContext(TraceIdGenerator.generate(), appName(), MAL_ROOT_INVOKE_ID);
                childCtx.setUpAppName(appName());
            } else {
                // Create child invoke context
                childCtx = ctx.createChildInvoke();
                // 设置上游appName。如没有设置本应用名称
                childCtx.setUpAppName(ctx.getUpAppName());
            }
            InvokeContext.set(childCtx);
            if (ctx != null) {
                childCtx.setClusterTest(ctx.isClusterTest() || childCtx.isClusterTest() || isClusterTest);
                childCtx.setDebug(ctx.isDebug() || childCtx.isDebug() || isDebug);
            }
            if (isClusterTest) {
                Pradar.setClusterTest(true);
            }
            if (isDebug) {
                Pradar.setDebug(true);
            }
        } catch (Throwable re) {
            LOGGER.error("startRpc", re);
        }
    }

    /**
     * 开始客户端调用
     *
     * @param serviceName 服务名称
     * @param methodName  方法名称
     */
    static public void startClientInvoke(String serviceName, String methodName) {
        try {
            startInvoke();
            InvokeContext ctx = InvokeContext.get();
            if (null == ctx) {
                return;
            }
            ctx.startClientInvoke(serviceName, methodName);
            if (Pradar.isDebug()) {
                DebugTestInfoPusher.doRecord("beforeFirst");
                DebugTestInfoPusher.doRecord("beforeLast");
            }
        } catch (Throwable re) {
            LOGGER.error("rpcClientSend", re);
        }
    }

    /**
     * 记录 DUBBO 客户端收到RPC响应的事件
     *
     * @param resultCode 参考 RPC_RESULT_开头的结果码。有响应表示RPC成功，但业务上有可能还是失败，因此有不同的结果码。
     * @deprecated 应该使用明确指定 rpcType 的方法 {@link #endClientInvoke(String, int)}
     */
    static public void endClientInvoke(String resultCode) {
        endClientInvoke(resultCode, MiddlewareType.TYPE_RPC);
    }

    /**
     * 记录客户端收到RPC响应的事件
     *
     * @param resultCode 参考 RPC_RESULT_开头的结果码。有响应表示RPC成功，但业务上有可能还是失败，因此有不同的结果码。
     * @param type       类型为：TYPE_TRACE,TYPE_DUBBO_CLIENT,TYPE_DB,TYPE_METAQ,TYPE_SEARCH
     */
    static public void endClientInvoke(String resultCode, int type) {
        try {
            if (Pradar.isSuccessResult()) {
                DebugTestInfoPusher.doRecord("afterFirst");
            } else {
                DebugTestInfoPusher.doRecord("exceptionFirst");
            }
            InvokeContext ctx = InvokeContext.get();
            if (null == ctx) {
                return;
            }
            ctx.endClientInvoke(resultCode, type);
            commitInvokeContext(ctx);
            if (Pradar.isDebug()) {
                if (Pradar.isSuccessResult()) {
                    DebugTestInfoPusher.doRecord("afterLast");
                } else {
                    DebugTestInfoPusher.doRecord("exceptionLast");
                }
            }
            // 弹出当前 ctx
            InvokeContext.set(ctx.parentInvokeContext);
        } catch (Throwable re) {
            LOGGER.error("rpcClientRecv", re);
        }
    }

    /**
     * 判断是否是一个空的上下文
     * 这种情况如 dubbox 外层会有一个 web server 的 trace，到内部执行到 dubbo 时
     * 上下文中是没有值的，只是一个空的 map，所以需要判断一下，免得上下文出现错误
     *
     * @param ctxObj
     * @return
     */
    static private boolean isEmptyContext(Object ctxObj) {
        if (ctxObj == null) {
            return true;
        }
        if (ctxObj instanceof Map) {
            return ((Map) ctxObj).isEmpty();
        }
        return false;
    }

    /**
     * 服务端收到请求，兼容多个插件埋点重复的问题，兼容服务端与客户端均为本地的问题
     *
     * @param service
     * @param method
     * @param remoteAppName
     * @param ctxObj
     */
    static public void startServerInvoke(String service, String method, String remoteAppName, Object ctxObj) {
        try {
            /**
             * 修改原有方式，防止在rpc接收之前会经过其他中间件的埋点导致出错
             */
            InvokeContext ctx = InvokeContext.get();
            InvokeContext childCtx = null;
            /**
             * 兼容一下特殊场景
             */
            boolean isClusterTest = false;
            boolean isDebug = false;
            if (ctx != null && ctx.isEmpty()) {
                if (ctx.isEmpty()) {
                    isClusterTest = ctx.isClusterTest();
                    isDebug = ctx.isDebug();
                    popInvokeContext();
                    ctx = InvokeContext.get();
                }
            }

            if (null == ctx) {
                if (!isEmptyContext(ctxObj)) {
                    childCtx = createInvokeContext(ctxObj);
                } else {
                    childCtx = new InvokeContext(TraceIdGenerator.generate(), appName(), MAL_ROOT_INVOKE_ID
                            , method, service);
                    childCtx.setUpAppName(appName());
                }
            } else {
                /**
                 * 解决兼容性问题
                 */
                if (!isEmptyContext(ctxObj)) {
                    childCtx = createServerInvokeContext(ctxObj, ctx);
                }
                if (childCtx == null) {
                    // Create child invoke context
                    childCtx = ctx.createChildInvoke();
                    // 设置上游appName。如没有设置本应用名称
                    childCtx.setUpAppName(ctx.getUpAppName());
                    if (ctxObj instanceof Map) {
                        isClusterTest = isClusterTest | ClusterTestUtils.isClusterTestRequest(((Map<String, String>) ctxObj).get(PradarService.PRADAR_CLUSTER_TEST_KEY));
                        if (isClusterTest) {
                            childCtx.setClusterTest(true);
                        }
                        isDebug = isDebug | ClusterTestUtils.isDebugRequest(((Map<String, String>) ctxObj).get(PradarService.PRADAR_DEBUG_KEY));
                        if (isDebug) {
                            childCtx.setDebug(true);
                        }
                    }
                }
            }
            InvokeContext.set(childCtx);
            if (ctx != null) {
                childCtx.setClusterTest(ctx.isClusterTest() || childCtx.isClusterTest());
                childCtx.setDebug(ctx.isDebug() || childCtx.isDebug());
            }
            ctx = InvokeContext.get();
            if (null == ctx) {
                return;
            }
            ctx.startServerInvoke(service, method);
            if (StringUtils.isNotBlank(remoteAppName)) {
                ctx.setRemoteAppName(remoteAppName);
            }
            if (Pradar.isDebug()) {
                DebugTestInfoPusher.doRecord("beforeFirst");
                DebugTestInfoPusher.doRecord("beforeLast");
            }
            if (isClusterTest) {
                Pradar.setClusterTest(true);
            }
            if (isDebug) {
                Pradar.setDebug(true);
            }
        } catch (Throwable re) {
            LOGGER.error("rpcServerRecv", re);
        }
    }

    /**
     * 服务端收到请求，兼容多个插件埋点重复的问题，兼容服务端与客户端均为本地的问题
     *
     * @param service
     * @param method
     * @param ctxObj
     */
    static public void startServerInvoke(String service, String method, Object ctxObj) {
        startServerInvoke(service, method, null, ctxObj);
    }

    /**
     * 服务端收到RPC请求
     */
    static public void startServerInvoke(String service, String method, String remoteAppName) {
        try {
            /**
             * 修改原有方式，防止在rpc接收之前会经过其他中间件的埋点导致出错
             */
            startInvoke();
            InvokeContext ctx = InvokeContext.get();
            if (null == ctx) {
                return;
            }
            ctx.startServerInvoke(service, method);
            if (StringUtils.isNotBlank(remoteAppName)) {
                ctx.setRemoteAppName(remoteAppName);
            }
            if (Pradar.isDebug()) {
                DebugTestInfoPusher.doRecord("beforeFirst");
                DebugTestInfoPusher.doRecord("beforeLast");
            }
        } catch (Throwable re) {
            LOGGER.error("rpcServerRecv", re);
        }
    }

    /**
     * DUBBO服务端返回RPC响应，只允许DUBBO使用，Threadlocal变量会被清空
     *
     * @deprecated 应该使用明确指定 rpcType 的方法 {@link #endServerInvoke(int)}
     */
    static public void endServerInvoke() {
        endServerInvoke(MiddlewareType.TYPE_RPC);
    }

    /**
     * 服务端返回RPC响应，指定 invoke 类型，Threadlocal变量会被清空
     *
     * @param type 类型为：   TYPE_DUBBO_SERVER,TYPE_METAQ
     */
    static public void endServerInvoke(int type) {
        endServerInvoke(null, type);
    }

    /**
     * 服务端返回RPC响应，指定 invoke 类型，Threadlocal变量会被清空
     *
     * @param resultCode 参考 RPC_RESULT_开头的结果码。有响应表示RPC成功，但业务上有可能还是失败，因此有不同的结果码。
     * @param type       类型为：   TYPE_DUBBO_SERVER,TYPE_METAQ
     */
    static public void endServerInvoke(String resultCode, int type) {
        try {
            if (Pradar.isSuccessResult()) {
                DebugTestInfoPusher.doRecord("afterFirst");
            } else {
                DebugTestInfoPusher.doRecord("exceptionFirst");
            }
            InvokeContext ctx = InvokeContext.get();
            if (null == ctx) {
                return;
            }

            ctx.endServerInvoke(type, resultCode);
            commitInvokeContext(ctx);
            if (Pradar.isDebug()) {
                if (Pradar.isSuccessResult()) {
                    DebugTestInfoPusher.doRecord("afterLast");
                } else {
                    DebugTestInfoPusher.doRecord("exceptionLast");
                }
            }
            // 弹出当前 ctx
            InvokeContext.set(ctx.parentInvokeContext);
        } catch (Throwable re) {
            LOGGER.error("rpcServerSend", re);
        }
    }

    /**
     * 服务端返回RPC响应，指定 invoke 类型，Threadlocal变量会被清空
     *
     * @param resultCode 参考 RPC_RESULT_开头的结果码。有响应表示RPC成功，但业务上有可能还是失败，因此有不同的结果码。
     */
    static public void endServerInvoke(String resultCode) {
        try {
            if (Pradar.isSuccessResult()) {
                DebugTestInfoPusher.doRecord("afterFirst");
            } else {
                DebugTestInfoPusher.doRecord("exceptionFirst");
            }
            InvokeContext ctx = InvokeContext.get();
            if (null == ctx) {
                return;
            }

            ctx.endServerInvoke(resultCode);
            commitInvokeContext(ctx);
            if (Pradar.isDebug()) {
                if (Pradar.isSuccessResult()) {
                    DebugTestInfoPusher.doRecord("afterLast");
                } else {
                    DebugTestInfoPusher.doRecord("exceptionLast");
                }
            }
            // 弹出当前 ctx
            InvokeContext.set(ctx.parentInvokeContext);
        } catch (Throwable re) {
            LOGGER.error("rpcServerSend", re);
        }
    }

    /**
     * 设置调用类型
     *
     * @param type
     */
    static public void setInvokeType(int type) {
        InvokeContext context = getInvokeContext();
        if (context != null) {
            context.setInvokeType(type);
        }
    }

    /**
     * 获取全局唯一的Traceid
     */
    static public String getTraceAppName() {
        InvokeContext ctx = InvokeContext.get();
        return null == ctx ? null : ctx.traceAppName;
    }

    static public String getUpAppName() {
        InvokeContext ctx = InvokeContext.get();
        return null == ctx ? null : ctx.upAppName;
    }

    static public String getRemoteAppName() {
        InvokeContext ctx = InvokeContext.get();
        return null == ctx ? null : ctx.remoteAppName;
    }

    static public String getRemoteIp() {
        InvokeContext ctx = InvokeContext.get();
        return null == ctx ? null : ctx.remoteIp;
    }


    /**
     * 获取全局唯一的Traceid
     */
    static public String getTraceId() {
        InvokeContext ctx = InvokeContext.get();
        return null == ctx ? null : ctx.traceId;
    }

    /**
     * 获取当前方法invoke调用层次
     */
    static public String getInvokeId() {
        InvokeContext ctx = InvokeContext.get();
        return null == ctx ? null : ctx.invokeId;
    }

    static public String getChildInvokeId() {
        InvokeContext ctx = InvokeContext.get();
        return null == ctx ? null : ctx.nextChildInvokeId();
    }

    /**
     * 获取当前rpc调用type
     */
    static public Integer getLogType() {
        InvokeContext ctx = InvokeContext.get();
        return null == ctx ? null : ctx.logType;
    }

    /**
     * 获取当前rpc是否成功
     */
    static public Boolean isSuccessResult() {
        InvokeContext ctx = InvokeContext.get();
        return null == ctx ? Boolean.FALSE :
                (ResultCode.INVOKE_RESULT_SUCCESS.equals(ctx.resultCode) || "200".equals(ctx.resultCode));
    }

    /**
     * 设置请求内容
     *
     * @param request
     */
    static public void request(Object request) {
        InvokeContext ctx = InvokeContext.get();
        if (ctx != null) {
            ctx.request = request;
        }
    }

    /**
     * 设置响应内容
     *
     * @param response
     */
    static public void response(Object response) {
        InvokeContext ctx = InvokeContext.get();
        if (ctx != null) {
            ctx.response = response;
        }
    }


    /**
     * invoke 上追加的key value信息，会打印到当前 invoke 日志中。 与添加业务信息的 {@link #putUserData(String, String)} 不同，
     * attribute 不会跟随 invoke 调用传递，只对本地当前的这一次 invoke 有效
     *
     * @see #putUserData(String, String)
     */
    static public void attribute(String key, String value) {
        createContextIfNotExists(true).putLocalAttribute(key, value);
    }

    /**
     * 获取随 Pradar 通过 DUBBO、MetaQ 等中间件传递的业务信息
     *
     * @param key 不能为空
     */
    static public String getUserData(String key) {
        InvokeContext ctx = InvokeContext.get();
        return null != ctx ? ctx.getUserData(key) : null;
    }

    /**
     * 放置 key 对应的业务信息，这个信息会打印到当前 invoke 的日志之中。 信息会随 Pradar 通过 DUBBO、MetaQ 等中间件传递。
     * 数据在调用链里面的兄弟间、父子间传递，但不会往回传。 如果仅仅希望添加业务信息，不需要信息被传递，可以使用 {@link #attribute(String, String)}
     *
     * @param key   不能为空
     * @param value 值，不能有回车、换行、“|” 等符号
     * @return 返回成功还是失败
     * @see #attribute(String, String)
     */
    static public boolean putUserData(String key, String value) {
        InvokeContext ctx = InvokeContext.get();
        if (ctx == null) {
            LOGGER.error("can't found a available InvokeContext to set key:{}", key);
            return false;
        }
        ctx.putUserData(key, value);
        return true;
    }

    /**
     * 判断是否具有用户属性
     *
     * @param key 用户属性 key
     * @return true|false
     */
    static public boolean hasUserData(String key) {
        InvokeContext ctx = InvokeContext.get();
        if (ctx == null) {
            return false;
        }
        return ctx.hasUserData(key);
    }

    /**
     * 清除 key 对应的业务信息
     *
     * @param key 不能为空
     * @return 原来的值
     */
    static public String removeUserData(String key) {
        InvokeContext ctx = InvokeContext.get();
        return null != ctx ? ctx.removeUserData(key) : null;
    }

    /**
     * 获取随 Pradar 通过 DUBBO、METAQ 等中间件传递的业务信息。 供内部使用，业务应该使用 {@link #getUserData(String)}
     */
    static public Map<String, String> getUserDataMap() {
        InvokeContext ctx = InvokeContext.get();
        return null != ctx ? ctx.getUserDataMap() : null;
    }

    /**
     * 导出业务信息，供中间件传输 Pradar 上下文时使用。
     */
    static public String exportUserData() {
        InvokeContext ctx = InvokeContext.get();
        return null != ctx ? ctx.exportUserData() : null;
    }

    /**
     * 用于业务方希望追加相关数据到rpc调用链中，比如想把业务的方法中的某个参数值打印出来，放到rpc的日志中。
     *
     * @param msg 用户希望追加的内容，不能有回车、换行、“|” 等符号
     */
    static public void callBack(String msg) {
        if (msg != null && msg.length() < MAX_BIZ_LOG_SIZE) {
            InvokeContext ctx = InvokeContext.get();
            if (null == ctx) {
                return;
            }
            ctx.callBackMsg = msg;
        }
    }

    /**
     * rpc请求大小
     */
    static public void requestSize(long size) {
        InvokeContext ctx = InvokeContext.get();
        if (null == ctx) {
            return;
        }
        ctx.requestSize = size;
    }

    /**
     * 追加远程服务地址
     *
     * @param remoteIp 远程机器ip地址
     */
    static public void remoteIp(String remoteIp) {
        InvokeContext ctx = InvokeContext.get();
        if (null == ctx) {
            return;
        }
        ctx.remoteIp = remoteIp;
    }

    /**
     * 追加远程端口
     *
     * @param port 远程机器端口
     */
    static public void remotePort(String port) {
        InvokeContext ctx = InvokeContext.get();
        if (null == ctx) {
            return;
        }
        ctx.port = port;
    }

    /**
     * 追加远程端口
     *
     * @param port 远程机器端口
     */
    static public void remotePort(int port) {
        InvokeContext ctx = InvokeContext.get();
        if (null == ctx) {
            return;
        }
        ctx.port = String.valueOf(port);
    }

    /**
     * rpc响应的大小
     */
    static public void responseSize(long size) {
        InvokeContext ctx = InvokeContext.get();
        if (null == ctx) {
            return;
        }
        ctx.responseSize = size;
    }

    static public void startTime(long startTime) {
        InvokeContext ctx = InvokeContext.get();
        if (null == ctx) {
            return;
        }
        ctx.startTime = startTime;
    }

    /**
     * 返回当前上下文是否有错误
     * 这个只是在特殊的时候需要标记，正常的上下文是否有错误在
     * trace 跟踪时即可获取到
     *
     * @return
     */
    static public boolean hasError() {
        InvokeContext ctx = InvokeContext.get();
        if (null == ctx) {
            return false;
        }
        return ctx.isHasError();
    }

    /**
     * 设置当前上下文是否有错误
     * 这个只是在特殊的时候需要标记，正常的上下文是否有错误在
     * trace 跟踪时即可获取到
     *
     * @param hasError 当前上下文是否有错误
     * @return
     */
    static public void setError(boolean hasError) {
        InvokeContext ctx = InvokeContext.get();
        if (null == ctx) {
            return;
        }
        ctx.setHasError(hasError);
    }

    /**
     * 生成全局唯一的traceid
     *
     * @param ip 用户出入的ip地址，如果非法或者为空，则使用当前机器的ip地址
     */
    static public String generateTraceId(String ip) {
        return TraceIdGenerator.generate(ip);
    }

    /**
     * 设置是否需要线程兜底 commit
     *
     * @param isThreadCommit
     */
    static public void setThreadCommit(boolean isThreadCommit) {
        InvokeContext context = getInvokeContext();
        if (context != null) {
            context.setThreadCommit(isThreadCommit);
        }
    }

    /**
     * 返回是否需要线程兜底 commit
     *
     * @return
     */
    static public boolean isThreadCommit() {
        InvokeContext context = getInvokeContext();
        if (context != null) {
            return context.isThreadCommit();
        }
        return false;
    }

    /**
     * 提交调用上下文，生成日志。这是一个为了提高中间件埋点性能而特别设置的内部方法， 在不依赖 ThreadLocal 的场景使用。
     */
    static public void commitInvokeContext(InvokeContext ctx) {
        if (((ctx.logType >= 0 && !PradarSwitcher.isRpcOff() && PradarSwitcher.isTraceEnabled() && ctx.isTraceSampled()) || ctx.isDebug()) && !ctx.isEmpty()) {
            rpcAppender.append(ctx);
        }
        /**
         * 如果是压测流量，则设置有压测流量
         */
        if (ctx.isClusterTest) {
            hasPressureRequest.set(true);
        }
    }

    /**
     * 清理是否有压测流量请求标记，用于收集性能数据时使用
     */
    static public boolean clearHasPressureRequest() {
        return hasPressureRequest.compareAndSet(true, false);
    }

    /**
     * 强制刷新缓存中的日志内容到文件中去，一般建议外部在程序结束阶段时调用， 使用中频繁调用会导致性能下降
     */
    static public void flush() {
        PradarLogDaemon.flushAndWait();
    }

    /**
     * 获取服务配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    /**
     * 获取服务配置
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 返回配置值
     */
    public static String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null) {
            value = System.getenv(key);
        }
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 返回request是否打开
     *
     * @return true/false
     */
    public static boolean isRequestOn() {
        return getBooleanProperty("plugin.request.on", true);
    }

    /**
     * 返回exception是否打开
     *
     * @return true/false
     */
    public static boolean isResponseOn() {
        return getBooleanProperty("plugin.response.on", true);
    }

    /**
     * 返回response是否打开
     *
     * @return true/false
     */
    public static boolean isExceptionOn() {
        return getBooleanProperty("plugin.exception.on", true);
    }

    /**
     * 返回插件请求最大长度
     *
     * @return
     */
    public static Integer getPluginRequestSize() {
        return getIntProperty("plugin.request.size", 1000);
    }

    /**
     * 返回插件响应最大长度
     *
     * @return
     */
    public static Integer getPluginResponseSize() {
        return getIntProperty("plugin.response.size", 1000);
    }

    /**
     * 获取int配置
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 返回配置值
     */
    public static Integer getIntProperty(String key, Integer defaultValue) {
        String value = getProperty(key);
        if (org.apache.commons.lang.math.NumberUtils.isDigits(value)) {
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
    public static Integer getIntProperty(String key) {
        return getIntProperty(key, null);
    }

    /**
     * 获取long配置
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 返回配置值
     */
    public static Long getLongProperty(String key, Long defaultValue) {
        String value = getProperty(key);
        if (org.apache.commons.lang.math.NumberUtils.isDigits(value)) {
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
    public static Long getLongProperty(String key) {
        return getLongProperty(key, null);
    }

    /**
     * 获取long配置
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 返回配置值
     */
    public static Boolean getBooleanProperty(String key, Boolean defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            return Boolean.valueOf(value);
        }
        return defaultValue;
    }

    /**
     * 获取int配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    public static Boolean getBooleanProperty(String key) {
        return getBooleanProperty(key, null);
    }
}
