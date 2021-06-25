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

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 应用基本信息表
 */
@Data
@TableName(value = "pradar_app_info")
public class PradarAppInfoEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用名
     */
    @TableField(value = "app_name")
    private String appName;

    /**
     * 负责人姓名
     */
    @TableField(value = "manager_name")
    private String managerName;

    /**
     * 产品线
     */
    @TableField(value = "product_line")
    private String productLine;

    /**
     * 分组
     */
    @TableField(value = "app_group")
    private String appGroup;

    /**
     * 主机IP
     */
    @TableField(value = "host_ip")
    private String hostIp;

    /**
     * 最后更新时间
     */
    @TableField(value = "modify_time")
    private LocalDateTime modifyTime;

    /**
     * 插入时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 0：未删除 1：删除
     */
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * PE
     */
    @TableField(value = "PE")
    private String pe;

    /**
     * 应用管理员
     */
    @TableField(value = "app_manager")
    private String appManager;

    /**
     * SCM管理员
     */
    @TableField(value = "SCM")
    private String scm;

    /**
     * DBA
     */
    @TableField(value = "DBA")
    private String dba;

    /**
     * 工号
     */
    @TableField(value = "job_number")
    private String jobNumber;

    /**
     * 电话号码
     */
    @TableField(value = "sms_number")
    private String smsNumber;

    /**
     * 是否反向注册:null:否;1:否;2:是
     */
    @TableField(value = "reverser_registration")
    private String reverserRegistration;
}
