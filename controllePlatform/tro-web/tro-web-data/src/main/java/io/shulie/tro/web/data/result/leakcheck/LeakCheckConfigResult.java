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

package io.shulie.tro.web.data.result.leakcheck;

import java.util.Date;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/12/31 2:35 下午
 * @Description:
 */
@Data
public class LeakCheckConfigResult {
    private Long id;

    /**
     * 业务活动id
     */
    private Long businessActivityId;

    /**
     * 数据源id
     */
    private Long datasourceId;

    /**
     * 漏数sql主键集合，逗号分隔
     */
    private String leakcheckSqlIds;

    /**
     * 租户id
     */
    private Long customerId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否有效 0:有效;1:无效
     */
    private Boolean isDeleted;
}
