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
package com.pamirs.attach.plugin.apache.dubbo.interceptor;

import com.pamirs.pradar.ResultCode;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

/**
 * @author vincent
 * @version v0.1 2016/12/27 10:51
 */
class DubboUtils {

    public static final String PLUGIN_NAME = "apache-dubbo";

    static boolean isLocalHost(String ip) {
        return "127.0.0.1".equals(ip) || "localhost".equalsIgnoreCase(ip);
    }

    static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    static Object getRequest(Invocation invocation) {
        Object[] requestParams = invocation.getArguments();
        if (requestParams != null && requestParams.length == 1) {
            return requestParams[0];
        }
        return requestParams;
    }

    static Object getResponse(Result result) {
        if (result == null) {
            return null;
        }
        if (result.hasException()) {
            return result.getException();
        }
        return result.getValue();
    }

    static long getResponseSize(Result result) {
        return 0;
    }

    static long getRequestSize() {
        return 0;
    }


    static String getResultCode(Throwable e) {
        if (e == null) {
            return ResultCode.INVOKE_RESULT_SUCCESS;
        }
        if (e instanceof com.alibaba.dubbo.rpc.RpcException) {
            com.alibaba.dubbo.rpc.RpcException rpcException = (com.alibaba.dubbo.rpc.RpcException) e;
            if (rpcException.getCode() == com.alibaba.dubbo.rpc.RpcException.NETWORK_EXCEPTION
                    || rpcException.getCode() == com.alibaba.dubbo.rpc.RpcException.TIMEOUT_EXCEPTION) {
                return ResultCode.INVOKE_RESULT_TIMEOUT;
            } else if (rpcException.getCode() == RpcException.BIZ_EXCEPTION) {
                return ResultCode.INVOKE_RESULT_DUBBO_ERR;
            } else {
                return ResultCode.INVOKE_RESULT_FAILED;
            }
        }
        if (e instanceof RemotingException) {
            return ResultCode.INVOKE_RESULT_TIMEOUT;
        }
        return ResultCode.INVOKE_RESULT_FAILED;
    }

    static String getParameterTypesString(Class<?>[] classes) {
        if (classes == null || classes.length == 0) {
            return "()";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        for (Class<?> clazz : classes) {
            if (clazz == null) {
                continue;
            }
            builder.append(clazz.getSimpleName()).append(',');
        }
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append(')');
        return builder.toString();
    }
}
