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

package io.shulie.tro.web.app.common;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import com.github.pagehelper.PageInfo;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.response.BaseResponse;
import io.shulie.tro.web.common.domain.ErrorInfo;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author: vernon
 * @Date: 2019/9/20 17:56
 * @Description:
 * @deprecated 默认响应格式包装类
 */
public class Response<T> {

    /**
     * 数据总条数在 header 中返回，这是 header 名称
     */
    public static final String PAGE_TOTAL_HEADER = "x-total-count";
    public static final boolean DEFAULT_SUCCESS = true;
    /**
     * 错误信息实体
     */
    private ErrorInfo error;
    /**
     * 返回数据，如果请求失败，则为空
     */
    @ApiModelProperty(name = "data", value = "返回的具体数据")
    private T data;
    /**
     * 成功标记
     */
    @ApiModelProperty(name = "success", value = "是否成功")
    private Boolean success;

    private Boolean unsuccess;

    public Response(T data) {
        this(null, data, DEFAULT_SUCCESS);
    }

    public Response(ErrorInfo error, boolean success) {
        this(error, null, success);
    }

    public Response(ErrorInfo error, T data, boolean success) {
        this.error = error;
        this.data = data;
        this.success = success;
    }

    public Response(ErrorInfo error, T data, boolean success, int code) {
        this.error = error;
        this.data = data;
        this.success = success;
    }

    public static Response failByType(String code, String msgTemplate, String headerType) {
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
            .getResponse();
        response.setHeader("Access-Control-Expose-Headers", "type");
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setMsgTemplate(msgTemplate);
        response.setHeader("type", headerType);

        return new Response(errorInfo, null, false, Integer.parseInt(code));

    }

    /**
     * 返回成功,无内容
     *
     * @param <T>
     * @return
     */
    public static <T> Response<T> success() {
        return new Response<>(null);
    }

    /**
     * 返回成功
     *
     * @return
     */
    public static <T> Response<T> success(T data) {
        if (data instanceof PageInfo) {
            return success(data);
        }
        if (data instanceof List) {
            return (Response<T>)successList((List)data);
        }
        return new Response<>(data);
    }

    public static Response<List> successList(List data) {
        permissionHook(data);
        return new Response<>(data);
    }

    public static <T> Response<List<T>> success(PageInfo<T> data) {
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
            .getResponse();
        response.setHeader("Access-Control-Expose-Headers", PAGE_TOTAL_HEADER);
        response.setHeader(PAGE_TOTAL_HEADER, data.getTotal() + "");
        permissionHook(data);
        return new Response<>(data.getList());
    }
    private static <T> void permissionHook(List<T> list) {
        if (list == null){
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            T responseObj = list.get(i);
            if (responseObj instanceof BaseResponse){
                BaseResponse baseResponse = (BaseResponse)responseObj;
                baseResponse.permissionControl();
            }
        }
    }
    private static <T> void permissionHook(PageInfo<T> data) {
        permissionHook(data.getList());
    }

    public static <T> Response<List<T>> successPagingList(PagingList<T> data) {
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
            .getResponse();
        response.setHeader("Access-Control-Expose-Headers", PAGE_TOTAL_HEADER);
        response.setHeader(PAGE_TOTAL_HEADER, data.getTotal() + "");
        return new Response<>(data.getList());
    }

    public static <T> Response<List<T>> success(List<T> data, long total) {
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
            .getResponse();
        response.setHeader("Access-Control-Expose-Headers", PAGE_TOTAL_HEADER);
        response.setHeader(PAGE_TOTAL_HEADER, total + "");
        return new Response<>(data);
    }

    /**
     * 返回失败，使用传入的错误码
     */
    public static Response fail(String code, String msgTemplate, Object... args) {
        OperationLogContextHolder.ignoreLog();
        ErrorInfo errorInfo = ErrorInfo.build(code, msgTemplate, args);
        return new Response<>(errorInfo, false);
    }

    /**
     * 返回失败，使用传入的错误码
     */
    public static Response fail(String msgTemplate, Object... args) {
        OperationLogContextHolder.ignoreLog();
        ErrorInfo errorInfo = ErrorInfo.build("500", msgTemplate, args);
        return new Response<>(errorInfo, false);
    }

    public Response setTotal(Long total) {
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
