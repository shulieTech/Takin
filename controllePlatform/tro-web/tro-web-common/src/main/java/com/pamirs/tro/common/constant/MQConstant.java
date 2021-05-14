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

package com.pamirs.tro.common.constant;

/**
 * 说明: mq消费常量类
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/8/16 14:38
 */
public class MQConstant {

    /**
     * ESB/IBM的主键id,1表示ESB,2表示IBM
     */
    public static final String EBMID = "tepId";

    /**
     * ROCKETMQ的主键id
     */
    public static final String ROCKETMQID = "trpId";

    /**
     * 控制ROCKETMQ发送消息的线程数量
     */
    public static final String THREADCOUNT = "threadCount";

    /**
     * 控制ROCKETMQ发送消息的大小
     */
    public static final String MESSAGESIZE = "messageSize";

    /**
     * 消息类型,1表示ESB,2表示IBM,3表示ROCKETMQ
     */
    public static final String MSGTYPE = "msgType";

    /**
     * 消息id
     */
    public static final String MSGID = "msgId";

    /**
     * 队列管理器
     */
    public static final String QUEUEMANAGER = "queueManager";

    /**
     * 消息地址
     */
    public static final String MSGHOST = "msgHost";

    /**
     * 系统队列通道
     */
    public static final String QUEUECHANNEL = "queueChannel";

    public static final String REQUESTCOMOUT = "requestComout";

    /**
     * 消息端口
     */
    public static final String MSGPORT = "msgPort";

    /**
     * 集群ip
     */
    public static final String MSGIP = "msgIp";

    /**
     * 传输类型
     */
    public static final String TRANSPORTTYPE = "transportType";

    /**
     * 编码字符集标识符
     */
    public static final String CCSID = "ccsid";

    /**
     * 基础队列名称
     */
    public static final String BASEQUEUENAME = "baseQueueName";

    /**
     * esbcode
     */
    public static final String ESBCODE = "esbcode";

    /**
     * 订阅主题
     */
    public static final String TOPIC = "topic";

    /**
     * 组名
     */
    public static final String GROUPNAME = "groupName";

    /**
     * 消息休眠时间
     */
    public static final String SLEEPTIME = "sleepTime";

    /**
     * 消息发送数量
     */
    public static final String MSGCOUNT = "msgCount";

    /**
     * ESB类型
     */
    public static final String ESB = "1";

    /**
     * IBM类型
     */
    public static final String IBM = "2";

    /**
     * ROCKETMQ类型
     */
    public static final String ROCKETMQ = "3";

    /**
     * DPBOOT_ROCKETMQ类型
     */
    public static final String DPBOOT_ROCKETMQ = "4";

}
