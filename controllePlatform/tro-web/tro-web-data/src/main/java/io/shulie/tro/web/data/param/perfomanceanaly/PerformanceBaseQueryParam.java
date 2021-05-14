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

package io.shulie.tro.web.data.param.perfomanceanaly;

import lombok.Data;

/**
 * @ClassName PerformanceBaseQueryParam
 * @Description
 * @Author qianshui
 * @Date 2020/11/4 下午2:35
 */
@Data
public class PerformanceBaseQueryParam {

    private String agentId;

    private String appName;

    private String appIp;

    private String startTime;

    private String endTime;
}
