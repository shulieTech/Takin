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

package io.shulie.tro.web.app.service.linkManage;

import java.util.List;

import com.pamirs.tro.entity.domain.dto.EntranceSimpleDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessActiveIdAndNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessFlowDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessFlowIdAndNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.EntranceDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.MiddleWareNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.SceneDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.SystemProcessIdAndNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.SystemProcessViewListDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.TechLinkDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.TopologicalGraphVo;
import com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics.LinkHistoryInfoDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics.LinkRemarkDto;
import com.pamirs.tro.entity.domain.entity.linkmanage.statistics.StatisticsQueryVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.BusinessFlowVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.BusinessLinkVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.MiddleWareEntity;
import com.pamirs.tro.entity.domain.vo.linkmanage.TechLinkVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.queryparam.BusinessQueryVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.queryparam.SceneQueryVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.queryparam.TechQueryVo;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.response.application.ApplicationDetailResponse;
import io.shulie.tro.web.app.response.linkmanage.BusinessActivityNameResponse;
import io.shulie.tro.web.app.response.linkmanage.BusinessLinkResponse;
import io.shulie.tro.web.app.response.linkmanage.MiddleWareResponse;
import io.shulie.tro.web.app.response.linkmanage.TechLinkResponse;

/**
 * @Auther: vernon
 * @Date: 2019/11/29 14:43
 * @Description:
 */
public interface LinkManageService {
    /////////////技术链路
    //Result 使用业务中台的返回
    //从图数据库拉取数据 入参：入口名字，出参：Json

    TechLinkResponse fetchLink(String applicationName, String entrance);

    TopologicalGraphVo fetchGraph(String body);

    List<EntranceDto> fetchApp();

    List<EntranceDto> fetchEntrance(String applicationName);

    //前端保存技术链路的接口，入参：links,出参:Result,link.link = 1
    Response storetechLink(TechLinkVo links);

    //链路删除 入参：linkType = 0,链路名字
    Response deleteLink(String id);

    //技术链路修改 入惨:Link.linkType = 0,技术链路id，
    Response modifyLink(TechLinkVo links);

    //技术链路查询 入参:技术链路名字，入口，变更状态 vo.linkType 出参：TechLinkDto
    Response<List<SystemProcessViewListDto>> gettechLinksViwList(TechQueryVo vo);

    Response addBusinessLink(BusinessLinkVo links);

    Response modifyBussinessLink(BusinessLinkVo link);

    //业务链路查询 入参:技术链路名字，入口，变更状态 vo.LinkType = 0,vo.linkChange 出参：TechLinkDto
    Response getBussisnessLinks(BusinessQueryVo vo);

    //场景删除接口: 入参：场景名集合
    Response deleteScene(String sceneId);

    //场景查询接口:sceneName:场景名字,linkName:链路名，entrace:入口,ischange：是否有变化
    Response<List<SceneDto>> getScenes(SceneQueryVo vo);

    Response getMiddleWareInfo(StatisticsQueryVo vo);

    LinkRemarkDto getstatisticsInfo();

    Response deletBusinessLink(String id);

    LinkHistoryInfoDto getChart();

    List<MiddleWareEntity> getAllMiddleWareTypeList();

    List<SystemProcessIdAndNameDto> ggetAllSystemProcess(String systemProcessName);

    List<SystemProcessIdAndNameDto> getAllSystemProcessCanrelateBusiness(String systemProcessName);

    List<String> entranceFuzzSerach(String entrance);

    /**
     * 根据业务活动名称, 查询登录用户下
     * 所有业务链路的名称和id
     * @param businessActiveName 业务活动名称
     * @return 所有业务链路的名称和id
     */
    List<BusinessActiveIdAndNameDto> businessActiveNameFuzzSearch(String businessActiveName);

    TechLinkDto fetchTechLinkDetail(String id);

    BusinessLinkResponse getBussisnessLinkDetail(String id);

    // SceneDetailDto deleteSceneLinkRelatedByBusinessId(String businessId, String sceneId);

    List<MiddleWareEntity> businessProcessMiddleWares(List<String> ids);

    BusinessFlowDto getBusinessFlowDetail(String id);

    void modifyBusinessFlow(BusinessFlowVo vo) throws Exception;

    void addBusinessFlow(BusinessFlowVo vo) throws Exception;

    List<BusinessFlowIdAndNameDto> businessFlowIdFuzzSearch(String businessFlowName) throws Exception;

    List<MiddleWareNameDto> cascadeMiddleWareNameAndVersion(String middleWareType) throws Exception;

    List<MiddleWareNameDto> getDistinctMiddleWareName();

    List<EntranceSimpleDto> getEntranceByAppName(String applicationName);

    List<ApplicationDetailResponse> getApplicationDetailsByAppName(String applicationName, String entrance,
        String linkApplicationName) throws Exception;

    List<MiddleWareResponse> getMiddleWareResponses(String applicationName);

    /**
     * 根据业务流程id查询业务活动id和名称
     *
     * @param businessFlowId
     * @return
     */
    List<BusinessActivityNameResponse> getBusinessActiveByFlowId(Long businessFlowId);
}
