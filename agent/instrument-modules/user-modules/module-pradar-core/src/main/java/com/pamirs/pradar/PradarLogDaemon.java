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

import com.shulie.instrument.simulator.api.executors.ExecutorServiceFactory;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Pradar 定时检查，主要有下面的行为： <ul> <li>删除 .deleted 结尾的文件 <li>检测文件开关 <li>间隔一段时间输出一次索引 <li>日志文件被删除，则尝试重新创建之
 * <li>定时强制输出文件内容 <li> </ul>
 */
class PradarLogDaemon implements Runnable {

    private static final String SWITCH_ON = "on";
    private static final String SWITCH_OFF = "off";

    private static AtomicBoolean running = new AtomicBoolean(false);

    private static final CopyOnWriteArrayList<PradarAppender> watchedAppenders
            = new CopyOnWriteArrayList<PradarAppender>();

    // 文件开关
    private static final File configFile = new File(Pradar.PRADAR_LOG_DIR + "config.properties");

    private final static Logger LOGGER = LoggerFactory.getLogger(PradarLogDaemon.class);

    private static ScheduledFuture scheduledFuture;

    /**
     * 定期检测日志文件：如果被删除，则尝试重新创建之；强制刷新 appender
     */
    static final PradarAppender watch(PradarAppender appender) {
        watchedAppenders.addIfAbsent(appender);
        return appender;
    }

    static final boolean unwatch(PradarAppender appender) {
        return watchedAppenders.remove(appender);
    }

    @Override
    public void run() {

        // 文件开关检测
        checkFileSwitches();

        // 定时清理
        cleanupFiles();

        // 如果被删除，则尝试重新创建之；强制刷新 appender
        flushAndReload();
    }

    private void cleanupFiles() {
        for (PradarAppender watchedAppender : watchedAppenders) {
            try {
                watchedAppender.cleanup();
            } catch (Throwable e) {
                LOGGER.error("fail to cleanup: {}", watchedAppender, e);
            }
        }
    }

    private void flushAndReload() {
        for (PradarAppender watchedAppender : watchedAppenders) {
            try {
                watchedAppender.reload();
            } catch (Throwable e) {
                LOGGER.error("fail to reload: {}", watchedAppender, e);
            }
        }
    }

    private void checkFileSwitches() {

        try {
            Properties configurations = new Properties();
            if (configFile.exists()) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(configFile);
                    configurations.load(fis);
                } catch (Throwable e) {
                    LOGGER.error("Load configuration file error", e);
                } finally {
                    if (null != fis) {
                        fis.close();
                    }
                }
            }
            if (checkConfiguration(configurations)) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(configFile);
                    configurations.store(fos, "Store [" + configFile.getParentFile().getName() + "] configuration file.");
                } catch (Throwable e) {
                    LOGGER.error("Store configuration file error", e);
                } finally {
                    if (null != fos) {
                        fos.close();
                    }
                }
            }
        } catch (Throwable e) {
            LOGGER.error("Check on/off file error", e);
        }
    }

    private String getSystemProperty(String propertyName) {
        String value = System.getProperty(propertyName);
        if (value == null) {
            value = System.getenv(propertyName);
        }
        return value;
    }

    private boolean checkConfiguration(Properties configurations) {
        boolean configFileRefresh = false;
        String rpcSwitch = configurations.getProperty("pradar.rpc.switch", getSystemProperty("pradar.rpc.switch"));
        if (PradarSwitcher.isRpcOff() && SWITCH_ON.equals(rpcSwitch)) {
            PradarSwitcher.turnRpcOn();
            configFileRefresh = true;
        } else if (!PradarSwitcher.isRpcOff() && SWITCH_OFF.equals(rpcSwitch)) {
            PradarSwitcher.turnRpcOff();
            configFileRefresh = true;
        }
        if (!SWITCH_ON.equals(rpcSwitch) && !SWITCH_OFF.equals(rpcSwitch)) {
            configFileRefresh = true;
        }
        configurations.setProperty("pradar.rpc.switch", PradarSwitcher.isRpcOff() ? SWITCH_OFF : SWITCH_ON);

        String bizSwitch = configurations.getProperty("pradar.biz.switch", getSystemProperty("pradar.biz.switch"));
        if (PradarSwitcher.isBizOff() && SWITCH_ON.equals(bizSwitch)) {
            PradarSwitcher.turnBizOn();
            configFileRefresh = true;
        } else if (!PradarSwitcher.isBizOff() && SWITCH_OFF.equals(bizSwitch)) {
            PradarSwitcher.turnBizOff();
            configFileRefresh = true;
        }
        if (!SWITCH_ON.equals(bizSwitch) && !SWITCH_OFF.equals(bizSwitch)) {
            configFileRefresh = true;
        }
        configurations.setProperty("pradar.biz.switch", PradarSwitcher.isBizOff() ? SWITCH_OFF : SWITCH_ON);

        String userDataSwitch = configurations.getProperty("pradar.user.data.switch", getSystemProperty("pradar.user.data.switch"));
        if (!PradarSwitcher.isUserDataEnabled() && SWITCH_ON.equals(userDataSwitch)) {
            PradarSwitcher.turnUserDataOn();
            configFileRefresh = true;
        } else if (PradarSwitcher.isUserDataEnabled() && SWITCH_OFF.equals(userDataSwitch)) {
            PradarSwitcher.turnUserDataOff();
            configFileRefresh = true;
        }
        if (!SWITCH_ON.equals(userDataSwitch) && !SWITCH_OFF.equals(userDataSwitch)) {
            configFileRefresh = true;
        }
        configurations.setProperty("pradar.user.data.switch", PradarSwitcher.isUserDataEnabled() ? SWITCH_ON : SWITCH_OFF);

        String logDumpSwitch = configurations.getProperty("pradar.log.dump.switch", getSystemProperty("pradar.log.dump.switch"));
        if (!PradarSwitcher.isLogDumpEnabled() && SWITCH_ON.equals(logDumpSwitch)) {
            PradarSwitcher.turnLogDumpOn();
            configFileRefresh = true;
        } else if (PradarSwitcher.isLogDumpEnabled() && SWITCH_OFF.equals(logDumpSwitch)) {
            PradarSwitcher.turnLogDumpOff();
            configFileRefresh = true;
        }
        if (!SWITCH_ON.equals(logDumpSwitch) && !SWITCH_OFF.equals(logDumpSwitch)) {
            configFileRefresh = true;
        }
        configurations.setProperty("pradar.log.dump.switch", PradarSwitcher.isLogDumpEnabled() ? SWITCH_ON : SWITCH_OFF);

        String samplingInterval = configurations.getProperty("pradar.sampling.interval", getSystemProperty("pradar.sampling.interval"));
        if (!PradarSwitcher.getSamplingZkConfig()
                && NumberUtils.isDigits(samplingInterval)
                && PradarSwitcher.getSamplingInterval() != Integer.parseInt(samplingInterval)) {
            PradarSwitcher.setSamplingInterval(Integer.parseInt(samplingInterval));
            configFileRefresh = true;
        }

        String mustSamplingInterval = configurations.getProperty("pradar.mustsampling.interval", getSystemProperty("pradar.mustsampling.interval"));
        if (NumberUtils.isDigits(mustSamplingInterval)
                && PradarSwitcher.getMustSamplingInterval() != Integer.valueOf(mustSamplingInterval)) {
            PradarSwitcher.setMustSamplingInterval(Integer.valueOf(mustSamplingInterval));
            configFileRefresh = true;
        }
        configurations.setProperty("pradar.mustsampling.interval", String.valueOf(PradarSwitcher.getMustSamplingInterval()));
        return configFileRefresh;
    }

    static void start() {
        if (PradarSwitcher.isPradarLogDaemonEnabled() && running.compareAndSet(false, true)) {
            int logDaemonInterval = Pradar.PRADAR_LOG_DAEMON_INTERVAL;
            scheduledFuture = ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.scheduleAtFixedRate(new PradarLogDaemon(), logDaemonInterval, logDaemonInterval, TimeUnit.SECONDS);
        } else {
            LOGGER.warn("PradarLogDaemon start failed. cause by logDaemonSwitcher: {}, runningStatus: {}", PradarSwitcher.isPradarLogDaemonEnabled(), running.get());
        }
    }

    static void shutdown() {
        if (scheduledFuture != null && !scheduledFuture.isDone() && !scheduledFuture.isCancelled()) {
            try {
                scheduledFuture.cancel(true);
            } catch (Throwable e) {
                LOGGER.error("shutdown PradarLogDaemon failed: ", e);
            }
        }
    }

    static void flushAndWait() {
        for (PradarAppender watchedAppender : watchedAppenders) {
            try {
                if (watchedAppender instanceof AsyncAppender) {
                    ((AsyncAppender) watchedAppender).flushAndWait();
                } else {
                    watchedAppender.flush();
                }
            } catch (Throwable e) {
                LOGGER.error("fail to flush: {}", watchedAppender, e);
            }
        }
    }
}
