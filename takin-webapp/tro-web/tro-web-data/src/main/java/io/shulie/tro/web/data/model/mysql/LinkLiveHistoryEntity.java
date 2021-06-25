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

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 链路探活历史数据
 */
@Data
@TableName(value = "t_link_live_history")
public class LinkLiveHistoryEntity {
    /**
     * 探活时间戳
     */
    @TableField(value = "live_time")
    private Long liveTime;

    /**
     * 探活ID
     */
    @TableField(value = "live_id")
    private Long liveId;

    /**
     * 事务成功率
     */
    @TableField(value = "success_rate")
    private BigDecimal successRate;
}
