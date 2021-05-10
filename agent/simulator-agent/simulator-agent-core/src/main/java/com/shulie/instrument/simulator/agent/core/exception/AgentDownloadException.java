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
package com.shulie.instrument.simulator.agent.core.exception;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/23 7:50 下午
 */
public class AgentDownloadException extends RuntimeException {
    public AgentDownloadException() {
        super();
    }

    public AgentDownloadException(String message) {
        super(message);
    }

    public AgentDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentDownloadException(Throwable cause) {
        super(cause);
    }
}
