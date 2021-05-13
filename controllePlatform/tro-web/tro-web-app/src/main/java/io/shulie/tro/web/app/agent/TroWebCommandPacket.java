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

package io.shulie.tro.web.app.agent;

import io.shulie.tro.channel.bean.CommandPacket;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.agent
 * @date 2021/1/25 2:28 下午
 */
@Data
public class TroWebCommandPacket extends CommandPacket {
    private String agentId;
    /**
     * 响应等待时间
     */
    private Long timeoutMillis;
    /**
     * 是否运行执行多次，true:运行，false:不允许
     */
    private Boolean isAllowMultipleExecute;

}
