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

import java.util.Date;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.param.fastdebug
 * @date 2020/12/29 10:18 上午
 */
@Data
public class FastDebugResultParam {
    /**
     * 调试name
     */
    private String name;

    private String businessLinkName;
    /**
     * 调试配置id
     */
    private Long configId;

    /**
     * 完整url
     */
    private String requestUrl;

    private String httpMethod;


    /**
     * 漏数检查数据,每次自己报存一份，并保持结果
     */
    private String leakageCheckData;



    /**
     * 通过response解析出来traceId
     */
    private String traceId;



    /**
     * 错误信息
     */
    private String errorMessage;


    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 租户id
     */
    private Long customerId;
    /**
     * 操作人
     */
    private Long creatorId;

    public FastDebugResultParam() {
        this.gmtCreate = new Date();
        this.gmtModified = new Date();
    }
}
