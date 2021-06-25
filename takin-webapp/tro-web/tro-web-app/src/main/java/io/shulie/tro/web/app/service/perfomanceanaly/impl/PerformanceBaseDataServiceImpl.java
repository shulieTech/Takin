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

import java.util.Date;
import java.util.List;

import com.pamirs.tro.common.util.DateUtils;
import io.shulie.tro.web.app.convert.performace.PerformanceBaseInputConvert;
import io.shulie.tro.web.app.input.PerformanceBaseDataCreateInput;
import io.shulie.tro.web.app.response.perfomanceanaly.ReportTimeResponse;
import io.shulie.tro.web.app.service.async.AsyncService;
import io.shulie.tro.web.app.service.perfomanceanaly.PerformanceBaseDataService;
import io.shulie.tro.web.app.service.perfomanceanaly.ReportDetailService;
import io.shulie.tro.web.data.dao.perfomanceanaly.PerformanceBaseDataDAO;
import io.shulie.tro.web.data.param.perfomanceanaly.PerformanceBaseDataParam;
import io.shulie.tro.web.data.param.perfomanceanaly.PerformanceBaseQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName PerformanceBaseDataServiceImpl
 * @Description
 * @Author qianshui
 * @Date 2020/11/4 下午2:44
 */
@Service
@Slf4j
public class PerformanceBaseDataServiceImpl implements PerformanceBaseDataService {

    @Autowired
    private PerformanceBaseDataDAO performanceBaseDataDAO;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private ReportDetailService reportDetailService;

    @Override
    public void cache(PerformanceBaseDataCreateInput input) {
        PerformanceBaseDataParam param = PerformanceBaseInputConvert.INSTANCE.inputToParam(input);
        asyncService.savePerformanceBaseData(param);
    }

    @Override
    public List<String> getProcessName(Long reportId, String appName) {
        PerformanceBaseQueryParam param = new PerformanceBaseQueryParam();
        param.setAppName(appName);
        ReportTimeResponse response = reportDetailService.getReportTime(reportId);
        param.setStartTime(response.getStartTime());
        param.setEndTime(response.getEndTime());
        return performanceBaseDataDAO.getProcessNameList(param);
    }

    @Override
    public void clearData(Integer time) {
        Date nSecond = DateUtils.getPreviousNSecond(time);
        performanceBaseDataDAO.clearData(DateUtils.dateToString(nSecond, DateUtils.FORMATE_YMDHMS));
    }
}
