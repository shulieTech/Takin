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

package com.pamirs.tro.entity.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;

/**
 * 说明: 抽取的链路关联表实体
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/12/26 15:52
 */
public abstract class RelationLinkModel extends BaseEntity {

    /**
     * 父链路id
     */
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    protected long parentLinkId;

    /**
     * 子链路id
     */
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    protected long childLinkId;

    /**
     * 横向链路编号
     */
    protected int linkOrder;

    /**
     * 纵向链路编号
     */
    protected int linkBank;

    public long getParentLinkId() {
        return parentLinkId;
    }

    public void setParentLinkId(long parentLinkId) {
        this.parentLinkId = parentLinkId;
    }

    public long getChildLinkId() {
        return childLinkId;
    }

    public void setChildLinkId(long childLinkId) {
        this.childLinkId = childLinkId;
    }

    public int getLinkOrder() {
        return linkOrder;
    }

    public void setLinkOrder(int linkOrder) {
        this.linkOrder = linkOrder;
    }

    public int getLinkBank() {
        return linkBank;
    }

    public void setLinkBank(int linkBank) {
        this.linkBank = linkBank;
    }

    @Override
    public String toString() {
        return "RelationLink{" +
            "parentLinkId=" + parentLinkId +
            ", childLinkId=" + childLinkId +
            ", linkOrder=" + linkOrder +
            ", linkBank=" + linkBank +
            '}';
    }
}
