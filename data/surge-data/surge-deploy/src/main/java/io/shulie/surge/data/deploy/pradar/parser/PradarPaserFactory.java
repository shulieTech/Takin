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
