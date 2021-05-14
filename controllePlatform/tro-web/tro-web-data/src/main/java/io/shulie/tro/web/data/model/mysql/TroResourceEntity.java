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
@TableName(value = "t_tro_resource")
public class TroResourceEntity {
    /**
     * 资源id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父资源id
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 资源类型(0:菜单1:按钮 2:数据)
     */
    @TableField(value = "type")
    private Boolean type;

    /**
     * 资源编码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 资源名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 资源别名
     */
    @TableField(value = "alias")
    private String alias;

    /**
     * 资源值（菜单是url，应用是应用id）
     */
    @TableField(value = "value")
    private String value;

    /**
     * 排序
     */
    @TableField(value = "sequence")
    private Integer sequence;

    /**
     * 扩展字段，k-v形式存在
     */
    @TableField(value = "features")
    private String features;

    /**
     * 操作权限
     */
    @TableField(value = "action")
    private String action;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 是否有效 0:有效;1:无效
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;
}
