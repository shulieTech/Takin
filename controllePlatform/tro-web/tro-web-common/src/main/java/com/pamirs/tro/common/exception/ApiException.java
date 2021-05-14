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

package com.pamirs.tro.common.exception;

/**
 * api异常类
 *
 * @author shulie
 * @version v1.0
 * @2018年5月16日
 */
public class ApiException extends RuntimeException {

    //异常序列化id
    private static final long serialVersionUID = 1L;
    //异常码字段
    private final int code;

    /**
     * 私有构造方法
     *
     * @param code 异常码
     */
    private ApiException(int code) {
        this.code = code;
    }

    /**
     * 私有构造方法
     *
     * @param code    异常码
     * @param message 异常信息
     */
    private ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 私有构造方法
     *
     * @param code      异常码
     * @param message   异常信息
     * @param rootCause 异常源链
     */
    private ApiException(int code, String message, Exception rootCause) {
        super(message, rootCause);
        this.code = code;
    }

    /**
     * 创建api异常
     *
     * @param code 异常码
     * @return api异常
     */
    public static ApiException create(int code) {
        return new ApiException(code);
    }

    /**
     * 创建api异常
     *
     * @param code    异常码
     * @param message 异常信息
     * @return api异常
     */
    public static ApiException create(int code, String message) {
        return new ApiException(code, message);
    }

    /**
     * 创建api异常
     *
     * @param code      异常码
     * @param message   异常信息
     * @param rootCause 异常源链
     * @return api异常
     */
    public static ApiException create(int code, String message, Exception rootCause) {
        return new ApiException(code, message, rootCause);
    }

    /**
     * 获取异常码
     *
     * @return 异常码
     */
    public int getCode() {
        return code;
    }

}
