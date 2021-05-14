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

package io.shulie.tro.web.app.convert.statistics;

import java.util.List;
import java.util.stream.Collectors;

import io.shulie.tro.cloud.open.resp.statistics.PressureListTotalResp;
import io.shulie.tro.cloud.open.resp.statistics.PressurePieTotalResp;
import io.shulie.tro.cloud.open.resp.statistics.ReportTotalResp;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.web.app.output.statistics.PressureListTotalOutput;
import io.shulie.tro.web.app.output.statistics.PressurePieTotalOutput;
import io.shulie.tro.web.app.output.statistics.ReportTotalOutput;
import io.shulie.tro.web.app.output.statistics.ScriptLabelListTotalOutput;
import io.shulie.tro.web.app.response.statistics.PressureListTotalResponse;
import io.shulie.tro.web.app.response.statistics.PressurePieTotalResponse;
import io.shulie.tro.web.app.response.statistics.ReportTotalResponse;
import io.shulie.tro.web.app.response.statistics.ScriptLabelListTotalResponse;
import io.shulie.tro.web.data.result.statistics.ScriptLabelListTotalResult;
import org.springframework.beans.BeanUtils;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.open.entrypoint.convert
 * @date 2020/12/1 7:14 下午
 */
public class StatisticsConvert {
    public static PressurePieTotalResponse of(PressurePieTotalOutput output) {
        return JsonHelper.json2Bean(JsonHelper.bean2Json(output),PressurePieTotalResponse.class);
    }
    public static PressurePieTotalOutput ofCloud(PressurePieTotalResp resp) {
        return JsonHelper.json2Bean(JsonHelper.bean2Json(resp),PressurePieTotalOutput.class);
    }
    public static ReportTotalResponse of(ReportTotalOutput output) {
        ReportTotalResponse response = new ReportTotalResponse();
        BeanUtils.copyProperties(output,response);
        return response;
    }
    public static ReportTotalOutput ofCloud(ReportTotalResp resp) {
        ReportTotalOutput output = new ReportTotalOutput();
        BeanUtils.copyProperties(resp,output);
        return output;
    }
    public static List<PressureListTotalResponse> ofList(List<PressureListTotalOutput> output) {
        List<PressureListTotalResponse> responses = output.stream().map(out -> {
            PressureListTotalResponse response = new PressureListTotalResponse();
            BeanUtils.copyProperties(out,response);
            return response;
        }).collect(Collectors.toList());
        return responses;
    }
    public static List<PressureListTotalOutput> ofListCloud(List<PressureListTotalResp> resps) {
        List<PressureListTotalOutput> outputs = resps.stream().map(resp -> {
            PressureListTotalOutput output = new PressureListTotalOutput();
            BeanUtils.copyProperties(resp,output);
            return output;
        }).collect(Collectors.toList());
        return outputs;
    }


    public static List<ScriptLabelListTotalResponse> ofListOutput(List<ScriptLabelListTotalOutput> output) {
        List<ScriptLabelListTotalResponse> resps = output.stream().map(out -> {
            ScriptLabelListTotalResponse resp = new ScriptLabelListTotalResponse();
            BeanUtils.copyProperties(out,resp);
            return resp;
        }).collect(Collectors.toList());
        return resps;
    }
  public static List<ScriptLabelListTotalOutput> ofListResult(List<ScriptLabelListTotalResult> output) {
        List<ScriptLabelListTotalOutput> resps = output.stream().map(out -> {
            ScriptLabelListTotalOutput resp = new ScriptLabelListTotalOutput();
            BeanUtils.copyProperties(out,resp);
            return resp;
        }).collect(Collectors.toList());
        return resps;
    }


}
