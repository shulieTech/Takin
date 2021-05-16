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


import com.google.common.collect.Lists;
import io.shulie.surge.data.common.aggregation.AggregatableRecord;
import io.shulie.surge.data.common.aggregation.TimestampSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 实时调用统计
 *
 * @author pamirs
 */
public class CallFlowMetrics implements AggregatableRecord<CallFlowMetrics>, TimestampSupport {
    private static final long serialVersionUID = -7825832597006300781L;
    private transient long timestamp;
    private List<String> values;

    public CallFlowMetrics() {
        this(System.currentTimeMillis(), null);
    }

    public CallFlowMetrics(long timestamp, String... values) {
        if (values == null) {
            this.values = Lists.newArrayList();
        } else {
            this.values = new ArrayList<>(Arrays.asList(values));
        }
        this.timestamp = timestamp;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public void reset() {
        for (int i = 0; i < values.size(); i++) {
            values.clear();
        }
    }

    public int length() {
        return values.size();
    }

    @Override
    public void aggregateFrom(CallFlowMetrics other) {
        if (other != null) {
            this.values.addAll(other.values);
            this.timestamp = other.getTimestamp();
        }
    }

    @Override
    public byte[] toBytes() throws IOException {
        return "".getBytes();
    }

    @Override
    public void fromBytes(byte[] bytes) throws IOException {

    }

    @Override
    public String toString() {
        return "CallFlowMetrics{" +
                "timestamp=" + timestamp +
                ", values=" + values +
                '}';
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
