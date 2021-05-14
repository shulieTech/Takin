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

package io.shulie.tro.web.app.service.report;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.domain.dto.report.ApplicationDTO;
import com.pamirs.tro.entity.domain.dto.report.BottleneckInterfaceDTO;
import com.pamirs.tro.entity.domain.dto.report.MachineDetailDTO;
import com.pamirs.tro.entity.domain.dto.report.ReportCountDTO;
import com.pamirs.tro.entity.domain.dto.report.RiskApplicationCountDTO;
import com.pamirs.tro.entity.domain.dto.report.RiskMacheineDTO;
import com.pamirs.tro.entity.domain.vo.report.ReportLocalQueryParam;

/**
 * @ClassName ReportLocalService
 * @Description
 * @Author qianshui
 * @Date 2020/7/27 下午8:25
 */
public interface ReportLocalService {

    ReportCountDTO getReportCount(Long reportId);

    PageInfo<BottleneckInterfaceDTO> listBottleneckInterface(ReportLocalQueryParam queryParam);

    RiskApplicationCountDTO listRiskApplication(Long reportId);

    PageInfo<RiskMacheineDTO> listRiskMachine(ReportLocalQueryParam queryParam);

    MachineDetailDTO getMachineDetail(Long reportId, String applicationName, String machineIp);

    List<ApplicationDTO> listApplication(Long reportId, String applicationName);

    PageInfo<MachineDetailDTO> listMachineDetail(ReportLocalQueryParam queryParam);

    Long getTraceFailedCount(Long reportId);
}
