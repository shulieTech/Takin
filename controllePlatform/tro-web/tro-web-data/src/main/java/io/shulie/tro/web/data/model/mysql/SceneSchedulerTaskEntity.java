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
 * @Date: 2020-11-30 21:17
 * @Description:
 */

@Data
@TableName(value = "t_scene_scheduler_task")
public class SceneSchedulerTaskEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "scene_id")
    private Long sceneId ;

    @TableField(value = "user_id")
    private Long userId ;

    @TableField(value = "content")
    private String content ;

    /**
     * 0：待执行，1:执行中；2:已执行
     */
    @TableField(value = "is_executed")
    private Integer isExecuted ;

    @TableField(value = "execute_time")
    private Date executeTime ;

    @TableField(value = "is_deleted")
    private Boolean isDeleted ;

    @TableField(value = "gmt_create")
    private Date gmtCreate ;

    @TableField(value = "gmt_update")
    private Date gmtUpdate ;


}
