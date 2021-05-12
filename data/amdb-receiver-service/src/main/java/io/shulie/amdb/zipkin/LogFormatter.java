package io.shulie.amdb.zipkin;

import com.pamirs.pradar.log.parser.trace.RpcBased;
import io.shulie.surge.data.deploy.pradar.link.model.TTrackClickhouseModel;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.util.PropertyPlaceholderHelper;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

/**
 * @author vincent
 */
public class LogFormatter {
    public static final Charset DEFAULT_CHARSET = Charset.forName("GB18030");
    public static final String LOG_FORMATTER_V15 = "${traceId}|${startTime}|${agentId}|${entranceNodeId}|null|${rpcId}|${logType}|${rpcType}|${appName}|${traceAppName}|${upAppName}|${cost}|${middlewareName}|${serviceName}|${methodName}|${remoteIp}|${port}|${resultCode}|${requestSize}|${responseSize}|${request}|${response}|${clusterTest}|${callBackMsg}|!0.2|@s@1127585c@|@";

    /**
     *
     * @param tTrackClickhouseModel
     * @return
     */
    public String format(TTrackClickhouseModel tTrackClickhouseModel) {
        PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");
        BeanMap beanMap = new BeanMap(tTrackClickhouseModel);

        String logFormat = LOG_FORMATTER_V15;
        switch (tTrackClickhouseModel.getVersion()) {
            case "1.5":
            case "15":
                logFormat = LOG_FORMATTER_V15;
        }
        return propertyPlaceholderHelper.replacePlaceholders(logFormat, new PropertyPlaceholderHelper.PlaceholderResolver() {
            /**
             * Resolves the supplied placeholder name into the replacement value.
             *
             * @param placeholderName the name of the placeholder to resolve.
             * @return the replacement value or {@code null} if no replacement is to be made.
             */
            @Override
            public String resolvePlaceholder(String placeholderName) {
                return beanMap.get(placeholderName) == null ? "" : String.valueOf(beanMap.get(placeholderName));
            }
        });
    }


    /**
     *
     * @param rpcBased
     * @return
     */
    public String format(RpcBased rpcBased) {
        PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");
        BeanMap beanMap = new BeanMap(rpcBased);

        String logFormat = LOG_FORMATTER_V15;
        switch (rpcBased.getVersion()) {
            case "1.5":
            case "15":
                logFormat = LOG_FORMATTER_V15;
        }
        return propertyPlaceholderHelper.replacePlaceholders(logFormat, new PropertyPlaceholderHelper.PlaceholderResolver() {
            /**
             * Resolves the supplied placeholder name into the replacement value.
             *
             * @param placeholderName the name of the placeholder to resolve.
             * @return the replacement value or {@code null} if no replacement is to be made.
             */
            @Override
            public String resolvePlaceholder(String placeholderName) {
                return beanMap.get(placeholderName) == null ? "" : String.valueOf(beanMap.get(placeholderName));
            }
        });
    }
}
