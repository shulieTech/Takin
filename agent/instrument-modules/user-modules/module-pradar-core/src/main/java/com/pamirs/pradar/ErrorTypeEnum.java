/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.pradar;

/**
 * @author wangjian
 * @since 2020/8/13 16:01
 */
public enum ErrorTypeEnum {
    DataSource("datasource", "数据源异常"),
    ShadowJob("shadow-job", "影子job异常"),
    AgentError("agent", "Agent异常"),
    MQ("MQ", "消息队列异常"),
    WebFilter("web-filter", "容器异常"),
    LinkGuardEnhance("link-guard-enhance", "挡板异常"),
    RedisServer("redisServer", "redis影子数据源异常"),
    ShadowEsServer("shadowEsServer", "es影子数据源异常"),
    ;

    private String errorType;

    private String errorCnDesc;

    public String getErrorType() {
        return errorType;
    }

    public String getErrorCnDesc() {
        return errorCnDesc;
    }

    ErrorTypeEnum(String errorType, String errorCnDesc) {
        this.errorType = errorType;
        this.errorCnDesc = errorCnDesc;
    }
}
