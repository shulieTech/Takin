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
package com.pamirs.attach.plugin.common.web;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * filter 内部拦截，增对url-pattern方式不能满足的一个补偿
 *
 * @author: wangxian<tangdahu @ pamirs.top>
 * @date:2016/10/10
 */
public class StaticFileFilter {
    public static final boolean USE_LOCAL_IP = Boolean.valueOf(System.getProperty("pradar.localip.use", "false")).booleanValue();
    public static final String PRADAR_FILTER = "Pradar-Internal-Filter";//过滤内部请求

    /**
     * default ignore exts.
     */
    private static Set<String> defaultIgnoreExts = new HashSet<String>(Arrays.asList(".css", ".js", ".jpg", ".jpeg", ".png", ".gif", ".ico", ".bmp", ".pic",
            ".txt", ".csv", ".xls", ".xlsx", ".doc", ".docx", ".ppt", ".pptx", ".exe", ".com", ".dll", ".so", ".bat", ".sh", ".wof", ".woff"));

    public static boolean needFilter(String requestUri) {

        String exName = getSuffix(requestUri);
        if (exName == null) {
            return false;
        }
        if (defaultIgnoreExts.contains(exName.toLowerCase())) {
            return true;
        }
        return false;
    }

    public static String getSuffix(String urlPath) {
        if (urlPath == null) {
            return null;
        }
        String fileSuffix = urlPath.toLowerCase();
        if (fileSuffix.indexOf('/') != -1) {
            fileSuffix = fileSuffix.substring(fileSuffix.lastIndexOf('/') + 1);
        }
        if (fileSuffix.indexOf('?') != -1) {
            fileSuffix = fileSuffix.substring(0, fileSuffix.indexOf('?'));
        }
        if (fileSuffix.indexOf(';') != -1) {
            fileSuffix = fileSuffix.substring(0, fileSuffix.indexOf(';'));
        }
        if (fileSuffix.indexOf('.') == -1) {
            return null;
        }
        return fileSuffix.substring(fileSuffix.lastIndexOf('.'));
    }

}
