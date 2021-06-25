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
@TableName(value = "t_fast_debug_stack_info")
public class FastDebugStackInfoEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * appName
     */
    @TableField(value = "app_name")
    private String appName;




    /**
     * agentId
     */
    @TableField(value = "agent_id")
    private String agentId;


    /**
     * traceId
     */
    @TableField(value = "trace_id")
    private String traceId;

    /**
     * rpcid
     */
    @TableField(value = "rpc_id")
    private String rpcId;

    /**
     * 日志级别
     */
    @TableField(value = "level")
    private String level;

    /**
     * type
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * stack信息
     */
    @TableField(value = "content")
    private String content;


    /**
     * 是否栈信息
     */
    @TableField(value = "is_stack")
    private Boolean isStack;

    /**
     * 状态 0: 正常 1： 删除
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @TableField(value = "gmt_update")
    private Date gmtUpdate;
}