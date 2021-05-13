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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 业务链路和技术链路关系表
 */
@Data
@TableName(value = "t_bs_tch_link")
public class BsTchLinkEntity {
    /**
     * 业务链路id
     */
    @TableField(value = "BLINK_ID")
    private Long blinkId;

    /**
     * 技术链路id
     */
    @TableField(value = "TLINK_ID")
    private Long tlinkId;

    /**
     * 技术链路横向排序编号
     */
    @TableField(value = "TLINK_ORDER")
    private Integer tlinkOrder;

    /**
     * 技术链路竖向排序编号
     */
    @TableField(value = "TLINK_BANK")
    private Integer tlinkBank;

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
}
