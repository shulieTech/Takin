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

package io.shulie.tro.cloud.open.req.report;

import io.shulie.tro.cloud.open.req.HttpCloudRequest;
import lombok.Data;

/**
 * @ClassName WarnCreateReq
 * @Description 创建告警
 * @Author qianshui
 * @Date 2020/11/18 上午11:41
 */
@Data
public class WarnCreateReq extends HttpCloudRequest{
    private static final long serialVersionUID = 4235614311515898729L;

    private Long id;

    private Long ptId;

    private Long slaId;

    private String slaName;

    private Long businessActivityId;

    private String businessActivityName;

    private String warnContent;

    private Double realValue;

    private String warnTime;
}
