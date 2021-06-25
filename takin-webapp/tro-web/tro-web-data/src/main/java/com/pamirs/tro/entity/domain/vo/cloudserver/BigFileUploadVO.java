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

package com.pamirs.tro.entity.domain.vo.cloudserver;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-05-12 14:43
 * @Description:
 */

@Data
public class BigFileUploadVO implements Serializable {
    private static final long serialVersionUID = 3132972991439604004L;

    private String fileName;

    private String topic;

    private Long sceneId;

    private String license;

    private List<String> data;

    private Boolean generateVirtualFile = false;

    private Integer dataCount;

}
