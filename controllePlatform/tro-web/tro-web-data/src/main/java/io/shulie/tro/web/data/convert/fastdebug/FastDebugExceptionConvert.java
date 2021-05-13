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

package io.shulie.tro.web.data.convert.fastdebug;

import java.util.List;

import io.shulie.tro.web.common.vo.fastdebug.FastDebugExceptionVO;
import io.shulie.tro.web.data.model.mysql.FastDebugExceptionEntity;
import io.shulie.tro.web.data.param.fastdebug.FastDebugExceptionParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.convert.fastdebug
 * @date 2020/12/29 11:07 上午
 */
@Mapper
public interface FastDebugExceptionConvert {
    FastDebugExceptionConvert INSTANCE = Mappers.getMapper(FastDebugExceptionConvert.class);

    /**
     * 转换
     * @param params
     * @return
     */
    List<FastDebugExceptionEntity> ofList(List<FastDebugExceptionParam> params);

    @Mappings({
        @Mapping(target = "agentId",
            expression = "java(org.apache.commons.lang3.StringUtils.join(param.getAgentId(),\",\"))")
    })
    FastDebugExceptionEntity of(FastDebugExceptionParam param);

    /**
     * 转换
     * @param params
     * @return
     */

    List<FastDebugExceptionVO> ofListResult(List<FastDebugExceptionEntity> params);

    @Mappings({
        @Mapping(target = "agentId",
            expression = "java(org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getAgentId())?com.google.common.collect.Lists.newArrayList(entity.getAgentId().split(\",\")):new java.util.ArrayList())")
    })
    FastDebugExceptionVO of(FastDebugExceptionEntity entity);

    /**
     * 转换
     * @param params
     * @return
     */
    List<FastDebugExceptionVO> ofListVo(List<FastDebugExceptionEntity> params);

}
