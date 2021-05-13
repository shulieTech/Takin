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
 * 应用状态表
 */
@Data
@TableName(value = "pradar_app_agent")
public class PradarAppAgentEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 主机IP
     */
    @TableField(value = "app_group_id")
    private Long appGroupId;

    /**
     * 主机IP PORT
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 机器名
     */
    @TableField(value = "machine_room")
    private String machineRoom;

    /**
     * 主机IP
     */
    @TableField(value = "host_port")
    private String hostPort;

    /**
     * 主机名称
     */
    @TableField(value = "hostname")
    private String hostname;

    /**
     * agent状态，1：已上线，2：暂停中，3：已下线
     */
    @TableField(value = "agent_status")
    private Integer agentStatus;

    /**
     * agent版本
     */
    @TableField(value = "agent_version")
    private String agentVersion;

    /**
     * 插入时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "modify_time")
    private LocalDateTime modifyTime;

    @TableField(value = "deleted")
    private Integer deleted;
}
