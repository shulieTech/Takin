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

package io.shulie.surge.data.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author vincent
 */
public class TopicFormatUtils {

    private static final String PREFIX = "PT_";
    private static final String DEFIX = "_PT";

    /**
     * @param topic
     * @return
     */
    public static String format(String topic) {
        if (StringUtils.isBlank(topic)) {
            return topic;
        }
        if (StringUtils.startsWith(topic, PREFIX.toUpperCase()) || StringUtils.startsWith(topic, PREFIX.toLowerCase())) {
            return StringUtils.substring(topic, PREFIX.length());
        }
        if (StringUtils.endsWith(topic, DEFIX.toUpperCase()) || StringUtils.endsWith(topic, DEFIX.toLowerCase())) {
            return StringUtils.substring(topic, 0, topic.length() - PREFIX.length());
        }
        return topic;
    }

    /**
     * @param topic
     * @return
     */
    public static String replaceAll(String topic) {
        if (StringUtils.isBlank(topic)) {
            return topic;
        }
        if (topic.contains(PREFIX) || topic.contains(DEFIX)
                || topic.contains(PREFIX.toLowerCase())
                || topic.contains(DEFIX.toLowerCase())) {
            topic = topic.replaceAll(PREFIX, "");
            topic = topic.replaceAll(DEFIX, "");
            topic = topic.replaceAll(PREFIX.toLowerCase(), "");
            topic = topic.replaceAll(DEFIX.toLowerCase(), "");
        }
        return topic;
    }
}
