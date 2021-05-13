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

package io.shulie.tro.web.data.param.tracemanage;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zhaoyong
 */
@Data
public class TraceManageDeployUpdateParam {

    private Long id;

    /**
     * 追踪对象id
     */
    private Long traceManageId;

    /**
     * 追踪对象实例名称
     */
    private String traceDeployObject;

    /**
     * 追踪凭证
     */
    private String sampleId;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 0:没有;1:有;2未知
     */
    private Integer hasChildren;

    /**
     * 行号
     */
    private Integer lineNum;

    /**
     * 评价耗时
     */
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
     * 状态0:待采集;1:采集中;2:采集结束
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 拓展字段,用于填写agent错误信息
     */
    private String feature;
}
