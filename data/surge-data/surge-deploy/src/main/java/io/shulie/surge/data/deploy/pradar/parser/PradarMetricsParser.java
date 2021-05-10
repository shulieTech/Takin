package io.shulie.surge.data.deploy.pradar.parser;

import com.pamirs.pradar.log.parser.ProtocolParserFactory;
import com.pamirs.pradar.log.parser.metrics.MetricsBased;
import io.shulie.surge.data.runtime.digest.DigestContext;
import io.shulie.surge.data.runtime.parser.DataParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author vincent
 */
public class PradarMetricsParser implements DataParser<String, MetricsBased> {

    private static final Logger logger = LoggerFactory.getLogger(PradarMetricsParser.class);

    private static final String HEADER_DATA_VERSION = "dataVersion";
    private static final String HEADER_HOST_IP = "hostIp";

    /**
     * 创建数据处理上下文
     *
     * @param header
     * @param content
     * @return
     */
    @Override
    public DigestContext<MetricsBased> createContext(Map<String, Object> header, String content) {
        String dataVersion = String.valueOf(header.getOrDefault(HEADER_DATA_VERSION, "1.0"));
        String hostIp = String.valueOf(header.getOrDefault(HEADER_HOST_IP, ""));
        content += '|' + hostIp + '|' + dataVersion;
        MetricsBased metricsBased = ProtocolParserFactory.getFactory().getMetricsProtocolParser(dataVersion).parse(hostIp, dataVersion, content);
        if (metricsBased == null) {
            logger.warn("未解析到日志信息->" + content);
            return null;
        }
        long now = System.currentTimeMillis();
        metricsBased.setLog(content);

        DigestContext<MetricsBased> context = new DigestContext<MetricsBased>();
        context.setProcessTime(now);
        context.setContent(metricsBased);
        context.setHeader(header);
        context.setEventTime(metricsBased.getLogTime());
        return context;
    }

}
