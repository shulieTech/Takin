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
