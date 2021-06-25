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
 * ROCKETMQ生产消息数据表
 */
@Data
@TableName(value = "t_rocketmq_producer")
public class RocketmqProducerEntity {
    /**
     * ROCKETMQ生产者id
     */
    @TableId(value = "TRP_ID", type = IdType.INPUT)
    private Long trpId;

    @TableField(value = "MSG_TYPE")
    private String msgType;

    /**
     * 数据字典类型
     */
    @TableField(value = "DICT_TYPE")
    private String dictType;

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
     * 消息休眠时间,默认为毫秒
     */
    @TableField(value = "SLEEP_TIME")
    private Long sleepTime;

    /**
     * 消息发送数量,默认为条
     */
    @TableField(value = "MSG_COUNT")
    private Long msgCount;

    /**
     * 线程数量
     */
    @TableField(value = "THREAD_COUNT")
    private Integer threadCount;

    /**
     * 消息大小
     */
    @TableField(value = "MESSAGE_SIZE")
    private Long messageSize;

    /**
     * 发送消息成功数量
     */
    @TableField(value = "MSG_SUCCESS_COUNT")
    private Long msgSuccessCount;

    /**
     * 生产消息状态, 0未生产消息 1正在生产消息 2已停止生产消息 3生产消息异常  4正在生产但是有发送失败的数据
     */
    @TableField(value = "PRODUCE_STATUS")
    private String produceStatus;

    /**
     * 开始生产消息时间
     */
    @TableField(value = "PRODUCE_START_TIME")
    private LocalDateTime produceStartTime;

    /**
     * 停止生产消息时间
     */
    @TableField(value = "PRODUCE_END_TIME")
    private LocalDateTime produceEndTime;

    /**
     * 上次停止生产时间
     */
    @TableField(value = "LAST_PRODUCE_TIME")
    private LocalDateTime lastProduceTime;

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
     * 线程数量
     */
    @TableField(value = "THREAD_COUNT1")
    private Long threadCount1;
}
