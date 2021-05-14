/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.cloud.common.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by xuyh at 2020/3/27 10:35.
 */
public class RestContext {
    public static InheritableThreadLocal<HttpServletRequest> httpServletRequestThreadLocal = new InheritableThreadLocal<>();
    public static InheritableThreadLocal<HttpServletResponse> httpServletResponseThreadLocal = new InheritableThreadLocal<>();
    public static InheritableThreadLocal<LoginUser> userThreadLocal = new InheritableThreadLocal<>();
    public static InheritableThreadLocal<String> filterSqlThreadLocal = new InheritableThreadLocal<>();


    public static HttpServletRequest getHttpServletRequest() {
        return httpServletRequestThreadLocal.get();
    }

    public static void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        httpServletRequestThreadLocal.set(httpServletRequest);
    }

    public static HttpServletResponse gertHttpServletResponse() {
        return httpServletResponseThreadLocal.get();
    }

    public static void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        httpServletResponseThreadLocal.set(httpServletResponse);
    }

    public static LoginUser getUser() {
        return userThreadLocal.get();
    }

    public static void setUser(LoginUser user) {
        userThreadLocal.set(user);
    }


    public static void clear() {
        filterSqlThreadLocal.remove();
        userThreadLocal.remove();
        httpServletResponseThreadLocal.remove();
        httpServletRequestThreadLocal.remove();
    }

    public static String getFilterSql() {
        return filterSqlThreadLocal.get();
    }

    public static void setFilterSql(String filterSql) {
        filterSqlThreadLocal.set(filterSql);
    }
}

