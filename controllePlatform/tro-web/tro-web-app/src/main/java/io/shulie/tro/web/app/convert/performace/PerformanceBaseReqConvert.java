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

import io.shulie.tro.web.app.input.PerformanceBaseDataCreateInput;
import io.shulie.tro.web.app.req.perfomanceanaly.PerformanceBaseDataReq;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName PerformanceBaseReqConvert
 * @Description
 * @Author qianshui
 * @Date 2020/11/10 上午10:57
 */
@Mapper
public interface PerformanceBaseReqConvert {

    PerformanceBaseReqConvert INSTANCE = Mappers.getMapper(PerformanceBaseReqConvert.class);

    PerformanceBaseDataCreateInput reqToInput(PerformanceBaseDataReq source);
}
