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

import java.util.List;

import com.pamirs.tro.common.util.http.DateUtil;
import io.shulie.tro.web.app.input.PerformanceBaseDataCreateInput;
import io.shulie.tro.web.common.vo.perfomanceanaly.PerformanceThreadDataVO;
import io.shulie.tro.web.data.param.perfomanceanaly.PerformanceBaseDataParam;
import io.shulie.tro.web.data.param.perfomanceanaly.PerformanceThreadDataParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName PerformanceBaseInputConvert
 * @Description
 * @Author qianshui
 * @Date 2020/11/10 上午10:57
 */
@Mapper(imports = DateUtil.class)
public interface PerformanceBaseInputConvert {

    PerformanceBaseInputConvert INSTANCE = Mappers.getMapper(PerformanceBaseInputConvert.class);

    PerformanceThreadDataParam inputToParam(PerformanceThreadDataVO source);

    List<PerformanceThreadDataParam> inputToParamList(List<PerformanceThreadDataVO> sources);

    PerformanceBaseDataParam inputToParam(PerformanceBaseDataCreateInput source);
}
