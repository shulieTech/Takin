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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 场景添加暂存表
 */
@Data
@TableName(value = "t_scene_add_temp_table")
public class SceneAddTempTableEntity {
    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 业务链路id
     */
    @TableField(value = "BUSENESS_ID")
    private String busenessId;

    /**
     * 技术链路id
     */
    @TableField(value = "TECH_ID")
    private String techId;

    /**
     * 业务链路的上级业务链路
     */
    @TableField(value = "PARENT_BUSINESS_ID")
    private String parentBusinessId;
}
