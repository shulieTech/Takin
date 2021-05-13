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

package io.shulie.tro.web.data.param.application;

import java.util.Date;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/11/9 9:19 下午
 * @Description:
 */
@Data
public class ApplicationWhiteListCreateParam {

    /**
     * 白名单id
     */
    private long wlistId;

    /**
     * 接口名称
     */
    //    @NotBlank(message = "接口名称不能为空")
    private String interfaceName;

    /**
     * 白名单类型
     */
    private String type;

    /**
     * 字典分类
     */
    private String dictType;

    /**
     * 应用id
     */
    private String applicationId;

    /**
     * 负责人工号
     */
    private String principalNo;

    /**
     * 是否可用
     */
    private String useYn;

    /**
     * mq类型： 1ESB 2IBM 3ROCKETMQ 4DPBOOT_ROCKETMQ
     * 当且仅当白名单类型是MQ(即type=5)时才有值
     */
    private String mqType;

    /**
     * 队列名称
     */
    private String queueName;

    /**
     * IP端口号, 即nameServer
     * 当且仅当mq是ROCKETMQ或DPBOOT_ROCKETMQ时有值
     */
    private String ipPort;

    /**
     * http类型：1页面 2接口
     */
    private String httpType;

    /**
     * 页面分类：1普通页面加载(3s) 2简单查询页面/复杂界面(5s) 3复杂查询页面(8s)
     */
    private String pageLevel;

    /**
     * 接口类型：1简单操作/查询 2一般操作/查询 3复杂操作 4涉及级联嵌套调用多服务操作 5调用外网操作
     */
    private String interfaceLevel;

    /**
     * JOB调度间隔：1调度间隔≤1分钟 2调度间隔≤5分钟 3调度间隔≤15分钟 4调度间隔≤60分钟
     */
    private String jobInterval;

    // @Field createTime : 数据插入时间
    private Date createTime;

    // @Field updateTime : 数据更新时间
    private Date updateTime;

    private Long customerId;

    private Long userId;

    /**
     * 是否手工添加
     */
    private Boolean isHandwork;

    /**
     * 是否全局
     */
    private Boolean isGlobal;

}
