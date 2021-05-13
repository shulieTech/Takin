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

package com.pamirs.tro.entity.domain.dto.report;

import java.io.Serializable;
import java.util.List;

import com.pamirs.tro.entity.domain.risk.ReportLinkDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ReportPradarLinkDTO
 * @Description 报告链路明细
 * @Author qianshui
 * @Date 2020/8/21 上午11:02
 */
@Data
@ApiModel
public class ReportPradarLinkDTO implements Serializable {

    private static final long serialVersionUID = 3770206572844505462L;

    @ApiModelProperty(value = "总时长")
    private Integer totalRT;

    private List<ReportLinkDetail> details;
}
