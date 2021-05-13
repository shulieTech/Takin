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

package com.pamirs.tro.entity.domain.vo.user;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/3/24 下午4:54
 * @Description:
 */
@Data
@ApiModel(value = "UserVo", description = "用户管理入参")
public class UserVo implements Serializable {
    private static final long serialVersionUID = -5220955768143660545L;
    @ApiModelProperty(name = "id", value = "用户id")
    private Long id;
    @ApiModelProperty(name = "name", value = "登录账号")
    private String name;
    @ApiModelProperty(name = "nick", value = "用户名称")
    private String nick;
    @ApiModelProperty(name = "key", value = "用户key")
    private String key;
    @ApiModelProperty(name = "password", value = "密码")
    private String password;
    @ApiModelProperty(name = "model", value = "使用模式")
    private Integer model;
    @ApiModelProperty(name = "role", value = "角色")
    private Integer role;
    @ApiModelProperty(name = "status", value = "状态")
    private Integer status;
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @ApiModelProperty(name = "updateTime", value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtUpdate;
    @ApiModelProperty(value = "可用流量")
    private String flowAmount;
}
