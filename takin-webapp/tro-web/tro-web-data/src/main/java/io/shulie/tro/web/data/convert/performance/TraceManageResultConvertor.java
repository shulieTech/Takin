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

package io.shulie.tro.web.data.convert.performance;

import io.shulie.tro.web.data.model.mysql.TraceManageDeployEntity;
import io.shulie.tro.web.data.model.mysql.TraceManageEntity;
import io.shulie.tro.web.data.result.tracemanage.TraceManageDeployResult;
import io.shulie.tro.web.data.result.tracemanage.TraceManageResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author zhaoyong
 */
@Mapper
public interface TraceManageResultConvertor {

    TraceManageResultConvertor INSTANCE = Mappers.getMapper(TraceManageResultConvertor.class);


    TraceManageResult ofTraceManageResult(TraceManageEntity traceManageEntity);

    TraceManageDeployResult ofTraceManageDeployResult(TraceManageDeployEntity traceManageDeployEntity);
}
