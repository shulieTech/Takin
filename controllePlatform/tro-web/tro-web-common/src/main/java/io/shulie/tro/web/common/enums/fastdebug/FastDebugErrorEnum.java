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

package io.shulie.tro.web.common.enums.fastdebug;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.common.enums.fastdebug
 * @date 2021/1/26 5:15 下午
 */
@AllArgsConstructor
@Getter
public enum FastDebugErrorEnum {
    ANALYSIS_DATA_TIMEOUT("分析数据超时","warn"),
    /**
     * 丢失节点列表
     */
    CALLSTACK_LOST_NODE_DETAIL("%s","callstack"),
    /**
     * 丢失节点
     */
    CALLSTACK_LOST_NODE("%s","node_lost"),
    /**
     *异常节点
     */
    CALLSTACK_EXCEPTION_NODE("%s","node_exception"),
    /**
     * 未知节点
     */
    CALLSTACK_UNKNOWN_NODE("%s","node_unknown"),
    /**
     * 流量标识异常
     */
    CALLSTACK_SIGN_EXCEPTION_NODE("%s","node_sign_exception"),

    // 异常
    REQUEST_FAILED("请求失败-%s","error"),
    REQUEST_TRACE_ID_FAILED("请求失败-traceId未返回","error"),
    LEAK_VERIFY_EXCEPTION("数据验证异常,异常结果:%s","error"),
    APP_EXCEPTION("配置异常%s个","error"),
    BUSINESS_FLOW_EXCEPTION("存在业务流量","error"),
    /**
     * 节点异常 + 未知节点
     */
    CALLSTACK_EXCEPTION("调用栈异常%s个","error"),
    EXCEPTION("一栈式调试工具验证异常，请联系管理员~","error")

    ;

    private String errorName;
    private String type;

}
