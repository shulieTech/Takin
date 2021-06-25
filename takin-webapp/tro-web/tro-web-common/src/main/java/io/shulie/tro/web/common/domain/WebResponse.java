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

package io.shulie.tro.web.common.domain;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author: vernon
 * @Date: 2019/9/20 17:56
 * @Description:
 * @deprecated 未来都不需要返回这个了，controller层会自动封装
 */
@Deprecated
public class WebResponse<T> implements Serializable {

    /**
     * 数据总条数在 header 中返回，这是 header 名称
     */
    public static final String PAGE_TOTAL_HEADER = "x-total-count";
    public static final boolean DEFAULT_SUCCESS = true;
    private static final long serialVersionUID = -1975641525974734365L;
    /**
     * 错误信息实体
     */
    private ErrorInfo error;
    /**
     * 返回数据，如果请求失败，则为空
     */
    private T data;
    /**
     * 成功标记
     */
    private Boolean success;

    public WebResponse() {}

    public WebResponse(T data) {
        this(null, data, DEFAULT_SUCCESS);
    }

    public WebResponse(ErrorInfo error, boolean success) {
        this(error, null, success);
    }

    public WebResponse(ErrorInfo error, T data, boolean success) {
        this.error = error;
        this.data = data;
        this.success = success;
    }

    public WebResponse(ErrorInfo error, T data, boolean success, int code) {
        this.error = error;
        this.data = data;
        this.success = success;
    }

    /**
     * 返回成功,无内容
     *
     * @param <T>
     * @return
     */
    public static <T> WebResponse<T> success() {
        return new WebResponse<>(null);
    }

    /**
     * 返回成功
     *
     * @return
     */
    public static <T> WebResponse<T> success(T data) {
        if (data instanceof PageInfo) {
            return success(data);
        }
        return new WebResponse<>(data);
    }

    public static <T> WebResponse<List<T>> success(PageInfo<T> data) {
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
            .getResponse();
        response.setHeader("Access-Control-Expose-Headers", PAGE_TOTAL_HEADER);
        response.setHeader(PAGE_TOTAL_HEADER, data.getTotal() + "");
        return new WebResponse<>(data.getList());
    }

    public static <T> WebResponse<List<T>> success(List<T> data, long total) {
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
            .getResponse();
        response.setHeader("Access-Control-Expose-Headers", PAGE_TOTAL_HEADER);
        response.setHeader(PAGE_TOTAL_HEADER, total + "");
        return new WebResponse<>(data);
    }

    /**
     * 返回失败，使用传入的错误码
     */
    public static WebResponse fail(String code, String msgTemplate, Object... args) {
        ErrorInfo errorInfo = ErrorInfo.build(code, msgTemplate, args);
        return new WebResponse<>(errorInfo, false);
    }

    /**
     * 返回失败，使用传入的错误码
     */
    public static WebResponse fail(String msgTemplate, Object... args) {
        ErrorInfo errorInfo = ErrorInfo.build("500", msgTemplate, args);
        return new WebResponse<>(errorInfo, false);
    }

    public WebResponse setTotal(Long total) {
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
            .getResponse();
        response.setHeader("Access-Control-Expose-Headers", PAGE_TOTAL_HEADER);

        response.setHeader(PAGE_TOTAL_HEADER, total + "");
        return this;
    }

    public ErrorInfo getError() {
        return error;
    }

    public void setError(ErrorInfo error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
