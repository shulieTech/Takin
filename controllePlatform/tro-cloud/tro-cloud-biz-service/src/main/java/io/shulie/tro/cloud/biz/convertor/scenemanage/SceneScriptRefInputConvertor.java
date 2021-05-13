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

package io.shulie.tro.cloud.biz.convertor.scenemanage;

import com.pamirs.tro.entity.domain.entity.scenemanage.SceneScriptRef;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneScriptRefInput;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: mubai
 * @Date: 2020-10-30 14:32
 * @Description:
 */
@Mapper
public interface SceneScriptRefInputConvertor {
    SceneScriptRefInputConvertor INSTANCE = Mappers.getMapper(SceneScriptRefInputConvertor.class);

    SceneScriptRefInput of(SceneScriptRef scriptRef);

    List<SceneScriptRefInput> ofList(List<SceneScriptRef> scriptRefs);
}
