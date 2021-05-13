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

package io.shulie.tro.web.app.convert.leakverify;

import java.util.List;

import io.shulie.tro.web.app.output.leakverify.LeakVerifyDeployDetailOutput;
import io.shulie.tro.web.app.output.leakverify.LeakVerifyDeployOutput;
import io.shulie.tro.web.app.response.leakverify.LeakVerifyDeployDetailResponse;
import io.shulie.tro.web.app.response.leakverify.LeakVerifyDeployResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author zhaoyong
 */
@Mapper
public interface LeakVerifyDeployRespConverter {

    LeakVerifyDeployRespConverter INSTANCE = Mappers.getMapper(LeakVerifyDeployRespConverter.class);

    /**
     * 前端返回值转换
     *
     * @param leakVerifyDeployOutputs
     * @return
     */
    List<LeakVerifyDeployResponse> ofListLeakVerifyDeployResponse(List<LeakVerifyDeployOutput> leakVerifyDeployOutputs);

    /**
     * 转换
     *
     * @param leakVerifyDeployDetailOutputs
     * @return
     */
    List<LeakVerifyDeployDetailResponse> ofListLeakVerifyDeployDetailResponse(
        List<LeakVerifyDeployDetailOutput> leakVerifyDeployDetailOutputs);
}
