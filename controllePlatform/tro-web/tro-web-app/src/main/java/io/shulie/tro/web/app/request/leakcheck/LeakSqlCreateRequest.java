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

package io.shulie.tro.web.app.request.leakcheck;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/12/31 3:27 下午
 * @Description:
 */
@Data
@ApiModel("漏数配置对象")
public class LeakSqlCreateRequest {

    @ApiModelProperty("业务活动id")
    @NotNull
    private Long businessActivityId;

    @ApiModelProperty("数据源id")
    @NotNull
    private Long datasourceId;

    @ApiModelProperty("sql集合")
    @NotEmpty
    @Size(max = 100)
    private List<String> sqls;
}
