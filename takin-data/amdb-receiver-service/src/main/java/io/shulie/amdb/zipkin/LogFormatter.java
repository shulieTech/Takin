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

package io.shulie.amdb.zipkin;

import com.pamirs.pradar.log.parser.trace.RpcBased;
import org.springframework.util.PropertyPlaceholderHelper;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import org.apache.commons.beanutils.BeanMap;

/**
 * @author vincent
 */
public class LogFormatter {
    public static final Charset DEFAULT_CHARSET = Charset.forName("GB18030");
    public static final String LOG_FORMATTER_V15 = "${traceId}|${startTime}|${agentId}|${entranceNodeId}|null|${rpcId}|${logType}|${rpcType}|${appName}|${traceAppName}|${upAppName}|${cost}|${middlewareName}|${serviceName}|${methodName}|${remoteIp}|${port}|${resultCode}|${requestSize}|${responseSize}|${request}|${response}|${clusterTest}|${callBackMsg}|!0.2|@s@1127585c@|@";

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
