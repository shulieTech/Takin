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
 * @author 无涯
 * @Package io.shulie.tro.web.data.model.mysql
 * @date 2020/12/4 11:08 上午
 */
@Data
@TableName(value = "t_performance_thread_stack_data")
public class PerformanceThreadStackDataEntity {

    @TableField(value = "thread_stack")
    private String threadStack;

    @TableField(value = "thread_stack_link")
    private Long threadStackLink;

    @TableField(value = "gmt_create")
    private Date gmtCreate;
}
