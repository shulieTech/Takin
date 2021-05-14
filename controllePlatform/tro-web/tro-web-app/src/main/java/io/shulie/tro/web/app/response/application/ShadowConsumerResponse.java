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

package io.shulie.tro.web.app.response.application;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.shulie.tro.web.common.enums.shadow.ShadowMqConsumerType;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2021-02-04
 */
@Data
public class ShadowConsumerResponse {

    private Long id;

    /**
     * AMDB梳理的没有入库，所有没有id
     */
    private String unionId;

    private ShadowMqConsumerType type;

    private String topicGroup;

    private Boolean enabled;

    private Date gmtCreate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtUpdate;

    private Boolean canEdit = true;

    private Boolean canRemove = true;

    private Boolean canEnableDisable = true;

    private Integer deleted ;

    private String feature ;
}
