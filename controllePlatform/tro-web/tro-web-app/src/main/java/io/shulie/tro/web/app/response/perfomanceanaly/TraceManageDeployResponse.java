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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zhaoyong
 */
@Data
public class TraceManageDeployResponse implements Serializable {
    private static final long serialVersionUID = 1090375506083828146L;

    @ApiModelProperty(value = "方法追踪实例id")
    private Long id;

    /**
     * 追踪对象id
     */
    @ApiModelProperty(value = "追踪对象id")
    private Long traceManageId;

    /**
     * 追踪对象实例名称
     */
    @ApiModelProperty(value = "追踪对象实例名称")
    private String traceDeployObject;

    /**
     * 追踪凭证
     */
    @ApiModelProperty(value = "追踪凭证")
    private String sampleId;

    /**
     * 级别
     */
    @ApiModelProperty(value = "级别")
    private Integer level;

    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private Long parentId;

    /**
     * 0:没有;1:有;2未知
     */
    @ApiModelProperty(value = "0:没有;1:有;2未知")
    private Integer hasChildren;

    /**
     * 行号
     */
    @ApiModelProperty(value = "行号")
    private Integer lineNum;

    /**
     * 平均耗时
     */
    @ApiModelProperty(value = "平均耗时")
    private BigDecimal avgCost;

    /**
     * 中位数
     */
    private BigDecimal p50;
    private BigDecimal p90;
    private BigDecimal p95;
    private BigDecimal p99;
    private BigDecimal min;
    private BigDecimal max;


    /**
     * 状态0:待采集;1:采集中;2:采集结束;3:采集超时
     */
    @ApiModelProperty(value = "状态0:待采集;1:采集中;2:采集结束;3:采集超时")
    private Integer status;


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
     * 拓展字段
     */
    private String feature;


    @ApiModelProperty(value = "子集")
    private List<TraceManageDeployResponse> children;
}
