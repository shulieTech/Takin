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
@TableName(value = "t_fast_debug_result")
public class FastDebugResultEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 调试配置name
     */
    @TableField(value = "name")
    private String name;

    /**
     * 业务活动名称
     */
    @TableField(value = "business_link_name")
    private String businessLinkName;

    /**
     * 调试配置id
     */
    @TableField(value = "config_id")
    private Long configId;

    /**
     * 业务活动name,组装体
     */
    @TableField(value = "request_url")
    private String requestUrl;

    /**
     * 业务活动name,组装体
     */
    @TableField(value = "http_method")
    private String httpMethod;

    /**
     * 请求体，包含url,body,header
     */
    @TableField(value = "request")
    private String request;

    /**
     * 漏数检查数据,每次自己报存一份，并保持结果
     */
    @TableField(value = "leakage_check_data")
    private String leakageCheckData;

    /**
     * 请求返回体
     */
    @TableField(value = "response")
    private String response;

    /**
     * 请求返回体
     */
    @TableField(value = "error_message")
    private String errorMessage;

    /**
     * 通过response解析出来traceId
     */
    @TableField(value = "trace_id")
    private String traceId;

    /**
     * 调用时长
     */
    @TableField(value = "call_time")
    private Long callTime;

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
     * 操作人
     */
    @TableField(value = "creator_id")
    private Long creatorId;

    /**
     * 0:失败；1：成功；调试中根据config中status判断
     */
    @TableField(value = "debug_result")
    private Boolean debugResult;
}