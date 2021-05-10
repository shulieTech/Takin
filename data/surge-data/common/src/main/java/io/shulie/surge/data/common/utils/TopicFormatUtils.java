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
