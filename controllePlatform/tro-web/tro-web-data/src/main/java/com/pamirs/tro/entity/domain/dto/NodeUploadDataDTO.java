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

package com.pamirs.tro.entity.domain.dto;

import java.io.Serializable;
import java.util.Map;

import com.pamirs.tro.common.util.DateUtils;
import lombok.Data;

/**
 * @Author: mubai<chengjiacai @ shulie.io>
 * @Date: 2020-04-03 10:08
 * @Description: 应用上传接入状态信息，
 */

@Data
public class NodeUploadDataDTO implements Serializable {

    private static final long serialVersionUID = 2907229827465846525L;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 节点唯一键
     */
    private String nodeKey;

    /**
     * agent id
     */
    private String agentId;

    /**
     * 开关异常信息
     */
    private Map<String, Object> switchErrorMap;

    /**
     * 异常时间
     */
    private String exceptionTime = DateUtils.getNowDateStr();

    @Override
    public String toString() {
        return "NodeUploadDataDTO{" +
                "applicationName='" + applicationName + '\'' +
                ", nodeKey='" + nodeKey + '\'' +
                ", switchErrorMap=" + switchErrorMap +
                '}';
    }

}
