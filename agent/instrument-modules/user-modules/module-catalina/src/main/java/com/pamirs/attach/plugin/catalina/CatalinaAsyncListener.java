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

package com.pamirs.attach.plugin.catalina;


import com.pamirs.attach.plugin.common.web.RequestTracer;
import com.pamirs.attach.plugin.common.web.ServletRequestTracer;
import com.pamirs.pradar.Pradar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class CatalinaAsyncListener implements AsyncListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();
    private final boolean isInfo = logger.isInfoEnabled();
    private final AsyncContext asyncContext;
    private final Map<String, String> invokeContext_;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final RequestTracer requestTracer;


    public CatalinaAsyncListener(final AsyncContext asyncContext, final Map<String, String> invokeContext_, final HttpServletRequest request, final HttpServletResponse response) {
        this.asyncContext = asyncContext;
        this.invokeContext_ = invokeContext_;
        this.request = request;
        this.response = response;
        this.requestTracer = new ServletRequestTracer();
    }

    @Override
    public void onComplete(AsyncEvent asyncEvent) throws IOException {
        if (isDebug) {
            logger.debug("Complete asynchronous operation. event={}", asyncEvent);
        }

        if (asyncEvent == null) {
            if (isInfo) {
                logger.info("Invalid event. event is null");
            }
            return;
        }

        try {
            HttpServletResponse response = null;
            if (asyncEvent.getSuppliedResponse() instanceof HttpServletResponse) {
                response = ((HttpServletResponse) asyncEvent.getSuppliedResponse());
            } else {
                response = this.response;
            }
            Pradar.setInvokeContext(invokeContext_);
            requestTracer.endTrace(request, response, null, String.valueOf(getStatusCode(asyncEvent)));
        } catch (Throwable t) {
            if (isInfo) {
                logger.info("Failed to async event handle. event={}", asyncEvent, t);
            }
        } finally {
            Pradar.clearInvokeContext();
        }
    }

    @Override
    public void onTimeout(AsyncEvent asyncEvent) throws IOException {
        if (isDebug) {
            logger.debug("Timeout asynchronous operation. event={}", asyncEvent);
        }

        if (asyncEvent == null) {
            if (isDebug) {
                logger.debug("Invalid event. event is null");
            }
            return;
        }

        try {
            Pradar.setInvokeContext(invokeContext_);
            requestTracer.endTrace(request, response, asyncEvent.getThrowable(), "408");
        } catch (Throwable t) {
            logger.info("Failed to async event handle. event={}", asyncEvent, t);
        } finally {
            Pradar.clearInvokeContext();
        }
    }

    @Override
    public void onError(AsyncEvent asyncEvent) throws IOException {
        if (isDebug) {
            logger.debug("Error asynchronous operation. event={}", asyncEvent);
        }

        if (asyncEvent == null) {
            if (isInfo) {
                logger.info("Invalid event. event is null");
            }
            return;
        }

        try {
            Pradar.setInvokeContext(invokeContext_);
            requestTracer.endTrace(request, response, asyncEvent.getThrowable(), "500");
        } catch (Throwable t) {
            if (isInfo) {
                logger.info("Failed to async event handle. event={}", asyncEvent, t);
            }
        } finally {
            Pradar.clearInvokeContext();
        }
    }

    @Override
    public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
    }

    private int getStatusCode(final AsyncEvent asyncEvent) {
        try {
            if (asyncEvent.getSuppliedResponse() instanceof HttpServletResponse) {
                return ((HttpServletResponse) asyncEvent.getSuppliedResponse()).getStatus();
            }
        } catch (Throwable ignored) {
        }
        return 200;
    }

}
