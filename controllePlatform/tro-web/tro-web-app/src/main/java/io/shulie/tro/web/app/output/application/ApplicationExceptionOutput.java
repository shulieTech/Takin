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

package io.shulie.tro.web.app.output.application;

import java.util.List;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.output.application
 * @date 2021/1/7 11:52 上午
 */
@Data
public class ApplicationExceptionOutput {
    /**
     * 应用名
     */
    private String applicationName;

    /**
     * agentId
     */
    private List<String> agentIds;
    /**
     * 异常类型
     */
    private String type;

    /**
     * 异常编码
     */
    private String code;

    /**
     * 异常描述
     */
    private String description;

    /**
     * 异常时间
     */
    private String time;
}
