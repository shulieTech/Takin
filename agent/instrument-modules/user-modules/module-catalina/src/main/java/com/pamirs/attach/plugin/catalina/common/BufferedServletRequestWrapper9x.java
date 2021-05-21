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
package com.pamirs.attach.plugin.catalina.common;

import com.pamirs.attach.plugin.common.web.IBufferedServletRequestWrapper;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Session;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.AsyncContextImpl;
import org.apache.catalina.mapper.MappingData;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.ServerCookies;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * Request 包装类
 * 注意：这种使用方法并不是最佳实践，很容易带来类找不到的问题，所以在重写方法的时候需要注意避免加载新的业务类
 * 因为这个时候Simulator 已经获取不到业务类加载器了，就会报找不到类的错误
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/2/20 5:01 下午
 */
public class BufferedServletRequestWrapper9x extends Request implements IBufferedServletRequestWrapper {

    private byte[] buffer;
    private final Request request;
    protected RequestFacade facade;

    public BufferedServletRequestWrapper9x(Request request) {
        super(request.getConnector());
        this.request = request;
        this.setCoyoteRequest(request.getCoyoteRequest());
        this.facade = new RequestFacade(this);
    }

    private void initBuffer() {
        try {
            InputStream is = request.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int read;
            while ((read = is.read(buff)) > 0) {
                baos.write(buff, 0, read);
            }
            this.buffer = baos.toByteArray();
        } catch (IOException e) {
            //ignore
        }
    }


    @Override
    public void setAsyncSupported(boolean asyncSupported) {
        request.setAsyncSupported(asyncSupported);
    }

    @Override
    public void recycle() {
        request.recycle();
    }

    @Override
    public Context getContext() {
        return request.getContext();
    }

    @Override
    public boolean getDiscardFacades() {
        return request.getDiscardFacades();
    }

    @Override
    public FilterChain getFilterChain() {
        return request.getFilterChain();
    }

    @Override
    public void setFilterChain(FilterChain filterChain) {
        request.setFilterChain(filterChain);
    }

    @Override
    public Host getHost() {
        return request.getHost();
    }

    @Override
    public MappingData getMappingData() {
        return request.getMappingData();
    }

    @Override
    public HttpServletRequest getRequest() {
        return facade;
    }

    @Override
    public void setRequest(HttpServletRequest applicationRequest) {
        request.setRequest(applicationRequest);
    }

    @Override
    public Response getResponse() {
        return request.getResponse();
    }

    @Override
    public void setResponse(Response response) {
        request.setResponse(response);
    }

    @Override
    public InputStream getStream() {
        return request.getStream();
    }

    @Override
    public Wrapper getWrapper() {
        return request.getWrapper();
    }

    @Override
    public ServletInputStream createInputStream() throws IOException {
        return request.createInputStream();
    }

    @Override
    public void finishRequest() throws IOException {
        request.finishRequest();
    }

    @Override
    public Object getNote(String name) {
        return request.getNote(name);
    }

    @Override
    public void removeNote(String name) {
        request.removeNote(name);
    }

    @Override
    public void setLocalPort(int port) {
        request.setLocalPort(port);
    }

    @Override
    public void setNote(String name, Object value) {
        request.setNote(name, value);
    }

    @Override
    public void setRemoteAddr(String remoteAddr) {
        request.setRemoteAddr(remoteAddr);
    }

    @Override
    public void setRemoteHost(String remoteHost) {
        request.setRemoteHost(remoteHost);
    }

    @Override
    public void setSecure(boolean secure) {
        request.setSecure(secure);
    }

    @Override
    public void setServerPort(int port) {
        request.setServerPort(port);
    }

    @Override
    public void setContentType(String contentType) {
        request.setContentType(contentType);
    }

    @Override
    public boolean isAsyncDispatching() {
        return request.isAsyncDispatching();
    }

    @Override
    public boolean isAsyncCompleting() {
        return request.isAsyncCompleting();
    }

    @Override
    public boolean isAsync() {
        return request.isAsync();
    }

    @Override
    public AsyncContextImpl getAsyncContextInternal() {
        return request.getAsyncContextInternal();
    }

    @Override
    public void addCookie(Cookie cookie) {
        request.addCookie(cookie);
    }

    @Override
    public void addLocale(Locale locale) {
        request.addLocale(locale);
    }

    @Override
    public void clearCookies() {
        request.clearCookies();
    }

    @Override
    public void clearLocales() {
        request.clearLocales();
    }

    @Override
    public void setAuthType(String type) {
        request.setAuthType(type);
    }

    @Override
    public void setPathInfo(String path) {
        request.setPathInfo(path);
    }

    @Override
    public void setRequestedSessionCookie(boolean flag) {
        request.setRequestedSessionCookie(flag);
    }

    @Override
    public void setRequestedSessionId(String id) {
        request.setRequestedSessionId(id);
    }

    @Override
    public void setRequestedSessionURL(boolean flag) {
        request.setRequestedSessionURL(flag);
    }

    @Override
    public void setRequestedSessionSSL(boolean flag) {
        request.setRequestedSessionSSL(flag);
    }

    @Override
    public String getDecodedRequestURI() {
        return request.getDecodedRequestURI();
    }

    @Override
    public MessageBytes getDecodedRequestURIMB() {
        return request.getDecodedRequestURIMB();
    }

    @Override
    public void setUserPrincipal(Principal principal) {
        request.setUserPrincipal(principal);
    }

    @Override
    public boolean isTrailerFieldsReady() {
        return request.isTrailerFieldsReady();
    }

    @Override
    public Map<String, String> getTrailerFields() {
        return request.getTrailerFields();
    }

    @Override
    public PushBuilder newPushBuilder() {
        return request.newPushBuilder();
    }

    @Override
    public PushBuilder newPushBuilder(HttpServletRequest request) {
        return this.request.newPushBuilder(request);
    }

    @Override
    public ServerCookies getServerCookies() {
        return request.getServerCookies();
    }

    @Override
    public HttpServletMapping getHttpServletMapping() {
        return request.getHttpServletMapping();
    }

    @Override
    public MessageBytes getRequestPathMB() {
        return request.getRequestPathMB();
    }

    @Override
    public Principal getPrincipal() {
        return request.getPrincipal();
    }

    @Override
    public Session getSessionInternal() {
        return request.getSessionInternal();
    }

    @Override
    public void changeSessionId(String newSessionId) {
        request.changeSessionId(newSessionId);
    }

    @Override
    public Session getSessionInternal(boolean create) {
        return request.getSessionInternal(create);
    }

    @Override
    public boolean isParametersParsed() {
        return request.isParametersParsed();
    }

    @Override
    public boolean isFinished() {
        return request.isFinished();
    }

    @Override
    public String getRequestURI() {
        return request.getRequestURI();
    }

    @Override
    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }

    @Override
    public void setCoyoteRequest(org.apache.coyote.Request coyoteRequest) {
        request.setCoyoteRequest(coyoteRequest);
    }

    @Override
    public org.apache.coyote.Request getCoyoteRequest() {
        return request.getCoyoteRequest();
    }

    @Override
    public long getContentLengthLong() {
        return request.getContentLengthLong();
    }

    @Override
    public Connector getConnector() {
        return request.getConnector();
    }

    @Override
    public String getAuthType() {
        return request.getAuthType();
    }

    @Override
    public Cookie[] getCookies() {
        return request.getCookies();
    }

    @Override
    public long getDateHeader(String name) {
        return request.getDateHeader(name);
    }

    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return request.getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return request.getHeaderNames();
    }

    @Override
    public int getIntHeader(String name) {
        return request.getIntHeader(name);
    }

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public String getPathInfo() {
        return request.getPathInfo();
    }

    @Override
    public String getPathTranslated() {
        return request.getPathTranslated();
    }

    @Override
    public String getContextPath() {
        return request.getContextPath();
    }

    @Override
    public String getQueryString() {
        return request.getQueryString();
    }

    @Override
    public String getRemoteUser() {
        return request.getRemoteUser();
    }

    @Override
    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }

    @Override
    public Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }

    @Override
    public String getRequestedSessionId() {
        return request.getRequestedSessionId();
    }

    @Override
    public StringBuffer getRequestURL() {
        return request.getRequestURL();
    }

    @Override
    public String getServletPath() {
        return request.getServletPath();
    }

    @Override
    public HttpSession getSession(boolean create) {
        return request.getSession(create);
    }

    @Override
    public HttpSession getSession() {
        return request.getSession();
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return request.isRequestedSessionIdValid();
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return request.isRequestedSessionIdFromCookie();
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return request.isRequestedSessionIdFromURL();
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return request.isRequestedSessionIdFromUrl();
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return request.authenticate(response);
    }

    @Override
    public void login(String username, String password) throws ServletException {
        request.login(username, password);
    }

    @Override
    public void logout() throws ServletException {
        request.logout();
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return request.getParts();
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return request.getPart(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return request.getAttributeNames();
    }

    @Override
    public String getCharacterEncoding() {
        return request.getCharacterEncoding();
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        request.setCharacterEncoding(env);
    }

    @Override
    public int getContentLength() {
        return request.getContentLength();
    }

    @Override
    public String getContentType() {
        return request.getContentType();
    }

    @Override
    public String getParameter(String name) {
        return request.getParameter(name);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return request.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        return request.getParameterValues(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return request.getParameterMap();
    }

    @Override
    public String getProtocol() {
        return request.getProtocol();
    }

    @Override
    public String getScheme() {
        return request.getScheme();
    }

    @Override
    public String getServerName() {
        return request.getServerName();
    }

    @Override
    public int getServerPort() {
        return request.getServerPort();
    }

    @Override
    public String getRemoteAddr() {
        return request.getRemoteAddr();
    }

    @Override
    public String getRemoteHost() {
        return request.getRemoteHost();
    }

    @Override
    public void setAttribute(String name, Object o) {
        request.setAttribute(name, o);
    }

    @Override
    public void removeAttribute(String name) {
        request.removeAttribute(name);
    }

    @Override
    public Locale getLocale() {
        return request.getLocale();
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return request.getLocales();
    }

    @Override
    public boolean isSecure() {
        return request.isSecure();
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return request.getRequestDispatcher(path);
    }

    @Override
    public String getRealPath(String path) {
        return request.getRealPath(path);
    }

    @Override
    public int getRemotePort() {
        return request.getRemotePort();
    }

    @Override
    public String getLocalName() {
        return request.getLocalName();
    }

    @Override
    public String getLocalAddr() {
        return request.getLocalAddr();
    }

    @Override
    public int getLocalPort() {
        return request.getLocalPort();
    }

    @Override
    public ServletContext getServletContext() {
        return request.getServletContext();
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return request.startAsync();
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return request.startAsync(servletRequest, servletResponse);
    }

    @Override
    public boolean isAsyncStarted() {
        return request.isAsyncStarted();
    }

    @Override
    public boolean isAsyncSupported() {
        return request.isAsyncSupported();
    }

    @Override
    public AsyncContext getAsyncContext() {
        return request.getAsyncContext();
    }

    @Override
    public DispatcherType getDispatcherType() {
        return request.getDispatcherType();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.buffer == null) {
            initBuffer();
        }
        return new BufferedServletInputStream(this.buffer);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (buffer == null) {
            initBuffer();
        }
        String characterEncoding = request.getCharacterEncoding();
        if (characterEncoding == null) {
            characterEncoding = "ISO8859-1";
        }
        return new BufferedReaderWrapper(new InputStreamReader(getInputStream(), characterEncoding));
    }

    @Override
    public byte[] getBody() {
        if (null == this.buffer) {
            return null;
        }
        return this.buffer;
    }

    @Override
    public String changeSessionId() {
        return request.changeSessionId();
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return request.upgrade(handlerClass);
    }

    static class BufferedServletInputStream extends ServletInputStream {
        private ByteArrayInputStream inputStream;

        public BufferedServletInputStream(byte[] buffer) {
            this.inputStream = new ByteArrayInputStream(buffer);
        }

        @Override
        public int available() throws IOException {
            return inputStream.available();
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return inputStream.read(b, off, len);
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

    }
}
