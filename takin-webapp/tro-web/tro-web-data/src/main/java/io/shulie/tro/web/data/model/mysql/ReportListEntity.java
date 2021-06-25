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
 * 压测报告列表
 */
@Data
@TableName(value = "t_report_list")
public class ReportListEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "second_link_id")
    private Long secondLinkId;

    /**
     * 二级链路的TPS
     */
    @TableField(value = "second_link_tps")
    private Integer secondLinkTps;

    /**
     * 业务链路名称
     */
    @TableField(value = "link_service_name")
    private String linkServiceName;

    /**
     * 基础链路名称
     */
    @TableField(value = "link_basic_name")
    private String linkBasicName;

    /**
     * 基础链路实体
     */
    @TableField(value = "link_basic")
    private String linkBasic;

    /**
     * 压测开始时间
     */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /**
     * 压测结束时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /**
     * rt达标标准
     */
    @TableField(value = "rt_standard")
    private Integer rtStandard;

    /**
     * 0:压测完成;1:压测中
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "modify_time")
    private LocalDateTime modifyTime;

    /**
     * 是否已删除
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;
}
