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

package io.shulie.tro.web.data.result.fastdebug;

import java.util.Date;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.response.fastdebug
 * @date 2020/12/28 11:13 上午
 */
@Data
public class FastDebugResult {
    private Long id;

    private String name;

    /**
     * 业务活动名称
     */
    private String businessLinkName;

    /**
     * 调试配置id
     */
    private Long configId;

    /**
     * 业务活动name,组装体
     */
    private String requestUrl;

    /**
     * 请求方法
     */
    private String httpMethod;

    /**
     * 请求体，包含url,body,header
     */
    private String request;

    /**
     * 漏数检查数据,每次自己报存一份，并保持结果
     */
    private String leakageCheckData;

    /**
     * 请求返回体
     */
    private String response;

    /**
     * 请求返回体
     */
    private String errorMessage;

    /**
     * 通过response解析出来traceId
     */
    private String traceId;

    /**
     * 调用时长
     */
    private Long callTime;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;


    /**
     * 操作人
     */
    private Long creatorId;

    /**
     * 0:失败；1：成功；调试中根据config中status判断
     */
    private Boolean debugResult;

    /**
     * 租户
     */
    private Long customerId;

}
