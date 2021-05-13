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

/**
 * @Author: mubai
 * @Date: 2020-11-30 11:49
 * @Description:
 */

@Data
@TableName(value = "t_scene_tag_ref")
public class SceneTagRefEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 场景id
     */
    @TableField(value = "scene_id")
    private Long sceneId;

    /**
     * 标签id
     */
    @TableField(value = "tag_id")
    private Long tagId;

    @TableField(value = "gmt_create")
    private String gmtCreate;

    @TableField(value = "gmt_update")
    private String gmtUpdate;
}
