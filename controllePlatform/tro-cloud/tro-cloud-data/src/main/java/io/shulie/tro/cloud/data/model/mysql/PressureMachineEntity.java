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

package io.shulie.tro.cloud.data.model.mysql;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_pressure_machine")
public class PressureMachineEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务id
     */
    @TableField(value = "task_id")
    private Long taskId;

    /**
     * 压力机公网IP
     */
    @TableField(value = "public_ip")
    private String publicIp;

    /**
     * 压力机内网IP
     */
    @TableField(value = "private_ip")
    private String privateIp;

    /**
     * 实例ID
     */
    @TableField(value = "instance_id")
    private String instanceId;

    /**
     * 实例名称
     */
    @TableField(value = "instance_name")
    private String instanceName;

    /**
     * 区域ID
     */
    @TableField(value = "region_id")
    private String regionId;

    /**
     * 区域ID
     */
    @TableField(value = "region_name")
    private String regionName;

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
     * 第三方规格描述
     */
    @TableField(value = "ref_spec")
    private String refSpec;

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
     * 过期时间
     */
    @TableField(value = "expire_date")
    private LocalDateTime expireDate;

    /**
     * 状态 1、开通中 2、开通成功 3、开通失败 4：启动中 5、启动成功 6、启动失败 7、初始化中 8、初始化失败 9、运行中 10、销毁中 11、已过期 12、已锁定 13、销毁失败 14、已销毁
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 拓展属性
     */
    @TableField(value = "feature")
    private String feature;

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
