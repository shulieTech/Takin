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

package io.shulie.amdb.request.query;

import io.shulie.amdb.common.request.PagingRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TAmdbAppBatchAppQueryRequest extends PagingRequest {
    /**
     * 应用名称
     */
    private List<String> appNames;

    /**
     * 应用ID
     */
    private List<Integer> appIds;

    /**
     * 客户Id
     */
    private String tenantKey;

    /**
     * 查询内容
     */
    private List<String> fields;

    public List<String> getFields() {
        if(fields==null){
            fields = new ArrayList<>();
        }
        return fields;
    }
}
