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

package io.shulie.tro.web.data.result.linkmange;

import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.dto.linkmanage.TopologicalGraphEntity;
import com.pamirs.tro.entity.domain.dto.linkmanage.UnKnowNode;
import com.pamirs.tro.entity.domain.entity.linkmanage.figure.LinkEdge;
import com.pamirs.tro.entity.domain.entity.linkmanage.figure.LinkVertex;
import com.pamirs.tro.entity.domain.vo.linkmanage.MiddleWareEntity;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2019/11/29 15:40
 * @Description:技术链路出参
 */
@Data
public class TechLinkResult {

    private Long linkId;
    private String techLinkName;
    private String techLinkCount;
    private String isChange;
    private String changeRemark;
    private String bodyBefore;
    private String bodyAfter;
    private String linkNode;
    private String changeType;
    private java.util.Date createTime;
    private java.util.Date updateTime;
    private String entrance;
    private String candelete;
    private List<MiddleWareEntity> middleWareEntities;
    private TopologicalGraphEntity topologicalGraphEntity;
    private String requestParam;
    private List<UnKnowNode> unKnowNodeList;
    private Map<Object, LinkVertex> linkVertexMap;
    private List<LinkEdge> edges;

}
