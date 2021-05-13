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
 * @Date: 2021-02-24 16:16
 * @Description: 数据源导出配置
 */

@Data
public class DsExcelVO extends BaseRowModel implements Serializable {
    private static final long serialVersionUID = -8089242784938560042L;

    @ExcelProperty(value ="dbType",index = 0)
    private Byte dbType;

    @ExcelProperty(value ="dsType",index = 1)
    private Byte dsType;

    @ExcelProperty(value ="url",index = 2)
    private String url;

    @ExcelProperty(value ="config",index = 3)
    private String config ;

    @ExcelProperty(value ="parseConfig",index = 4)
    private String parseConfig ;

    @ExcelProperty(value ="status",index = 5)
    private Byte status;

}
