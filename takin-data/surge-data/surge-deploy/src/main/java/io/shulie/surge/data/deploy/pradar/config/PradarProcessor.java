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

package io.shulie.surge.data.deploy.pradar.config;


import io.shulie.surge.data.deploy.pradar.parser.PradarPaserFactory;
import io.shulie.surge.data.runtime.digest.DigestContext;
import io.shulie.surge.data.runtime.parser.DataParser;
import io.shulie.surge.data.runtime.processor.DefaultProcessor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * pradar 默认执行器
 */
public class PradarProcessor extends DefaultProcessor<String, DigestContext> {
    private static final long MAX_LOG_PROCESS_TIME_BEFORE = TimeUnit.HOURS.toMillis(1);


    public static final String DATA_TYPE = "dataType";

    /**
     * 获取解析器
     *
     * @param header
     * @return
     */
    @Override
    protected DataParser<String, DigestContext> getDataParser(Map<String, Object> header) {
        Byte dataType = (Byte) header.get(DATA_TYPE);
        return PradarPaserFactory.getParser(dataType);
    }


    /**
     * 移除延迟数据
     *
     * @param eventTime
     * @return
     */
    @Override
    public boolean removeDelay(long eventTime) {
//        return false;
        return System.currentTimeMillis() - eventTime > MAX_LOG_PROCESS_TIME_BEFORE;
    }
}
