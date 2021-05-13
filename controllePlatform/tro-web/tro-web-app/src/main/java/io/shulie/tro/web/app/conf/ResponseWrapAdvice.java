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

package io.shulie.tro.web.app.conf;

import java.io.File;

import com.github.pagehelper.PageInfo;
import com.pamirs.tro.common.ResponseOk.ResponseResult;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.common.domain.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author shiyajian
 * create: 2020-09-24
 */
@ControllerAdvice(basePackages = "io.shulie.tro")
@Slf4j
public class ResponseWrapAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
        Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
        ServerHttpResponse response) {
        if (body instanceof Response
            || body instanceof WebResponse
            || body instanceof File
            || body instanceof ResponseResult
            || body instanceof io.shulie.tro.common.beans.response.ResponseResult) {
            return body;
        }
        if (body instanceof PageInfo) {
            return Response.success(body);
        }
        if (body instanceof PagingList) {
            return Response.successPagingList((PagingList)body);
        }
        return Response.success(body);
    }
}
