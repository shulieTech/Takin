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

package io.shulie.tro.web.data.param.exception;

import java.util.Date;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.param
 * @date 2021/1/4 7:39 下午
 */
@Data
public class ExceptionParam {
    private Long id;
    /**
     * 异常类型
     */
    private String type;

    /**
     * 异常编码
     */
    private String code;

    /**
     * agent异常编码
     */
    private String agentCode;

    /**
     * 异常描述
     */
    private String description;

    /**
     * 处理建议
     */
    private String suggestion;

    /**
     * 发生次数
     */
    private Long count;



    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 软删
     */
    private Boolean isDeleted;

    public ExceptionParam() {
        this.gmtCreate = new Date();
        this.gmtModified = new Date();
    }
}
