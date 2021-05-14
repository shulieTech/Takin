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

package io.shulie.tro.web.data.param.tracenode;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.param.tracenode
 * 注释部分，目前不用之后有用到，可以进行添加2
 * @date 2020/12/29 12:04 下午
 */
@Data
public class TraceNodeInfoParam {
    /**
     * 当前应用名称
     */
    private String appName;

    /**
     * trace id
     */
    private String traceId;

    ///**
    // * 层级
    // */
    //private int level;

    ///**
    // * 父序号
    // */
    //private int parentIndex;

    ///**
    // * 序号
    // */
    //private int index;

    /**
     * rpcId
     */
    private String rpcId;

    ///**
    // * rpc类型
    // */
    //private Integer rpcType = 0;

    /**
     * 日志类型 2:LOG_TYPE_RPC_CLIENT;1:LOG_TYPE_TRACE
     */
    private Integer logType = 0;

    /**
     * 入口的app名称
     */
    private String traceAppName;

    /**
     * 上游的app名称
     */
    private String upAppName;

    /**
     * 开始时间的时间戳
     */
    private long startTime;

    /**
     * 耗时
     */
    private long cost;

    /**
     * 中间件名称
     */
    private String middlewareName;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 远程IP
     */
    private String remoteIp;

    ///**
    // * 端口
    // */
    //private String port;

    /**
     * 结果编码
     */
    private String resultCode;

    ///**
    // * 请求大小
    // */
    //private long requestSize;

    ///**
    // * 响应大小
    // */
    //private long responseSize;

    /**
     * 请求内容
     */
    private String request;

    /**
     * 响应内容
     */
    private String response;

    /**
     * 是否是压测流量
     */
    private Boolean clusterTest;

    /**
     * 附加信息，如sql
     */
    private String callbackMsg;

    ///**
    // * 采样值
    // */
    //private Integer samplingInterval = 1;

    ///**
    // * 本地方法追踪时的ID
    // */
    //private String localId;

    ///**
    // * 通过rpc之间传递的attributes
    // */
    //private Map<String, String> attributes;

    ///**
    // * 本地的attributes，不通过rpc之前传递
    // */
    //private Map<String, String> localAttributes;

    ///**
    // * 是否是异步
    // */
    //private boolean async;

    ///**
    // * 日志原文
    // */
    //protected String log;

    ///**
    // * 日志版本号
    // */
    //protected String version;

    /**
     * 日志所属机器ip
     */
    private String hostIp;

    /**
     * agent id
     */
    private String agentId;

    /**
     * 负责人id
     */
    private Long customerId;


    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * rpc类型
     */
    private int rpcType;

    /**
     * 端口
     */
    private int port;

    /**
     * 是否有未知节点
     */
    private Boolean unknownFlag;

    public TraceNodeInfoParam() {
        gmtCreate = new Date();
        gmtModified = new Date();
    }
}
