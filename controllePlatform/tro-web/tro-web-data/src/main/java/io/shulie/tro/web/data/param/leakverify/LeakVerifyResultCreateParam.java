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

package io.shulie.tro.web.data.param.leakverify;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2021/1/5 8:16 下午
 * @Description:
 */
@Data
public class LeakVerifyResultCreateParam {
    private Long id;

    /**
     * 引用类型 0:压测场景;1:业务流程;2:业务活动
     */
    private Integer refType;

    /**
     * 引用id
     */
    private Long refId;

    /**
     * 报告id
     */
    private Long reportId;

    /**
     * 数据源id
     */
    private Long dbresourceId;

    /**
     * 数据源名称
     */
    private String dbresourceName;

    /**
     * 数据源地址
     */
    private String dbresourceUrl;

    /**
     * 是否漏数 0:正常;1:漏数;2:未检测;3:检测失败
     */
//    private Integer status;

    /**
     * 租户id
     */
    private Long customerId;

    /**
     * 用户id
     */
    private Long userId;
}
