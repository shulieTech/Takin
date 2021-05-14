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

package io.shulie.tro.web.app.request.fastdebug;

import io.shulie.tro.web.common.vo.fastdebug.ContentTypeVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.request.fastdebug
 * @date 2020/12/28 2:57 下午
 */
@Data
public class FastDebugConfigRequest {
    /**
     * 调试名称
     */
    @ApiModelProperty(value = "调试名称", required = true)
    private String name;

    /**
     * 请求地址或者域名
     */
    @ApiModelProperty(value = "请求地址或者域名", required = true)
    private String requestUrl;

    /**
     * 请求类型
     */
    @ApiModelProperty(value = "请求类型", required = true)
    private String httpMethod;

    /**
     * 请求头
     */
    @ApiModelProperty("请求头")
    private String headers;

    /**
     * cookies
     */
    @ApiModelProperty("cookies")
    private String cookies;

    /**
     * 请求体
     */
    @ApiModelProperty("请求体")
    private String body;

    /**
     * 响应超时时间
     */
    @ApiModelProperty(value = "响应超时时间", required = true)
    private Integer timeout;

    /**
     * 是否重定向
     */
    @ApiModelProperty(value = "是否重定向", required = true)
    private Boolean isRedirect;

    /**
     * 业务活动id
     */
    @ApiModelProperty(value = "业务活动id", required = true)
    private Long businessLinkId;

    /**
     * contentType数据
     */
    @ApiModelProperty(value = "contentType数据", required = true)
    private ContentTypeVO contentTypeVo;
}
