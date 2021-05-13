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

package com.pamirs.tro.entity.domain.risk;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @Author: xingchen
 * @ClassName: BaseApp
 * @Package: com.pamirs.tro.web.api.service.risk.vo
 * @Date: 2020/7/2913:29
 * @Description:
 */
@Data
public class BaseAppVo {
    private Long reportId;
    private String appName;
    private String appIp;
    private String agentIp;
    private Integer core;
    private BigDecimal memory;
    private BigDecimal disk;
    private BigDecimal mbps;
}
