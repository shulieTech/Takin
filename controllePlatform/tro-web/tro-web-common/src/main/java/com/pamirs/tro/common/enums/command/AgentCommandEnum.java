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

package com.pamirs.tro.common.enums.command;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author 无涯
 * @Package com.pamirs.tro.common.enums.command
 * @date 2021/1/21 10:51 上午
 * 用于枚举命令
 */
@AllArgsConstructor
@Getter
public enum AgentCommandEnum {
    // 拉取 agentTrace 方法追踪用,版本对应tro-4.6.1
    PULL_AGENT_TRACE_COMMAND("PULL_AGENT_TRACE_COMMAND","方法追踪通道"),
    // dump数据拉取
    PULL_DUMP_HEAP_COMMAND("PULL_DUMP_HEAP_COMMAND","dump数据")
    ;

    private String commandId;
    private String description;


}
