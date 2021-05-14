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
 * 链路检测表
 */
@Data
@TableName(value = "t_link_detection")
public class LinkDetectionEntity {
    /**
     * 主键id
     */
    @TableId(value = "LINK_DETECTION_ID", type = IdType.INPUT)
    private Long linkDetectionId;

    /**
     * 应用id
     */
    @TableField(value = "APPLICATION_ID")
    private Long applicationId;

    /**
     * 影子库整体同步检测状态(0未启用,1正在检测,2检测成功,3检测失败)
     */
    @TableField(value = "SHADOWLIB_CHECK")
    private Integer shadowlibCheck;

    /**
     * 影子库检测失败内容
     */
    @TableField(value = "SHADOWLIB_ERROR")
    private String shadowlibError;

    /**
     * 缓存预热校验状态(0未启用,1正在检测,2检测成功,3检测失败)
     */
    @TableField(value = "CACHE_CHECK")
    private Integer cacheCheck;

    /**
     * 缓存预热实时检测失败内容
     */
    @TableField(value = "CACHE_ERROR")
    private String cacheError;

    /**
     * 白名单校验状态(0未启用,1正在检测,2检测成功,3检测失败)
     */
    @TableField(value = "WLIST_CHECK")
    private Integer wlistCheck;

    /**
     * 白名单异常错误信息
     */
    @TableField(value = "WLIST_ERROR")
    private String wlistError;

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
