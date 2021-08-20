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

package io.shulie.surge.data.common.aggregation.metrics;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 数据统计维度，为了提高性能做了比较多优化
 * 注：这是一个不变量 Immutable
 *
 * @author pamirs
 */
public class Metric implements Serializable {

    private static final long serialVersionUID = -7824717738246876865L;

    private static final ThreadLocal<char[]> tbuffer = new ThreadLocal<char[]>() {
        @Override
        protected char[] initialValue() {
            return new char[4096];
        }
    };

    /**
     * 统计编号
     */
    private String metricId;
    /**
     * 前缀，支持带时间范围的批量扫描
     */
    private String[] prefixes;
    /**
     * rowkey 中的时间戳
     */
    private String time;
    /**
     * 后缀，一般用于附加额外的信息
     */
    private String[] suffixes;

    /**
     * 提高比较和哈希性能
     */
    private transient int hash;

    private Metric() {
        // 留给序列化/反序列化
    }

    private Metric(String metricId, String[] prefixes, String time, String[] suffixes) {
        this.prefixes = prefixes;
        this.time = time;
        this.suffixes = suffixes;
        this.metricId = metricId;
    }

    public static final Metric of(String metricId, String[] prefixes, String time, String[] suffixes) {
        return new Metric(metricId, prefixes, time, suffixes);
    }

    public static final Metric of(String metricId, String[] prefixes, long time, String[] suffixes) {
        return new Metric(metricId, prefixes, Long.toString(time, 10), suffixes);
    }

    protected final int hashCodeOf(char[] buffer, int start, int end) {
        int len = end - start;
        int h = 0;
        for (int i = 0; i < len; i++) {
            h = 31 * h + buffer[start++];
        }
        return h;
    }

    public String[] getPrefixes() {
        return prefixes;
    }

    public String getTime() {
        return time;
    }

    public String[] getSuffixes() {
        return suffixes;
    }

    public String getMetricId() {
        return metricId;
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            int result = 1;
            result = 31 * result + Arrays.hashCode(prefixes);
            result = 31 * result + Arrays.hashCode(suffixes);
            result = 31 * result + ((time == null) ? 0 : time.hashCode());
            hash = result;
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Metric other = (Metric) obj;
        // 如果有 hash，优先比较
        if (hash != 0 && other.hash != 0 && hash != other.hash)
            return false;
        if (!StringUtils.equals(this.metricId, other.metricId))
            return false;
        if (!Arrays.equals(prefixes, other.prefixes))
            return false;
        if (!Arrays.equals(suffixes, other.suffixes))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Metric{" +
                "metricId='" + metricId + '\'' +
                ", prefixes=" + Arrays.toString(prefixes) +
                ", time='" + time + '\'' +
                ", suffixes=" + Arrays.toString(suffixes) +
                ", hash=" + hash +
                '}';
    }
}
