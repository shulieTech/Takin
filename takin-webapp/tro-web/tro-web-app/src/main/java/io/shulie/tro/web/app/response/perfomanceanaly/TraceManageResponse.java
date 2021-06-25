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

package io.shulie.tro.web.app.response.perfomanceanaly;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zhaoyong
 */
@Data
public class TraceManageResponse implements Serializable {
    private static final long serialVersionUID = -6360254192231144843L;

    @ApiModelProperty(value = "traceManageId")
    private Long id;

    /**
     * 追踪对象
     */
    @ApiModelProperty(value = "追踪对象")
    private String traceObject;

    @ApiModelProperty(value = "是否该条数据采集结束")
    private boolean traceStatus;

    /**
     * 报告id
     */
    @ApiModelProperty(value = "报告id")
    private Long reportId;

    @ApiModelProperty(value = "agentId")
    private String agentId;

    /**
     * 服务器ip
     */
    @ApiModelProperty(value = "服务器ip")
    private String serverIp;

    /**
     * 进程id
     */
    @ApiModelProperty(value = "进程id")
    private Integer processId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 方法追踪实例，层级结构和信息
     */
    @ApiModelProperty(value = "方法追踪实例，层级结构和信息")
    private TraceManageDeployResponse traceManageDeployResponse;
}
