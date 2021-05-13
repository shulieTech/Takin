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

import com.pamirs.tro.common.constant.TROErrorEnum;

/**
 * 说明：压测模块异常枚举类
 *
 * @author shulie
 * @version 1.0
 * @date 2018年4月27日
 */
public class TROModuleException extends Exception {

    //序列号
    private static final long serialVersionUID = 1L;

    //TRO错误枚举类
    private TROErrorEnum tROErrorEnum;

    //异常信息
    private String message;

    /**
     * 构造方法
     *
     * @param tROErrorEnum 异常枚举
     */
    public TROModuleException(TROErrorEnum tROErrorEnum) {
        super(tROErrorEnum.getErrorMessage());
        this.tROErrorEnum = tROErrorEnum;
    }

    /**
     * 构造方法
     *
     * @param tROErrorEnum 异常枚举
     * @param cause        异常链
     */
    public TROModuleException(TROErrorEnum tROErrorEnum, Throwable cause) {
        super(tROErrorEnum.getErrorMessage(), cause);
        this.tROErrorEnum = tROErrorEnum;
    }

    /**
     * 构造方法
     *
     * @param cause 异常链
     */
    public TROModuleException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造方法
     *
     * @param message 错误信息
     */
    public TROModuleException(String message) {
        this.message = message;
    }

    /**
     * 2018年5月21日
     *
     * @return the message
     * @author shulie
     * @version 1.0
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 2018年5月21日
     *
     * @param message the message to set
     * @author shulie
     * @version 1.0
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 2018年5月21日
     *
     * @return the tROErrorEnum
     * @author shulie
     * @version 1.0
     */
    public TROErrorEnum gettROErrorEnum() {
        return tROErrorEnum;
    }

    /**
     * 获取错误信息
     *
     * @return 错误信息
     * @author shulie
     * @date 2018年5月21日
     * @version v1.0
     */
    public String getErrorMessage() {
        return gettROErrorEnum().getErrorMessage();
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     * @author shulie
     * @date 2018年5月21日
     * @version v1.0
     */
    public int getErrorCode() {
        return gettROErrorEnum().getErrorCode();
    }
}
