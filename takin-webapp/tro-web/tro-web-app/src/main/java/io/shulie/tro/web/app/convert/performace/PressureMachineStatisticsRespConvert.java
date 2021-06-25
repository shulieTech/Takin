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

import io.shulie.tro.web.app.response.perfomanceanaly.PressureMachineStatisticsResponse;
import io.shulie.tro.web.data.result.perfomanceanaly.PressureMachineStatisticsResult;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: mubai
 * @Date: 2020-11-13 18:07
 * @Description:
 */

@Mapper
public interface PressureMachineStatisticsRespConvert {

    PressureMachineStatisticsRespConvert INSTANCE = Mappers.getMapper(PressureMachineStatisticsRespConvert.class);

    PressureMachineStatisticsResponse of(PressureMachineStatisticsResult result);

    List<PressureMachineStatisticsResponse> ofs(List<PressureMachineStatisticsResult> results);

    @AfterMapping
    default void fillRespData(PressureMachineStatisticsResult source, @MappingTarget PressureMachineStatisticsResponse response) {
        Integer machineTotal = source.getMachineTotal();
        if (machineTotal == 0) {
            BigDecimal decimal = new BigDecimal(0);
            response.setOfflinePercent(decimal);
            response.setPressuredPercent(decimal);
            response.setFreePercent(decimal);
        } else {

            BigDecimal totalNum = new BigDecimal(source.getMachineTotal());
            BigDecimal freePercent = new BigDecimal(source.getMachineFree()).divide(totalNum,4, BigDecimal.ROUND_HALF_UP);
            BigDecimal presssuredPercent =new BigDecimal(source.getMachinePressured()).divide(totalNum,4, BigDecimal.ROUND_HALF_UP);
            BigDecimal offlinePercent = new BigDecimal(source.getMachineOffline()).divide(totalNum,4, BigDecimal.ROUND_HALF_UP);
            response.setFreePercent(freePercent);
            response.setPressuredPercent(presssuredPercent);
            response.setOfflinePercent(offlinePercent);
        }
    }
}
