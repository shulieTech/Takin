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
 * 影子消费者
 */
@Data
@TableName(value = "t_shadow_mq_consumer")
public class ShadowMqConsumerEntity {
    public static final String COL_TOPIC_GROUP = "topic_group";
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * topic
     */
    @TableField(value = "topic_group")
    private String topicGroup;

    /**
     * MQ类型
     */
    @TableField(value = "type")
    private String type;

    /**
     * 应用id
     */
    @TableField(value = "application_id")
    private Long applicationId;

    /**
     * 应用名称，冗余
     */
    @TableField(value = "application_name")
    private String applicationName;

    /**
     * 是否可用(0表示未启用,1表示已启用)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 租户id
     */
    @TableField(value = "customer_id")
    private Long customerId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 是否删除，0正常，1删除
     */
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 拓展字段
     */
    @TableField(value = "feature")
    private String feature;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    public static final String COL_ID = "id";

    public static final String COL_TOPIC = "topic";

    public static final String COL_TYPE = "type";

    public static final String COL_GROUP = "group";

    public static final String COL_APPLICATION_ID = "application_id";

    public static final String COL_APPLICATION_NAME = "application_name";

    public static final String COL_STATUS = "status";

    public static final String COL_CUSTOMER_ID = "customer_id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_DELETED = "deleted";

    public static final String COL_FEATURE = "feature";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}
