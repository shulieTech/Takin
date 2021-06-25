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

package io.shulie.tro.cloud.biz.service.scenetask;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pamirs.tro.entity.dao.report.TReportBusinessActivityDetailMapper;
import com.pamirs.tro.entity.dao.report.TReportMapper;
import com.pamirs.tro.entity.dao.scenemanage.TSceneManageMapper;
import com.pamirs.tro.entity.domain.entity.report.Report;
import com.pamirs.tro.entity.domain.entity.report.ReportBusinessActivityDetail;
import com.pamirs.tro.entity.domain.entity.scenemanage.SceneManage;
import com.pamirs.tro.entity.domain.entity.settle.AccountBook;
import com.pamirs.tro.entity.domain.vo.report.SceneTaskNotifyParam;
import com.pamirs.tro.entity.domain.vo.settle.AccountTradeRequest;
import io.shulie.tro.cloud.biz.collector.collector.CollectorService;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneManageWrapperInput;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneScriptRefInput;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneSlaRefInput;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneStartTrialRunInput;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneTaskQueryTpsInput;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneTaskStartInput;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneTaskUpdateTpsInput;
import io.shulie.tro.cloud.biz.output.scenemanage.SceneManageWrapperOutput;
import io.shulie.tro.cloud.biz.output.scenemanage.SceneStartTrialRunOutput;
import io.shulie.tro.cloud.biz.output.scenetask.SceneActionOutput;
import io.shulie.tro.cloud.biz.output.scenetask.SceneTaskQueryTpsOutput;
import io.shulie.tro.cloud.biz.service.scene.SceneManageService;
import io.shulie.tro.cloud.biz.service.scene.SceneTaskEventServie;
import io.shulie.tro.cloud.biz.service.scene.SceneTaskService;
import io.shulie.tro.cloud.biz.service.settle.AccountService;
import io.shulie.tro.cloud.biz.service.settle.SettleService;
import io.shulie.tro.cloud.biz.utils.SaxUtil;
import io.shulie.tro.cloud.common.bean.RuleBean;
import io.shulie.tro.cloud.common.bean.TimeBean;
import io.shulie.tro.cloud.common.bean.scenemanage.SceneBusinessActivityRefBean;
import io.shulie.tro.cloud.common.bean.scenemanage.SceneManageQueryOpitons;
import io.shulie.tro.cloud.common.bean.scenemanage.UpdateStatusBean;
import io.shulie.tro.cloud.common.bean.task.TaskResult;
import io.shulie.tro.cloud.common.constants.*;
import io.shulie.tro.cloud.common.enums.PressureTypeEnums;
import io.shulie.tro.cloud.common.enums.scenemanage.SceneManageStatusEnum;
import io.shulie.tro.cloud.common.exception.ApiException;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.cloud.common.utils.CustomUtil;
import io.shulie.tro.cloud.data.dao.report.ReportDao;
import io.shulie.tro.cloud.data.result.report.ReportResult;
import io.shulie.tro.cloud.data.dao.scenemanage.SceneManageDAO;
import io.shulie.tro.cloud.data.result.scenemanage.SceneManageListResult;
import io.shulie.tro.utils.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author 莫问
 * @Date 2020-04-22
 */
@Service
@Slf4j
public class SceneTaskServiceImpl implements SceneTaskService {

    @Resource
    private TSceneManageMapper TSceneManageMapper;

    @Autowired
    private SceneManageService sceneManageService;

    @Autowired
    private SceneTaskEventServie sceneTaskEventServie;

    @Resource
    private TReportMapper TReportMapper;

    @Autowired
    private ReportDao reportDao;

    @Resource
    private TReportBusinessActivityDetailMapper TReportBusinessActivityDetailMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SettleService settleService;

    @Autowired
    private RedisClientUtils redisClientUtils;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private SceneManageDAO sceneManageDao;

    @Override
    @Transactional
    public SceneActionOutput start(SceneTaskStartInput input) {
        return startTask(input, null);
    }

    private SceneActionOutput startTask(SceneTaskStartInput input, SceneStartTrialRunInput trialRunInput) {
        SceneManageQueryOpitons options = new SceneManageQueryOpitons();
        options.setIncludeBusinessActivity(true);
        options.setIncludeScript(true);
        SceneManageWrapperOutput sceneData = sceneManageService.getSceneManage(input.getSceneId(), options);
        // add by lipeng 设置引擎插件id
        sceneData.setEnginePluginIds(input.getEnginePluginIds());
        // add end
        if (trialRunInput != null) {
            sceneData.setPressureTestSecond(trialRunInput.getPressureTestSecond());
        }
        //启动前置校验
        preCheckStart(sceneData);

        //创建临时报表数据
        Report report = initReport(sceneData, input);

        SceneActionOutput sceneAction = new SceneActionOutput();
        sceneAction.setData(report.getId());
        // 报告已经完成，则退出
        if (report.getStatus() == ReportConstans.FINISH_STATUS) {
            //失败状态
            JSONObject jb = JSON.parseObject(report.getFeatures());
            sceneAction.setMsg(Arrays.asList(jb.getString(ReportConstans.PRESSURE_MSG).split(",")));
            return sceneAction;
        }

        //冻结流量 todo 需要带客户信息
        AccountTradeRequest request = new AccountTradeRequest();
        request.setExpectThroughput(sceneData.getConcurrenceNum());
        request.setIncreasingTime(sceneData.getIncreasingSecond());
        request.setPressureMode(sceneData.getPressureMode());
        request.setPressureTotalTime(sceneData.getTotalTestTime());
        request.setSceneId(input.getSceneId());
        request.setTaskId(report.getId());
        request.setPressureType(sceneData.getPressureType());
        request.setUid(sceneData.getCustomId());
        request.setStep(sceneData.getStep());
        settleService.lockAccount(request);

        //广播事件
        sceneTaskEventServie.callStartEvent(sceneData, report.getId());

        return sceneAction;
    }

    @Override
    public void stop(Long sceneId) {
        SceneManage sceneManage = TSceneManageMapper.selectByPrimaryKey(sceneId);
        if (sceneManage == null) {
            log.error("停止压测失败，场景[{}]不存在", sceneId);
            throw ApiException.create(Constants.API_ERROR_CODE, "停止压测失败，场景不存在");
        }
        if (SceneManageStatusEnum.ifStop(sceneManage.getStatus())) {
            log.error("停止压测失败，场景[{}]不再工作中", sceneId);
            throw ApiException.create(Constants.API_ERROR_CODE, "停止压测失败，场景状态不是压测中状态");
        }
        ReportResult reportResult = reportDao.getReportBySceneId(sceneId);

        if (reportResult != null) {
            sceneTaskEventServie.callStopEvent(reportResult);
        }
    }

    @Override
    public SceneActionOutput checkSceneTaskStatus(Long sceneId) {
        SceneActionOutput scene = new SceneActionOutput();
        SceneManage sceneManage = TSceneManageMapper.selectByPrimaryKey(sceneId);
        if (sceneManage != null) {
            // 监测启动状态
            scene.setData(SceneManageStatusEnum.getAdaptStatus(sceneManage.getStatus()).longValue());
            if (sceneManage.getStatus() >= 1) {
                ReportResult reportResult = reportDao.getReportBySceneId(sceneId);
                if (reportResult != null) {
                    scene.setReportId(reportResult.getId());
                    if (StringUtils.isNotEmpty(reportResult.getFeatures())) {
                        JSONObject jb = JSON.parseObject(reportResult.getFeatures());
                        scene.setMsg(Arrays.asList(jb.getString(ReportConstans.FEATURES_ERROR_MSG)));
                    }
                }
            }
        }
        return scene;
    }

    /**
     * 报告生成触发条件，metric数据完全上传至influxdb 才触发
     * 可以查看 finished 事件
     */
    @Override
    public void handleSceneTaskEvent(TaskResult taskResult) {
        if (taskResult != null && taskResult.getStatus() != null) {
            switch (taskResult.getStatus()) {
                case FAILED:
                    //启动失败
                    testFailed(taskResult);
                    break;
                case STARTED:
                    testStarted(taskResult);
                    break;
                default:
                    log.warn("其他状态暂时无需处理");
                    break;
            }
        }
    }

    @Override
    public String taskResultNotify(SceneTaskNotifyParam param) {
        return sceneTaskEventServie.callStartResultEvent(param);
    }

    @Override
    public void updateSceneTaskTps(SceneTaskUpdateTpsInput input) {
        String engineInstanceRedisKey = PressureInstanceRedisKey.getEngineInstanceRedisKey(input.getSceneId(), input.getReportId(),
                input.getCustomerId());
        Object totalIp = redisTemplate.opsForHash().get(engineInstanceRedisKey, PressureInstanceRedisKey.SecondRedisKey.REDIS_TPS_POD_NUM);
        if (totalIp == null){
            log.error("更新运行任务tps，获取不到pod总数！");
            return;
        }
        BigDecimal podTpsNum = new BigDecimal(input.getTpsNum()).divide(new BigDecimal(totalIp.toString()), 0, BigDecimal.ROUND_UP);
        redisTemplate.opsForHash().put(engineInstanceRedisKey,PressureInstanceRedisKey.SecondRedisKey.REDIS_TPS_ALL_LIMIT,input.getTpsNum()+"");
        redisTemplate.opsForHash().put(engineInstanceRedisKey,PressureInstanceRedisKey.SecondRedisKey.REDIS_TPS_LIMIT,podTpsNum+"");
    }

    @Override
    public SceneTaskQueryTpsOutput queryAdjustTaskTps(SceneTaskQueryTpsInput input) {
        String engineInstanceRedisKey = PressureInstanceRedisKey.getEngineInstanceRedisKey(input.getSceneId(), input.getReportId(),
                input.getCustomerId());
        Object object = redisTemplate.opsForHash().get(engineInstanceRedisKey, PressureInstanceRedisKey.SecondRedisKey.REDIS_TPS_ALL_LIMIT);

        SceneTaskQueryTpsOutput sceneTaskQueryTpsOutput = new SceneTaskQueryTpsOutput();
        if (object != null){
            sceneTaskQueryTpsOutput.setTotalTps(Long.parseLong(object.toString()));
            return sceneTaskQueryTpsOutput;
        }
        return null;
    }

    @Override
    public Long startFlowDebugTask(SceneManageWrapperInput input, List<Long> enginePluginIds) {

        Long sceneManageId;
        Long userId = CustomUtil.getUserId();
        //首先根据脚本实例id构建压测场景名称
        String pressureTestSceneName = SceneManageConstant.SCENE_MANAGER_FLOW_DEBUG + userId + "_" + input.getScriptId();

        //根据场景名称查询是否已经存在场景
        SceneManageListResult sceneManageResult = sceneManageDao.queryBySceneName(pressureTestSceneName);

        //不存在，新增压测场景
        if (sceneManageResult == null){
            input.setCustomId(userId);
            input.setPressureTestSceneName(pressureTestSceneName);
            input.setPressureType(PressureTypeEnums.FLOW_DEBUG.getCode());
            input.setConcurrenceNum(1);
            input.setIpNum(1);
            input.setPressureTestTime(new TimeBean(30L,"m"));
            input.setPressureMode(0);
            input.setUserId(userId);
            input.setType(1);

            SceneSlaRefInput sceneSlaRefInput = new SceneSlaRefInput();
            sceneSlaRefInput.setRuleName("FLOW_DEBUG_SLA");
            sceneSlaRefInput.setBusinessActivity( new String[]{"-1"});
            RuleBean ruleBean = new RuleBean();
            ruleBean.setIndexInfo(0);
            ruleBean.setCondition(0);
            ruleBean.setDuring(new BigDecimal("10000"));
            ruleBean.setTimes(100);
            sceneSlaRefInput.setRule(ruleBean);
            sceneSlaRefInput.setStatus(0);
            input.setStopCondition(Collections.singletonList(sceneSlaRefInput));
            input.setWarningCondition(Collections.EMPTY_LIST);
            sceneManageId = sceneManageService.addSceneManage(input);

            //修改压测脚本内容,将存在的压测标去掉
            List<SceneScriptRefInput> collect = input.getUploadFile().stream().filter(o -> o.getFileName().endsWith(".jmx")).collect(
                Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)){
                String destPath = sceneManageService.getDestPath(sceneManageId);
                //目前脚本里面只会有一个jmx文件
                SaxUtil.updatePressTestTags(destPath + collect.get(0).getFileName());
            }
        } else {
            sceneManageId = sceneManageResult.getId();
        }

        //启动该压测场景
        SceneTaskStartInput sceneTaskStartInput = new SceneTaskStartInput();
        sceneTaskStartInput.setSceneId(sceneManageId);
        sceneTaskStartInput.setUid(userId);
        sceneTaskStartInput.setEnginePluginIds(enginePluginIds);
        SceneActionOutput sceneActionDTO = startTask(sceneTaskStartInput, null);
        //返回报告id
        return sceneActionDTO.getData();
    }

    @Override
    public SceneStartTrialRunOutput startTrialRun(SceneStartTrialRunInput input) {
        SceneTaskStartInput sceneTaskStartInput = new SceneTaskStartInput();
        BeanUtils.copyProperties(input, sceneTaskStartInput);
        SceneActionOutput sceneActionDTO = startTask(sceneTaskStartInput, input);
        SceneStartTrialRunOutput sceneStartTrialRunOutput = new SceneStartTrialRunOutput();
        sceneStartTrialRunOutput.setData(sceneActionDTO.getData());
        sceneStartTrialRunOutput.setMsg(sceneActionDTO.getMsg());
        return sceneStartTrialRunOutput;
    }

    /**
     * 场景启动前置校验
     */
    private void preCheckStart(SceneManageWrapperOutput sceneData) {
        //流量判断
        if (sceneData.getCustomId() == null) {
            log.error("压测场景无绑定客户信息，id={}", sceneData.getId());
            throw ApiException.create(Constants.API_ERROR_CODE, "压测场景没有绑定客户信息");
        }
        AccountBook accountBook = accountService.getAccountByUserId(sceneData.getCustomId());
        if (accountBook != null && sceneData.getEstimateFlow() != null) {
            if (accountBook.getBalance().compareTo(sceneData.getEstimateFlow()) < 0) {
                log.error("[{}]场景流量不够，压测任务发起失败", sceneData.getId());
                throw ApiException.create(Constants.API_ERROR_CODE, "流量不足");
            }
        }
    }

    /**
     * 初始化报表
     *
     * @return
     */
    public Report initReport(SceneManageWrapperOutput scene, SceneTaskStartInput input) {
        Report report = new Report();
        report.setSceneId(scene.getId());
        report.setConcurrent(scene.getConcurrenceNum());
        report.setStatus(ReportConstans.INIT_STATUS);
        report.setCustomId(scene.getCustomId());
        report.setOperateId(input.getUid());
        report.setDeptId(input.getDeptId());
        //负责人默认启动人
        report.setUserId(input.getUid());
        report.setSceneName(scene.getPressureTestSceneName());
        if (scene.getFeatures() != null) {
            Map<String, String> map = JsonHelper.string2Obj(scene.getFeatures(),
                new TypeReference<Map<String, String>>() {});
            if (map != null && map.get(SceneManageConstant.FEATURES_SCRIPT_ID) != null) {
                report.setScriptId(Long.valueOf(map.get(SceneManageConstant.FEATURES_SCRIPT_ID)));
            }
        }
        int sumTps = scene.getBusinessActivityConfig().stream().mapToInt(SceneBusinessActivityRefBean::getTargetTPS).sum();
        report.setTps(sumTps);
        report.setPressureType(scene.getPressureType());
        report.setType(scene.getType());
        TReportMapper.insertSelective(report);

        //标记场景
        // 待启动,压测失败，停止压测（压测工作已停止） ---> 启动中
        Boolean updateFlag = sceneManageService.updateSceneLifeCycle(
            UpdateStatusBean.build(scene.getId(), report.getId(), scene.getCustomId())
                .checkEnum(SceneManageStatusEnum.WAIT, SceneManageStatusEnum.FAILED, SceneManageStatusEnum.STOP)
                .updateEnum(SceneManageStatusEnum.STARTING).build());
        if (!updateFlag) {
            //失败状态 获取最新的报告
            report = TReportMapper.selectByPrimaryKey(report.getId());
            return report;
        }
        Long reportId = report.getId();
        //初始化业务活动
        scene.getBusinessActivityConfig().stream().forEach(activity -> {
            ReportBusinessActivityDetail reportBusinessActivityDetail = new ReportBusinessActivityDetail();
            reportBusinessActivityDetail.setReportId(reportId);
            reportBusinessActivityDetail.setSceneId(scene.getId());
            reportBusinessActivityDetail.setBusinessActivityId(activity.getBusinessActivityId());
            reportBusinessActivityDetail.setBusinessActivityName(activity.getBusinessActivityName());
            reportBusinessActivityDetail.setApplicationIds(activity.getApplicationIds());
            reportBusinessActivityDetail.setBindRef(activity.getBindRef());
            reportBusinessActivityDetail.setTargetTps(new BigDecimal(activity.getTargetTPS()));
            reportBusinessActivityDetail.setTargetRt(new BigDecimal(activity.getTargetRT()));
            reportBusinessActivityDetail.setTargetSuccessRate(activity.getTargetSuccessRate());
            reportBusinessActivityDetail.setTargetSa(activity.getTargetSA());
            TReportBusinessActivityDetailMapper.insertSelective(reportBusinessActivityDetail);
        });

        log.info("启动[{}]场景测试，初始化报表数据,报表ID: {}", scene.getId(), report.getId());
        return report;
    }

    /**
     * 压测任务正常开启 这里实际是 pod 启动成功
     * 20200923 报告 开始时间 记录在redis 中
     *
     * @see CollectorService
     **/
    @Transactional(rollbackFor = Exception.class)
    public synchronized void testStarted(TaskResult taskResult) {
        log.info("场景[{}-{}-{}]启动新的压测任务", taskResult.getSceneId(), taskResult.getTaskId(), taskResult.getCustomerId());
        //场景压测进行中
        // job创建中 改成 pod工作中 隐式 状态严格 更新 解决 多pod 问题
        // 进行计数
        String podName = ScheduleConstants.getPodName(taskResult.getSceneId(), taskResult.getTaskId(),
            taskResult.getCustomerId());

        // 需要加锁  todo 验证锁是否获取到
        //if(redisClientUtils.lock(podName,taskResult.getTaskId().toString())) {
        redisClientUtils.increment(podName, 1);
        Integer num = Integer.parseInt(redisClientUtils.getString(podName));
        log.info("当前启动pod成功数量=【{}】", num);
        if (num == 1) {
            // 启动只更新一次
            sceneManageService.updateSceneLifeCycle(
                UpdateStatusBean.build(taskResult.getSceneId(), taskResult.getTaskId(), taskResult.getCustomerId())
                    .checkEnum(SceneManageStatusEnum.JOB_CREATEING)
                    .updateEnum(SceneManageStatusEnum.POD_RUNNING).build());
        }
        //    redisClientUtils.unlock(podName,taskResult.getTaskId().toString());
        //}
    }

    /**
     * 压测任务启动失败
     */
    @Transactional
    public void testFailed(TaskResult taskResult) {
        log.info("场景[{}]压测任务启动失败，失败原因:{}", taskResult.getSceneId(), taskResult.getMsg());
        Report report = TReportMapper.selectByPrimaryKey(taskResult.getTaskId());
        if (report != null && report.getStatus() == ReportConstans.INIT_STATUS) {
            SceneManage sceneManage = new SceneManage();
            sceneManage.setId(taskResult.getSceneId());
            sceneManage.setUpdateTime(new Date());
            sceneManage.setStatus(SceneManageStatusEnum.WAIT.getValue());
            TSceneManageMapper.updateByPrimaryKeySelective(sceneManage);

            //删除报表
            report.setGmtUpdate(new Date());
            report.setIsDeleted(1);
            report.setId(taskResult.getTaskId());
            JSONObject json = new JSONObject();
            json.put(ReportConstans.FEATURES_ERROR_MSG, taskResult.getMsg());
            report.setFeatures(json.toJSONString());
            TReportMapper.updateByPrimaryKeySelective(report);

            //释放流量
            settleService.unLockAccount(report.getCustomId(), taskResult.getTaskId().toString());
        }
    }

}
