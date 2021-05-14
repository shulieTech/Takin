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
* @Package io.shulie.tro.web.data.model.mysql
* @author 何仲奇
* @date 2020/12/29 7:18 下午
*/
@Data
@TableName(value = "t_fast_debug_exception")
public class FastDebugExceptionEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 结果id
     */
    @TableField(value = "result_id")
    private Long resultId;

    /**
     * traceId
     */
    @TableField(value = "trace_id")
    private String traceId;

    /**
     * rpc_id
     */
    @TableField(value = "rpc_id")
    private String rpcId;

    /**
     * 应用名
     */
    @TableField(value = "application_name")
    private String applicationName;


    /**
     * agentId
     */
    @TableField(value = "agent_id")
    private String agentId;

    /**
     * 异常类型
     */
    @TableField(value = "type")
    private String type;

    /**
     * 异常编码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 异常描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 处理建议
     */
    @TableField(value = "suggestion")
    private String suggestion;

    /**
     * 异常时间
     */
    @TableField(value = "time")
    private String time;

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
}