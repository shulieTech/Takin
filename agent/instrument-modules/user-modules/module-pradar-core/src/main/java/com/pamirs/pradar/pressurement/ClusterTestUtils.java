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
package com.pamirs.pradar.pressurement;

import com.pamirs.pradar.*;
import com.pamirs.pradar.exception.PradarException;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.json.ResultSerializer;
import com.pamirs.pradar.pressurement.agent.shared.exit.ArbiterHttpExit;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.shulie.instrument.simulator.api.ThrowableUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author xiaobin.zfb
 * @since 2020/7/8 5:26 下午
 */
public final class ClusterTestUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClusterTestUtils.class);

    /**
     * 判断是否是压测流量
     *
     * @param value
     * @return
     */
    public final static boolean isClusterTestRequest(String value) {
        if (StringUtils.equals(Pradar.PRADAR_CLUSTER_TEST_ON, value)
                || StringUtils.equalsIgnoreCase(value, Boolean.TRUE.toString())) {
            return true;
        }
        return StringUtils.endsWith(value, Pradar.PRADAR_CLUSTER_TEST_HTTP_USER_AGENT_SUFFIX);
    }

    /**
     * 判断是否是 debug 流量
     *
     * @param value
     * @return
     */
    public final static boolean isDebugRequest(String value) {
        if (StringUtils.equals(Pradar.PRADAR_DEBUG_ON, value)
                || StringUtils.equalsIgnoreCase(value, Boolean.TRUE.toString())) {
            return true;
        }

        return false;
    }

    /**
     * 判断是否是压测流量
     *
     * @param context
     * @return
     */
    public final static boolean isClusterTestRequest(Map<String, String> context) {
        if (context == null) {
            return false;
        }
        return isClusterTestRequest(PradarService.PRADAR_CLUSTER_TEST_KEY);
    }

    /**
     * rpc的压测流量验证
     *
     * @param className
     */
    public final static void validateRpcClusterTest(String className) {
        validateClusterTest();
        try {
            /**
             * 如果是压测流量则需要验证 rpc 的白名单,不在白名单中则报错
             */
            if (Pradar.isClusterTest()) {
                boolean isPassed = ArbiterHttpExit.shallWePassRpc(className);

                if (!isPassed) {
                    throw new PressureMeasureError("WhiteListError: [" + AppNameUtils.appName() + "] no interface name found in WhiteList : " + className);
                }
            }
        } catch (PressureMeasureError e) {
            String message = "WhiteListError: [" + AppNameUtils.appName() + "] no interface name found in WhiteList : " + className;
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("whiteList-0001")
                        .setMessage(message + "|url:" + className)
                        .setDetail(ResultSerializer.serializeObject(e, 2))
                        .report();
                throw e;
            }
        } catch (Throwable e) {
            String message = "WhiteListError: [" + AppNameUtils.appName() + "] validateRpcClusterTest err!";
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("whiteList-0001")
                        .setMessage(message)
                        .setDetail(ResultSerializer.serializeObject(e, 2))
                        .report();
                throw new PressureMeasureError(message, e);
            }
        }
    }

    /**
     * rpc的压测流量验证, 使用类名+方法名的组合方式,如果方法为空则单独匹配 className
     * 会匹配白名单中匹配的 className 和 className#methodName 的名单
     *
     * @param className  类名
     * @param methodName 方法名称
     */
    public final static void validateRpcClusterTest(String className, String methodName) {
        validateClusterTest();
        try {
            /**
             * 如果是压测流量则需要验证 rpc 的白名单,不在白名单中则报错
             */
            if (Pradar.isClusterTest()) {
                boolean isPassed = ArbiterHttpExit.shallWePassRpc(className, methodName);

                if (!isPassed) {
                    throw new PressureMeasureError("WhiteListError: [" + AppNameUtils.appName() + "] no interface name found in WhiteList : " + className + "#" + methodName);
                }
            }
        } catch (PressureMeasureError e) {
            String message = "WhiteListError: [" + AppNameUtils.appName() + "] no interface name found in WhiteList : " + className + "#" + methodName;
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("whiteList-0001")
                        .setMessage(message + "|url:" + className + "#" + methodName)
                        .setDetail(ResultSerializer.serializeObject(e, 2))
                        .report();
                throw e;
            }
        } catch (Throwable e) {
            String message = "WhiteListError: [" + AppNameUtils.appName() + "] validateRpcClusterTest err! WhiteList: " + className + "#" + methodName;
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("whiteList-0001")
                        .setMessage(message + "|url:" + className + "#" + methodName)
                        .setDetail(ResultSerializer.serializeObject(e, 2))
                        .report();
                throw new PressureMeasureError(message, e);
            }
        }
    }

    /**
     * 验证压测流量的有效性,如果是压测流量但是压测未开启则直接报错
     * 百世XingNg的rpc url
     */
    public final static void validateXingNgRpcClusterTest(String url) {
        validateClusterTest();
        try {
            if (Pradar.isClusterTest()) {
                boolean arbiterAllowRequest = ArbiterHttpExit.shallWePassHttpString(url);
                if (!arbiterAllowRequest) {
                    throw new PressureMeasureError(" WhiteListError: url [" + url + "] is not allowed in WhiteList.");
                }
            }
        } catch (PradarException e) {
            String message = "WhiteListError: {}";
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("whiteList-0001")
                        .setMessage(message)
                        .setDetail(ResultSerializer.serializeObject(e, 2))
                        .report();
                throw e;
            }
        } catch (PressureMeasureError e) {
            String message = "WhiteListError:  url [" + url + "] is not allowed in WhiteList.";
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("whiteList-0001")
                        .setMessage(message + "|url:" + url)
                        .setDetail(ResultSerializer.serializeObject(e, 2))
                        .report();
                throw e;
            }
        } catch (Throwable e) {
            String message = "WhiteListError: [" + AppNameUtils.appName() + "] validateRpcClusterTest err!";
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("whiteList-0001")
                        .setMessage(message)
                        .setDetail(ResultSerializer.serializeObject(e, 2))
                        .report();
                throw new PressureMeasureError(e);
            }
        }
    }

    private static void reportFastDebugErrorInfo(String code, Throwable e, String message) {
        if (Pradar.isDebug()) {
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.AgentError)
                    .setErrorCode(code)
                    .setMessage(message)
                    .setDetail(ThrowableUtils.toString(e))
                    .printFastDebugLog();
        }
    }

    /**
     * 验证压测流量的有效性,如果是压测流量但是压测未开启则直接报错
     */
    public final static void validateHttpClusterTest(String url) {
        validateClusterTest();
        try {
            if (Pradar.isClusterTest()) {
                boolean arbiterAllowRequest = ArbiterHttpExit.shallWePassHttpString(url);
                if (!arbiterAllowRequest) {
                    throw new PressureMeasureError("WhiteListError: [" + AppNameUtils.appName() + "] url [" + url + "] is not allowed in WhiteList.");
                }
            }
        } catch (PressureMeasureError e) {
            String message = "WhiteListError: [" + AppNameUtils.appName() + "] url [" + url + "] is not allowed in WhiteList.";
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("whiteList-0001")
                        .setMessage(message + "|url:" + url)
                        .setDetail(ResultSerializer.serializeObject(e, 2))
                        .report();
                throw e;
            }
        } catch (Throwable e) {
            String message = "WhiteListError: [" + AppNameUtils.appName() + "] url [" + url + "] is not allowed in WhiteList.";
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("whiteList-0001")
                        .setMessage(message + "|url:" + url)
                        .setDetail(ResultSerializer.serializeObject(e, 2))
                        .report();
                throw new PressureMeasureError(message, e);
            }
        }
    }

    /**
     * 验证压测流量的有效性,如果是压测流量但是压测未开启则直接报错
     */
    public final static void validateClusterTest(boolean isClusterTestRequest) {
        try {
            if (Pradar.isDebug()) {
                return;
            }
            if (!PradarSwitcher.isClusterTestEnabled() && isClusterTestRequest) {
                throw new PressureMeasureError("ClusterTestSwitcherError: [" + AppNameUtils.appName() + "] " + PradarSwitcher.PRADAR_SWITCHER_OFF, isClusterTestRequest);
            }
        } catch (PressureMeasureError e) {
            String message = "ClusterTestSwitcherError: [" + AppNameUtils.appName() + "] validateClusterTest err!";
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                reportFastDebugErrorInfo("agent-0008", e, message);
                throw e;
            }
        } catch (Throwable e) {
            String message = "ClusterTestSwitcherError: [" + AppNameUtils.appName() + "] validateClusterTest err!";
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                reportFastDebugErrorInfo("agent-0008", e, message);
                throw new PressureMeasureError(message, e);
            }
        }
    }

    /**
     * 验证压测流量的有效性,如果是压测流量但是压测未开启则直接报错
     */
    public final static void validateClusterTest() {
        try {
            if (Pradar.isDebug()) {
                return;
            }
            if (!PradarSwitcher.isClusterTestEnabled() && Pradar.isClusterTest()) {
                throw new PressureMeasureError("ClusterTestSwitcherError: [" + AppNameUtils.appName() + "] " + PradarSwitcher.PRADAR_SWITCHER_OFF);
            }
        } catch (PressureMeasureError e) {
            String message = "ClusterTestSwitcherError: [" + AppNameUtils.appName() + "] validateClusterTest err!";
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                reportFastDebugErrorInfo("agent-0008", e, message);
                throw e;
            }
        } catch (Throwable e) {
            String message = "ClusterTestSwitcherError: [" + AppNameUtils.appName() + "] validateClusterTest err!";
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                reportFastDebugErrorInfo("agent-0008", e, message);
                throw new PressureMeasureError(message, e);
            }
        }
    }

    /**
     * 校验压测流量
     *
     * @param context
     */
    public final static void validateClusterTest(Map<String, String> context) {
        try {
            if (Pradar.isDebug()) {
                return;
            }
            boolean isClusterTestRequest = isClusterTestRequest(context);
            if (isClusterTestRequest && !PradarSwitcher.isClusterTestEnabled()) {
                throw new PressureMeasureError("ClusterTestSwitcherError: [" + AppNameUtils.appName() + "] pradar switch is closed ,pressure request rejected !");
            }
        } catch (PressureMeasureError e) {
            String message = "ClusterTestSwitcherError: [" + AppNameUtils.appName() + "] pradar switch is closed ,pressure request rejected !";
            LOGGER.error(message, e);
            if (Pradar.isClusterTest()) {
                reportFastDebugErrorInfo("agent-0008", e, message);
                throw e;
            }
        } catch (Throwable t) {
            String message = "ClusterTestSwitcherError: [" + AppNameUtils.appName() + "] pradar switch is closed ,pressure request rejected !";
            LOGGER.error(message, t);
            if (Pradar.isClusterTest()) {
                reportFastDebugErrorInfo("agent-0008", t, message);
                throw new PressureMeasureError(message, t);
            }
        }
    }
}
