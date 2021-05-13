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

package io.shulie.tro.web.data.model.mysql;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 何仲奇
 * @Package io.shulie.tro.web.data.model.mysql
 * @date 2020/12/28 11:59 上午
 */
@Data
@TableName(value = "t_fast_debug_config_info")
public class FastDebugConfigInfoEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 调试名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 请求地址或者域名
     */
    @TableField(value = "request_url")
    private String requestUrl;

    /**
     * 请求地址或者域名
     */
    @TableField(value = "http_method")
    private String httpMethod;

    /**
     * 请求头
     */
    @TableField(value = "headers")
    private String headers;

    /**
     * cookies
     */
    @TableField(value = "cookies")
    private String cookies;

    /**
     * 请求体
     */
    @TableField(value = "body")
    private String body;

    /**
     * 响应超时时间
     */
    @TableField(value = "timeout")
    private Integer timeout;

    /**
     * 是否重定向
     */
    @TableField(value = "is_redirect")
    private Boolean isRedirect;

    /**
     * 业务活动id
     */
    @TableField(value = "business_link_id")
    private Long businessLinkId;

    /**
     * contentType数据
     */
    @TableField(value = "content_type")
    private String contentType;

    /**
     * 0：未调试，1，调试中
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified")
    private Date gmtModified;

    /**
     * 软删
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    /**
     * 租户 id customer
     */
    @TableField(value = "`customer_id`")
    private Long customerId;

    /**
     * 创建人
     */
    @TableField(value = "creator_id")
    private Long creatorId;

    /**
     * 修改人
     */
    @TableField(value = "modifier_id")
    private Long modifierId;
}