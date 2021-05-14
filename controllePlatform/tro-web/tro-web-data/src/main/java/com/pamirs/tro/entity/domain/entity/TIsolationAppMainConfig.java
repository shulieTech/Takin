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

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;
import lombok.Getter;
import lombok.Setter;

/**
 * 应用隔离配置表
 */
@Getter
@Setter
public class TIsolationAppMainConfig extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用id
     */
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private Long applicationId;

    /**
     * 是否检查网络
     */
    private Integer checkNetwork;

    /**
     * mock应用节点
     */
    private String mockAppNodes;

    /**
     * 隔离应用开关
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String errorMsg;

}
