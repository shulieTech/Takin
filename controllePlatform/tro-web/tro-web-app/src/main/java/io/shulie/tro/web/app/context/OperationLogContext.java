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

package io.shulie.tro.web.app.context;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-09-16
 */
@Data
public class OperationLogContext {

    private String operationType;

    private Long startTime;

    private Long endTime;

    private Long costTime;

    private Boolean success;

    /**
     * 本次是否不记录日志
     */
    private Boolean ignore = false;

    private Map<String, String> vars = new HashMap<>();

}
