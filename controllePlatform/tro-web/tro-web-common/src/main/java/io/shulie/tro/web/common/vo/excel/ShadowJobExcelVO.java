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

import java.io.Serializable;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2021-02-24 19:24
 * @Description:
 */

@Data
public class ShadowJobExcelVO extends BaseRowModel implements Serializable {
    private static final long serialVersionUID = -4797605438752120965L;

    @ExcelProperty(value ="name",index = 0)
    private String name;

    @ExcelProperty(value ="type",index = 1)
    private Integer type;

    @ExcelProperty(value ="configCode",index = 2)
    private String configCode;

    @ExcelProperty(value ="status",index = 3)
    private Integer status;

    @ExcelProperty(value ="active",index = 4)
    private Integer active;

    @ExcelProperty(value ="remark",index = 5)
    private String remark;

}
