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

package io.shulie.tro.web.common.vo.application;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 影子库/表 导出 vo
 *
 * @author loseself
 * @date 2021/3/27 6:51 下午
 **/
@Data
public class ApplicationDsManageExportVO {

    @ExcelProperty(value ="dbType",index = 0)
    private Integer dbType;

    @ExcelProperty(value ="dsType",index = 1)
    private Integer dsType;

    @ExcelProperty(value ="url",index = 2)
    private String url;

    @ExcelProperty(value ="status",index = 3)
    private Integer status;

    @ExcelProperty(value ="xml config",index = 4)
    private String config ;

    @ExcelProperty(value ="userName",index = 5)
    private String userName;

    @ExcelProperty(value ="shadowDbUrl",index = 6)
    private String shadowDbUrl ;

    @ExcelProperty(value ="shadowDbUserName",index = 7)
    private String shadowDbUserName ;

    @ExcelProperty(value ="shadowDbPassword",index = 8)
    private String shadowDbPassword ;

    @ExcelProperty(value ="shadowDbMinIdle",index = 9)
    private String shadowDbMinIdle ;

    @ExcelProperty(value ="shadowDbMaxActive",index = 10)
    private String shadowDbMaxActive ;

}
