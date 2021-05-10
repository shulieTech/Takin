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

import com.pamirs.pradar.PradarCoreUtils;
import com.pamirs.pradar.PradarService;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/8 3:52 下午
 */
public final class PradarServletUtils {
    /**
     * 判断是否是压测流量
     *
     * @param servletRequest
     * @return
     */
    public static boolean isClusterTestRequest(HttpServletRequest servletRequest) {
        String value = getProperty(servletRequest, PradarService.PRADAR_CLUSTER_TEST_KEY);
        if (StringUtils.isBlank(value)) {
            value = getProperty(servletRequest, PradarService.PRADAR_HTTP_CLUSTER_TEST_KEY);
        }
        return ClusterTestUtils.isClusterTestRequest(value);
    }

    /**
     * 判断是否是压测流量
     *
     * @param servletRequest
     * @return
     */
    public static boolean isDebugRequest(HttpServletRequest servletRequest) {
        String value = getProperty(servletRequest, PradarService.PRADAR_DEBUG_KEY);
        return ClusterTestUtils.isClusterTestRequest(value);
    }

    /**
     * 从request中获取指定的key
     *
     * @param request
     * @param keys
     * @return
     */
    public static String getProperty(HttpServletRequest request, String... keys) {
        if (request == null) {
            return null;
        }

        for (String key : keys) {
            String value = PradarCoreUtils.trim(request.getHeader(key));
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }

        return null;
    }

}
