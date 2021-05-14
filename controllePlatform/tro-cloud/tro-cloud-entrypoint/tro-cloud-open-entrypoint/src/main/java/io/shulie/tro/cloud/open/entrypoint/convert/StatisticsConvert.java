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

package io.shulie.tro.cloud.open.entrypoint.convert;

import java.util.List;
import java.util.stream.Collectors;

import io.shulie.tro.cloud.biz.output.statistics.PressureListTotalOutput;
import io.shulie.tro.cloud.biz.output.statistics.PressurePieTotalOutput;
import io.shulie.tro.cloud.biz.output.statistics.ReportTotalOutput;
import io.shulie.tro.cloud.open.resp.statistics.PressureListTotalResp;
import io.shulie.tro.cloud.open.resp.statistics.PressurePieTotalResp;
import io.shulie.tro.cloud.open.resp.statistics.ReportTotalResp;
import org.springframework.beans.BeanUtils;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.open.entrypoint.convert
 * @date 2020/12/1 7:14 下午
 */
public class StatisticsConvert {
    public static PressurePieTotalResp of(PressurePieTotalOutput output) {
        PressurePieTotalResp resp = new PressurePieTotalResp();
        BeanUtils.copyProperties(output,resp);
        return resp;
    }
    public static ReportTotalResp of(ReportTotalOutput output) {
        ReportTotalResp resp = new ReportTotalResp();
        BeanUtils.copyProperties(output,resp);
        return resp;
    }
    public static List<PressureListTotalResp> of(List<PressureListTotalOutput> output) {
        List<PressureListTotalResp> resps = output.stream().map(out -> {
            PressureListTotalResp resp = new PressureListTotalResp();
            BeanUtils.copyProperties(out,resp);
            return resp;
        }).collect(Collectors.toList());
        return resps;
    }
}
