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

package io.shulie.tro.web.data.result.leakverify;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2021/1/6 4:23 下午
 * @Description:
 */
@Data
public class LeakVerifyDetailResult {
    private Long id;
    /**
     * 验证结果id
     */
    private Long resultId;

    /**
     * 漏数sql
     */
    private String leakSql;

    /**
     * 是否漏数 0:正常;1:漏数;2:未检测;3:检测失败
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}
