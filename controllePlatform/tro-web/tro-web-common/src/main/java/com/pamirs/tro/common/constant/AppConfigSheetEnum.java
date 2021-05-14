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

package com.pamirs.tro.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: mubai
 * @Date: 2021-02-25 10:13
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum AppConfigSheetEnum {
    DADABASE("影子库/表", 10, 0),
    GUARD("挡板", 3, 1),
    JOB("job任务", 5, 2),
    WHITE("白名单", 3, 3),
    CONSUMER("影子消费者", 2, 4),
    BLACK("黑名单", 2, 5);

    private final String desc;

    private final Integer columnNum;

    /**
     * sheet 下标
     */
    private final Integer sheetNumber;

}
