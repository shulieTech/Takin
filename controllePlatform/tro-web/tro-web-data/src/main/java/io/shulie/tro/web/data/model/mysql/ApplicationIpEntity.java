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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 应用ip配置表
 */
@Data
@TableName(value = "t_application_ip")
public class ApplicationIpEntity {
    @TableId(value = "ID", type = IdType.INPUT)
    private String id;

    @TableField(value = "APPLICATION_NAME")
    private String applicationName;

    @TableField(value = "TYPE")
    private String type;

    @TableField(value = "IP")
    private String ip;

    @TableField(value = "SYSTEM_NAME")
    private String systemName;
}
