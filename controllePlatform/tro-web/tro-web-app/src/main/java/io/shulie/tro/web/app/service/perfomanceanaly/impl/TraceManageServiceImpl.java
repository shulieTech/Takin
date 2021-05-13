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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.entity.domain.dto.report.ReportDetailDTO;
import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.channel.bean.CommandSend;
import io.shulie.tro.channel.bean.CommandStatus;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.web.app.agent.AgentCommandEnum;
import io.shulie.tro.web.app.agent.AgentCommandFactory;
import io.shulie.tro.web.app.convert.performace.TraceManageResponseConvertor;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.request.perfomanceanaly.TraceManageCreateRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.TraceManageDeployQueryRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.TraceManageQueryListRequest;
import io.shulie.tro.web.app.response.perfomanceanaly.TraceManageCreateResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.TraceManageResponse;
import io.shulie.tro.web.app.service.perfomanceanaly.TraceManageService;
import io.shulie.tro.web.app.service.report.ReportService;
import io.shulie.tro.web.common.domain.WebResponse;
import io.shulie.tro.web.common.enums.TraceManageStatusEnum;
import io.shulie.tro.web.common.vo.agent.TraceVO;
import io.shulie.tro.web.config.sync.api.TraceManageSyncService;
import io.shulie.tro.web.data.dao.perfomanceanaly.TraceManageDAO;
import io.shulie.tro.web.data.param.tracemanage.TraceManageCreateParam;
import io.shulie.tro.web.data.param.tracemanage.TraceManageDeployCreateParam;
import io.shulie.tro.web.data.param.tracemanage.TraceManageDeployUpdateParam;
import io.shulie.tro.web.data.param.tracemanage.TraceManageQueryParam;
import io.shulie.tro.web.data.result.tracemanage.TraceManageDeployResult;
import io.shulie.tro.web.data.result.tracemanage.TraceManageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhaoyong
 */
@Component
@Slf4j
public class TraceManageServiceImpl implements TraceManageService {

    private static final int MAX_LEVEL = 5;
    @Autowired
    private TraceManageDAO traceManageDAO;
    @Autowired
    private TraceManageSyncService traceManageSyncService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private AgentCommandFactory agentCommandFactory;

    @Override
    public TraceManageCreateResponse createTraceManage(TraceManageCreateRequest traceManageCreateRequest) throws Exception {
        TraceManageCreateResponse traceManageCreateResponse = new TraceManageCreateResponse();
        String processName = traceManageCreateRequest.getProcessName();
        if (StringUtils.isBlank(processName)) {
            throw new TroWebException(ExceptionCode.TRACE_MANAGE_PARAM_VALID_ERROR, "进程名称为空！");
        }
        String[] split = StringUtils.split(processName, "|");
        String agentId = split[1];
        String serverIp = split[0];
        int num = traceManageDAO.countTraceIngManageDeployByAgentId(agentId);
        if (num > 0) {
            throw new TroWebException(ExceptionCode.TRACE_MANAGE_VALID_ERROR, "当前应用已有追踪任务执行中，请稍后重试");
        }
        if (traceManageCreateRequest.getReportId() == null) {
            throw new TroWebException(ExceptionCode.TRACE_MANAGE_PARAM_VALID_ERROR, "报告id为空！");
        }
        WebResponse reportDetail = reportService.getReportByReportId(traceManageCreateRequest.getReportId());
        if (reportDetail == null || !reportDetail.getSuccess() || reportDetail.getData() == null) {
            throw new TroWebException(ExceptionCode.TRACE_MANAGE_VALID_ERROR, "找不到当前对应的报告！");
        }
        String bean2Json = JsonHelper.bean2Json(reportDetail.getData());
        ReportDetailDTO reportDetailDTO = JsonHelper.json2Bean(bean2Json, ReportDetailDTO.class);
        if (reportDetailDTO.getTaskStatus() != 1) {
            throw new TroWebException(ExceptionCode.TRACE_MANAGE_VALID_ERROR, "当前报告不在压测中，不能追踪该报告！");
        }
        //不管数据库有没有这个数据，当前新增一条追踪数据
        if (traceManageCreateRequest.getTraceManageDeployId() != null) {
            TraceManageDeployResult traceManageDeployResult = traceManageDAO.queryTraceManageDeployById(
                traceManageCreateRequest.getTraceManageDeployId());
            if (traceManageDeployResult == null) {
                throw new TroWebException(ExceptionCode.TRACE_MANAGE_VALID_ERROR, "找不到对应的追踪方法实例！");
            }
            if (traceManageDeployResult.getLevel() >= MAX_LEVEL) {
                throw new TroWebException(ExceptionCode.TRACE_MANAGE_VALID_ERROR, "当前追踪实例已达到5层，请重新开始追踪！");
            }
            if (StringUtils.isBlank(traceManageDeployResult.getSampleId())) {
                String sampleId = createSampleId(traceManageDeployResult.toString());
                traceManageDeployResult.setSampleId(sampleId);
                TraceManageDeployUpdateParam updateParam = new TraceManageDeployUpdateParam();
                BeanUtils.copyProperties(traceManageDeployResult, updateParam);
                updateParam.setStatus(TraceManageStatusEnum.TRACE_RUNNING.getCode());
                traceManageDAO.updateTraceManageDeploy(updateParam);
                createZkTraceManage(agentId, traceManageDeployResult.getSampleId(),
                    traceManageDeployResult.getTraceDeployObject());
                traceManageCreateResponse.setSampleId(sampleId);
            }

        } else {
            TraceManageCreateParam traceManageCreateParam = new TraceManageCreateParam();
            traceManageCreateParam.setTraceObject(traceManageCreateRequest.getTraceObject());
            traceManageCreateParam.setReportId(traceManageCreateRequest.getReportId());
            traceManageCreateParam.setAgentId(agentId);
            traceManageCreateParam.setServerIp(serverIp);
            BeanUtils.copyProperties(traceManageCreateRequest, traceManageCreateParam);
            String sampleId = createSampleId(traceManageCreateParam.toString());
            traceManageDAO.createTraceManageAndDeploy(traceManageCreateParam, sampleId);
            createZkTraceManage(agentId, sampleId, traceManageCreateParam.getTraceObject());
            traceManageCreateResponse.setSampleId(sampleId);
        }
        return traceManageCreateResponse;
    }

    /**
     * 构建zk数据请求
     */
    private void createZkTraceManage(String agentId, String sampleId, String traceDeployObject) throws Exception {
        // agent交互通道
        Map<String, Object> param = Maps.newHashMap();
        param.put("sampleId", sampleId);
        String[] split = StringUtils.split(traceDeployObject, "#");
        param.put("class", split[0]);
        param.put("method", split[1]);
        param.put("limits", 100);
        param.put("wait", 3000);
        agentCommandFactory.send(AgentCommandEnum.PULL_AGENT_INFO_TRACE_COMMAND, agentId, param);
    }

    private String createSampleId(String param) {
        // 改成uuid
        return UUID.randomUUID().toString();
    }

    @Override
    public TraceManageResponse queryTraceManageDeploy(TraceManageDeployQueryRequest traceManageDeployQueryRequest) {
        if (traceManageDeployQueryRequest == null) {
            throw new TroWebException(ExceptionCode.TRACE_MANAGE_PARAM_VALID_ERROR, "查询参数不能为空！");
        }
        if (traceManageDeployQueryRequest.getId() == null && StringUtils.isBlank(
            traceManageDeployQueryRequest.getSampleId())) {
            throw new TroWebException(ExceptionCode.TRACE_MANAGE_PARAM_VALID_ERROR, "查询参数不能都为空！");
        }
        Boolean traceStatus;
        if (!StringUtils.isBlank(traceManageDeployQueryRequest.getSampleId())) {
            //有追踪凭证，说明是查询实况的报告信息
            TraceManageDeployResult traceManageDeployResult = traceManageDAO.queryTraceManageDeployBySampleId(
                traceManageDeployQueryRequest.getSampleId());
            if (traceManageDeployResult == null) {
                throw new TroWebException(ExceptionCode.TRACE_MANAGE_VALID_ERROR, "找不到对应的追踪方法实例！");
            }
            traceManageDeployQueryRequest.setId(traceManageDeployResult.getTraceManageId());
            //如果当前追踪没有结果，告诉前端没有结束
            traceStatus = !TraceManageStatusEnum.isRunning(traceManageDeployResult.getStatus());
            //如果追踪状态为3，表示追踪已经失败
            if (TraceManageStatusEnum.TRACE_TIMEOUT.getCode().equals(traceManageDeployResult.getStatus())) {
                throw new TroWebException(ExceptionCode.TRACE_MANAGE_TIMEOUT, "请稍后重试！");
            }

            if (TraceManageStatusEnum.AGENT_TRACE_ERROR.getCode().equals(traceManageDeployResult.getStatus())) {
                String errorMessage = TraceManageStatusEnum.AGENT_TRACE_ERROR.getName();
                if (StringUtils.isNotBlank(traceManageDeployResult.getFeature())) {
                    Map<String, String> error = JsonHelper.string2Obj(traceManageDeployResult.getFeature(),
                        new TypeReference<Map<String, String>>() {});
                    errorMessage = error.get("agentError");
                }
                throw new TroWebException(ExceptionCode.TRACE_MANAGE_ERROR, errorMessage);
            }

        } else {
            traceStatus = true;
        }
        if (traceManageDeployQueryRequest.getId() != null) {
            TraceManageResponse result = getTraceManageResponseByTraceManageResult(
                traceManageDAO.queryTraceManageById(traceManageDeployQueryRequest.getId()));
            result.setTraceStatus(traceStatus);
            return result;
        }
        return null;
    }

    @Override
    public void uploadTraceInfo(CommandPacket commandPacket) {
        if (commandPacket == null) {
            log.error("上传trace信息，参数为空");
            throw new TroWebException(ExceptionCode.TRACE_MANAGE_PARAM_VALID_ERROR, "上传参数不能为空！");
        }
        // 获取sampleId
        String sampleId = Optional.ofNullable(commandPacket.getSend())
            .map(CommandSend::getParam).map(map -> map.get("sampleId").toString())
            .orElseThrow(() -> new TroWebException(ExceptionCode.TRACE_MANAGE_PARAM_VALID_ERROR, "上传参数唯一id为空！"));
        TraceManageDeployResult deployResult = traceManageDAO.queryTraceManageDeployBySampleId(sampleId);
        if (deployResult == null) {
            log.error("上传trace信息,根据sampleId查询结果为空,sampleId:{}", sampleId);
            throw new TroWebException(ExceptionCode.TRACE_MANAGE_PARAM_VALID_ERROR, "根据sampleId找不到对应的值!");
        }
        TraceManageDeployUpdateParam traceManageDeployUpdateParam = new TraceManageDeployUpdateParam();
        BeanUtils.copyProperties(deployResult, traceManageDeployUpdateParam);

        CommandStatus commandStatus = commandPacket.getStatus();
        Map<String, String> error = Maps.newHashMap();
        if (!CommandStatus.COMMAND_COMPLETED_SUCCESS.equals(commandStatus)) {
            // 失败
            error.put("agentError", "agent执行失败，命令状态" + commandStatus.getStatus());
            updateTraceError(error, traceManageDeployUpdateParam);
            return;
        }
        if (commandPacket.getResponse() == null) {
            // 失败
            error.put("agentError", "agent执行失败，响应结果为空");
            updateTraceError(error, traceManageDeployUpdateParam);
            return;
        }

        if (!commandPacket.getResponse().isSuccess()) {
            // 失败
            error.put("agentError", "agent执行失败，命令结果:" + commandPacket.getResponse().getMessage());
            updateTraceError(error, traceManageDeployUpdateParam);
            return;
        }
        List<LinkedHashMap<String, Object>> result =
            (List<LinkedHashMap<String, Object>>)commandPacket.getResponse().getResult();
        if (result.size() == 0) {
            // 失败
            error.put("agentError", "agent执行失败，命令结果:agent无trace日志返回");
            updateTraceError(error, traceManageDeployUpdateParam);
            return;
        }
        Map<String, List<Long>> costMap = Maps.newHashMap();
        // 转换traceVO
        TraceVO traceVO = convertTraceVo(costMap, result,traceManageDeployUpdateParam);
        // 下级节点不变
        if(traceManageDeployUpdateParam.getLineNum() == null) {
            BeanUtils.copyProperties(traceVO, traceManageDeployUpdateParam);
        }
        traceManageDeployUpdateParam.setStatus(TraceManageStatusEnum.TRACE_CLOSE.getCode());
        traceManageDeployUpdateParam.setHasChildren(CollectionUtils.isNotEmpty(traceVO.getChildren()) ? 1 : 0);
        traceManageDAO.updateTraceManageDeploy(traceManageDeployUpdateParam);
        if (CollectionUtils.isNotEmpty(traceVO.getChildren())) {
            List<TraceManageDeployCreateParam> createParams = Lists.newArrayList();
            // todo 优化空间，先适配前端改
            getCreateParams(createParams, traceVO.getChildren(), deployResult);
            traceManageDAO.createTraceManageDeploy(createParams);
        }
    }

    private TraceVO convertTraceVo(Map<String, List<Long>> costMap, List<LinkedHashMap<String, Object>> result,
        TraceManageDeployUpdateParam param) {
        // 组装costMap,平整化层级树，并且求出最深的序列
        Map<Integer, Integer> deepestLevel = convertCostMap(costMap, result);
        TraceVO traceVO = new TraceVO();
        for (Integer key : deepestLevel.keySet()) {
            LinkedHashMap<String, Object> linkedHashMap = result.get(key);
            LinkedHashMap<String, Object> root = (LinkedHashMap<String, Object>)linkedHashMap.get("root");
            // 组装map ->traceVO
            costMapToTraceVO(costMap, root, traceVO, "root",param);
            if (null == root.get("children")) {
                return traceVO;
            }
            List<LinkedHashMap<String, Object>> children = (List<LinkedHashMap<String, Object>>)root.get("children");
            forEachChildrenToTraceVO(costMap, children, traceVO,param);
        }
        return traceVO;
    }

    private void forEachChildrenToTraceVO(Map<String, List<Long>> costMap,
        List<LinkedHashMap<String, Object>> children, TraceVO traceVO,TraceManageDeployUpdateParam param) {
        if (children == null || children.size() == 0) {
            return;
        }
        for (LinkedHashMap<String, Object> childrenMap : children) {
            if (!childrenMap.containsKey("className")) {
                continue;
            }
            costMapToTraceVO(costMap, childrenMap, traceVO, "children",param);
            if (null == childrenMap.get("children")) {
                continue;
            }
            forEachChildrenToTraceVO(costMap, (List<LinkedHashMap<String, Object>>)childrenMap.get("children"),
                traceVO,param);
        }
    }

    private void costMapToTraceVO(Map<String, List<Long>> costMap, LinkedHashMap<String, Object> map,
        TraceVO traceVO, String type,TraceManageDeployUpdateParam param) {
        if ("root".equals(type)) {
            TraceVO temp = getTraceVO(map, costMap,param,null);
            if (temp != null) {
                BeanUtils.copyProperties(temp, traceVO);
            }
            return;
        }
        TraceVO children = getTraceVO(map, costMap,param,traceVO);
        if (traceVO.getChildren() == null) {
            traceVO.setChildren(Lists.newArrayList());
        }
        traceVO.getChildren().add(children);
    }

    /**
     * 组装数据
     *
     * @return
     */
    private TraceVO getTraceVO(LinkedHashMap<String, Object> map, Map<String, List<Long>> costMap,
        TraceManageDeployUpdateParam param,TraceVO targetVO) {
        String key = getKey(map);
        if (costMap.get(key) == null || costMap.get(key).size() == 0) {
            return null;
        }
        String className = String.valueOf(map.get("className"));
        String methodName = String.valueOf(map.get("methodName"));
        Integer line = Integer.valueOf(String.valueOf(map.get("line")));
        TraceVO traceVO = new TraceVO();
        traceVO.setTraceDeployObject(className + "#" + methodName);
        traceVO.setLineNum(line);
        // 可以中位数、分位数计算,需要计算比例
        BigDecimal source = param.getAvgCost();
        BigDecimal scale = (source == null || targetVO == null || targetVO.getAvgCost() == null)? BigDecimal.valueOf(1):
            source.divide(targetVO.getAvgCost(),6, RoundingMode.HALF_UP);

        traceVO.setCosts(costMap.get(key));
        traceVO.setAvgCost(getAvgCost(costMap.get(key)).multiply(scale).setScale(6, RoundingMode.HALF_UP));
        traceVO.setP50(getPercentile(costMap.get(key), 0.50).multiply(scale).setScale(6, RoundingMode.HALF_UP));
        traceVO.setP90(getPercentile(costMap.get(key), 0.90).multiply(scale).setScale(6, RoundingMode.HALF_UP));
        traceVO.setP95(getPercentile(costMap.get(key), 0.95).multiply(scale).setScale(6, RoundingMode.HALF_UP));
        traceVO.setP99(getPercentile(costMap.get(key), 0.99).multiply(scale).setScale(6, RoundingMode.HALF_UP));
        traceVO.setMax(getMaxMinCost(costMap.get(key), "max").multiply(scale).setScale(6, RoundingMode.HALF_UP));
        traceVO.setMin(getMaxMinCost(costMap.get(key), "min").multiply(scale).setScale(6, RoundingMode.HALF_UP));
        return traceVO;
    }

    /**
     * 分位数计算
     *
     * @return
     */
    public BigDecimal getPercentile(List<Long> costs, double p) {
        int n = costs.size();
        List<Long> sortCosts = costs.stream().sorted().collect(Collectors.toList());
        double px = p * (n - 1);
        int i = (int)java.lang.Math.floor(px);
        double g = px - i;
        if (g == 0) {
            return BigDecimal.valueOf(sortCosts.get(i + 1) / 1000000.0).setScale(6, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.valueOf(((1 - g) * sortCosts.get(i) + g * sortCosts.get(i + 1)) / 1000000.0)
                .setScale(6, RoundingMode.HALF_UP);
        }
    }

    /**
     * 求最大最小
     *
     * @return
     */
    public BigDecimal getMaxMinCost(List<Long> costs, String type) {
        OptionalDouble optionalDouble;
        if ("max".equals(type)) {
            optionalDouble = costs.stream().mapToDouble(Long::longValue).max();
        } else {
            optionalDouble = costs.stream().mapToDouble(Long::longValue).min();
        }
        if (optionalDouble.isPresent()) {
            return BigDecimal.valueOf(optionalDouble.getAsDouble() / 1000000.0).setScale(6, RoundingMode.HALF_UP);
        }
        return BigDecimal.valueOf(0);
    }

    /**
     * 求平均值
     *
     * @return
     */
    public BigDecimal getAvgCost(List<Long> costs) {
        OptionalDouble optionalDouble = costs.stream().mapToDouble(Long::longValue).average();
        if (optionalDouble.isPresent()) {
            return BigDecimal.valueOf(optionalDouble.getAsDouble() / 1000000.0).setScale(6, RoundingMode.HALF_UP);
        }
        return BigDecimal.valueOf(0);
    }

    private Map<Integer, Integer> convertCostMap(Map<String, List<Long>> costMap,
        List<LinkedHashMap<String, Object>> result) {
        // 求出 最深序列
        Integer level = 0, i = 0, deepest = 0;
        for (LinkedHashMap<String, Object> map : result) {
            LinkedHashMap<String, Object> root = (LinkedHashMap<String, Object>)map.get("root");
            if (!root.containsKey("className")) {
                continue;
            }
            // 核心代码，组装costMap
            getCostMap(costMap, root);
            if (null == root.get("children")) {
                continue;
            }
            List<LinkedHashMap<String, Object>> children = (List<LinkedHashMap<String, Object>>)root.get("children");
            // 递归子节点，将子节点数据组装到costMap
            Integer temp = forEachChildren(costMap, children, 0);
            // 大小排序
            if (temp > level) {
                level = temp;
                deepest = i;
            }
            i++;
        }
        Map<Integer, Integer> deepestLevel = Maps.newHashMap();
        deepestLevel.put(deepest, level);
        return deepestLevel;
    }

    /**
     * 递归源数据子节点，组装到costMap
     *
     * @return
     */
    private Integer forEachChildren(Map<String, List<Long>> costMap, List<LinkedHashMap<String, Object>> children,
        Integer temp) {
        if (children == null || children.size() == 0) {
            return temp;
        }
        temp = temp + 1;
        for (LinkedHashMap<String, Object> childrenMap : children) {
            if (!childrenMap.containsKey("className")) {
                continue;
            }
            // 核心代码，组装costMap
            getCostMap(costMap, childrenMap);
            if (null == childrenMap.get("children")) {
                continue;
            }
            temp = forEachChildren(costMap, (List<LinkedHashMap<String, Object>>)childrenMap.get("children"), temp);
        }
        return temp;
    }

    /**
     * 获取costMap的key值
     *
     * @return
     */
    private String getKey(LinkedHashMap<String, Object> map) {
        String className = String.valueOf(map.get("className"));
        String methodName = String.valueOf(map.get("methodName"));
        int line = Integer.parseInt(String.valueOf(map.get("line")));
        return className + "#" + methodName + "#" + line;
    }

    /**
     * 核心代码
     */
    private void getCostMap(Map<String, List<Long>> costMap, LinkedHashMap<String, Object> map) {
        Long cost = map.get("cost") == null ? 0 : Long.parseLong(map.get("cost").toString());
        costMap.compute(getKey(map), (k, v) -> {
            List<Long> longs = (v == null ? Lists.newArrayList() : v);
            // 纳秒级，需要转化成毫秒级
            longs.add(cost);
            return longs;
        });
    }

    private void updateTraceError(Map<String, String> error, TraceManageDeployUpdateParam updateParam) {
        updateParam.setStatus(TraceManageStatusEnum.AGENT_TRACE_ERROR.getCode());
        updateParam.setFeature(JsonHelper.bean2Json(error));
        traceManageDAO.updateTraceManageDeploy(updateParam);
    }

    @Override
    public List<TraceManageResponse> queryTraceManageList(TraceManageQueryListRequest traceManageQueryListRequest) {
        TraceManageQueryParam traceManageQueryParam = new TraceManageQueryParam();
        BeanUtils.copyProperties(traceManageQueryListRequest, traceManageQueryParam);
        List<TraceManageResult> traceManageResults = traceManageDAO.queryTraceManageList(traceManageQueryParam);
        if (CollectionUtils.isNotEmpty(traceManageResults)) {
            return traceManageResults.stream().map(TraceManageResponseConvertor.INSTANCE::ofTraceManageResponse)
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 构建方法追踪实例的创建参数
     *
     * @return
     */
    private void getCreateParams(List<TraceManageDeployCreateParam> createParams, List<TraceVO> children,
        TraceManageDeployResult result) {
        if (CollectionUtils.isNotEmpty(children)) {
            for (TraceVO vo : children) {
                TraceManageDeployCreateParam createParam = new TraceManageDeployCreateParam();
                BeanUtils.copyProperties(vo, createParam);
                createParam.setTraceManageId(result.getTraceManageId());
                createParam.setLevel(result.getLevel() + 1);
                // 层级关系靠这个，todo 有优化空间，先临时适配原版
                createParam.setParentId(result.getId());
                createParam.setTraceDeployObject(vo.getTraceDeployObject());
                //createParam.setLineNum(vo.getLineNum());
                //createParam.setAvgTime(vo.getAvgCost());
                createParam.setStatus(TraceManageStatusEnum.TRACE_WAIT.getCode());
                createParam.setHasChildren(2);
                createParams.add(createParam);
            }
        }
    }

    private TraceManageResponse getTraceManageResponseByTraceManageResult(TraceManageResult traceManageResult) {
        if (traceManageResult == null) {
            return null;
        }
        TraceManageResponse traceManageResponse = TraceManageResponseConvertor.INSTANCE.ofTraceManageResponse(
            traceManageResult);
        if (traceManageResult.getTraceManageDeployResult() != null) {
            traceManageResponse.setTraceManageDeployResponse(TraceManageResponseConvertor.INSTANCE
                .ofTraceManageDeployResponse(traceManageResult.getTraceManageDeployResult()));
        }
        return traceManageResponse;
    }

}
