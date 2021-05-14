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

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_trace_manage")
public class TraceManageEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 追踪对象
     */
    @TableField(value = "trace_object")
    private String traceObject;

    /**
     * 报告id
     */
    @TableField(value = "report_id")
    private Long reportId;

    @TableField(value = "agent_id")
    private String agentId;

    /**
     * 服务器ip
     */
    @TableField(value = "server_ip")
    private String serverIp;

    /**
     * 进程id
     */
    @TableField(value = "process_id")
    private Integer processId;

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
     * 拓展字段
     */
    @TableField(value = "feature")
    private String feature;
}
