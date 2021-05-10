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
package com.shulie.instrument.simulator.module.express;

/**
 * 表达式异常
 */
public class ExpressException extends Exception {

    private final String express;

    /**
     * 表达式异常
     *
     * @param express 原始表达式
     * @param cause   异常原因
     */
    public ExpressException(String express, Throwable cause) {
        super(cause);
        this.express = express;
    }

    /**
     * 获取表达式
     *
     * @return 返回出问题的表达式
     */
    public String getExpress() {
        return express;
    }
}
