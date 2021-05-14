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

package io.shulie.tro.web.app.controller.linkmanage;

import java.util.List;

import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessActiveIdAndNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.MiddleWareNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.SystemProcessIdAndNameDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics.LinkHistoryInfoDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics.LinkRemarkDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics.LinkRemarkmiddleWareDto;
import com.pamirs.tro.entity.domain.entity.linkmanage.MiddleWareDistinctVo;
import com.pamirs.tro.entity.domain.entity.linkmanage.statistics.StatisticsQueryVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.MiddleWareEntity;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.response.linkmanage.BusinessActivityNameResponse;
import io.shulie.tro.web.app.service.linkManage.LinkManageService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: vernon
 * @Date: 2019/11/29 14:05
 * @Description:链路管理
 * @see io.shulie.tro.web.app.controller.activity.ActivityController
 * @see io.shulie.tro.web.app.controller.application.ApplicationEntranceController
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "linkmanage", value = "链路标注")
@Deprecated
public class LinkManageController {

    @Autowired
    private LinkManageService linkManageService;



    @GetMapping("/link/statistic")
    @ApiOperation("链路统计")
    public Response<LinkRemarkDto> getstatisticsInfo() {
        LinkRemarkDto dto = linkManageService.getstatisticsInfo();
        return Response.success(dto);
    }

    @GetMapping("/link/statistic/middleware")
    @ApiOperation("链路统计中间件信息查询")
    public Response<List<LinkRemarkmiddleWareDto>> getMiddleWareInfo(StatisticsQueryVo vo) {
        // TODO: 2019/12/10
        return linkManageService.getMiddleWareInfo(vo);
    }

    @GetMapping("/link/statistic/chart")
    @ApiOperation("趋势图拉取接口")
    public Response<LinkHistoryInfoDto> getChart() {
        LinkHistoryInfoDto dto = linkManageService.getChart();
        return Response.success(dto);
    }

    @GetMapping("/link/linkmanage/middleware")
    @ApiOperation("查询数据库中所有中间件类型")
    public Response<List<MiddleWareEntity>> getAllMiddleWareTypeList() {

        try {
            List<MiddleWareEntity> list = linkManageService.getAllMiddleWareTypeList();
            return Response.success(list);
        } catch (Exception e) {
            return Response.fail("0", e.getMessage(), null);
        }
    }

    /**
     * 获取所有的系统流程名字和id
     *
     * @return
     */
    @GetMapping("/link/tech/linkmanage/all")
    @ApiOperation("系统流程名字和id的模糊搜索,不传数据则全部搜索")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.SYSTEM_PROCESS,
        needAuth = ActionTypeEnum.QUERY
    )
    public Response<List<SystemProcessIdAndNameDto>> systemProcessFuzzSearch(
            @ApiParam(name = "systemProcessName", value = "系统流程名字") String systemProcessName) {
        try {
            List<SystemProcessIdAndNameDto> dto = linkManageService.ggetAllSystemProcess(systemProcessName);
            return Response.success(dto);
        } catch (Exception e) {
            return Response.fail("0", e.getMessage(), null);
        }
    }

    /**
     * 获取所有的系统流程名字和id
     *
     * @return
     */
    @GetMapping("/link/tech/linkmanage/canRelate/all")
    @ApiOperation("系统流程名字和id的模糊搜索,不传数据则全部搜索")
    public Response<List<SystemProcessIdAndNameDto>> systemProcessFuzzSearch2(
            @ApiParam(name = "systemProcessName", value = "系统流程名字") String systemProcessName) {
        try {
            List<SystemProcessIdAndNameDto> dto = linkManageService.getAllSystemProcessCanrelateBusiness(
                    systemProcessName);

            return Response.success(dto);
        } catch (Exception e) {
            return Response.fail("0", e.getMessage(), null);
        }
    }

    @GetMapping("/link/entrance")
    @ApiOperation("获取所有的入口")
    public Response<List<String>> entranceFuzzSerach(@ApiParam(name = "entrance", value = "入口名") String entrance) {
        try {
            List<String> result = linkManageService.entranceFuzzSerach(entrance);
            return Response.success(result);

        } catch (Exception e) {
            return Response.fail("0", e.getMessage(), null);
        }
    }

    @GetMapping("/link/bussinessActive")
    @ApiOperation("获取所有的业务活动名字和id")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.BUSINESS_ACTIVITY,
        needAuth = ActionTypeEnum.QUERY
    )
    public Response<List<BusinessActiveIdAndNameDto>> bussinessActiveNameFuzzSearch(
            @ApiParam(name = "bussinessActiveName", value = "业务活动名字") String bussinessActiveName) {

        try {
            List<BusinessActiveIdAndNameDto> result
                    = linkManageService.businessActiveNameFuzzSearch(bussinessActiveName);
            return Response.success(result);
        } catch (Exception e) {
            return Response.fail("0", e.getMessage(), null);
        }
    }

    @GetMapping("/link/business/manage/getBusinessActiveByFlowId")
    @ApiOperation("根据流程id获取所有的业务活动名字和id")
    public List<BusinessActivityNameResponse> getBusinessActiveByFlowId(Long businessFlowId) {
        return linkManageService.getBusinessActiveByFlowId(businessFlowId);
    }


    /**
     * 业务流程页面的中间件去重
     *
     * @return
     */
    @PostMapping("/link/scene/middlewares")
    @ApiOperation("业务流程页面的中间件去重")
    public Response<List<MiddleWareEntity>> businessProcessMiddleWares(
            @ApiParam(name = "vo", value = "业务活动主键集合")
            @RequestBody MiddleWareDistinctVo vo) {
        try {
            //   List<String> ids = vos.stream().map(vo -> vo.getId()).collect(Collectors.toList());

            List<String> ids = vo.getIds();
            List<MiddleWareEntity> lists = linkManageService.businessProcessMiddleWares(ids);
            return Response.success(lists);
        } catch (Exception e) {
            return Response.fail("0", e.getMessage(), null);
        }
    }

    @GetMapping("/link/midlleWare/cascade")
    @ApiOperation("中间件级联关系拉取")
    public Response<List<MiddleWareNameDto>> cascadeMiddleWareNameAndVersion(@ApiParam(name = "middleWareType"
            , value = "中间件类型") String middleWareType) {

        try {
            List<MiddleWareNameDto> dtos = linkManageService.cascadeMiddleWareNameAndVersion(middleWareType);
            return Response.success(dtos);

        } catch (Exception e) {
            return Response.fail("0", e.getMessage(), null);
        }
    }

    @GetMapping("/link/middleWare/name")
    @ApiOperation("中间件名字去重")
    public Response<List<MiddleWareNameDto>> getDistinctMiddleWareName() {
        try {
            List<MiddleWareNameDto> dtos = linkManageService.getDistinctMiddleWareName();
            return Response.success(dtos);
        } catch (Exception e) {
            return Response.failByType("0", e.getMessage(), null);
        }
    }

}
