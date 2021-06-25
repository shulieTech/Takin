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
 * @Date: 2021-03-01 10:13
 * @Description:
 */

@Data
public class ShadowConsumerExcelVO implements Serializable {
    private static final long serialVersionUID = 295989495644985767L;

    /**
     * topic
     */
    @ExcelProperty(value ="topicGroup",index = 0)
    private String topicGroup;

    /**
     * MQ类型
     */
    @ExcelProperty(value ="type",index = 1)
    private String type;

    /**
     * 是否可用
     */
    @ExcelProperty(value ="status",index = 2)
    private Integer status;

}
