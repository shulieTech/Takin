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

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 黑名单管理
 */
@Data
@TableName(value = "t_black_list")
public class BlackListEntity {
    /**
     * 主键id
     */
    @TableId(value = "BLIST_ID", type = IdType.AUTO)
    private Long blistId;


    /**
     * 黑名单类型
     */
    @TableField(value = "type")
    private Integer type ;

    /**
     * 应用id
     */
    @TableField(value = "APPLICATION_ID")
    private Long applicationId ;

    /**
     * 插入时间
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;

    /**
     * 变更时间
     */
    @TableField(value = "gmt_modified")
    private Date gmtModified;


    /**
     * redis的键
     */
    @TableField(value = "REDIS_KEY")
    private String redisKey;

    /**
     * 是否可用(0表示未启动,1表示启动,2表示启用未校验)
     */
    @TableField(value = "USE_YN")
    private Integer useYn;

    /**
     * 租户id
     */
    @TableField(value = "customer_id", fill = FieldFill.INSERT)
    private Long customerId;

    /**
     * 用户id
     */
    @TableField(value = "user_id", fill = FieldFill.INSERT)
    private Long userId;


    /**
     * 是否删除 0-未删除、1-已删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

}
