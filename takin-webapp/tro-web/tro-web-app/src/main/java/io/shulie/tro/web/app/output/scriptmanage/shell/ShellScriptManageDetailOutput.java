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

package io.shulie.tro.web.app.output.scriptmanage.shell;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author shulie
 */
@Data
public class ShellScriptManageDetailOutput implements Serializable {
    private static final long serialVersionUID = 573256048970054542L;

    /**
     * 脚本id
     */
    private Long id;

    /**
     * 脚本实例id
     */
    private Long scriptDeployId;
    /**
     * 名称
     */
    @JsonProperty("scriptName")
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 脚本类型;0为jmeter脚本,1为shell脚本
     */
    @JsonProperty("scriptType")
    private Integer type;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updateTime")
    private Date gmtUpdate;

    /**
     * 脚本版本
     */
    private Integer scriptVersion;


    /**
     * shell脚本内容
     */
    private String content;

    /**
     * 文件类型
     */
    private Integer fileType;
    /**
     * 版本列表
     */
    private List<Map<String,Object>> versions;

}
