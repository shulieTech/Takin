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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName PerformanceThreadDataEntity
 * @Description 性能线程数据表
 * @Author qianshui
 * @Date 2020/11/4 下午1:53
 */
@Data
@TableName(value = "t_performance_thread_data")
public class PerformanceThreadDataEntity {

    @TableField(value = "base_id")
    private Long baseId;

    @TableField(value = "agent_id")
    private String agentId;

    @TableField(value = "app_name")
    private String appName;

    @TableField(value = "app_ip")
    private String appIP;

    @TableField(value = "timestamp")
    private String timestamp;

    @TableField(value = "thread_data")
    private String threadData;

    @TableField(value = "gmt_create")
    private Date gmtCreate;

}
