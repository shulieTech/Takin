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

package io.shulie.tro.web.app.response.datasource;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.shulie.tro.web.app.response.tagmanage.TagManageResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-12-28
 */
@Data
@ApiModel("数据源列表返回的简单对象")
public class DatasourceListResponse {

    @ApiModelProperty("数据源ID")
    private Long datasourceId;

    @ApiModelProperty("数据源名称")
    private String datasourceName;

    @ApiModelProperty("数据源类型")
    private DataSourceTypeResponse type;

    @ApiModelProperty("数据源连接")
    private String jdbcUrl;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("标签")
    private List<TagManageResponse> tags;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtUpdate;
}
