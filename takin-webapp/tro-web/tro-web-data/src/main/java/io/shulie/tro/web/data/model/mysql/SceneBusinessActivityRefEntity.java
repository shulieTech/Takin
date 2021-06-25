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
@TableName(value = "t_scene_business_activity_ref")
public class SceneBusinessActivityRefEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 场景ID
     */
    @TableField(value = "scene_id")
    private Long sceneId;

    /**
     * 业务活动id
     */
    @TableField(value = "business_activity_id")
    private Long businessActivityId;

    /**
     * 业务活动名称
     */
    @TableField(value = "business_activity_name")
    private String businessActivityName;

    /**
     * 关联应用id，多个用,隔开
     */
    @TableField(value = "application_ids")
    private String applicationIds;

    /**
     * 目标值，json
     */
    @TableField(value = "goal_value")
    private String goalValue;

    /**
     * 绑定关系：jmx文件采样器名称
     */
    @TableField(value = "bind_ref")
    private String bindRef;

    @TableField(value = "is_deleted")
    private Integer isDeleted;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "create_name")
    private String createName;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(value = "update_name")
    private String updateName;
}
