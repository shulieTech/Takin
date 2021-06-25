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

package io.shulie.tro.web.app.controller.scenemanage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.pamirs.tro.entity.domain.dto.scenemanage.SceneBusinessActivityRefDTO;
import com.pamirs.tro.entity.domain.dto.scenemanage.SceneManageWrapperDTO;
import com.pamirs.tro.entity.domain.dto.scenemanage.SceneScriptRefDTO;
import com.pamirs.tro.entity.domain.vo.scenemanage.FlowVO;
import com.pamirs.tro.entity.domain.vo.scenemanage.SceneBusinessActivityRefVO;
import com.pamirs.tro.entity.domain.vo.scenemanage.SceneManageIdVO;
import com.pamirs.tro.entity.domain.vo.scenemanage.SceneManageQueryVO;
import com.pamirs.tro.entity.domain.vo.scenemanage.SceneManageWrapperVO;
import com.pamirs.tro.entity.domain.vo.scenemanage.SceneScriptRefVO;
import io.shulie.tro.cloud.open.resp.strategy.StrategyResp;
import io.shulie.tro.common.beans.annotation.ModuleDef;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.request.scenemanage.SceneSchedulerDeleteRequest;
import io.shulie.tro.web.app.response.leakcheck.LeakSqlRefsResponse;
import io.shulie.tro.web.app.service.scenemanage.SceneManageService;
import io.shulie.tro.web.app.service.scenemanage.SceneSchedulerTaskService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.shulie.tro.web.auth.api.exception.TroAuthException;
import io.shulie.tro.web.common.domain.WebResponse;
import io.shulie.tro.web.common.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SceneManageController
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午2:31
 */
@RestController
@RequestMapping("/api/scenemanage")
@Api(tags = "压测场景管理")
public class SceneManageController {

    @Autowired
    private SceneManageService sceneManageService;

    @Autowired
    private SceneSchedulerTaskService sceneSchedulerTaskService;

    @PostMapping
    @ApiOperation("新增压测场景")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.PRESSURE_TEST_MANAGE,
        subModuleName = BizOpConstants.SubModules.PRESSURE_TEST_SCENE,
        logMsgKey = BizOpConstants.Message.MESSAGE_PRESSURE_TEST_SCENE_CREATE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.PRESSURE_TEST_SCENE,
        needAuth = ActionTypeEnum.CREATE
    )
    public WebResponse add(@RequestBody @Valid SceneManageWrapperVO sceneVO) throws TroAuthException {
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.CREATE);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.SCENE_NAME, sceneVO.getPressureTestSceneName());
        WebResponse checkResponse = sceneManageService.checkParam(sceneVO);
        if (!checkResponse.getSuccess()) {
            return checkResponse;
        }

        WebResponse webResponse = sceneManageService.addScene(sceneVO,
            RestContext.getUser() != null ? RestContext.getUser().getId() : null);
        if (!webResponse.getSuccess()) {
            OperationLogContextHolder.ignoreLog();
        }
        return webResponse;
    }

    @PutMapping
    @ApiOperation("修改压测场景")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.PRESSURE_TEST_MANAGE,
        subModuleName = BizOpConstants.SubModules.PRESSURE_TEST_SCENE,
        logMsgKey = BizOpConstants.Message.MESSAGE_PRESSURE_TEST_SCENE_UPDATE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.PRESSURE_TEST_SCENE,
        needAuth = ActionTypeEnum.UPDATE
    )
    public WebResponse update(@RequestBody @Valid SceneManageWrapperVO sceneVO) {
        WebResponse checkResponse = sceneManageService.checkParam(sceneVO);
        if (!checkResponse.getSuccess()) {
            OperationLogContextHolder.ignoreLog();
            return checkResponse;
        }
        // cloud 获得场景详情
        ResponseResult oldData = sceneManageService.detailScene(sceneVO.getId());

        if (Objects.isNull(oldData.getData())) {
            OperationLogContextHolder.ignoreLog();
            return new WebResponse("该压测场景不存在");
        }

        // 数据转换一下...
        SceneManageWrapperDTO oldSceneData = JsonUtil.json2bean(JsonUtil.bean2Json(oldData.getData()),
            SceneManageWrapperDTO.class);
        // 场景名称
        String sceneName = Optional.ofNullable(sceneVO.getPressureTestSceneName())
            .orElse(oldSceneData.getPressureTestSceneName());

        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.UPDATE);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.SCENE_ID, String.valueOf(oldSceneData.getId()));
        OperationLogContextHolder.addVars(BizOpConstants.Vars.SCENE_NAME, sceneName);

        Long userId = RestContext.getUser() != null ? RestContext.getUser().getId() : null;
        WebResponse updateResponse = sceneManageService.updateScene(sceneVO, userId);
        if (!updateResponse.getSuccess()) {
            return updateResponse;
        }

        String selectiveContent = this.compareChangeContent(sceneVO, oldSceneData);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.SCENE_SELECTIVE_CONTENT, selectiveContent);
        if (!updateResponse.getSuccess()) {
            OperationLogContextHolder.ignoreLog();
        }
        return updateResponse;
    }

    /**
     * 比较更换的内容
     *
     * @param sceneVO 欲更新的场景内容
     * @param sceneData 本地场景内容
     * @return
     */
    private String compareChangeContent(SceneManageWrapperVO sceneVO, SceneManageWrapperDTO sceneData) {
        String selectiveContent = "";
        List<SceneBusinessActivityRefVO> currentBusinessActivityConfigList = sceneVO.getBusinessActivityConfig();
        currentBusinessActivityConfigList = currentBusinessActivityConfigList.stream().map(
            sceneBusinessActivityRefVO -> {
                SceneBusinessActivityRefVO tmpSceneBusinessActivityRefVO = new SceneBusinessActivityRefVO();
                tmpSceneBusinessActivityRefVO.setBusinessActivityName(
                    sceneBusinessActivityRefVO.getBusinessActivityName());
                return tmpSceneBusinessActivityRefVO;
            }).collect(Collectors.toList());
        //转成同一业务活动对象再进行业务活动对象属性的比较
        List<SceneBusinessActivityRefVO> oldBusinessActivityConfigList;
        List<SceneBusinessActivityRefDTO> sceneBusinessActivityRefDTOList = sceneData.getBusinessActivityConfig();
        oldBusinessActivityConfigList = sceneBusinessActivityRefDTOList.stream().map(sceneBusinessActivityRefDTO -> {
            SceneBusinessActivityRefVO sceneBusinessActivityRefVO = new SceneBusinessActivityRefVO();
            sceneBusinessActivityRefVO.setBusinessActivityName(sceneBusinessActivityRefDTO.getBusinessActivityName());
            return sceneBusinessActivityRefVO;
        }).collect(Collectors.toList());
        //比较业务活动的变更内容
        if (!JSONObject.toJSONString(currentBusinessActivityConfigList).equals(
            JSONObject.toJSONString(oldBusinessActivityConfigList))) {
            List<SceneBusinessActivityRefVO> sceneBusinessActivityRefVOList = sceneVO.getBusinessActivityConfig();
            String businessActivityNames = sceneBusinessActivityRefVOList.stream().map(
                SceneBusinessActivityRefVO::getBusinessActivityName).collect(Collectors.joining(","));
            selectiveContent = selectiveContent + "｜业务活动名称：" + businessActivityNames;
        }
        //比较脚本变更内容
        List<SceneScriptRefVO> currentSceneScriptRefVOList = sceneVO.getUploadFile();
        currentSceneScriptRefVOList = currentSceneScriptRefVOList
            .stream()
            .filter(sceneScriptRefVO -> 0 == sceneScriptRefVO.getIsDeleted())
            .map(sceneScriptRefVO -> {
                SceneScriptRefVO tmpSceneScriptRefVo = new SceneScriptRefVO();
                tmpSceneScriptRefVo.setFileName(sceneScriptRefVO.getFileName());
                tmpSceneScriptRefVo.setUploadTime(sceneScriptRefVO.getUploadTime());
                return tmpSceneScriptRefVo;
            }).collect(Collectors.toList());
        currentSceneScriptRefVOList.sort((o1, o2) -> {
            String o1FileName = o1.getFileName();
            String o2FileName = o2.getFileName();
            return o1FileName.compareTo(o2FileName);
        });

        List<SceneScriptRefVO> oldSceneScriptRefVOList;
        List<SceneScriptRefDTO> sceneScriptRefDTOList = sceneData.getUploadFile();
        oldSceneScriptRefVOList = sceneScriptRefDTOList.stream().map(sceneScriptRefDTO -> {
            SceneScriptRefVO sceneScriptRefVO = new SceneScriptRefVO();
            sceneScriptRefVO.setFileName(sceneScriptRefDTO.getFileName());
            sceneScriptRefVO.setUploadTime(sceneScriptRefDTO.getUploadTime());
            return sceneScriptRefVO;
        }).collect(Collectors.toList());
        oldSceneScriptRefVOList.sort((o1, o2) -> {
            String o1FileName = o1.getFileName();
            String o2FileName = o2.getFileName();
            return o1FileName.compareTo(o2FileName);
        });
        if (!JSONObject.toJSONString(currentSceneScriptRefVOList).equals(
            JSONObject.toJSONString(oldSceneScriptRefVOList))) {
            if (CollectionUtils.isNotEmpty(currentSceneScriptRefVOList)) {
                String fileNames = currentSceneScriptRefVOList.stream().map(SceneScriptRefVO::getFileName).collect(
                    Collectors.joining(","));
                selectiveContent = selectiveContent + "｜压测文件：" + fileNames;
            }
        }
        return selectiveContent;
    }

    @DeleteMapping
    @ApiOperation("删除压测场景")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.PRESSURE_TEST_MANAGE,
        subModuleName = BizOpConstants.SubModules.PRESSURE_TEST_SCENE,
        logMsgKey = BizOpConstants.Message.MESSAGE_PRESSURE_TEST_SCENE_DELETE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.PRESSURE_TEST_SCENE,
        needAuth = ActionTypeEnum.DELETE
    )
    public WebResponse delete(@RequestBody @Valid SceneManageIdVO deleteVO) {
        ResponseResult webResponse = sceneManageService.detailScene(deleteVO.getId());
        if (Objects.isNull(webResponse.getData())) {
            OperationLogContextHolder.ignoreLog();
            return new WebResponse("该压测场景不存在");
        }
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.DELETE);
        SceneManageWrapperDTO sceneData = JSON.parseObject(JSON.toJSONString(webResponse.getData()),
            SceneManageWrapperDTO.class);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.SCENE_ID, String.valueOf(sceneData.getId()));
        OperationLogContextHolder.addVars(BizOpConstants.Vars.SCENE_NAME, sceneData.getPressureTestSceneName());
        WebResponse webResponse2 = sceneManageService.deleteScene(deleteVO);
        if (!webResponse2.getSuccess()) {
            OperationLogContextHolder.ignoreLog();
        }
        return webResponse2;
    }

    @GetMapping("/detail")
    @ApiOperation(value = "压测场景详情")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.PRESSURE_TEST_SCENE,
        needAuth = ActionTypeEnum.QUERY
    )
    public ResponseResult getDetail(@ApiParam(name = "id", value = "ID", required = true) Long id) {
        return sceneManageService.detailScene(id);
    }


    @GetMapping("/list")
    @ApiOperation(value = "压测场景列表")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.PRESSURE_TEST_SCENE,
        needAuth = ActionTypeEnum.QUERY
    )
    public WebResponse getList(@ApiParam(name = "current", value = "页码", required = true) Integer current,
        @ApiParam(name = "pageSize", value = "页大小", required = true) Integer pageSize,
        @ApiParam(name = "customName", value = "客户名称") String customName,
        @ApiParam(name = "customId", value = "客户ID") Long customId,
        @ApiParam(name = "sceneId", value = "压测场景ID") Long sceneId,
        @ApiParam(name = "sceneName", value = "压测场景名称") String sceneName,
        @ApiParam(name = "status", value = "压测状态") Integer status,
        @ApiParam(name = "tagId", value = "标签id") Long tagId,
        @ApiParam(name = "lastPtStartTime", value = "压测结束时间") String lastPtStartTime,
        @ApiParam(name = "lastPtEndTime", value = "压测结束时间") String lastPtEndTime
    ) {
        SceneManageQueryVO queryVO = new SceneManageQueryVO();
        queryVO.setCurrent(current);
        queryVO.setCurrentPage(current);
        queryVO.setPageSize(pageSize);
        queryVO.setCustomName(customName);
        queryVO.setCustomId(customId);
        queryVO.setSceneId(sceneId);
        queryVO.setSceneName(sceneName);
        queryVO.setStatus(status);
        queryVO.setTagId(tagId);
        queryVO.setLastPtStartTime(lastPtStartTime);
        queryVO.setLastPtEndTime(lastPtEndTime);
        return sceneManageService.getPageList(queryVO);
    }

    @PostMapping("/flow/calc")
    @ApiOperation(value = "流量计算")
    public Response calcFlow(@RequestBody FlowVO vo) {
        return Response.success(sceneManageService.calcFlow(vo));
    }

    @GetMapping("/ipnum")
    @ApiOperation(value = "获取机器数量范围")
    public ResponseResult<StrategyResp> getIpNum(
        @ApiParam(name = "concurrenceNum", value = "并发数量")
        @RequestParam(value = "concurrenceNum", required = false) Integer concurrenceNum,
        @ApiParam(name = "tpsNum", value = "并发数量")
        @RequestParam(value = "tpsNum", required = false) Integer tpsNum
    ) {
        return sceneManageService.getIpNum(concurrenceNum, tpsNum);
    }

    @DeleteMapping("/scheduler")
    @ApiOperation(value = "取消定时启动")
    public void cancelSceneSchedulerPressure(@RequestBody SceneSchedulerDeleteRequest request) {
        sceneSchedulerTaskService.deleteBySceneId(request.getSceneId());

    }

    @GetMapping("/{sceneId}")
    public List<LeakSqlRefsResponse> getRefResponse(@Valid @NotNull @PathVariable Long sceneId) {
        return null;
    }
}
