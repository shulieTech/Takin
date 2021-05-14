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

import java.util.List;

import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2021-02-22 11:30
 * @Description:
 */
@Data
public class ExcelSheetVO<T> {
    /**
     * sheet 名称
     */
    private String sheetName;
    /**
     * 数据列表
     */
    private List<T> data;
    /**
     * 数据类型
     */
    private Class<T> excelModelClass;
    /**
     * sheet编号
     */
    private Integer sheetNum;

}
