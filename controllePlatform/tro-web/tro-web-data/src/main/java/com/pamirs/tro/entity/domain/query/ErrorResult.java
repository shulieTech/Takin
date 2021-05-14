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

package com.pamirs.tro.entity.domain.query;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/**
 * 错误信息结果类
 * 用于封装错误信息和错误代码
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 */
public class ErrorResult implements Serializable {
    //普通错误码
    public static final int GENERAL_CODE = 999;
    //普通错误名称
    public static final String GENERAL_NAME = "SERVICE_NAME";
    /**
     * 序列号
     */
    private static final long serialVersionUID = 4521021961844996809L;
    //错误码
    private int errorCode;
    //	错误名称
    private String errorName;
    //错误信息
    private String errorMessage;

    /**
     * 默认无参构造
     */
    public ErrorResult() {
        this(GENERAL_CODE, GENERAL_NAME, null);
    }

    public ErrorResult(int code, String name, String message) {
        this.setError(code, name, message);
    }

    /**
     * 构造错误信息
     *
     * @param code    错误码
     * @param name    错误名称
     * @param message 错误信息
     */
    public void setError(int code, String name, String message) {
        this.errorCode = code;
        this.errorName = name;
        this.errorMessage = message;
    }

    public void setError(String name, String message) {
        this.setError(GENERAL_CODE, name, message);
    }

    public boolean hasError() {
        return StringUtils.isNotBlank(this.errorMessage) && StringUtils.isNotBlank(this.errorName);
    }

    /**
     * 2018年5月17日
     *
     * @return the errorCode
     * @author shulie
     * @version 1.0
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * 2018年5月17日
     *
     * @param errorCode the errorCode to set
     * @author shulie
     * @version 1.0
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 2018年5月17日
     *
     * @return the errorName
     * @author shulie
     * @version 1.0
     */
    public String getErrorName() {
        return errorName;
    }

    /**
     * 2018年5月17日
     *
     * @param errorName the errorName to set
     * @author shulie
     * @version 1.0
     */
    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    /**
     * 2018年5月17日
     *
     * @return the errorMessage
     * @author shulie
     * @version 1.0
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 2018年5月17日
     *
     * @param errorMessage the errorMessage to set
     * @author shulie
     * @version 1.0
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
