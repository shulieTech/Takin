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

package io.shulie.tro.web.app.request.datasource;

import java.util.List;

import javax.validation.constraints.Size;

import com.pamirs.tro.common.constant.DataSourceVerifyTypeEnum;
import io.shulie.tro.common.beans.page.PagingDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-12-28
 */
@Data
@ApiModel("数据源查询对象")
public class DataSourceQueryRequest extends PagingDevice {

    @ApiModelProperty("数据库类型")
    private DataSourceVerifyTypeEnum type;

    @ApiModelProperty("标签")
    private List<Long> tagsIdList;

    @ApiModelProperty("数据源名称")
    @Size(max = 50)
    private String datasourceName;

    @ApiModelProperty("数据源地址")
    @Size(max = 100)
    private String jdbcUrl;
}
