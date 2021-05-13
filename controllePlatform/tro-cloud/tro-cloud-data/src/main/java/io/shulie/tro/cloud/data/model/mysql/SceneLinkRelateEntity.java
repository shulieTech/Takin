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

/**
 * 链路场景关联表
 */
@Data
@TableName(value = "t_scene_link_relate")
public class SceneLinkRelateEntity {
    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 链路入口
     */
    @TableField(value = "ENTRANCE")
    private String entrance;

    /**
     * 是否有效 0:有效;1:无效
     */
    @TableField(value = "IS_DELETED")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 业务链路ID
     */
    @TableField(value = "BUSINESS_LINK_ID")
    private String businessLinkId;

    /**
     * 技术链路ID
     */
    @TableField(value = "TECH_LINK_ID")
    private String techLinkId;

    /**
     * 场景ID
     */
    @TableField(value = "SCENE_ID")
    private String sceneId;

    /**
     * 当前业务链路ID的上级业务链路ID
     */
    @TableField(value = "PARENT_BUSINESS_LINK_ID")
    private String parentBusinessLinkId;

    /**
     * 前端数结构对象key
     */
    @TableField(value = "FRONT_UUID_KEY")
    private String frontUuidKey;
}
