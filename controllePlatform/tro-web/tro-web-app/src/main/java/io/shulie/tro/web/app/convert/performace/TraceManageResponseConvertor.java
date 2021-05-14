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

package io.shulie.tro.web.app.convert.performace;

import io.shulie.tro.web.app.response.perfomanceanaly.TraceManageDeployResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.TraceManageResponse;
import io.shulie.tro.web.app.response.scriptmanage.ScriptManageDeployResponse;
import io.shulie.tro.web.data.result.scriptmanage.ScriptManageDeployResult;
import io.shulie.tro.web.data.result.tracemanage.TraceManageDeployResult;
import io.shulie.tro.web.data.result.tracemanage.TraceManageResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author zhaoyong
 */
@Mapper
public interface TraceManageResponseConvertor {
    TraceManageResponseConvertor INSTANCE = Mappers.getMapper(TraceManageResponseConvertor.class);

    TraceManageResponse ofTraceManageResponse(TraceManageResult traceManageResult);

    TraceManageDeployResponse ofTraceManageDeployResponse(TraceManageDeployResult traceManageDeployResult);

    List<ScriptManageDeployResponse> ofListScriptManageDeployResponse(List<ScriptManageDeployResult> scriptManageDeployResults);
}
