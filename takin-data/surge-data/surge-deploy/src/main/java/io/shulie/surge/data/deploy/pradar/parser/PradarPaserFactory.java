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

package io.shulie.surge.data.deploy.pradar.parser;

import com.google.common.collect.Maps;
import com.pamirs.pradar.log.parser.DataType;
import io.shulie.surge.data.runtime.digest.DigestContext;
import io.shulie.surge.data.runtime.parser.DataParser;

import java.util.Map;

/**
 * 日志解析工厂类
 * @author vincent
 */
public class PradarPaserFactory {

    static PradarPaserFactory INSTANCE = new PradarPaserFactory();

    Map<Byte, DataParser> dataParserMap = Maps.newHashMap();

    PradarPaserFactory() {
        dataParserMap.put(DataType.TRACE_LOG, new PradarLogParser());
        dataParserMap.put(DataType.METRICS_LOG, new PradarMetricsParser());
        dataParserMap.put(DataType.MONITOR_LOG, new PradarBaseParser());
    }

    public static DataParser<String, DigestContext> getParser(Byte dataType) {
        return INSTANCE.dataParserMap.get(dataType);
    }
}
