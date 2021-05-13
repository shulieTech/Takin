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

package io.shulie.tro.web.common.vo.fastdebug;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.common.vo.fastdebug
 * @date 2020/12/28 11:22 上午
 */
@Data
public class FastDebugConfigVO {
    /**
     * 调试名称
     */
    private String name;

    /**
     * get post delete put
     */
    private String httpMethod;

    /**
     * 完整url
     */
    private String requestUrl;

    /**
     * 请求头
     */
    private String headers;

    /**
     * cookies
     */
    private String cookies;

    /**
     * 请求体
     */
    private String body;

    /**
     * 响应超时时间
     */
    private Integer timeout;

    /**
     * 是否重定向
     */
    private Boolean isRedirect;

    /**
     * 业务活动id
     */
    private Long businessLinkId;

    /**
     * 租户id
     */
    private Long customerId;

}
