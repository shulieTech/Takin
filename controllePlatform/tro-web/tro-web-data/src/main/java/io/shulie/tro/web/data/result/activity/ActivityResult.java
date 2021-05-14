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

package io.shulie.tro.web.data.result.activity;

import java.util.Map;

import io.shulie.amdb.common.enums.ServerMiddlewareTypeEnum;
import io.shulie.tro.web.amdb.bean.common.EntranceTypeEnum;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-12-30
 */
@Data
public class ActivityResult {

    private Long activityId;

    private String activityName;

    private String applicationName;

    private String entranceName;

    private EntranceTypeEnum type;

    private String changeBefore;

    private String changeAfter;

    private Boolean isChange;

    private Long userId;

    private Long customerId;

    /**
     * 业务链路是否否核心链路 0:不是;1:是
     */
    private Integer isCore;

    private String activityLevel;

    /**
     * 业务域: 0:订单域", "1:运单域", "2:结算域
     */
    private String businessDomain;

    private String method;

    private String rpcType;

    private String extend;

    private String serviceName;

}
