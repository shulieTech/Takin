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

package io.shulie.tro.web.app.service.user.Impl;

import java.util.List;

import com.pamirs.tro.entity.domain.dto.ApplicationSwitchStatusDTO;
import com.pamirs.tro.entity.domain.dto.linkmanage.SystemProcessViewListDto;
import com.pamirs.tro.entity.domain.query.ApplicationQueryParam;
import com.pamirs.tro.entity.domain.vo.ApplicationVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.queryparam.TechQueryVo;
import com.pamirs.tro.entity.domain.vo.user.WorkBenchVo;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.service.ApplicationService;
import io.shulie.tro.web.app.service.linkManage.LinkManageService;
import io.shulie.tro.web.app.service.user.WorkBenchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: mubai
 * @Date: 2020-06-28 21:08
 * @Description:
 */

@Service
public class WorkBenchServiceImpl implements WorkBenchService {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private LinkManageService linkManageService;

    @Override
    public WorkBenchVo getUserWorkBench(Long uid) {
        WorkBenchVo workBenchVo = new WorkBenchVo();
        if (uid == null) {
            return workBenchVo;
        }
        //压测开关
        Response response = applicationService.userAppSwitchInfo();
        if (response != null && response.getData() != null) {
            ApplicationSwitchStatusDTO statusDTO = (ApplicationSwitchStatusDTO)response.getData();
            workBenchVo.setSwitchStatus(statusDTO.getSwitchStatus());
        }
        //应用数量
        ApplicationQueryParam queryParam = new ApplicationQueryParam();
        queryParam.setPageSize(-1);
        Response appResponse = applicationService.getApplicationList(queryParam);
        if (appResponse != null && appResponse.getData() != null) {
            List<ApplicationVo> applicationVoList = (List<ApplicationVo>)appResponse.getData();
            int errorNum = 0;
            for (ApplicationVo vo : applicationVoList) {
                if (vo.getAccessStatus() != null && vo.getAccessStatus().equals(3)) {
                    errorNum++;
                }
            }
            workBenchVo.setApplicationNum(applicationVoList == null ? 0 : applicationVoList.size());
            workBenchVo.setAccessErrorNum(errorNum);
        }
        //系统流程
        TechQueryVo techQuery = new TechQueryVo();
        techQuery.setPageSize(Integer.MAX_VALUE);
        Response<List<SystemProcessViewListDto>> listResponse = linkManageService.gettechLinksViwList(techQuery);
        if (listResponse != null && listResponse.getData() != null) {
            int changeNum = 0;
            for (SystemProcessViewListDto vo : listResponse.getData()) {
                if (vo.getIsChange() != null && "1".equals(vo.getIsChange())) {
                    changeNum++;
                }
            }
            workBenchVo.setChangedProcessNum(changeNum);
            workBenchVo.setSystemProcessNum(listResponse.getData().size());
        } else {
            workBenchVo.setSystemProcessNum(0);
            workBenchVo.setChangedProcessNum(0);
        }
        return workBenchVo;
    }
}
