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

import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2021-02-24 19:37
 * @Description:
 */

@Data
public class WhiteListExcelVO implements Serializable {

    private static final long serialVersionUID = 6164987851541460315L;
    /**
     * 接口名称
     */
    @ExcelProperty(value ="interfaceName",index = 0)
    private String interfaceName;

    /**
     * 白名单类型
     */
    @ExcelProperty(value ="type",index = 1)
    private String type;

    /**
     * 字典分类
     */
    @ExcelProperty(value ="dictType",index = 2)
    private String dictType;

    /**
     * 白名单状态
     */
    @ExcelProperty(value ="status",index = 3)
    private Integer useYn;

    /**
     * 全局
     */
    @ExcelProperty(value ="isGlobal",index = 4)
    private String isGlobal;

    /**
     * 手工
     */
    @ExcelProperty(value ="isHandwork",index = 5)
    private String isHandwork;

    /**
     * 生效应用
     */
    @ExcelProperty(value ="effectAppNames",index = 6)
    private String effectAppNames;




}
