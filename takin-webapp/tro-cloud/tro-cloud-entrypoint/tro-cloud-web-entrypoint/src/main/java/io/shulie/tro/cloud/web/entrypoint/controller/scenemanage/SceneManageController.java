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

package io.shulie.tro.cloud.web.entrypoint.controller.scenemanage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.fastjson.JSON;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.cloud.biz.cache.DictionaryCache;
import io.shulie.tro.cloud.biz.convertor.ConvertUtil;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneManageQueryInput;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneManageWrapperInput;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneSlaRefInput;
import io.shulie.tro.cloud.biz.output.scenemanage.SceneManageListOutput;
import io.shulie.tro.cloud.biz.output.scenemanage.SceneManageWrapperOutput;
import io.shulie.tro.cloud.biz.output.strategy.StrategyOutput;
import io.shulie.tro.cloud.biz.service.scene.SceneManageService;
import io.shulie.tro.cloud.biz.service.strategy.StrategyConfigService;
import io.shulie.tro.cloud.biz.utils.SlaUtil;
import io.shulie.tro.cloud.common.bean.collector.SendMetricsEvent;
import io.shulie.tro.cloud.common.bean.scenemanage.SceneManageQueryOpitons;
import io.shulie.tro.cloud.common.constants.APIUrls;
import io.shulie.tro.cloud.common.constants.DicKeyConstant;
import io.shulie.tro.cloud.common.constants.SceneManageConstant;
import io.shulie.tro.cloud.common.exception.ApiException;
import io.shulie.tro.cloud.common.utils.CustomUtil;
import io.shulie.tro.cloud.common.utils.FlowUtil;
import io.shulie.tro.cloud.common.utils.ListHelper;
import io.shulie.tro.cloud.web.entrypoint.convert.SceneBusinessActivityRefRespConvertor;
import io.shulie.tro.cloud.web.entrypoint.convert.SceneManageReqConvertor;
import io.shulie.tro.cloud.web.entrypoint.convert.SceneManageRespConvertor;
import io.shulie.tro.cloud.web.entrypoint.convert.SceneScriptRefRespConvertor;
import io.shulie.tro.cloud.web.entrypoint.convert.SceneSlaRefRespConvertor;
import io.shulie.tro.cloud.web.entrypoint.request.scenemanage.SceneManageDeleteRequest;
import io.shulie.tro.cloud.web.entrypoint.request.scenemanage.SceneManageWrapperRequest;
import io.shulie.tro.cloud.web.entrypoint.response.scenemanage.BusinessActivityDetailResponse;
import io.shulie.tro.cloud.web.entrypoint.response.scenemanage.SceneDetailResponse;
import io.shulie.tro.cloud.web.entrypoint.response.scenemanage.SceneDetailResponse.ScriptDetailResponse;
import io.shulie.tro.cloud.web.entrypoint.response.scenemanage.SceneDetailResponse.SlaDetailResponse;
import io.shulie.tro.cloud.web.entrypoint.response.scenemanage.SceneManageWrapperResponse;
import io.shulie.tro.cloud.web.entrypoint.response.scenemanage.SceneManageWrapperResponse.SceneSlaRefResponse;
import io.shulie.tro.cloud.web.entrypoint.response.strategy.StrategyResponse;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @ClassName SceneManageController
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午2:31
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL + "scenemanage")
@Api(tags = "压测场景管理")
public class SceneManageController {

    //原本调用方式，需要将token放入header中
    public static final String PAGE_TOTAL_HEADER = "x-total-count";

    @Autowired
    private SceneManageService sceneManageService;

    @Autowired
    private StrategyConfigService strategyConfigService;

    @Autowired
    private DictionaryCache dictionaryCache;

    @Resource
    private TUserMapper tUserMapper;

    @PostMapping
    @ApiOperation(value = "新增压测场景")
    public ResponseResult add(@RequestBody @Valid SceneManageWrapperRequest wrapperRequest) {
        try {
            wrapperRequest.setCustomId(CustomUtil.getUserId());
            SceneManageWrapperInput input = SceneManageReqConvertor.INSTANCE.ofSceneManageWrapperInput(wrapperRequest);
            input.setUserId(wrapperRequest.getUid());
            return ResponseResult.success(sceneManageService.addSceneManage(input));
        } catch (ApiException exception) {
            return ResponseResult.fail(String.valueOf(exception.getCode()), exception.getMessage());
        } catch (Exception e) {
            return ResponseResult.fail("系统错误", "");
        }
    }

    @PutMapping
    @ApiOperation(value = "修改压测场景")
    public ResponseResult update(@RequestBody @Valid SceneManageWrapperRequest wrapperRequest) {
        try {
            SceneManageWrapperInput input = SceneManageReqConvertor.INSTANCE.ofSceneManageWrapperInput(wrapperRequest);
            sceneManageService.updateSceneManage(input);
            return ResponseResult.success();
        } catch (ApiException exception) {
            return ResponseResult.fail(String.valueOf(exception.getCode()), exception.getMessage(), "");
        } catch (Exception e) {
            return ResponseResult.fail("系统错误", "");
        }
    }

    @DeleteMapping
    @ApiOperation(value = "删除压测场景")
    public ResponseResult delete(@RequestBody SceneManageDeleteRequest deleteRequest) {
        sceneManageService.delete(deleteRequest.getId());
        return ResponseResult.success();
    }

    /**
     * 供编辑使用
     */
    @GetMapping("/detail")
    @ApiOperation(value = "压测场景编辑详情")
    public ResponseResult<SceneManageWrapperResponse> getDetailForEdit(@ApiParam(name = "id", value = "ID") Long id) {
        SceneManageQueryOpitons options = new SceneManageQueryOpitons();
        options.setIncludeBusinessActivity(true);
        options.setIncludeScript(true);
        options.setIncludeSLA(true);

        try {
            SceneManageWrapperOutput sceneManage = sceneManageService.getSceneManage(id, options);
            assembleFeatures(sceneManage);
            ResponseResult<SceneManageWrapperResponse> resp = wrapperSceneManage(sceneManage);
            return resp;
        } catch (ApiException exception) {
            return ResponseResult.fail(String.valueOf(exception.getCode()), exception.getMessage(), "");
        }
    }


    public void assembleFeatures(SceneManageWrapperOutput resp) {
        String features = resp.getFeatures();
        if (StringUtils.isBlank(features)) {
            return;
        }
        HashMap<String, Object> map = JSON.parseObject(features, HashMap.class);
        Integer configType = -1;
        if (map.containsKey(SceneManageConstant.FEATURES_CONFIG_TYPE)) {
            configType = (Integer) map.get(SceneManageConstant.FEATURES_CONFIG_TYPE);
            resp.setConfigType(configType);
        }
        if (map.containsKey(SceneManageConstant.FEATURES_SCRIPT_ID)) {
            Long scriptId = Long.valueOf(map.get(SceneManageConstant.FEATURES_SCRIPT_ID).toString());
            if (configType != -1 && configType == 1) {
                //业务活动
                List<SceneManageWrapperOutput.SceneBusinessActivityRefOutput> businessActivityConfig = resp
                    .getBusinessActivityConfig();
                for (SceneManageWrapperOutput.SceneBusinessActivityRefOutput data : businessActivityConfig) {
                    data.setScriptId(scriptId);
                    //业务活动的脚本id也在外面放一份
                    resp.setScriptId(scriptId);
                }
            } else {
                resp.setScriptId(scriptId);
            }
        }
        Object businessFlowId = map.get(SceneManageConstant.FEATURES_BUSINESS_FLOW_ID);
        resp.setBusinessFlowId(businessFlowId == null ? null : Long.valueOf(businessFlowId.toString()));

        if(map.containsKey(SceneManageConstant.FEATURES_SCHEDULE_INTERVAL)){
            Integer schedualInterval = (Integer) map.get(SceneManageConstant.FEATURES_SCHEDULE_INTERVAL);
            resp.setScheduleInterval(schedualInterval);
        }
    }


    /**
     * 供详情使用
     */
    @GetMapping("/content")
    @ApiOperation(value = "压测场景详情")
    public ResponseResult<SceneDetailResponse> getContent(@ApiParam(value = "id") Long id) {
        ResponseResult<SceneManageWrapperResponse> resDTO = getDetailForEdit(id);
        if (!resDTO.getSuccess()) {
            return ResponseResult.fail(resDTO.getError().getCode(), resDTO.getError().getMsg(), "");
        }
        return ResponseResult.success(convertSceneManageWrapperDTO2SceneDetailDTO(resDTO.getData()));
    }

    @GetMapping("/list")
    @ApiOperation(value = "压测场景列表")
    public ResponseResult<List<SceneManageListOutput>> getList(
        @ApiParam(name = "current", value = "页码", required = true) Integer current,
        @ApiParam(name = "pageSize", value = "页大小", required = true) Integer pageSize,
        @ApiParam(name = "customName", value = "客户名称") String customName,
        @ApiParam(name = "customId", value = "客户ID") Long customId,
        @ApiParam(name = "sceneId", value = "压测场景ID") Long sceneId,
        @ApiParam(name = "sceneName", value = "压测场景名称") String sceneName,
        @ApiParam(name = "status", value = "压测状态") Integer status,
        @ApiParam(name = "sceneIds", value = "场景ids，逗号分割") String sceneIds,
        @ApiParam(name = "lastPtStartTime", value = "压测结束时间") String lastPtStartTime,
        @ApiParam(name = "lastPtEndTime", value = "压测结束时间") String lastPtEndTime
    ) {

        /**
         * 1、封装参数
         * 2、调用查询服务
         * 3、返回指定格式
         */
        SceneManageQueryInput queryVO = new SceneManageQueryInput();
        queryVO.setCurrentPage(current);
        queryVO.setPageSize(pageSize);
        queryVO.setCustomName(customName);
        queryVO.setCustomId(customId);
        queryVO.setSceneId(sceneId);
        queryVO.setSceneName(sceneName);
        queryVO.setStatus(status);
        List<Long> sceneIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(sceneIds)) {
            String[] strList = sceneIds.split(",");
            for (String s : strList) {
                sceneIdList.add(Long.valueOf(s));
            }
        }
        queryVO.setSceneIds(sceneIdList);
        queryVO.setLastPtStartTime(lastPtStartTime);
        queryVO.setLastPtEndTime(lastPtEndTime);
        PageInfo<SceneManageListOutput> pageInfo = sceneManageService.queryPageList(queryVO);
        return ResponseResult.success(pageInfo.getList(), pageInfo.getTotal());
    }

    @PostMapping("/flow/calc")
    @ApiOperation(value = "流量计算")
    public ResponseResult calcFlow(@RequestBody SceneManageWrapperRequest wrapperRequest) {

        SceneManageWrapperInput input = new SceneManageWrapperInput();
        BeanUtils.copyProperties(wrapperRequest, input);
        BigDecimal flow = FlowUtil.calcEstimateFlow(ConvertUtil.convert(input));
        return ResponseResult.success(flow.setScale(2, RoundingMode.HALF_UP));
    }

    /**
     * 并发数量
     *
     * @param concurrenceNum
     * @return
     */
    @GetMapping("/ipnum")
    @ApiOperation(value = "获取机器数量范围")
    public ResponseResult<StrategyResponse> getIpNum(@ApiParam(name = "concurrenceNum", value = "并发数量")  Integer concurrenceNum) {
        StrategyOutput output = strategyConfigService.getStrategy(concurrenceNum,null);
        StrategyResponse strategyResponse = new StrategyResponse();
        BeanUtils.copyProperties(output, strategyResponse);
        return ResponseResult.success(strategyResponse);
    }

    /**
     * 解析脚本
     *
     * @param scriptId
     * @param uploadPath
     * @return
     */
    @GetMapping("/parse/script")
    @ApiOperation(value = "解析脚本")
    public ResponseResult<Map<String, Object>> parseScript(
        @ApiParam(name = "scriptId", value = "脚本ID") Long scriptId,
        @ApiParam(name = "uploadPath", value = "上传路径") String uploadPath, boolean absolutePath) {
        return ResponseResult.success(sceneManageService.parseScript(scriptId, uploadPath, absolutePath));
    }


    private ResponseResult<SceneManageWrapperResponse> wrapperSceneManage(SceneManageWrapperOutput sceneManage) {

        SceneManageWrapperResponse response = SceneManageRespConvertor.INSTANCE.of(sceneManage);

        response.setBusinessActivityConfig(
            SceneBusinessActivityRefRespConvertor.INSTANCE.ofList(sceneManage.getBusinessActivityConfig()));
        response.setStopCondition(SceneSlaRefRespConvertor.INSTANCE.ofList(sceneManage.getStopCondition()));
        response.setWarningCondition(SceneSlaRefRespConvertor.INSTANCE.ofList(sceneManage.getWarningCondition()));
        response.setUploadFile(SceneScriptRefRespConvertor.INSTANCE.ofList(sceneManage.getUploadFile()));
        response.setScheduleInterval(sceneManage.getScheduleInterval());

        if (sceneManage == null || CollectionUtils.isEmpty(response.getBusinessActivityConfig())) {
            return ResponseResult.fail("场景不存在或者没有业务活动配置", "");
        }
        sceneManage.getBusinessActivityConfig().stream().forEach(data -> {
            if (StringUtils.isBlank(data.getBindRef())) {
                data.setBindRef("-1");
            }
            if (StringUtils.isBlank(data.getApplicationIds())) {
                data.setApplicationIds("-1");
            }
        });
        return ResponseResult.success(response);
    }

    private SceneDetailResponse convertSceneManageWrapperDTO2SceneDetailDTO(SceneManageWrapperResponse wrapperDTO) {
        SceneDetailResponse detailDTO = new SceneDetailResponse();
        //基本信息
        detailDTO.setId(wrapperDTO.getId());
        detailDTO.setSceneName(wrapperDTO.getPressureTestSceneName());
        detailDTO.setCustomId(wrapperDTO.getCustomId());
        if (wrapperDTO.getCustomId() != null) {
            User user = tUserMapper.selectById(wrapperDTO.getCustomId());
            detailDTO.setCustomName(user != null ? user.getNick() : null);
        }
        detailDTO.setUpdateTime(wrapperDTO.getUpdateTime());
        detailDTO.setLastPtTime(wrapperDTO.getLastPtTime());
        detailDTO.setStatus(
                dictionaryCache.getObjectByParam(DicKeyConstant.SCENE_MANAGE_STATUS, wrapperDTO.getStatus()));
        //业务活动
        if (CollectionUtils.isNotEmpty(wrapperDTO.getBusinessActivityConfig())) {
            List<BusinessActivityDetailResponse> activity = Lists.newArrayList();
            wrapperDTO.getBusinessActivityConfig().forEach(data -> {
                BusinessActivityDetailResponse dto = new BusinessActivityDetailResponse();
                dto.setBusinessActivityId(data.getBusinessActivityId());
                dto.setBusinessActivityName(data.getBusinessActivityName());
                dto.setTargetTPS(data.getTargetTPS());
                dto.setTargetRT(data.getTargetRT());
                dto.setTargetSuccessRate(data.getTargetSuccessRate());
                dto.setTargetSA(data.getTargetSA());
                activity.add(dto);
            });
            detailDTO.setBusinessActivityConfig(activity);
        }
        //施压配置
        detailDTO.setConcurrenceNum(wrapperDTO.getConcurrenceNum());
        detailDTO.setIpNum(wrapperDTO.getIpNum());
        detailDTO.setPressureMode(
                dictionaryCache.getObjectByParam(DicKeyConstant.PT_MODEL, wrapperDTO.getPressureMode()));
        detailDTO.setPressureTestTime(wrapperDTO.getPressureTestTime());
        detailDTO.setIncreasingTime(wrapperDTO.getIncreasingTime());
        detailDTO.setStep(wrapperDTO.getStep());
        detailDTO.setEstimateFlow(wrapperDTO.getEstimateFlow());
        //上传文件
        if (CollectionUtils.isNotEmpty(wrapperDTO.getUploadFile())) {
            List<ScriptDetailResponse> script = Lists.newArrayList();
            wrapperDTO.getUploadFile().forEach(data -> {
                ScriptDetailResponse dto = new ScriptDetailResponse();
                dto.setFileName(data.getFileName());
                dto.setUploadTime(data.getUploadTime());
                dto.setFileType(dictionaryCache.getObjectByParam(DicKeyConstant.FILE_TYPE, data.getFileType()));
                dto.setUploadedData(data.getUploadedData());
                dto.setIsSplit(dictionaryCache.getObjectByParam(DicKeyConstant.IS_DELETED, data.getIsSplit()));
                script.add(dto);
            });
            detailDTO.setUploadFile(script);
        }

        //SLA配置
        if (CollectionUtils.isNotEmpty(wrapperDTO.getStopCondition())) {
            List<SlaDetailResponse> sla = Lists.newArrayList();
            wrapperDTO.getStopCondition().forEach(data -> {
                SlaDetailResponse stop = new SlaDetailResponse();
                stop.setRuleName(data.getRuleName());
                stop.setBusinessActivity(
                        convertIdsToNames(data.getBusinessActivity(), detailDTO.getBusinessActivityConfig()));
                stop.setRule(buildRule(data));
                stop.setStatus(dictionaryCache.getObjectByParam(DicKeyConstant.LIVE_STATUS, data.getStatus()));
                sla.add(stop);
            });
            detailDTO.setStopCondition(sla);
        }

        if (CollectionUtils.isNotEmpty(wrapperDTO.getWarningCondition())) {
            List<SlaDetailResponse> sla = Lists.newArrayList();
            wrapperDTO.getWarningCondition().forEach(data -> {
                SlaDetailResponse stop = new SlaDetailResponse();
                stop.setRuleName(data.getRuleName());
                stop.setBusinessActivity(
                        convertIdsToNames(data.getBusinessActivity(), detailDTO.getBusinessActivityConfig()));
                stop.setRule(buildRule(data));
                stop.setStatus(dictionaryCache.getObjectByParam(DicKeyConstant.LIVE_STATUS, data.getStatus()));
                sla.add(stop);
            });
            detailDTO.setWarningCondition(sla);
        }

        return detailDTO;
    }

    private String buildRule(SceneSlaRefResponse slaRefDTO) {
        SceneSlaRefInput input = new SceneSlaRefInput();
        BeanUtils.copyProperties(slaRefDTO, input);
        Map<String, Object> dataMap = SlaUtil.matchCondition(input, new SendMetricsEvent());
        StringBuffer sb = new StringBuffer();
        sb.append(dataMap.get("type"));
        sb.append(dataMap.get("compare"));
        sb.append(slaRefDTO.getRule().getDuring());
        sb.append(dataMap.get("unit"));
        sb.append("连续出现");
        sb.append(slaRefDTO.getRule().getTimes());
        sb.append("次");
        return sb.toString();
    }

    private String convertIdsToNames(String[] ids, List<BusinessActivityDetailResponse> detailList) {
        if (ids == null || ids.length == 0 || CollectionUtils.isEmpty(detailList)) {
            return null;
        }
        Map<String, String> detailMap = ListHelper.transferToMap(detailList,
                data -> String.valueOf(data.getBusinessActivityId()),
                BusinessActivityDetailResponse::getBusinessActivityName);

        StringBuffer sb = new StringBuffer();
        for (String id : ids) {
            if ("-1".equals(id)) {
                sb.append("所有");
            } else {
                sb.append(detailMap.get(id));
            }
            sb.append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }

    /**
     * todo 临时方案，后面逐渐去掉这种网络请求
     */
    private void setHttpResponseHeader(Long total) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setHeader("Access-Control-Expose-Headers", PAGE_TOTAL_HEADER);
        response.setHeader(PAGE_TOTAL_HEADER, total + "");
    }
}
