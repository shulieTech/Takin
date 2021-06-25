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

package com.pamirs.tro.entity.domain.vo;

import java.util.List;
import java.util.Set;

/**
 * 链路拓扑图
 *
 * @author 298403
 */
public class TLinkTopologyShowVo {

    /**
     * 链路列表
     */
    private Set<TopologyLink> linkList;

    /**
     * 链路节点方向
     */
    private List<TopologyNode> nodeList;

    public Set<TopologyLink> getLinkList() {
        return linkList;
    }

    public void setLinkList(Set<TopologyLink> linkList) {
        this.linkList = linkList;
    }

    public List<TopologyNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<TopologyNode> nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public String toString() {
        return "TLinkTopologyShowVo{" +
            "linkList=" + linkList +
            ", nodeList=" + nodeList +
            '}';
    }

}
