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
package com.shulie.instrument.simulator.core.util;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/27 4:21 下午
 */
public class HttpResponseHeaderParser {
    public final static String CONTENT_LENGTH = "Content-Length";
    public final static String CONTENT_TYPE = "Content-Type";
    public final static String ACCEPT_RANGES = "Accept-Ranges";

    private Map<String, String> headerMap;

    public HttpResponseHeaderParser() {
        this.headerMap = new HashMap<String, String>();
    }

    public void addResponseHeaderLine(String line) {
        if (line.contains(": ")) {
            String[] kv = line.split(": ");
            if (StringUtils.equalsIgnoreCase(StringUtils.trim(kv[0]), CONTENT_LENGTH)) {
                headerMap.put(CONTENT_LENGTH, StringUtils.trim(kv[1]));
            } else if (StringUtils.equalsIgnoreCase(StringUtils.trim(kv[0]), CONTENT_TYPE)) {
                headerMap.put(CONTENT_TYPE, StringUtils.trim(kv[1]));
            } else {
                headerMap.put(StringUtils.trim(kv[0]), StringUtils.trim(kv[1]));
            }
        }
    }

    public int getFileLength() {
        if (headerMap.get(CONTENT_LENGTH) == null) {
            return 0;
        }
        return Integer.parseInt(headerMap.get(CONTENT_LENGTH));
    }

    public String getFileType() {
        return headerMap.get(CONTENT_TYPE);
    }

    public Map<String, String> getHeaders() {
        return headerMap;
    }
}
