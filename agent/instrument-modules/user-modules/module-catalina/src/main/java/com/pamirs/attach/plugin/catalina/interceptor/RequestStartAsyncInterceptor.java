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
package com.pamirs.attach.plugin.catalina.interceptor;

import com.pamirs.attach.plugin.catalina.CatalinaAsyncListener;
import com.pamirs.attach.plugin.common.web.BufferedServletRequestWrapper;
import com.pamirs.attach.plugin.common.web.RequestTracer;
import com.pamirs.attach.plugin.common.web.ServletRequestTracer;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.shulie.instrument.simulator.api.filter.ClassDescriptor;
import com.shulie.instrument.simulator.api.filter.Filter;
import com.shulie.instrument.simulator.api.filter.MethodDescriptor;
import com.shulie.instrument.simulator.api.listener.InitializingBean;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;
import com.shulie.instrument.simulator.api.resource.LoadedClassDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/13 3:12 下午
 */
public class RequestStartAsyncInterceptor extends AroundInterceptor implements InitializingBean {
    private final static Logger logger = LoggerFactory.getLogger(RequestStartAsyncInterceptor.class);
    @Resource
    private LoadedClassDataSource loadedClassDataSource;
    private final RequestTracer requestTracer;

    public RequestStartAsyncInterceptor() {
        this.requestTracer = new ServletRequestTracer();
    }


    @Override
    public void doBefore(Advice advice) throws Throwable {
        ServletRequest servletRequest = (ServletRequest) advice.getParameterArray()[0];
        ServletResponse servletResponse = (ServletResponse) advice.getParameterArray()[1];
        if (!(servletRequest instanceof HttpServletRequest)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Invalid target object, The javax.servlet.http.HttpServletRequest interface is not implemented. target={}", servletRequest);
            }
        }

        if (!(servletResponse instanceof HttpServletResponse)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Invalid target object, The javax.servlet.http.HttpServletResponse interface is not implemented. target={}", servletRequest);
            }
        }

        try {
            advice.changeParameter(0,new BufferedServletRequestWrapper((HttpServletRequest) servletRequest));
            requestTracer.startTrace((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, Pradar.WEB_SERVER_NAME);
        } finally {
            advice.mark(TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS);
        }
    }

    @Override
    public void doAfter(Advice advice) throws Throwable {
        try {
            if (!advice.hasMark(TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS)) {
                return;
            }
            final ServletRequest servletRequest = (ServletRequest) advice.getParameterArray()[0];
            final ServletResponse servletResponse = (ServletResponse) advice.getParameterArray()[1];
            if (!(servletRequest instanceof HttpServletRequest)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Invalid target object, The javax.servlet.http.HttpServletRequest interface is not implemented. target={}", servletRequest);
                }
            }

            if (!(servletResponse instanceof HttpServletResponse)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Invalid target object, The javax.servlet.http.HttpServletResponse interface is not implemented. target={}", servletRequest);
                }
            }

            if (!validate(advice.getTarget(), advice.getReturnObj())) {
                return;
            }
            final AsyncContext asyncContext = (AsyncContext) advice.getReturnObj();
            final AsyncListener asyncListener = new CatalinaAsyncListener(asyncContext, Pradar.popInvokeContextMap(), (HttpServletRequest) servletRequest, (HttpServletResponse) servletRequest);
            asyncContext.addListener(asyncListener);
        } finally {
            advice.unMark(TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS);
            Pradar.clearInvokeContext();
        }
    }

    @Override
    public void doException(Advice advice) throws Throwable {
        try {
            if (!advice.hasMark(TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS)) {
                return;
            }
            final ServletResponse servletResponse = (ServletResponse) advice.getParameterArray()[1];
            if (Pradar.isDebug()) {
                ((HttpServletResponse) servletResponse).setHeader("traceId", Pradar.getTraceId());
            }
            final ServletRequest servletRequest = (ServletRequest) advice.getParameterArray()[0];
            if (!(servletRequest instanceof HttpServletRequest)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Invalid target object, The javax.servlet.http.HttpServletRequest interface is not implemented. target={}", servletRequest);
                }
            }

            if (!(servletResponse instanceof HttpServletResponse)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Invalid target object, The javax.servlet.http.HttpServletResponse interface is not implemented. target={}", servletRequest);
                }
            }
            requestTracer.endTrace((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, advice.getThrowable(), "500");
        } finally {
            advice.unMark(TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS);
            Pradar.clearInvokeContext();
        }

    }

    private boolean validate(final Object target, final Object result) {
        if (result == null) {
            return false;
        }

        if (!(target instanceof HttpServletRequest)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Invalid target object, The javax.servlet.http.HttpServletRequest interface is not implemented. target={}", target);
            }
            return false;
        }
        if (!(result instanceof AsyncContext)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Invalid result object, The javax.servlet.AsyncContext interface is not implemented. result={}.", result);
            }
            return false;
        }
        return true;
    }

    @Override
    public void init() {
        Set<Class<?>> classSet = loadedClassDataSource.find(new Filter() {
            @Override
            public boolean doClassNameFilter(String javaClassName) {
                return javaClassName.startsWith("org.jboss.as.ejb3.");
            }

            @Override
            public boolean doClassFilter(ClassDescriptor classDescriptor) {
                return classDescriptor.getClassName().startsWith("org.jboss.as.ejb3.");
            }

            @Override
            public List<BuildingForListeners> doMethodFilter(MethodDescriptor methodDescriptor) {
                return Collections.EMPTY_LIST;
            }

            @Override
            public List<BuildingForListeners> getAllListeners() {
                return Collections.EMPTY_LIST;
            }
        });
        if (classSet.isEmpty()) {
            Pradar.WEB_SERVER_NAME = "tomcat";
        } else {
            Pradar.WEB_SERVER_NAME = "jboss-eap";
        }
    }
}
