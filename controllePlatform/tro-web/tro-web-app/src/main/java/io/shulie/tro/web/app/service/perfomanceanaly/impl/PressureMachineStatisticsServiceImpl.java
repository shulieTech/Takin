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

package io.shulie.tro.web.app.service.perfomanceanaly.impl;

import com.google.common.collect.Lists;
import com.pamirs.tro.common.util.DateUtils;
import io.shulie.tro.web.app.convert.performace.PressureMachineStatisticsRespConvert;
import io.shulie.tro.web.app.request.perfomanceanaly.PressureMachineStatisticsRequest;
import io.shulie.tro.web.app.response.perfomanceanaly.PressureMachineStatisticsResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.TypeValueDateVo;
import io.shulie.tro.web.app.service.perfomanceanaly.PressureMachineStatisticsService;
import io.shulie.tro.web.data.dao.perfomanceanaly.PressureMachineStatisticsDao;
import io.shulie.tro.web.data.param.perfomanceanaly.PressureMachineStatisticsInsertParam;
import io.shulie.tro.web.data.param.perfomanceanaly.PressureMachineStatisticsQueryParam;
import io.shulie.tro.web.data.result.perfomanceanaly.PressureMachineStatisticsResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: mubai
 * @Date: 2020-11-13 11:36
 * @Description:
 */

@Service
public class PressureMachineStatisticsServiceImpl implements PressureMachineStatisticsService {
    @Autowired
    private PressureMachineStatisticsDao pressureMachineStatisticsDao;

    @Override
    public void statistics() {
        PressureMachineStatisticsResult statistics = getStatistics();
        PressureMachineStatisticsInsertParam insertParam = new PressureMachineStatisticsInsertParam();
        BeanUtils.copyProperties(statistics, insertParam);
        pressureMachineStatisticsDao.insert(insertParam);
    }

    @Override
    public void insert(PressureMachineStatisticsInsertParam param) {
        pressureMachineStatisticsDao.insert(param);
    }

    @Override
    public PressureMachineStatisticsResponse getNewlyStatistics() {
        PressureMachineStatisticsResult newlyStatistics = pressureMachineStatisticsDao.getNewlyStatistics();
        return PressureMachineStatisticsRespConvert.INSTANCE.of(newlyStatistics);
    }

    @Override
    public List<TypeValueDateVo> queryByExample(PressureMachineStatisticsRequest request) {
        PressureMachineStatisticsQueryParam param = new PressureMachineStatisticsQueryParam();
        BeanUtils.copyProperties(request, param);
        List<PressureMachineStatisticsResult> list = pressureMachineStatisticsDao.queryByExample(param);
        //将数据进行采样，取200个点
        list = pointSample(list);
        return assembleData(list);
    }

    List<PressureMachineStatisticsResult> pointSample(List<PressureMachineStatisticsResult> source) {
        List<PressureMachineStatisticsResult> results = new ArrayList<>();
        int step = 0;
        if (source != null && source.size() > 100) {
            step = source.size() / 100;
            for (int i = 0; i + step < source.size(); i++) {
                i = i + step;
                results.add(source.get(i));
            }
            results.add(source.get(source.size() - 1));
        } else {
            results = source;
        }
        return results;
    }

    private List<TypeValueDateVo> assembleData(List<PressureMachineStatisticsResult> list) {
        List<TypeValueDateVo> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        for (PressureMachineStatisticsResult result : list) {
            String time = result.getGmtCreate().substring(0, 19);
            time = time.replaceAll("T", " ");
            TypeValueDateVo totalVo = new TypeValueDateVo();
            totalVo.setType("总数");
            totalVo.setValue(result.getMachineTotal());
            totalVo.setDate(time);

            TypeValueDateVo pressuredVo = new TypeValueDateVo();
            pressuredVo.setDate(time);
            pressuredVo.setValue(result.getMachinePressured());
            pressuredVo.setType("压测中");

            TypeValueDateVo freeVo = new TypeValueDateVo();
            freeVo.setDate(time);
            freeVo.setValue(result.getMachineFree());
            freeVo.setType("空闲");

            TypeValueDateVo offlineVo = new TypeValueDateVo();
            offlineVo.setType("离线");
            offlineVo.setDate(time);
            offlineVo.setValue(result.getMachineOffline());

            resultList.add(totalVo);
            resultList.add(pressuredVo);
            resultList.add(freeVo);
            resultList.add(offlineVo);
        }
        return resultList;
    }

    @Override
    public PressureMachineStatisticsResult getStatistics() {
        //获取压力机
        return pressureMachineStatisticsDao.statistics();
    }

    @Override
    public void clearRubbishData() {
        Date previousNDay = DateUtils.getPreviousNDay(91);
        pressureMachineStatisticsDao.clearRubbishData(DateUtils.dateToString(previousNDay, DateUtils.FORMATE_YMDHMS));
    }

}
