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

package io.shulie.tro.web.data.param.application;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/11/4 3:24 下午
 * @Description:
 */
@Data
public class ApplicationCreateParam {

    //@Field applicationId : 应用编号
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long applicationId;

    //@Field applicationName : 应用名称
    @NotBlank(message = "应用名称不能为空")
    private String applicationName;

    //@Field applicationName : 应用说明
    @NotBlank(message = "应用说明不能为空")
    private String applicationDesc;

    //@Field ddlScriptPath : 影子库表结构脚本路径`
    @NotBlank(message = "影子库表结构脚本路径不能为空")
    private String ddlScriptPath;

    //@Field cleanScriptPath : 数据清理脚本路径
    @NotBlank(message = "数据清理脚本路径不能为空")
    private String cleanScriptPath;

    //@Field readyScriptPath : 基础数据准备脚本路径
    @NotBlank(message = "基础数据准备脚本路径不能为空")
    private String readyScriptPath;

    //@Field basicScriptPath : 铺底数据脚本路径
    @NotBlank(message = "铺底数据脚本路径不能为空")
    private String basicScriptPath;

    //@Field cacheScriptPath : 缓存预热脚本地址
    @NotBlank(message = "缓存预热脚本地址不能为空")
    private String cacheScriptPath;

    // @Field useYn : 是否可用(0表示不可用;1表示可用)
    //@Range(min=0,max=1,message="有效值必须在0和1之间")
    private String useYn;

    //'接入状态； 0：正常 ； 1；待配置 ；2：待检测 ; 3：异常
    //@Range(min=0,max=3,message="应用接入状态")
    private Integer accessStatus;

    private String exceptionInfo;

    //节点数量
    private Integer nodeNum;

    private String switchStatus;
    //@Field cacheExpTime : 缓存失效时间
    @Min(value = 0, message = "缓存过期时间最小为0,0表示永不过期")
    private String cacheExpTime;
    //告警人，在链路探活中使用
    private String alarmPerson;
    private String pradarVersion;
    private Long customerId;
    private Long userId;

}
