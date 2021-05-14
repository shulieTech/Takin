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

package io.shulie.tro.web.data.param.machine;

import com.pamirs.tro.entity.domain.PagingDevice;
import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-11-16 11:31
 * @Description:
 */

@Data
public class PressureMachineLogQueryParam extends PagingDevice {

    private String startTime;

    private String endTime;

    private Long machineId ;
}
