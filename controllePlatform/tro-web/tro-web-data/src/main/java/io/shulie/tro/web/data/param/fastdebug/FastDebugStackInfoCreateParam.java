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

package io.shulie.tro.web.data.param.fastdebug;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-12-28 11:54
 * @Description:
 */

@Data
public class FastDebugStackInfoCreateParam implements Serializable {

    private static final long serialVersionUID = -8560153626724396746L;


    private String appName ;

    private String agentId;

    /**
     * traceId
     */
    private String traceId;

    /**
     * rpcid
     */
    private String rpcId;


    /**
     * 日志级别
     */
    private String level;

    /**
     * type
     */
    private Integer type;

    /**
     * stack信息
     */
    private String content;

    /**
     * 是否栈信息
     */
    private Boolean isStack;

    /**
     * 状态 0: 正常 1： 删除
     */
    private Boolean isDelete;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtUpdate;
}
