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

public class TPradarShadowTable {

    /**
     * 操作类型
     */
    private List<String> opType;

    /**
     * 表明
     */
    private String tableName;

    public List<String> getOpType() {
        return opType;
    }

    public void setOpType(List<String> opType) {
        this.opType = opType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "TPradarShadowTable{" +
            "opType='" + opType + '\'' +
            ", tableName='" + tableName + '\'' +
            '}';
    }

}
