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
@TableName(value = "t_scene_manage")
public class SceneManageEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户id
     */
    @TableField(value = "custom_id")
    private Long customId;

    /**
     * 场景名称
     */
    @TableField(value = "scene_name")
    private String sceneName;

    /**
     * 参考数据字典 场景状态
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 最新压测时间
     */
    @TableField(value = "last_pt_time")
    private LocalDateTime lastPtTime;

    /**
     * 施压配置
     */
    @TableField(value = "pt_config")
    private String ptConfig;

    /**
     * 脚本类型：0-Jmeter 1-Gatling
     */
    @TableField(value = "script_type")
    private Integer scriptType;

    /**
     * 是否删除：0-否 1-是
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_name")
    private String createName;

    /**
     * 最后修改时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 最后修改人
     */
    @TableField(value = "update_name")
    private String updateName;
}