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
* @author 无涯
* @description:
* @date 2020/12/11 2:54 下午
*/
@Data
@TableName(value = "t_script_execute_result")
public class ScriptExecuteResultEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 实例id
     */
    @TableField(value = "script_deploy_id")
    private Long scripDeployId;

    /**
     * 脚本id
     */
    @TableField(value = "script_id")
    private Long scriptId;

    /**
     * 脚本id
     */
    @TableField(value = "script_version")
    private Integer scriptVersion;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;

    /**
     * 执行人
     */
    @TableField(value = "executor")
    private String executor;

    /**
     * 执行结果
     */
    @TableField(value = "success")
    private Boolean success;

    /**
     * 执行结果
     */
    @TableField(value = "result")
    private String result;

}
