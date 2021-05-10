package io.shulie.surge.data.deploy.pradar.parser;

import com.pamirs.pradar.log.parser.ProtocolParserFactory;
import com.pamirs.pradar.log.parser.monitor.MonitorBased;
import io.shulie.surge.data.runtime.digest.DigestContext;
import io.shulie.surge.data.runtime.parser.DataParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 *
 */
public class PradarBaseParser implements DataParser<String, MonitorBased> {

    private static final Logger logger = LoggerFactory.getLogger(PradarBaseParser.class);

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
    public DigestContext<MonitorBased> createContext(Map<String, Object> header, String content) {
        String dataVersion = String.valueOf(header.getOrDefault(HEADER_DATA_VERSION, "1.0"));
        String hostIp = String.valueOf(header.getOrDefault(HEADER_HOST_IP, ""));
        content += '|' + hostIp + '|' + dataVersion;
        MonitorBased monitorBased = ProtocolParserFactory.getFactory().getMonitorProtocolParser(dataVersion).parse(hostIp, dataVersion, content);
        if (monitorBased == null) {
            logger.warn("未解析到日志信息->" + content);
            return null;
        }
        long now = System.currentTimeMillis();
        monitorBased.setLog(content);

        DigestContext<MonitorBased> context = new DigestContext<MonitorBased>();
        context.setProcessTime(now);
        context.setContent(monitorBased);
        context.setHeader(header);
        context.setEventTime(monitorBased.getLogTime() * 1000);
        return context;
    }

}
