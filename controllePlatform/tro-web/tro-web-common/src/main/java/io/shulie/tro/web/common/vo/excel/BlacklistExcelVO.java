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

package io.shulie.tro.web.common.vo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: mubai
 * @Date: 2021-03-01 10:13
 * @Description:
 */

@Data
public class BlacklistExcelVO implements Serializable {
    private static final long serialVersionUID = 295989495644985767L;

    /**
     * redisKey
     */
    @ExcelProperty(value ="redisKey",index = 0)
    private String redisKey ;

    /**
     * 是否可用(状态)
     */
    @ExcelProperty(value ="status",index = 1)
    private Integer useYn;

}
