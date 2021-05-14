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

package com.pamirs.tro.entity.domain.entity.report;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.shulie.tro.cloud.common.serialize.BigDecimalSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Report {

    private Long id;

    /**
     * 场景ID
     */
    private Long sceneId;

    /**
     * 客户ID
     */
    private Long customId;

    /**
     * 流量
     */
    private BigDecimal amount;

    /**
     * 场景名称
     */
    private String sceneName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 报表生成状态:0/就绪状态，1/已生成
     */
    private Integer status;

    /**
     * 报告类型；0普通场景，1流量调试
     */
    private Integer type;

    /**
     * 压测结论: 0/不通过，1/通过
     */
    private Integer conclusion;

    /**
     * 请求总数
     */
    private Long totalRequest;


    /**
     * 施压类型,0:并发,1:tps,2:自定义;不填默认为0
     */
    private Integer pressureType;

    /**
     * 平均并发数
     */
    private BigDecimal avgConcurrent;

    /**
     * 目标TPS
     */
    private Integer tps;

    /**
     * 平均tps
     */
    private BigDecimal avgTps;

    /**
     * 平均rt
     */
    @JsonProperty
    @JsonSerialize(using = BigDecimalSerialize.class)
    private BigDecimal avgRt;

    /**
     * 最大并发
     */
    private Integer concurrent;

    /**
     * 成功率
     */
    private BigDecimal successRate;

    /**
     * sa
     */
    private BigDecimal sa;

    /**
     * 操作人ID
     */
    private Long operateId;

    /**
     * 扩展字段，JSON数据格式
     */
    private String features;

    /**
     * 是否删除:0/正常，1、已删除
     */
    private Integer isDeleted;

    private Date gmtCreate;

    private Date gmtUpdate;

    private Long deptId;

    private Long userId;

    private Long scriptId;
}
