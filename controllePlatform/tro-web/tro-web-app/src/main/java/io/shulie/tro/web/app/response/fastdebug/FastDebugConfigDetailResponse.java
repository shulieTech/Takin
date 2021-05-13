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

package io.shulie.tro.web.app.response.fastdebug;

import com.pamirs.tro.entity.domain.vo.linkmanage.BusinessLinkVo;
import io.shulie.tro.web.common.vo.fastdebug.ContentTypeVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.response.fastdebug
 * @date 2020/12/28 11:13 上午
 */
@Data
public class FastDebugConfigDetailResponse {
    @ApiModelProperty("id")
    private Long id;

    /**
     * 调试名称
     */
    @ApiModelProperty("调试名称")
    private String name;

    /**
     * 请求类型
     */
    @ApiModelProperty("请求类型")
    private String httpMethod;

    /**
     * 请求地址或者域名
     */
    @ApiModelProperty("请求地址或者域名")
    private String requestUrl;

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
    @ApiModelProperty("响应超时时间")
    private Integer timeout;

    /**
     * 是否重定向
     */
    @ApiModelProperty("是否重定向")
    private Boolean isRedirect;

    /**
     * 业务活动id
     */
    @ApiModelProperty("业务活动id")
    private Long businessLinkId;

    /**
     * contentType数据
     */
    @ApiModelProperty("contentType数据")
    private ContentTypeVO contentTypeVo;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private String gmtModified;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String modifierName;

    /**
     * 业务活动
     */
    @ApiModelProperty("业务活动详情")
    private BusinessLinkVo businessLinkVo;

}
