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
 * 链路探活配置表
 */
@Data
@TableName(value = "t_link_live_config")
public class LinkLiveConfigEntity {
    /**
     * 主键ID
     */
    @TableId(value = "live_id", type = IdType.AUTO)
    private Long liveId;

    /**
     * 应用名称
     */
    @TableField(value = "application_name")
    private String applicationName;

    /**
     * 接口名称
     */
    @TableField(value = "interface_name")
    private String interfaceName;

    /**
     * 接口类型
     */
    @TableField(value = "interface_type")
    private String interfaceType;

    /**
     * 告警阈值，事务成功率
     */
    @TableField(value = "threshold")
    private String threshold;

    /**
     * 告警策略，默认是WX_OFFICIAL
     */
    @TableField(value = "alarm_policy")
    private String alarmPolicy;

    /**
     * 探活取数来源,1SPT 2PRADAR
     */
    @TableField(value = "live_source")
    private String liveSource;

    /**
     * 是否入口压测链路，是Y 否N
     */
    @TableField(value = "entry_flag")
    private String entryFlag;

    /**
     * 场景名称，仅入口或SPT来源时需要填写
     */
    @TableField(value = "scene_name")
    private String sceneName;

    /**
     * 阿斯旺ID，仅入口或SPT来源时需要填写
     */
    @TableField(value = "aswan_id")
    private String aswanId;

    /**
     * 探活状态，0未开启, 1开启
     */
    @TableField(value = "live_status")
    private String liveStatus;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}
