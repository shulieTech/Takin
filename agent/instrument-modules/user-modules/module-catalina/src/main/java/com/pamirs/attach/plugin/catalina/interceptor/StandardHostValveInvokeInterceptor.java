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

import com.pamirs.attach.plugin.catalina.common.CatalinaVersion;
import com.pamirs.attach.plugin.catalina.common.Servlet2ApiHelperImpl;
import com.pamirs.attach.plugin.common.web.RequestTracer;
import com.pamirs.attach.plugin.common.web.ServletRequestTracer;
import com.pamirs.attach.plugin.common.web.servlet.ServletApiHelper;
import com.pamirs.attach.plugin.common.web.servlet.impl.Servlet3ApiHelper;
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
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/13 3:03 下午
 */
public class StandardHostValveInvokeInterceptor extends AroundInterceptor implements InitializingBean {
    private final static Logger logger = LoggerFactory.getLogger(StandardHostValveInvokeInterceptor.class);

    @Resource
    private LoadedClassDataSource loadedClassDataSource;

    private ServletApiHelper servletApiHelper;
    private final RequestTracer<HttpServletRequest, HttpServletResponse> requestTracer;

    public StandardHostValveInvokeInterceptor() {
        this.servletApiHelper = newServletApi();
        this.requestTracer = new ServletRequestTracer();
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

    @Override
    public void doBefore(Advice advice) throws Throwable {
        Request request = (Request) advice.getParameterArray()[0];
        Response response = (Response) advice.getParameterArray()[1];
        if (servletApiHelper.isAsyncDispatcherBefore(request)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Skip async servlet request event. isAsyncStarted={}, dispatcherType={}", request.isAsyncStarted(), request.getDispatcherType());
            }
            return;
        }
        try {
            advice.changeParameter(0, CatalinaVersion.getRequest(request));
            requestTracer.startTrace(request, response, Pradar.WEB_SERVER_NAME);
        } finally {
            advice.mark(TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS);
        }
    }

    private ServletApiHelper newServletApi() {
        try {
            ServletRequest.class.getMethod("isAsyncStarted");
        } catch (NoSuchMethodException e) {
            return new Servlet2ApiHelperImpl();
        }
        return new Servlet3ApiHelper();
    }

    @Override
    public void doAfter(Advice advice) throws Throwable {
        try {
            Response response = (Response) advice.getParameterArray()[1];
            if (!advice.hasMark(TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS)) {
                return;
            }
            Request request = (Request) advice.getParameterArray()[0];
            if (servletApiHelper.isAsyncDispatcherAfter(request)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Skip async servlet request event. isAsyncStarted={}, dispatcherType={}", request.isAsyncStarted(), request.getDispatcherType());
                }
                return;
            }
            requestTracer.endTrace(request, response, null, String.valueOf(servletApiHelper.getStatus(response)));
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
            Response response = (Response) advice.getParameterArray()[1];
            Request request = (Request) advice.getParameterArray()[0];
            if (servletApiHelper.isAsyncDispatcherAfter(request)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Skip async servlet request event. isAsyncStarted={}, dispatcherType={}", request.isAsyncStarted(), request.getDispatcherType());
                }
                return;
            }
            requestTracer.endTrace(request, response, advice.getThrowable(), String.valueOf(servletApiHelper.getStatus(response)));
        } finally {
            advice.unMark(TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS);
            Pradar.clearInvokeContext();
        }
    }
}
