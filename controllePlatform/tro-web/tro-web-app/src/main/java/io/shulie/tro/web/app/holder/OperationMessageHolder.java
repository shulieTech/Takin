/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.web.app.holder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-09-16
 */
@Slf4j
@Component
public class OperationMessageHolder implements InitializingBean {

    public static final Map<String, String> MESSAGE_HOLDER = new ConcurrentHashMap<>();

    private OperationMessageHolder() { /* no instance */ }

    public static String formatMessage(String key, Map<String, String> vars) {
        String messagePattern = MESSAGE_HOLDER.get(key);
        if (messagePattern == null) {
            log.error("日志记录失败，key[{}]对应的描述信息在文件中没有发现", key);
            return "";
        }
        StringSubstitutor sub = new StringSubstitutor(vars);
        try {
            return sub.replace(messagePattern);
        } catch (Exception e) {
            log.error("日志记录失败，key[{}]对应的描述信息转换失败,messagePattern:[{}],变量为:[{}]", key, messagePattern, vars);
            return "";
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try (InputStream logFileInput = this.getClass().getClassLoader()
            .getResourceAsStream("log_message/operation_log.properties")) {
            if (logFileInput == null) {
                log.error("操作日志描述文件加载失败，在classpath下面，没有找到/log_message的目录");
                throw new IllegalArgumentException(
                    "操作日志描述文件加载失败，在classpath下面，没有找到[log_message/operation_log.properties]的文件");
            }
            Properties properties = new Properties();
            properties.load(new InputStreamReader(logFileInput, StandardCharsets.UTF_8));
            Enumeration<Object> keys = properties.keys();
            while (keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                String value = (String)properties.get(key);
                MESSAGE_HOLDER.put(key, value);
            }
        } catch (Exception e) {
            log.error("读取操作日志的文件失败！");
        }
    }
}
