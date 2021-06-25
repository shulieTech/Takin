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

import io.shulie.surge.data.common.aggregation.AggregatableRecord;
import io.shulie.surge.data.common.aggregation.TimestampSupport;
import io.shulie.surge.data.common.utils.Bytes;
import io.shulie.surge.data.common.utils.FormatUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;

/**
 * 实时调用统计
 *
 * @author pamirs
 */
public class CallStat implements AggregatableRecord<CallStat>, TimestampSupport {

    private static final long serialVersionUID = -2763638750561198315L;
    private static final long[] EMPTY_ARRAY = new long[0];

    private transient long timestamp;
    private long[] values;
    private String traceId;

    public CallStat() {
        this("", EMPTY_ARRAY);
    }

    public CallStat(long... values) {
        this.values = values;
    }

    public CallStat(String traceId, long... values) {
        this.traceId = traceId;
        this.values = values;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long get(int pos) {
        return pos >= values.length ? 0 : values[pos];
    }

    public void set(int pos, long value) {
        if (pos < values.length) {
            values[pos] = value;
        }
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void reset() {
        for (int i = 0; i < values.length; i++) {
            values[i] = 0;
        }
    }

    public int length() {
        return values.length;
    }

    @Override
    public void aggregateFrom(CallStat other) {
        final long[] values2 = other.values;
        final int len = values2.length;
        if (len != this.values.length) {
            if (this.values.length == 0) {
                // 刚初始化出来
                this.values = Arrays.copyOf(values2, len);
                this.traceId = other.traceId;
            }
            return;
        }

        //选取耗时最长的
        if (values2.length > 3 && other.values.length > 3) {
            if (values[2] > 0 && values2[2] > 0) {
                if (StringUtils.isBlank(traceId)) {
                    traceId = other.traceId;
                }
            } else if (values[1] < values2[1]) {
                this.traceId = other.traceId;
            }
        }
        if (len <= 8) {
            switch (len) {
                case 8:
                    values[7] += values2[7];
                case 7:
                    values[6] += values2[6];
                case 6:
                    values[5] += values2[5];
                case 5:
                    values[4] += values2[4];
                case 4:
                    values[3] += values2[3];
                case 3:
                    values[2] += values2[2];
                case 2:
                    values[1] += values2[1];
                case 1:
                    values[0] += values2[0];
                case 0:
            }
        } else {
            for (int i = 0; i < len; ++i) {
                values[i] += values2[i];
            }
        }
    }

    @Override
    public byte[] toBytes() throws IOException {
        return Bytes.toBytes(FormatUtils.join(values, "|") + '|' + traceId);
    }

    @Override
    public void fromBytes(byte[] bytes) throws IOException {
        String str = Bytes.toString(bytes);
        String[] splits = StringUtils.split(str, '|');
        final int len = splits.length - 1;
        long[] values = new long[len];
        for (int i = 0; i < len; ++i) {
            values[i] = Long.parseLong(splits[i]);
        }
        this.values = values;
        this.traceId = splits[splits.length - 1];
    }

    @Override
    public String toString() {
        return "{" + FormatUtils.toSecondTimeString(timestamp) + ": " + FormatUtils.join(values, ",") + ',' + traceId + "}";
    }
}
