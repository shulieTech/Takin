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
import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.vo.ApplicationVo;
import lombok.Data;

/**
 * @Author: mubai<chengjiacai @ shulie.io>
 * @Date: 2020-04-07 13:47
 * @Description:
 */

@Data
public class ApplicationSwitchStatusDTO implements Serializable {
    private static final long serialVersionUID = -8743442521630586570L;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * OPENED("已开启",0),
     * OPENING("开启中",1),
     * OPEN_FAILING("开启异常",2),
     * CLOSED("已关闭",3),
     * CLOSING("关闭中",4),
     * CLOSE_FAILING("关闭异常",5)
     */
    private String switchStatus;

    /**
     * 异常信息
     */
    private Map<String, Object> exceptionMap;

    /**
     * 节点唯一键
     */
    private String nodeKey;

    /**
     * 节点ip
     */
    private String nodeIP;

    /**
     * 节点列表信息
     */
    private List<ApplicationVo> errorList;

    /**
     * 应用接入状态
     */
    private Integer accessStatus;

}
