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

package io.shulie.tro.web.data.param.log;

import java.util.Date;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/9/24 4:43 下午
 * @Description:
 */
@Data
public class OperationLogCreateParam {
    /**
     * id
     */
    private Long id;

    /**
     * 操作模块-主模块
     */
    private String module;

    /**
     * 操作模块-子模块
     */
    private String subModule;

    /**
     * 操作类型
     */
    private String type;

    /**
     * 操作状态
     */
    private String status;

    /**
     * 操作内容描述
     */
    private String content;

    /**
     * 操作人id
     */
    private Long userId;

    /**
     * 操作人名称
     */
    private String userName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 状态 0: 正常 1： 删除
     */
    private Boolean isDelete;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtUpdate;
}
