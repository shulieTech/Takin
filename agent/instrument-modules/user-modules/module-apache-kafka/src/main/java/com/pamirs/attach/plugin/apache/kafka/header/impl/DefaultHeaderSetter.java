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
package com.pamirs.attach.plugin.apache.kafka.header.impl;

import com.pamirs.attach.plugin.apache.kafka.header.HeaderSetter;
import com.pamirs.pradar.Pradar;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;

import java.util.Map;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/10 7:54 下午
 */
public class DefaultHeaderSetter implements HeaderSetter {
    @Override
    public void setHeader(ProducerRecord producerRecord) {
        Map<String, String> ctx = Pradar.getInvokeContextTransformMap();
        Headers headers = producerRecord.headers();
        for (Map.Entry<String, String> entry : ctx.entrySet()) {
            if (entry == null) {
                continue;
            }
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            headers.add(new RecordHeader(entry.getKey(), entry.getValue().getBytes()));
        }
    }
}
