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

package io.shulie.tro.web.data.model.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 影子JOB任务配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "t_shadow_job_config")
public class ShadowJobConfigEntity extends BaseEntity {

    /**
     * 应用ID
     */
    @TableField(value = "application_id")
    private Long applicationId;

    /**
     * 任务名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * JOB类型 0-quartz、1-elastic-job、2-xxl-job
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 配置代码
     */
    @TableField(value = "config_code")
    private String configCode;

    /**
     * 0-可用 1-不可用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 0-可用 1-不可用
     */
    @TableField(value = "active")
    private Integer active;

    /**
     * 租户id
     */
    private Long customerId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

}
