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

import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 数据字典基础数据表
 */
@Data
@TableName(value = "t_dictionary_data")
public class DictionaryDataEntity {
    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.INPUT)
    private String id;

    /**
     * 数据字典分类
     */
    @TableField(value = "DICT_TYPE")
    private String dictType;

    /**
     * 序号
     */
    @TableField(value = "VALUE_ORDER")
    private Integer valueOrder;

    /**
     * 值名称
     */
    @TableField(value = "VALUE_NAME")
    private String valueName;

    /**
     * 值代码
     */
    @TableField(value = "VALUE_CODE")
    private String valueCode;

    /**
     * 语言
     */
    @TableField(value = "LANGUAGE")
    private String language;

    /**
     * 是否启用
     */
    @TableField(value = "ACTIVE")
    private String active;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDate createTime;

    /**
     * 更新时间
     */
    @TableField(value = "MODIFY_TIME")
    private LocalDate modifyTime;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_USER_CODE")
    private String createUserCode;

    /**
     * 更新人
     */
    @TableField(value = "MODIFY_USER_CODE")
    private String modifyUserCode;

    /**
     * 备注信息
     */
    @TableField(value = "NOTE_INFO")
    private String noteInfo;

    /**
     * 版本号
     */
    @TableField(value = "VERSION_NO")
    private Long versionNo;
}
