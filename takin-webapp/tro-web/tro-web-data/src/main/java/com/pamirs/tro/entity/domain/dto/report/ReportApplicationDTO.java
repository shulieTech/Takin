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

import java.util.List;

import com.google.common.collect.Lists;
import lombok.Data;

/**
 * @ClassName ReportApplicationDTO
 * @Description
 * @Author qianshui
 * @Date 2020/11/6 下午4:53
 */
@Data
public class ReportApplicationDTO {

    private ReportDetailDTO reportDetail;

    private List<String> applicationNames = Lists.newArrayList();
}
