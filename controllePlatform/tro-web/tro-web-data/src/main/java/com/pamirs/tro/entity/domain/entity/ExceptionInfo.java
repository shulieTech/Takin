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

package com.pamirs.tro.entity.domain.entity;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/8/14 下午5:20
 * @Description: Agent上报应用异常状态实体类
 */
@Data
public class ExceptionInfo {
    /**
     * 异常编码
     */
    private String errorCode;
    /**
     * 异常简要
     */
    private String message;
    /**
     * 异常详情
     */
    private String detail;


    @Override
    public String toString() {
        return "->" +
            "异常编码:" + errorCode +
            ", 异常简要:" + message +
            ", 异常详情:" + detail;
    }
}
