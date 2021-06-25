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

package io.shulie.tro.web.app.response.leakverify;

import java.util.List;

import io.shulie.tro.web.common.vo.component.SelectVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2021/1/6 5:43 下午
 * @Description:
 */
@Data
public class LeakVerifyDsResultResponse {

    @ApiModelProperty("数据源ID")
    private Long datasourceId;

    @ApiModelProperty("数据源名称")
    private String datasourceName;

    @ApiModelProperty("数据源地址")
    private String jdbcUrl;

    @ApiModelProperty("验证结果")
    private Integer status;

    @ApiModelProperty("告警级别")
    private Integer warningLevel;

    @ApiModelProperty("当前数据源的漏数汇总结果")
    private SelectVO statusResponse;

    @ApiModelProperty("sql验证详情")
    private List<LeakVerifyDetailResponse> detailResponseList;
}
