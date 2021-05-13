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

package com.pamirs.tro.entity.domain.entity.linkmanage.figure;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.linkmanage.VertexOpData;
import com.pamirs.tro.entity.domain.vo.linkmanage.MiddleWareEntity;
import lombok.Data;

/**
 * vernon
 */
@Data
public class LinkVertex {
    List<MiddleWareEntity> middleWare;
    private Object id;
    private String applicationName;
    private Integer rpcType;
    private Long updateTime;
    private VertexOpData vertexOpData;

    public LinkVertex(Object id, String applicationName, Integer rpcType, Long updateTime,
        List<MiddleWareEntity> middleWare, VertexOpData vertexOpData) {
        this.id = id;
        this.applicationName = applicationName;
        this.rpcType = rpcType;
        this.updateTime = updateTime;
        this.middleWare = middleWare;
        this.vertexOpData = vertexOpData;
    }

}
