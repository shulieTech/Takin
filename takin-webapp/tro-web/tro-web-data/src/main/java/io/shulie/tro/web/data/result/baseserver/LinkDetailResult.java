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

package io.shulie.tro.web.data.result.baseserver;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xingchen
 * @ClassName: LinkDetailResult
 * @Package: com.pamirs.tro.web.api.service.risk.vo
 * @Date: 2020/7/2915:25
 * @Description:
 */
@Data
@ApiModel
public class LinkDetailResult implements Serializable {
    private String uuid;
    /**
     * 当前应用
     */
    @ApiModelProperty(value = "应用")
    private String appName;

    /**
     * 调用的接口信息/db/mq
     */
    @ApiModelProperty(value = "服务")
    private String serviceName;

    /**
     * 调用的接口类型
     */
    @ApiModelProperty(value = "类型")
    private String eventType;

    /**
     * 调用的接口信息/db/mq
     */
    private String event;

    /**
     * 总的请求数
     */
    @ApiModelProperty(value = "请求数")
    private Integer totalCount = 0;

    /**
     * 请求比
     */
    @ApiModelProperty(value = "请求比")
    private Double requestRate = 0D;

    /**
     * tps
     */
    @ApiModelProperty(value = "tps")
    private Double tps = 0D;

    /**
     * 最大RT
     */
    @ApiModelProperty(value = "最大RT")
    private Double maxRt = 0D;

    /**
     * 最小rt
     */
    @ApiModelProperty(value = "最小RT")
    private Double minRt = 0D;

    /**
     * 平均RT
     */
    @ApiModelProperty(value = "平均RT")
    private Double avgRt = 0D;

    /**
     * traceId
     */
    @ApiModelProperty(value = "traceId")
    private String traceId;

    @ApiModelProperty(value = "瓶颈标识")
    private Integer bottleneckFlag;

    @ApiModelProperty(value = "偏移量")
    private Integer offset;

    /**
     * 子链路
     */
    private List<LinkDetailResult> children;
}
