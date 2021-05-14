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

package com.pamirs.tro.entity.domain.entity.scenemanage;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class SceneManage implements Serializable {

    private static final long serialVersionUID = -5922461634087976404L;

    private Long id;

    private Long customId;

    private String sceneName;

    private Integer status;

    private Date lastPtTime;

    private Integer scriptType;

    private Integer type;

    private Integer isDeleted;

    private Date createTime;

    private String features;

    private String createName;

    private Date updateTime;

    private String updateName;

    private String ptConfig;

    private Long deptId;

    private Long userId;
}
