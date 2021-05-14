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

package io.shulie.tro.web.config.exception;

/**
 * @author zhaoyong
 * tro配置信息异常类
 */
public class TroConfigSyncException extends Exception {

    //序列号
    private static final long serialVersionUID = 1L;

    //异常信息
    private String message;

    /**
     * 构造方法
     */
    public TroConfigSyncException(String massage) {
        super(massage);
    }

    /**
     * 构造方法
     *
     * @param cause 异常链
     */
    public TroConfigSyncException(String massage, Throwable cause) {
        super(massage, cause);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
