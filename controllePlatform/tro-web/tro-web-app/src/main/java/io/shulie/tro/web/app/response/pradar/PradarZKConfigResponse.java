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

package io.shulie.tro.web.app.response.pradar;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.time.DateFormatUtils;

@Data
@ApiModel("pradar配置")
public class PradarZKConfigResponse {
    @ApiModelProperty("配置ID")
    int id;
    @ApiModelProperty("ZK路径")
    String zkPath;
    @ApiModelProperty("值类型:[String,Int,Boolean]")
    String type;
    @ApiModelProperty("值")
    String value;
    @ApiModelProperty("说明")
    String remark;
    @ApiModelProperty("创建时间")
    String createTime;
    @ApiModelProperty("更新时间")
    String modifyTime;
    @ApiModelProperty("是否可编辑")
    private Boolean canEdit = true;

    public PradarZKConfigResponse(int id, String zkPath, String type, String value, String remark, long createTime,
        long modifyTime) {
        this.id = id;
        this.zkPath = zkPath;
        this.type = type;
        this.value = value;
        this.remark = remark;
        this.createTime = DateFormatUtils.format(createTime, "yyyy-MM-dd HH:mm:ss");
        this.modifyTime = DateFormatUtils.format(modifyTime, "yyyy-MM-dd HH:mm:ss");
    }
}
