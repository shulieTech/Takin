package io.shulie.surge.data.runtime.digest;



import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * Pradar 日志处理的上下文
 *
 * @author pamirs
 */
public final class DigestContext<T extends Serializable> implements Serializable {

    /**
     * 当前处理的时间
     */
    private long processTime;
    /**
     * 数据时间
     */
    private long eventTime;

    private Map<String, Object> header = Maps.newHashMap();

    private T content;

    public long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }
}
