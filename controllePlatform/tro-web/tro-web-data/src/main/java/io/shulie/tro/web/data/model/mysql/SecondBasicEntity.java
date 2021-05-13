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
 * 二级链路基础链路关联关系表
 */
@Data
@TableName(value = "t_second_basic")
public class SecondBasicEntity {
    /**
     * 二级链路id
     */
    @TableField(value = "SECOND_LINK_ID")
    private Long secondLinkId;

    /**
     * 业务链路id
     */
    @TableField(value = "BASIC_LINK_ID")
    private Long basicLinkId;

    /**
     * 业务链路编号(每一条链路的排序)横向
     */
    @TableField(value = "BLINK_ORDER")
    private Integer blinkOrder;

    /**
     * 业务链路等级(二级链路下有基础链路1，基础链路2等)竖向
     */
    @TableField(value = "BLINK_BANK")
    private Integer blinkBank;

    /**
     * 插入时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 变更时间
     */
    @TableField(value = "UPDATE_TIME")
    private LocalDateTime updateTime;
}
