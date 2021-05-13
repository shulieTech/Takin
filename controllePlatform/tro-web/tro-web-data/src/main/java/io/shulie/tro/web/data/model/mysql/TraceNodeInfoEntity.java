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
 * @date 2021/1/6 4:05 下午
 */
@Data
@TableName(value = "t_trace_node_info")
public class TraceNodeInfoEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用名
     */
    @TableField(value = "app_name")
    private String appName;

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
     * rpcid
     */
    @TableField(value = "log_type")
    private Integer logType;

    /**
     * 入口的app名称
     */
    @TableField(value = "trace_app_name")
    private String traceAppName;

    /**
     * 上游的app名称
     */
    @TableField(value = "up_app_name")
    private String upAppName;

    /**
     * 开始时间的时间戳
     */
    @TableField(value = "start_time")
    private Long startTime;

    /**
     * 耗时
     */
    @TableField(value = "cost")
    private Long cost;

    /**
     * 中间件名称
     */
    @TableField(value = "middleware_name")
    private String middlewareName;

    /**
     * 服务名
     */
    @TableField(value = "service_name")
    private String serviceName;

    /**
     * 方法名
     */
    @TableField(value = "method_name")
    private String methodName;

    /**
     * 远程IP
     */
    @TableField(value = "remote_ip")
    private String remoteIp;

    /**
     * 结果编码
     */
    @TableField(value = "result_code")
    private String resultCode;

    /**
     * 请求内容
     */
    @TableField(value = "request")
    private String request;

    /**
     * 响应内容
     */
    @TableField(value = "response")
    private String response;

    /**
     * 1是压测流量，0是业务流量
     */
    @TableField(value = "cluster_test")
    private Boolean clusterTest;

    /**
     * 日志所属机器ip
     */
    @TableField(value = "host_ip")
    private String hostIp;

    /**
     * agent id
     */
    @TableField(value = "agent_id")
    private String agentId;

    /**
     * 附加信息，如sql
     */
    @TableField(value = "callback_msg")
    private String callbackMsg;


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

    /**
     * rpc类型
     */
    @TableField(value = "`rpc_type`")
    private Integer rpcType = 0;

    /**
     * 端口
     */
    @TableField(value = "`port`")
    private Integer port;

    /**
     * 是否下游有未知节点
     */
    @TableField(value = "`is_upper_unknown_node`")
    private Boolean isUpperUnknownNode;
}