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
@TableName(value = "t_datasource_tag_ref")
public class DatasourceTagRefEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据源id
     */
    @TableField(value = "datasource_id")
    private Long datasourceId;

    /**
     * 标签id
     */
    @TableField(value = "tag_id")
    private Long tagId;

    @TableField(value = "gmt_create")
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_update")
    private LocalDateTime gmtUpdate;

    public static final String COL_ID = "id";

    public static final String COL_DATASOURCE_ID = "datasource_id";

    public static final String COL_TAG_ID = "tag_id";

    public static final String COL_GMT_CREATE = "gmt_create";

    public static final String COL_GMT_UPDATE = "gmt_update";
}
