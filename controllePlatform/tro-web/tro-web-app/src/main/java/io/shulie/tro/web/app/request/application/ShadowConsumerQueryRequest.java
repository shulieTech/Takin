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

package io.shulie.tro.web.app.request.application;

import javax.validation.constraints.NotNull;

import io.shulie.tro.common.beans.page.PagingDevice;
import io.shulie.tro.web.common.enums.shadow.ShadowMqConsumerType;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2021-02-05
 */
@Data
public class ShadowConsumerQueryRequest extends PagingDevice {

    private ShadowMqConsumerType type;

    private Boolean enabled;

    private String topicGroup;

    @NotNull
    private Long applicationId;
}
