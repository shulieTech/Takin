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

package io.shulie.tro.web.app.service.fastdebug;

import io.shulie.tro.web.app.agent.CommandSendDTO;
import io.shulie.tro.web.app.request.fastdebug.FastDebugAgentLogRequest;
import io.shulie.tro.web.app.request.fastdebug.FastDebugAppLogRequest;
import io.shulie.tro.web.app.response.fastdebug.AgentLogNamesResponse;
import io.shulie.tro.web.app.response.fastdebug.FastDebugLogResponse;

/**
 * @Author: mubai
 * @Date: 2020-12-29 10:06
 * @Description:
 */
public interface FastDebugLogService {

    FastDebugLogResponse getAppLog(FastDebugAppLogRequest logRequest) throws Exception;

    AgentLogNamesResponse getAgentLogNames(String appName, String agentId, String traceId) throws Exception;

    FastDebugLogResponse getAgentLog(FastDebugAgentLogRequest agentLogRequest) throws Exception;

    void sendPullLogCommand(CommandSendDTO send) throws Exception;

    CommandSendDTO buildPullAppCommand(String appName, String agentId, String traceId, String filePath);

    CommandSendDTO buildPullAgentCommand(String appName, String agentId, String traceId, String fileName);

}
