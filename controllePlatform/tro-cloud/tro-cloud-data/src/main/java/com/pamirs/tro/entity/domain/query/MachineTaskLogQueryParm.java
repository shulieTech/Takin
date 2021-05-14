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

import io.shulie.tro.common.beans.page.PagingDevice;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/5/13 下午9:23
 * @Description:
 */
@Data
public class MachineTaskLogQueryParm extends PagingDevice implements Serializable {
    private static final long serialVersionUID = -4979635191936291226L;
    private Long taskId;
}
