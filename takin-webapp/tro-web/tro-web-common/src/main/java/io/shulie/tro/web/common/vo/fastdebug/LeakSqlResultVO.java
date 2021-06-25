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

package io.shulie.tro.web.common.vo.fastdebug;

import io.shulie.tro.web.common.vo.component.SelectVO;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.common.vo.fastdebug
 * @date 2021/1/26 10:42 上午
 */
@Data
public class LeakSqlResultVO {
    private Integer order;
    private String sql;
    private SelectVO statusResponse;
}
