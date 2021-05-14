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

package io.shulie.tro.web.data.result.risk;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.collect.Lists;
import lombok.Data;

/**
 * @Author: xingchen
 * @ClassName: LinkDataResult
 * @Package: com.pamirs.tro.web.api.service.risk.vo
 * @Date: 2020/7/2915:25
 * @Description:
 */
@Data
public class LinkDataResult {
    private Long reportId;

    /**
     * 当前应用
     */
    private String appName;

    /**
     * 调用的接口信息/db/mq
     */
    private String serviceName;

    /**
     * 调用的接口类型
     */
    private String eventType;

    /**
     * 调用的接口信息/db/mq
     */
    private String event;

    /**
     * tps
     */
    private Double tps = 0D;

    /**
     * rt
     */
    private Double rt = 0D;

    /**
     * 错误数
     */
    private Integer errorCount = 0;

    /**
     * 同级计算权重值
     */
    private BigDecimal calcWeight;

    /**
     * 实际权重值（要和子节点分摊）
     */
    private BigDecimal realWeight;

    /**
     * 所在节点总数
     */
    private Integer nodeCount;

    /**
     * 子链路
     */
    private List<LinkDataResult> subLink = Lists.newArrayList();
}
