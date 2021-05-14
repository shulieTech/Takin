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
 * 业务字段采集配置表
 */
@Data
@TableName(value = "pradar_biz_key_config")
public class PradarBizKeyConfigEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业务key,不区分大小写
     */
    @TableField(value = "biz_key")
    private String bizKey;

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

    /**
     * 1，有效，0，删除
     */
    @TableField(value = "deleted")
    private Boolean deleted;
}
