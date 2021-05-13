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

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * MQ隔离配置表
 */
@Data
@TableName(value = "t_mq_isolate_config")
public class MqIsolateConfigEntity {
    /**
     * 主键id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 集群名称
     */
    @TableField(value = "CLUSTER_NAME")
    private String clusterName;

    /**
     * 集群别名
     */
    @TableField(value = "CLUSTER_ALIAS")
    private String clusterAlias;

    /**
     * NAMESERVER地址
     */
    @TableField(value = "NAMESRV_ADDR")
    private String namesrvAddr;

    /**
     * 压测NAMESERVER地址
     */
    @TableField(value = "PT_NAMESRV_ADDR")
    private String ptNamesrvAddr;

    /**
     * broker名称，多个用逗号分隔
     */
    @TableField(value = "BROKER_NAME")
    private String brokerName;

    /**
     * TOPIC名称，多个用逗号分隔
     */
    @TableField(value = "TOPIC")
    private String topic;

    /**
     * 消费组，多个用逗号分隔
     */
    @TableField(value = "CONSUMER_GROUP")
    private String consumerGroup;

    /**
     * 隔离状态(0:停写，1:读完成，2:隔离)
     */
    @TableField(value = "ISOLATED_STATUS")
    private Integer isolatedStatus;

    /**
     * broker属性
     */
    @TableField(value = "BROKER_PROPERTY")
    private String brokerProperty;

    /**
     * 是否有效: Y:有效 N:无效
     */
    @TableField(value = "ACTIVE")
    private String active;

    /**
     * 操作出错信息
     */
    @TableField(value = "FAILED_DETAIL")
    private String failedDetail;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 变更时间
     */
    @TableField(value = "UPDATE_TIME")
    private LocalDateTime updateTime;
}
