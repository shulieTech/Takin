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

package io.shulie.tro.web.app.input.scriptmanage;

import lombok.Data;

/**
* @Package io.shulie.tro.web.app.input.scriptmanage
* @author 无涯
* @description:
* @date 2020/12/8 7:26 下午
*/
@Data
public class ShellScriptManageUpdateInput {

    /**
     * 脚本实例id
     */
    private Long scriptDeployId;

    ///**
    // * 原脚本实例Id
    // */
    //private Long oldScriptDeployId;


    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 脚本版本
     */
    private Integer scriptVersion;

    /**
     * 脚本类型;0为jmeter脚本,1为shell脚本
     */
    private Integer type;

    /**
     * shell脚本内容
     */
    private String content;

    /**
     * 文件类型
     */
    private Integer fileType;


}
