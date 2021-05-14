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

package io.shulie.tro.cloud.biz.cloudserver;

import io.shulie.tro.cloud.biz.output.engine.EnginePluginSimpleInfoOutput;
import io.shulie.tro.cloud.data.result.engine.EnginePluginSimpleInfoResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 引擎插件简单参数转换
 *
 * @author lipeng
 * @date 2021-01-20 5:12 下午
 */
@Mapper
public interface EnginePluginSimpleResultConvert {

    EnginePluginSimpleResultConvert INSTANCE = Mappers.getMapper(EnginePluginSimpleResultConvert.class);

    EnginePluginSimpleInfoOutput of(EnginePluginSimpleInfoResult result);

    List<EnginePluginSimpleInfoOutput> ofs(List<EnginePluginSimpleInfoResult> results);
}
