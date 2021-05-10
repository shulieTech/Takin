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
package com.shulie.instrument.simulator.agent.core.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Logback日志框架工具类
 */
public class LogbackUtils {

    /**
     * 初始化Logback日志框架
     */
    public static void init(final InputStream is) {
        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        loggerContext.reset();
        final Logger logger = LoggerFactory.getLogger(LoggerFactory.class);
        try {
            configurator.doConfigure(is);
            logger.warn(SimulatorStringUtils.getLogo());
            if (logger.isInfoEnabled()) {
                logger.info("SIMULATOR: initializing logback success. input={};", is);
            }
        } catch (Throwable cause) {
            logger.warn("SIMULATOR: initialize logback failed. input={};", is, cause);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 初始化Logback日志框架
     *
     * @param logbackConfigFilePath logback配置文件路径
     */
    public static void init(final String logbackConfigFilePath) {
        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final JoranConfigurator configurator = new JoranConfigurator();
        final File configureFile = new File(logbackConfigFilePath);
        configurator.setContext(loggerContext);
        loggerContext.reset();
        InputStream is = null;
        final Logger logger = LoggerFactory.getLogger(LoggerFactory.class);
        try {
            is = new FileInputStream(configureFile);
            configurator.doConfigure(is);
            logger.error(SimulatorStringUtils.getLogo());
            if (logger.isInfoEnabled()) {
                logger.info("SIMULATOR: initializing logback success. file={};", configureFile);
            }
        } catch (Throwable cause) {
            logger.warn("SIMULATOR: initialize logback failed. file={};", configureFile, cause);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 销毁Logback日志框架
     */
    public static void destroy() {
        try {
            ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }

}
