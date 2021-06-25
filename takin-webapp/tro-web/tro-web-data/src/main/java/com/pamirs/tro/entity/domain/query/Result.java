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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 支持分页结果集
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 */
@Deprecated //todo service和controller都不需要包装这种对象，切面自动完成
public class Result<T> extends ErrorResult implements Serializable {
    //序列号
    private static final long serialVersionUID = -8722768053248374040L;
    //结果信息
    private String message;
    //结果(成功或失败)
    private boolean success = true;
    //结果数据
    private T data;

    /**
     * 2018年5月17日
     *
     * @return the message
     * @author shulie
     * @version 1.0
     */
    public String getMessage() {
        return message;
    }

    /**
     * 2018年5月17日
     *
     * @param message the message to set
     * @author shulie
     * @version 1.0
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 2018年5月17日
     *
     * @return the success
     * @author shulie
     * @version 1.0
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 2018年5月17日
     *
     * @param success the success to set
     * @author shulie
     * @version 1.0
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 2018年5月17日
     *
     * @return the data
     * @author shulie
     * @version 1.0
     */
    public T getData() {
        return data;
    }

    /**
     * 2018年5月17日
     *
     * @param data the data to set
     * @author shulie
     * @version 1.0
     */
    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * 设置错误信息
     */
    public void setError(int code, String name, String message) {
        super.setError(code, name, message);
        this.setSuccess(false);
    }
}
