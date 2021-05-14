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

package io.shulie.tro.web.amdb.bean.result.application;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-10-16
 */
@Data
public class ApplicationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 应用 id
    private Long appId;
    // 应用名称
    private String appName;
    // 应用描述
    private String appSummary;
    // app最新发布版本
    private String appVersionCode;
    // 应用负责人
    private String appManagerName;
    // 应用最后修改时间
    private Date appUpdateTime;
    // 应用是否异常
    private Boolean appIsException;
    //应用中间接jar包列表
    private LibraryDTO[] library;
    //实例节点数
    private InstanceInfoDTO instanceInfo;
}
