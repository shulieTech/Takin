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
package org.apache.catalina.connector;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Session;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.AsyncContextImpl;
import org.apache.catalina.mapper.MappingData;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.ServerCookies;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/3 5:27 下午
 */
public class Request implements HttpServletRequest {

    public Request() {

    }

    public Request(Connector connector) {

    }

    public void setCoyoteRequest(org.apache.coyote.Request coyoteRequest) {

    }

    public org.apache.coyote.Request getCoyoteRequest() {
        return null;
    }

    public void setAsyncSupported(boolean asyncSupported) {
    }

    public void recycle() {
    }

    public Context getContext() {
        return null;
    }

    public boolean getDiscardFacades() {
        return false;
    }

    public FilterChain getFilterChain() {
        return null;
    }

    public void setFilterChain(FilterChain filterChain) {
    }

    public Host getHost() {
        return null;
    }

    public MappingData getMappingData() {
        return null;
    }

    public HttpServletRequest getRequest() {
        return null;
    }

    public void setRequest(HttpServletRequest applicationRequest) {
    }

    public Response getResponse() {
        return null;
    }

    public void setResponse(Response response) {
    }

    public InputStream getStream() {
        return null;
    }

    public Wrapper getWrapper() {
        return null;
    }

    public ServletInputStream createInputStream() throws IOException {
        return null;
    }

    public void finishRequest() throws IOException {
    }

    public Object getNote(String name) {
        return null;
    }

    public void removeNote(String name) {
    }

    public void setLocalPort(int port) {
    }

    public void setNote(String name, Object value) {
    }

    public void setRemoteAddr(String remoteAddr) {
    }

    public void setRemoteHost(String remoteHost) {
    }

    public void setSecure(boolean secure) {
    }

    public void setServerPort(int port) {
    }

    public void setContentType(String contentType) {
    }

    public boolean isAsyncDispatching() {
        return false;
    }

    public boolean isAsyncCompleting() {
        return false;
    }

    public boolean isAsync() {
        return false;
    }

    public AsyncContextImpl getAsyncContextInternal() {
        return null;
    }

    public void addCookie(Cookie cookie) {
    }

    public void addLocale(Locale locale) {
    }

    public void clearCookies() {
    }

    public void clearLocales() {
    }

    public void setAuthType(String type) {
    }

    public void setPathInfo(String path) {
    }

    public void setRequestedSessionCookie(boolean flag) {
    }

    public void setRequestedSessionId(String id) {
    }

    public void setRequestedSessionURL(boolean flag) {
    }

    public void setRequestedSessionSSL(boolean flag) {
    }

    public String getDecodedRequestURI() {
        return null;
    }

    public MessageBytes getDecodedRequestURIMB() {
        return null;
    }

    public void setUserPrincipal(Principal principal) {
    }

    public boolean isTrailerFieldsReady() {
        return false;
    }

    public Map<String, String> getTrailerFields() {
        return null;
    }

    public PushBuilder newPushBuilder() {
        return null;
    }

    public PushBuilder newPushBuilder(HttpServletRequest request) {
        return null;
    }

    public ServerCookies getServerCookies() {
        return null;
    }

    public HttpServletMapping getHttpServletMapping() {
        return null;
    }

    public MessageBytes getRequestPathMB() {
        return null;
    }

    public Principal getPrincipal() {
        return null;
    }

    public Session getSessionInternal() {
        return null;
    }

    public void changeSessionId(String newSessionId) {
    }

    public Session getSessionInternal(boolean create) {
        return null;
    }

    public boolean isParametersParsed() {
        return false;
    }

    public boolean isFinished() {
        return false;
    }


    @Override
    public long getContentLengthLong() {
        return -1;
    }

    public Connector getConnector() {
        return null;
    }


    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String name) {
        return 0;
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return null;
    }

    @Override
    public int getIntHeader(String name) {
        return 0;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean create) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException {
    }

    @Override
    public void logout() throws ServletException {
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
    }

    @Override
    public int getContentLength() {
        return -1;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public String getParameter(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        return null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return -1;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String name, Object o) {
    }

    @Override
    public void removeAttribute(String name) {
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return -1;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return -1;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return null;
    }

    public void clearEncoders() {
    }

    public boolean read() throws IOException {
        return false;
    }

    public void setConnector(Connector connector) {

    }

    public void setContext(Context context) {

    }

    public void setWrapper(Wrapper wrapper) {

    }

    public CometEventImpl getEvent() {
        return null;
    }

    public boolean isComet() {
        return false;
    }

    public void setComet(boolean comet) {

    }

    public boolean getAvailable() {
        return false;
    }

    public void cometClose() {

    }

    public void setCometTimeout(long timeout) {

    }
}
