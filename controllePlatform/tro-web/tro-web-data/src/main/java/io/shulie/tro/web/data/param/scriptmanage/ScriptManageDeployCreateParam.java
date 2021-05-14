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

package io.shulie.tro.web.data.param.scriptmanage;

import lombok.Data;

/**
 * @author shulie
 */
@Data
public class ScriptManageDeployCreateParam {


    private Long scriptId;

    /**
     * 名称
     */
    private String name;

    /**
     * 关联类型(业务活动)
     */
    private String refType;

    /**
     * 关联值(活动id)
     */
    private String refValue;

    /**
     * 脚本类型;0为jmeter脚本
     */
    private Integer type;

    /**
     * 0代表新建，1代表调试通过
     */
    private Integer status;

    /**
     * 操作人id
     */
    private Long createUserId;

    /**
     * 操作人名称
     */
    private String createUserName;


    private Integer scriptVersion;

    /**
     * 拓展字段
     */
    private String feature;

    private Long customerId;

    /**
     * 描述
     */
    private String description;


}
