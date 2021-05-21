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

import com.pamirs.pradar.*;
import com.pamirs.pradar.common.HttpUtils;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.pamirs.pradar.pressurement.agent.shared.domain.DoorPlank;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.pressurement.agent.shared.service.impl.ArbiterEntrance;
import com.pamirs.pradar.pressurement.base.util.PropertyUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class RequestTracer<REQ, RESP> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestTracer.class);

    /**
     * 获取 IP的头信息
     */
    public static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP", // 优先获取其他代理设置的真实用户ip
            "X-Real-IP",          // enginx 设置 remoteIP，如果没有拿到 NS-Client-IP，那么这就是真实的用户 ip
            "NS-Client-IP",       // NAT 方式设置的ip
    };

    private static final String FAST_DEBUG_TRACE_ID_UPLOAD = "/api/fast/debug/trace/id/push";

    /**
     * 检查 ip 是否是有效 ip
     *
     * @param ip ip
     * @return true|false
     */
    private final boolean checkIP(String ip) {
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取 header 头
     *
     * @param request
     * @return
     */
    public abstract String getHeader(REQ request, String key);

    /**
     * 获取远程地址
     *
     * @param request
     * @return
     */
    public abstract String getRemoteAddr(REQ request);

    /**
     * 返回远程请求端口
     *
     * @param request
     * @return
     */
    public abstract String getRemotePort(REQ request);

    /**
     * 获取请求 url
     *
     * @param request
     * @return
     */
    public abstract String getRequestURI(REQ request);

    /**
     * 获取请求方法
     *
     * @param request
     * @return
     */
    public abstract String getMethod(REQ request);

    /**
     * 设置 attribute
     *
     * @param request
     * @param key
     * @param value
     */
    public abstract void setAttribute(REQ request, String key, Object value);

    /**
     * 获取 attribute 值
     *
     * @param request
     * @param key
     * @return
     */
    public abstract Object getAttribute(REQ request, String key);

    /**
     * 获取请求大小
     *
     * @param request
     * @return
     */
    public abstract long getContentLength(REQ request);

    /**
     * 获取请求参数
     *
     * @param request
     * @return
     */
    public abstract String getParams(REQ request);

    /**
     * 获取响应内容
     *
     * @param response
     * @return
     */
    public abstract String getResponse(RESP response);

    /**
     * 获取响应编码
     *
     * @param response
     * @return
     */
    public abstract String getStatusCode(RESP response, Throwable throwable);

    /**
     * 获取远程客户端的 IP
     */
    public final String getRemoteAddress(REQ request) {
        String ip = null;
        boolean valid = false;

        for (String header : IP_HEADERS) {
            ip = getHeader(request, header);
            valid = checkIP(ip);
            if (valid) {
                break;
            }
        }
        if (PradarCoreUtils.isBlank(ip) || !valid) {
            ip = getRemoteAddr(request);
        }

        if (ip == null) {
            ip = "127.0.0.1";
        }

        // 代理时会有逗号分隔的 ip，获取第一个即可
        int index = ip.indexOf(',');
        if (index != -1) {
            String firstIp = ip.substring(0, index).trim();
            if (checkIP(ip)) {
                ip = firstIp;
            }
        }
        return ip;
    }

    /**
     * 获取 TraceId。根据以下步骤： <ol> <li>从 ThreadLocal 获取</li> <li>从 URL 参数中 获取</li> <li>从 header 中
     * 获取</li> <li>如果上述都没有，则自动生成，如果 ip 不为 <code>null</code>，则基于指定的 ip， 否则，使用本机 ip </li> </ol>
     */
    public final String getTraceId(REQ request) {
        // 检查 header 中的调用链配置
        String traceId = PradarCoreUtils.trim(getHeader(request, PradarService.PRADAR_TRACE_ID_KEY));
        if (traceId != null && traceId.length() < 64) {
            return traceId;
        }
        return traceId;
    }


    /**
     * 获取debugId
     */
    public final String getFastDebugId(REQ request) {
        String debugId = PradarCoreUtils.trim(getHeader(request, PradarService.PRADAR_FAST_DEBUG_ID));
        return debugId;
    }

    /**
     * 是否需要过滤该请求
     *
     * @param request
     * @return
     */
    private boolean isNeedFilter(REQ request) {
        String checkTest = getRequestURI(request);
        return StaticFileFilter.needFilter(checkTest);
    }

    /**
     * 判断是否是压测流量
     *
     * @param request
     * @return
     */
    public boolean isClusterTestRequest(REQ request) {
        String value = getProperty(request, PradarService.PRADAR_HTTP_CLUSTER_TEST_KEY);
        if (StringUtils.endsWith(value, Pradar.PRADAR_CLUSTER_TEST_HTTP_USER_AGENT_SUFFIX)) {
            return true;
        }
        value = getProperty(request, PradarService.PRADAR_CLUSTER_TEST_KEY);
        return ClusterTestUtils.isClusterTestRequest(value);
    }

    /**
     * 判断是否是压测流量
     *
     * @param request
     * @return
     */
    public boolean isDebugRequest(REQ request) {
        String value = getProperty(request, PradarService.PRADAR_DEBUG_KEY);
        return ClusterTestUtils.isClusterTestRequest(value);
    }

    /**
     * 从request中获取指定的key
     *
     * @param request
     * @param keys
     * @return
     */
    public String getProperty(REQ request, String... keys) {
        if (request == null) {
            return null;
        }

        for (String key : keys) {
            String value = PradarCoreUtils.trim(getHeader(request, key));
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }

        return null;
    }

    /**
     * 开始调用链，注意，开始之后，不管后续处理是否正常，都需要调用。
     */
    public final void startTrace(REQ request, RESP response, String pluginName) {
        if (isNeedFilter(request)) {
            return;
        }

        String ip = null;
        if (!PradarSwitcher.USE_LOCAL_IP) {
            ip = getRemoteAddress(request);
        }
        String traceId = getTraceId(request);
        boolean isTraceIdBlank = false;
        if (StringUtils.isBlank(traceId)) {
            traceId = TraceIdGenerator.generate(ip);
            isTraceIdBlank = true;
        }

        String url = getRequestURI(request);

        boolean isClusterTestRequest = isClusterTestRequest(request);
        boolean isDebug = isDebugRequest(request);


        ClusterTestUtils.validateClusterTest(isClusterTestRequest);

        if (isDebug && isTraceIdBlank) {
            setResponseHeader(response, "traceId", traceId);
            pushTraceId(traceId, request);
        }

        if (isClusterTestRequest) {
            final DoorPlank arbiterDp = ArbiterEntrance.shallWePassHttp();
            if (Pradar.DOOR_CLOSED.equals(arbiterDp.getStatus())) {
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0008")
                        .setMessage("压测开关关闭")
                        .setDetail("data receiver filter is close!")
                        .printFastDebugLog();
                throw new PressureMeasureError("data receiver filter is close! " + arbiterDp, isClusterTestRequest);
            }
        }

        if (!isTraceIdBlank) {
            Map<String, String> context = new HashMap<String, String>();
            for (String key : Pradar.getInvokeContextTransformKeys()) {
                String value = getProperty(request, key);
                if (value != null) {
                    context.put(key, value);
                }
            }

            context.put(PradarService.PRADAR_TRACE_ID_KEY, traceId);
            context.put(PradarService.PRADAR_CLUSTER_TEST_KEY, String.valueOf(isClusterTestRequest));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("accept web server rpcServerRecv context:{}, currentContext:{} currentMiddleware:{}", context, Pradar.getInvokeContextMap(), Pradar.getMiddlewareName());
            }
            Pradar.startServerInvoke(url, StringUtils.upperCase(getMethod(request)), context);
            setAttribute(request, "isTrace", false);
        } else {
            Pradar.clearInvokeContext();
            Pradar.setDebug(isDebug);
            Pradar.startTrace(traceId, url, StringUtils.upperCase(getMethod(request)));
            Pradar.setClusterTest(isClusterTestRequest);
            Pradar.setDebug(isDebug);
            setAttribute(request, "isTrace", true);
        }

        final long contentLength = getContentLength(request);
        Pradar.requestSize(contentLength < 0 ? 0 : contentLength);
        String remoteIp = Pradar.getRemoteIp();
        if (StringUtils.isBlank(remoteIp) || "127.0.0.1".equals(remoteIp)) {
            remoteIp = getRemoteAddress(request);
        }
        if (StringUtils.isNotBlank(remoteIp)) {
            Pradar.remoteIp(remoteIp);
        }
        String port = getRemotePort(request);
        if (port != null) {
            Pradar.remotePort(port);
        }
        Pradar.middlewareName(pluginName);
    }

    /**
     * 推送TraceId到控制台
     *
     * @param traceId traceId
     * @param request 请求对象
     */
    protected void pushTraceId(String traceId, REQ request) {
        String fastDebugId = getFastDebugId(request);
        if (traceId == null || fastDebugId == null) {
            return;
        }
        String body = traceId.concat(",").concat(fastDebugId);
        HttpUtils.HttpResult httpResult = HttpUtils.doPost(PropertyUtil.getTroControlWebUrl() + FAST_DEBUG_TRACE_ID_UPLOAD, body);
        LOGGER.info("fast debug push trace id:{}, response status:{}", traceId, httpResult.getStatus());
    }

    /**
     * 设置 响应头部
     *
     * @param resp
     * @param key
     * @param value
     */
    public abstract void setResponseHeader(RESP resp, String key, Object value);

    /**
     * 结束 trace
     *
     * @param request    请求
     * @param response   响应
     * @param throwable  异常
     * @param resultCode 结果编码
     */
    public final void endTrace(REQ request, RESP response, Throwable throwable, String resultCode) {
        if (isNeedFilter(request)) {
            return;
        }

        if (Pradar.isDebug()) {
            setResponseHeader(response, "traceId", Pradar.getTraceId());
        }

        Object value = getAttribute(request, "isTrace");
        boolean isTrace = true;
        if (value != null && (value instanceof Boolean)) {
            isTrace = (Boolean) value;
        }

        /**
         * 这个放在后面是防止我们获取参数时使用默认编码导致在业务中使用自定义编码时会出现乱码
         * 但是这可能会导致我们获取到的参数是乱码
         */
        if (Pradar.isRequestOn()) {
            String params = getParams(request);
            Pradar.request(params);
        }

        if (Pradar.isResponseOn()) {
            String result = getResponse(response);
            if (throwable != null) {
                Pradar.response(throwable);
            } else {
                Pradar.response(result);
            }
        }

        if (isTrace) {
            if (throwable != null) {
                Pradar.endTrace(resultCode, MiddlewareType.TYPE_WEB_SERVER);
            } else {
                Pradar.endTrace(resultCode, MiddlewareType.TYPE_WEB_SERVER);
            }
        } else {
            if (throwable == null) {
                Pradar.endServerInvoke(resultCode, MiddlewareType.TYPE_WEB_SERVER);
            } else {
                Pradar.endServerInvoke(resultCode, MiddlewareType.TYPE_WEB_SERVER);
            }
        }
    }

    /**
     * 结束调用链。 注意：需要假定 response 已经提交，因此只能做只读操作，不能修改
     */
    public final void endTrace(REQ request, RESP response, Throwable throwable) {
        try {
            String resultCode = getStatusCode(response, throwable);
            endTrace(request, response, throwable, resultCode);
        } finally {
            Pradar.clearInvokeContext();
        }
    }

}
