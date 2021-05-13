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

package com.pamirs.tro.entity.domain.query;

import java.io.Serializable;

import lombok.Data;

/**
 * @Author: 慕白
 * @Date: 2020-03-05 11:10
 * @Description:
 */

@Data
public class WhiteListQueryParam implements Serializable {

    private static final long serialVersionUID = 3987892771068578283L;

    private String applicationId;

    private Long id;

    private Integer currentPage;

    private Integer pageSize;

}
