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

package io.shulie.tro.web.data.convert.tracenode;

import java.util.List;

import io.shulie.tro.web.data.model.mysql.TraceNodeInfoEntity;
import io.shulie.tro.web.data.param.tracenode.TraceNodeInfoParam;
import io.shulie.tro.web.data.result.tracenode.TraceNodeInfoResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.convert.tracenode
 * @date 2020/12/29 1:39 下午
 */
@Mapper
public interface TraceNodeInfoConvert {
    TraceNodeInfoConvert INSTANCE = Mappers.getMapper(TraceNodeInfoConvert.class);

    /**
     * 转换
     * @param params
     * @return
     */
    List<TraceNodeInfoEntity> ofList(List<TraceNodeInfoParam> params);

    /**
     * 转换
     * @param entity
     * @return
     */
    TraceNodeInfoResult of (TraceNodeInfoEntity entity);

    /**
     * 转换
     * @param entity
     * @return
     */
    List<TraceNodeInfoResult> ofListResult (List<TraceNodeInfoEntity> entity);
}
