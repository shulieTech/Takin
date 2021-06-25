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

package io.shulie.tro.web.app.request.scriptmanage;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.shulie.tro.common.beans.page.PagingDevice;
import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
public class ScriptManageDeployPageQueryRequest extends PagingDevice {
    private static final long serialVersionUID = 4907165876058485892L;

    /**
     * 脚本名称
     */
    @JsonProperty("scriptName")
    private String name;

    /**
     * tagId列表
     */
    @JsonProperty("tags")
    private List<Long> tagIds;

    /**
     * 业务活动id
     */
    @JsonProperty("businessActivity")
    private String businessActivityId;

    /**
     * 业务流程id
     */
    @JsonProperty("businessFlow")
    private String businessFlowId;

}
