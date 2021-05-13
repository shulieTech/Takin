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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
* @Package io.shulie.tro.web.data.model.mysql
* @author 何仲奇
* @date 2021/4/14 上午10:13
*/
@Data
@TableName(value = "t_whitelist_effective_app")
public class WhitelistEffectiveAppEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接口名
     */
    @TableField(value = "wlist_id")
    private Long wlistId;

    /**
     * 接口名
     */
    @TableField(value = "interface_name")
    private String interfaceName;

    /**
     * 类型
     */
    @TableField(value = "type")
    private String type;

    /**
     * 生效应用
     */
    @TableField(value = "EFFECTIVE_APP_NAME")
    private String effectiveAppName;

    /**
     * 租户id
     */
    @TableField(value = "customer_id")
    private Long customerId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified")
    private Date gmtModified;

    /**
     * 软删
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;
}