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

package io.shulie.amdb.adaptors.instance.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_amdb_app
 * @author 
 */
@Data
public class TAmdbApp implements Serializable {
    /**
     * 应用ID
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;
    /**
     * 应用类型
     */
    private String appType="APP";

    /**
     * 应用负责人
     */
    private String appManager;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 工程版本
     */
    private String projectVersion;

    /**
     * git地址
     */
    private String gitUrl;

    /**
     * 发布包名称
     */
    private String publishPackageName;

    /**
     * 工程子模块
     */
    private String projectSubmoudle;

    /**
     * 客户Id
     */
    private String customerId;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 应用说明
     */
    private String remark;

    /**
     * 扩展字段
     */
    private String ext;

    /**
     * 标记位
     */
    private Integer flag;

    /**
     * 创建人编码
     */
    private String creator;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 更新人编码
     */
    private String modifier;

    /**
     * 更新人名称
     */
    private String modifierName;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModify;

    private static final long serialVersionUID = 1L;
}