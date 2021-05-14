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
 * MQ虚拟消费消息表
 */
@Data
@TableName(value = "t_mq_msg")
public class MqMsgEntity {
    /**
     * 主键ID
     */
    @TableId(value = "MSG_ID", type = IdType.INPUT)
    private Long msgId;

    /**
     * 消息类型
     */
    @TableField(value = "MSG_TYPE")
    private String msgType;

    /**
     * 数据字典类型（ID值）
     */
    @TableField(value = "DICT_TYPE")
    private String dictType;

    /**
     * 消息地址(ESB和IBM使用)
     */
    @TableField(value = "MSG_HOST")
    private String msgHost;

    /**
     * 消息端口(ESB和IBM使用)
     */
    @TableField(value = "MSG_PORT")
    private String msgPort;

    /**
     * 集群ip(形式为host:port;host:port,ROCKETMQ使用)
     */
    @TableField(value = "MSG_IP")
    private String msgIp;

    /**
     * 订阅主题
     */
    @TableField(value = "TOPIC")
    private String topic;

    /**
     * 组名
     */
    @TableField(value = "GROUPNAME")
    private String groupname;

    /**
     * 系统队列通道
     */
    @TableField(value = "QUEUE_CHANNEL")
    private String queueChannel;

    /**
     * 队列管理器
     */
    @TableField(value = "QUEUE_MANAGER")
    private String queueManager;

    /**
     * 编码字符集标识符
     */
    @TableField(value = "CCSID")
    private String ccsid;

    /**
     * 基础队列名称
     */
    @TableField(value = "BASE_QUEUE_NAME")
    private String baseQueueName;

    /**
     * 传输类型
     */
    @TableField(value = "TRANSPORT_TYPE")
    private String transportType;

    /**
     * ESBCODE
     */
    @TableField(value = "ESBCODE")
    private String esbcode;

    /**
     * 消费状态, 0未消费 1正在消费 2已消费 3消费失败
     */
    @TableField(value = "CONSUME_STATUS")
    private String consumeStatus;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 消费开始时间
     */
    @TableField(value = "CONSUME_START_TIME")
    private LocalDateTime consumeStartTime;

    /**
     * 消费完成时间
     */
    @TableField(value = "CONSUME_END_TIME")
    private LocalDateTime consumeEndTime;

    /**
     * 上次消费时间
     */
    @TableField(value = "LAST_CONSUME_TIME")
    private LocalDateTime lastConsumeTime;
}
