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

package io.shulie.tro.web.app.service.statistics;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.common.constant.ScriptEnum;
import com.pamirs.tro.common.util.DateUtils;
import io.shulie.tro.cloud.open.req.statistics.PressureTotalReq;
import io.shulie.tro.cloud.open.resp.statistics.PressureListTotalResp;
import io.shulie.tro.cloud.open.resp.statistics.PressurePieTotalResp;
import io.shulie.tro.cloud.open.resp.statistics.ReportTotalResp;
import io.shulie.tro.web.app.convert.statistics.StatisticsConvert;
import io.shulie.tro.web.app.input.statistics.PressureTotalInput;
import io.shulie.tro.web.app.output.statistics.PressureListTotalOutput;
import io.shulie.tro.web.app.output.statistics.PressurePieTotalOutput;
import io.shulie.tro.web.app.output.statistics.PressurePieTotalOutput.PressurePieTotal;
import io.shulie.tro.web.app.output.statistics.ReportTotalOutput;
import io.shulie.tro.web.app.output.statistics.ScriptLabelListTotalOutput;
import io.shulie.tro.web.data.dao.statistics.StatisticsManageDao;
import io.shulie.tro.web.data.dao.user.TroUserDAO;
import io.shulie.tro.web.data.result.statistics.PressureListTotalResult;
import io.shulie.tro.web.data.result.statistics.PressurePieTotalResult;
import io.shulie.tro.web.data.result.statistics.ScriptLabelListTotalResult;
import io.shulie.tro.web.data.result.user.TroUserResult;
import io.shulie.tro.web.data.result.user.UserDetailResult;
import io.shulie.tro.web.diff.api.statistics.PressureStatisticsApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.service.statistics
 * @date 2020/11/30 9:36 下午
 */
@Service
public class PressureStatisticsServiceImpl implements PressureStatisticsService {
    @Resource
    private PressureStatisticsApi pressureStatisticsApi;

    @Resource
    private StatisticsManageDao statisticsManageDao;

    @Autowired
    private TroUserDAO troUserDAO;

    @Override
    public PressurePieTotalOutput getPressurePieTotal(PressureTotalInput input) {
        PressurePieTotalOutput output = new PressurePieTotalOutput();

        switch (input.getType()) {
            case "0":
                // 场景状态统计 需要cloud统计
                PressureTotalReq req = new PressureTotalReq();
                BeanUtils.copyProperties(input, req);
                PressurePieTotalResp resp = pressureStatisticsApi.getPressurePieTotal(req);
                output = StatisticsConvert.ofCloud(resp);
                break;
            case "1":
                //  脚本统计
                List<PressurePieTotalResult> list = statisticsManageDao.getPressureScenePieTotal(input.getStartTime(),
                    input.getEndTime());
                Map<String, Integer> totalMap = Maps.newHashMap();
                for (ScriptEnum scriptEnum : ScriptEnum.values()) {
                    totalMap.put(scriptEnum.getName(), 0);
                }
                list.forEach(data -> {
                    totalMap.put(ScriptEnum.getName(data.getType()), data.getCount());
                });
                List<PressurePieTotal> totals = totalMap.entrySet()
                    .stream().map(e -> new PressurePieTotal(e.getKey(), e.getValue())).collect(
                        Collectors.toList());
                Integer count = list.stream().mapToInt(PressurePieTotalResult::getCount).sum();
                output.setData(totals);
                output.setTotal(count);
                break;
            default: {}
        }
        return output;
    }

    @Override
    public ReportTotalOutput getReportTotal(PressureTotalInput input) {
        PressureTotalReq req = new PressureTotalReq();
        BeanUtils.copyProperties(input, req);
        ReportTotalResp resp = pressureStatisticsApi.getReportTotal(req);
        return StatisticsConvert.ofCloud(resp);
    }

    @Override
    public List<PressureListTotalOutput> getPressureListTotal(PressureTotalInput input) {
        List<PressureListTotalOutput> outputs = Lists.newArrayList();
        PressureTotalReq req = new PressureTotalReq();
        BeanUtils.copyProperties(input, req);
        switch (input.getType()) {
            case "0":
                //  压测场景次数统计   获取标签信息
                List<PressureListTotalResp> resp = pressureStatisticsApi.getPressureListTotal(req);
                outputs = StatisticsConvert.ofListCloud(resp);
                List<Long> ids = outputs.stream().map(PressureListTotalOutput::getId).collect(Collectors.toList());
                // 获取标签
                List<PressureListTotalResult> tags  = Lists.newArrayList();
                if(ids != null && ids.size() > 0) {
                    tags  = statisticsManageDao.getSceneTag(ids);
                }
                Map<Long, PressureListTotalResult> map = tags.stream().collect(
                    Collectors.toMap(PressureListTotalResult::getId, totalResult -> totalResult));
                outputs.forEach(output -> {
                    PressureListTotalResult result = map.get(output.getId());
                    if (result != null) {
                        output.setLabel(result.getTags());
                    }
                    // 创建人根据id查
                    if(StringUtils.isNotBlank(output.getCreateName())) {
                        TroUserResult userResult = troUserDAO.selectById(Long.valueOf(output.getCreateName()));
                        if(userResult != null) {
                            output.setCreateName(userResult.getName());
                        }
                    }
                });

                break;
            case "1":
                //  压测脚本次数统计
                //  获取 脚本id
                req.setScriptIds(statisticsManageDao.selectScriptManageDeployIds());
                List<PressureListTotalResp> totalResps = pressureStatisticsApi.getPressureListTotal(req);
                outputs = StatisticsConvert.ofListCloud(totalResps);
                // 获取ids
                List<Long> scriptIds = totalResps.stream().map(PressureListTotalResp::getId).collect(
                    Collectors.toList());
                List<PressureListTotalResult> results = Lists.newArrayList();
                if(scriptIds != null && scriptIds.size() > 0) {
                    results = statisticsManageDao.getScriptTag(scriptIds);
                }else {
                    // 脚本必须要有
                    return outputs;
                }
                Map<Long, PressureListTotalResult> totalResultMap = results.stream().collect(
                    Collectors.toMap(PressureListTotalResult::getId, totalResult -> totalResult));
                // 先时间，再count
                outputs.sort(Comparator.comparing(sort -> totalResultMap.get(sort.getId()).getGmtCreate(), Comparator.reverseOrder()));
                outputs.sort(Comparator.comparing(PressureListTotalOutput::getCount, Comparator.reverseOrder()));
                outputs.forEach(output -> {
                    PressureListTotalResult result = totalResultMap.get(output.getId());
                    if (result != null) {
                        output.setName(result.getName() + " 版本" + result.getScriptVersion());
                        output.setLabel(result.getTags());
                        output.setGmtCreate(DateUtils.dateToString(result.getGmtCreate(), DateUtils.FORMATE_YMDHMS));
                        output.setCreateName(result.getCreateName());
                    }
                });
                break;
            default: {}
        }
        return outputs;
    }

    @Override
    public List<ScriptLabelListTotalOutput> getScriptLabelListTotal(PressureTotalInput input) {
        List<ScriptLabelListTotalResult> results = statisticsManageDao.getScriptLabelListTotal(input.getStartTime(),
            input.getEndTime());
        return StatisticsConvert.ofListResult(results);
    }
}
