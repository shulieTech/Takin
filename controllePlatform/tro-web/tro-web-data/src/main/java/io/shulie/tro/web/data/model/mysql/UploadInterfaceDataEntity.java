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
 * dubbo和job接口上传收集表
 */
@Data
@TableName(value = "t_upload_interface_data")
public class UploadInterfaceDataEntity {
    /**
     * 抽数表id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * APP名
     */
    @TableField(value = "APP_NAME")
    private String appName;

    /**
     * 接口值
     */
    @TableField(value = "INTERFACE_VALUE")
    private String interfaceValue;

    /**
     * 上传数据类型 查看字典  暂时 1 dubbo 2 job
     */
    @TableField(value = "INTERFACE_TYPE")
    private Integer interfaceType;

    /**
     * 插入时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;
}
