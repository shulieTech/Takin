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

package io.shulie.tro.cloud.web.entrypoint.convert;

import io.shulie.tro.cloud.biz.input.scenemanage.SceneManageWrapperInput;
import io.shulie.tro.cloud.web.entrypoint.request.scenemanage.SceneManageWrapperRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author zhaoyong
 */
@Mapper
public interface SceneManageReqConvertor {

    SceneManageReqConvertor INSTANCE = Mappers.getMapper(SceneManageReqConvertor.class);

    /**
     * 入参转换
     * @param wrapperRequest
     * @return
     */
    SceneManageWrapperInput ofSceneManageWrapperInput(SceneManageWrapperRequest wrapperRequest);
}
