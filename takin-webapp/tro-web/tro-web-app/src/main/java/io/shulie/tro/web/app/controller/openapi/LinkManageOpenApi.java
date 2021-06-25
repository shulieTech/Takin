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

package io.shulie.tro.web.app.controller.openapi;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessActiveViewListDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessFlowDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.SceneDto;
import com.pamirs.tro.entity.domain.vo.linkmanage.queryparam.BusinessQueryVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.queryparam.SceneQueryVo;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.controller.openapi.converter.LinkManageOpenApiConverter;
import io.shulie.tro.web.app.controller.openapi.response.linkmanage.BusinessActiveViewListOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.linkmanage.BusinessFlowOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.linkmanage.BusinessLinkOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.linkmanage.SceneOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.linkmanage.SystemProcessViewListOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.linkmanage.TechLinkOpenApiResp;
import io.shulie.tro.web.app.response.linkmanage.BusinessLinkResponse;
import io.shulie.tro.web.app.service.linkManage.LinkManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: vernon
 * @Date: 2019/11/29 14:05
 * @Description:链路管理
 */
@RestController
@RequestMapping(APIUrls.TRO_OPEN_API_URL)
@Api(tags = "linkmanage", value = "链路标注")
public class LinkManageOpenApi {

    @Autowired
    private LinkManageService linkManageService;
    /**
     * 是否展示为图
     */
    @Value("${link.graph.enable:false}")
    private boolean graphEable;

    @GetMapping("/link/tech/linkManage")
    @ApiOperation("系统流程列表查询接口")
    public Response<List<SystemProcessViewListOpenApiResp>> gettechLinksViwList(
        @ApiParam(name = "linkName", value = "系统流程名字") String linkName,
        @ApiParam(name = "entrance", value = "入口") String entrance,
        @ApiParam(name = "ischange", value = "是否变更") String ischange,
        @ApiParam(name = "middleWareType", value = "中间件类型") String middleWareType,
        @ApiParam(name = "middleWareName", value = "中间件名称") String middleWareName,
        @ApiParam(name = "middleWareVersion", value = "中间件版本") String middleWareVersion,
        Integer current,
        Integer pageSize
    ) {
        return Response.success();
    }

    @GetMapping("/link/tech/linkManage/detail")
    @ApiOperation("从本地数据库查询系统流程详情查询接口")
    public Response<TechLinkOpenApiResp> fetchTechLinkDetail(
        @RequestParam("id")
        @ApiParam(name = "id", value = "系统流程主键") String id) {
        return Response.success();
    }

    @GetMapping("/link/business/manage")
    @ApiOperation("业务活动列表查询")
    public Response<List<BusinessActiveViewListOpenApiResp>> getBussisnessLinks(
        @ApiParam(name = "businessLinkName", value = "业务活动名字") String businessLinkName,
        @ApiParam(name = "entrance", value = "入口") String entrance,
        @ApiParam(name = "ischange", value = "是否变更") String ischange,
        @ApiParam(name = "domain", value = "业务域") String domain,
        @ApiParam(name = "middleWareType", value = "中间件类型") String middleWareType,
        @ApiParam(name = "middleWareName", value = "中间件名称") String middleWareName,
        @ApiParam(name = "middleWareVersion", value = "中间件版本号") String middleWareVersion,
        @ApiParam(name = "systemProcessName", value = "系统流程名字") String systemProcessName,
        Integer current,
        Integer pageSize
    ) {
        BusinessQueryVo vo = new BusinessQueryVo();
        vo.setBusinessLinkName(businessLinkName);
        vo.setEntrance(entrance);
        vo.setIschange(ischange);
        vo.setDomain(domain);
        vo.setMiddleWareType(middleWareType);
        vo.setMiddleWareName(middleWareName);
        vo.setTechLinkName(systemProcessName);
        vo.setVersion(middleWareVersion);
        vo.setCurrentPage(current);
        vo.setPageSize(pageSize);
        Response<List<BusinessActiveViewListDto>> bussisnessLinks = linkManageService.getBussisnessLinks(vo);
        return Response.success(
            LinkManageOpenApiConverter.INSTANCE.ofListBusinessActiveViewListOpenApiResp(bussisnessLinks.getData()));
    }

    @GetMapping("/link/business/manage/detail")
    @ApiOperation("业务活动详情查询")
    public Response<BusinessLinkOpenApiResp> getBussisnessLinkDetail(@ApiParam(name = "id", value = "业务活动主键")
    @RequestParam("id")
        String id) {
        try {
            BusinessLinkResponse businessLinkResponse = linkManageService.getBussisnessLinkDetail(id);
            return Response.success(
                LinkManageOpenApiConverter.INSTANCE.ofBusinessLinkOpenApiResp(businessLinkResponse));
        } catch (Exception e) {
            return Response.fail("0", e.getMessage());
        }

    }

    @GetMapping("/link/scene/manage")
    @ApiOperation("业务流程列表查询")
    public Response<List<SceneOpenApiResp>> getScenes
        (
            @ApiParam(name = "sceneId", value = "场景id") Long sceneId,
            @ApiParam(name = "sceneName", value = "业务流程名字") String sceneName,
            @ApiParam(name = "entrance", value = "入口") String entrance,
            @ApiParam(name = "ischange", value = "是否变更") String ischange,
            @ApiParam(name = "businessName", value = "业务活动名") String businessName,
            @ApiParam(name = "middleWareType", value = "中间件类型") String middleWareType,
            @ApiParam(name = "middleWareName", value = "中间件名字") String middleWareName,
            @ApiParam(name = "middleWareVersion", value = "中间件版本") String middleWareVersion,
            Integer current,
            Integer pageSize
        ) {
        SceneQueryVo vo = new SceneQueryVo();
        vo.setSceneId(sceneId);
        vo.setSceneName(sceneName);
        vo.setEntrace(entrance);
        vo.setIschanged(ischange);
        vo.setBusinessName(businessName);
        vo.setMiddleWareType(middleWareType);
        vo.setMiddleWareName(middleWareName);
        vo.setMiddleWareVersion(middleWareVersion);
        vo.setCurrentPage(current);
        vo.setPageSize(pageSize);
        Response<List<SceneDto>> scenes = linkManageService.getScenes(vo);
        return Response.success(LinkManageOpenApiConverter.INSTANCE.ofListSceneOpenApiResp(scenes.getData()));
    }

    @GetMapping("/link/scene/tree/detail")
    @ApiOperation("业务流程树详情获取")
    public Response<BusinessFlowOpenApiResp> getBusinessFlowDetail(@NotNull String id) {
        try {
            BusinessFlowDto dto = linkManageService.getBusinessFlowDetail(id);
            return Response.success(LinkManageOpenApiConverter.INSTANCE.ofBusinessFlowOpenApiResp(dto));
        } catch (Exception e) {
            return Response.fail("0", e.getMessage(), null);
        }
    }

}
