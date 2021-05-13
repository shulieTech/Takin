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
 * 链路瓶颈
 */
@Data
@TableName(value = "t_link_bottleneck")
public class LinkBottleneckEntity {
    /**
     * id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 应用名
     */
    @TableField(value = "APP_NAME")
    private String appName;

    /**
     * 关键字(TOPIC,JOB信息)
     */
    @TableField(value = "KEY_WORDS")
    private String keyWords;

    /**
     * 瓶颈类型(1、基础资源负载及异常，2、异步处理，3、TPS/RT稳定性，4，RT响应时间)
     */
    @TableField(value = "BOTTLENECK_TYPE")
    private Boolean bottleneckType;

    /**
     * 瓶颈等级(1、严重，2、普通，3、正常)
     */
    @TableField(value = "BOTTLENECK_LEVEL")
    private Boolean bottleneckLevel;

    /**
     * 瓶颈文本
     */
    @TableField(value = "TEXT")
    private String text;

    /**
     * JSON串
     */
    @TableField(value = "CONTENT")
    private String content;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "MODIFY_TIME")
    private LocalDateTime modifyTime;

    /**
     * 是否有效(Y/N)
     */
    @TableField(value = "ACTIVE")
    private String active;
}
