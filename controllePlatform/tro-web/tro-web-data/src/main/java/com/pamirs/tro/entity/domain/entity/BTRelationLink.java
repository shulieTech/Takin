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

/**
 * 说明: 业务链路和技术链路关联关系实体
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/12/26 17:01
 */
public class BTRelationLink extends RelationLinkModel {

    /**
     * 目标表
     */
    private String objTable;

    public String getObjTable() {
        return objTable;
    }

    public void setObjTable(String objTable) {
        this.objTable = objTable;
    }

    @Override
    public String toString() {
        return "BTRelationLink{" +
            "objTable='" + objTable + '\'' +
            ", parentLinkId=" + parentLinkId +
            ", childLinkId=" + childLinkId +
            ", linkOrder=" + linkOrder +
            ", linkBank=" + linkBank +
            '}';
    }
}
