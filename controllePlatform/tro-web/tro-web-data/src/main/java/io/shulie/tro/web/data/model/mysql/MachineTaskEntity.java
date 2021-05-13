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

@Data
@TableName(value = "t_machine_task")
public class MachineTaskEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务类型：1、开通任务 2、注销任务
     */
    @TableField(value = "task_type")
    private Integer taskType;

    /**
     * 云平台id
     */
    @TableField(value = "platform_id")
    private Long platformId;

    /**
     * 云平台名称
     */
    @TableField(value = "platform_name")
    private String platformName;

    /**
     * 账号id
     */
    @TableField(value = "account_id")
    private Long accountId;

    /**
     * 账号名称
     */
    @TableField(value = "account_name")
    private String accountName;

    /**
     * 规格id
     */
    @TableField(value = "spec_id")
    private Long specId;

    /**
     * 规格描述
     */
    @TableField(value = "spec")
    private String spec;

    /**
     * 开通类型：1、长期开通 2、短期抢占
     */
    @TableField(value = "open_type")
    private Integer openType;

    /**
     * 开通时长：长期开通单位为月，短期抢占单位为小时
     */
    @TableField(value = "open_time")
    private Integer openTime;

    /**
     * 机器数量
     */
    @TableField(value = "machine_num")
    private Integer machineNum;

    /**
     * 更多配置
     */
    @TableField(value = "extend_config")
    private String extendConfig;

    /**
     * 任务状态：1、开通中 2、开通失败 3、开通成功 4、销毁中 5、销毁失败 6、销毁成功
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 状态 0: 正常 1： 删除
     */
    @TableField(value = "is_delete")
    private Boolean isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    @TableField(value = "gmt_update")
    private LocalDateTime gmtUpdate;
}
