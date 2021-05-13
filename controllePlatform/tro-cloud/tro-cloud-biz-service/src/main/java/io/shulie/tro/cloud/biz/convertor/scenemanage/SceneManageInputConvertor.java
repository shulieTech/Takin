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

import io.shulie.tro.cloud.biz.input.scenemanage.SceneBusinessActivityRefInput;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author zhaoyong
 */
@Mapper
public interface SceneManageInputConvertor {

    SceneManageInputConvertor INSTANCE = Mappers.getMapper(SceneManageInputConvertor.class);

    /**
     * 入参转换
     * @param businessActivityConfig
     * @return
     */
    List<SceneBusinessActivityRefInput> ofListSceneBusinessActivityRefInput(List<SceneBusinessActivityRefInput> businessActivityConfig);
}
