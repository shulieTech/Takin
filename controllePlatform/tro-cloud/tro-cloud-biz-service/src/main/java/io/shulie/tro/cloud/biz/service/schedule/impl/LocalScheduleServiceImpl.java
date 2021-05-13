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

package io.shulie.tro.cloud.biz.service.schedule.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.entity.domain.entity.schedule.ScheduleRecord;
import com.pamirs.tro.entity.domain.vo.report.SceneTaskNotifyParam;
import com.pamirs.tro.entity.domain.vo.scenemanage.SceneManageStartRecordVO;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleInitParam;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleRunRequest;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleStartRequest;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleStopRequest;
import io.shulie.tro.cloud.biz.service.record.ScheduleRecordEnginePluginService;
import io.shulie.tro.cloud.biz.service.scene.SceneManageService;
import io.shulie.tro.cloud.biz.service.scene.SceneTaskService;
import io.shulie.tro.cloud.biz.service.schedule.ScheduleEventService;
import io.shulie.tro.cloud.biz.service.schedule.ScheduleService;
import io.shulie.tro.cloud.common.bean.scenemanage.UpdateStatusBean;
import io.shulie.tro.cloud.common.bean.task.TaskResult;
import io.shulie.tro.cloud.common.constants.PressureInstanceRedisKey;
import io.shulie.tro.cloud.common.constants.ScheduleConstants;
import io.shulie.tro.cloud.common.enums.scenemanage.SceneManageStatusEnum;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.cloud.common.utils.DesUtil;
import io.shulie.tro.cloud.common.utils.GsonUtil;
import io.shulie.tro.constants.TroRequestConstant;
import io.shulie.tro.eventcenter.Event;
import io.shulie.tro.eventcenter.annotation.IntrestFor;
import io.shulie.tro.k8s.service.MicroService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.biz.service.schedule.impl
 * @date 2021/5/6 4:00 下午
 */
@Slf4j
@Service
public class LocalScheduleServiceImpl implements ScheduleService {

    @Resource
    private com.pamirs.tro.entity.dao.schedule.TScheduleRecordMapper TScheduleRecordMapper;

    @Autowired
    private ScheduleEventService scheduleEvent;

    @Autowired
    private MicroService microService;

    @Autowired
    private SceneManageService sceneManageService;


    @Resource
    private ScheduleRecordEnginePluginService scheduleRecordEnginePluginService;


    @Value("${console.url}")
    private String console;

    @Value("${spring.redis.host}")
    private String engineRedisAddress;

    @Value("${spring.redis.port}")
    private String engineRedisPort;

    @Value("${spring.redis.password}")
    private String engineRedisPassword;

    @Autowired
    private RedisClientUtils redisClientUtils;


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private SceneTaskService sceneTaskService;

    /**
     * 调度任务路径
     */
    @Value("${pressure.engine.task.dir:/Users/hezhongqi/shulie/engine}")
    private String taskDir;

    /**
     * 调度任务路径
     */
    @Value("${pressure.engine.memSetting:-Xmx512m -Xms512m -Xss256K -XX:MaxMetaspaceSize=256m}")
    private String pressureEngineMemSetting;


    @Override
    @Transactional
    public void startSchedule(ScheduleStartRequest request) {
        log.info("启动调度, 请求数据：{}", request);
        //任务只处理一次
        ScheduleRecord schedule = TScheduleRecordMapper.getScheduleByTaskId(request.getTaskId());
        if (schedule != null) {
            log.error("调度任务[{}]已经启动", request.getTaskId());
            return;
        }
        //保存调度记录
        ScheduleRecord scheduleRecord = new ScheduleRecord();
        scheduleRecord.setPodNum(request.getTotalIp());
        scheduleRecord.setSceneId(request.getSceneId());
        scheduleRecord.setTaskId(request.getTaskId());
        scheduleRecord.setStatus(ScheduleConstants.SCHEDULE_STATUS_1);
        // 新增 客户id
        scheduleRecord.setCustomerId(request.getCustomerId());
        scheduleRecord.setPodClass(
            ScheduleConstants.getScheduleName(request.getSceneId(), request.getTaskId(), request.getCustomerId()));
        TScheduleRecordMapper.insertSelective(scheduleRecord);

        //add by lipeng 保存调度对应压测引擎插件记录信息
        scheduleRecordEnginePluginService.saveScheduleRecordEnginePlugins(
            scheduleRecord.getId(), request.getEnginePluginsFilePath());
        //add end

        //发布事件
        ScheduleRunRequest eventRequest = new ScheduleRunRequest();
        eventRequest.setScheduleId(scheduleRecord.getId());
        eventRequest.setRequest(request);
        //把数据放入缓存，初始化回调调度需要
        redisClientUtils.setString(ScheduleConstants.getScheduleName(request.getSceneId(), request.getTaskId(), request.getCustomerId()),
            JSON.toJSONString(eventRequest));

        // 总计 报告生成用到 调度期间出现错误，这份数据只存24小时
        redisClientUtils.set(ScheduleConstants.getPodTotal(request.getSceneId(), request.getTaskId(), request.getCustomerId()),
            request.getTotalIp(), 24 * 60 * 60 * 1000);
        //调度初始化
        scheduleEvent.initSchedule(eventRequest);
    }

    @Override
    public void stopSchedule(ScheduleStopRequest request) {
        log.info("停止调度, 请求数据：{}", request);
        ScheduleRecord scheduleRecord = TScheduleRecordMapper.getScheduleByTaskId(request.getTaskId());
        if (scheduleRecord != null) {
            // 增加中断
            String scheduleName = ScheduleConstants.getScheduleName(request.getSceneId(), request.getTaskId(),
                request.getCustomerId());
            redisClientUtils.set(ScheduleConstants.INTERRUPT_POD + scheduleName, true, 24 * 60 * 60 * 1000);
        }
    }

    @Override
    public void runSchedule(ScheduleRunRequest request) {
        // 场景生命周期更新 启动中(文件拆分完成) ---> 创建Job中
        sceneManageService.updateSceneLifeCycle(
            UpdateStatusBean.build(request.getRequest().getSceneId(),
                request.getRequest().getTaskId(),
                request.getRequest().getCustomerId()).checkEnum(
                SceneManageStatusEnum.STARTING, SceneManageStatusEnum.FILESPLIT_END)
                .updateEnum(SceneManageStatusEnum.JOB_CREATEING)
                .build());

        //创建容器需要的配置文件
        createEngineConfigMap(request);
        //通知配置文件建立成功
        notifyTaskResult(request);
        // 启动压测
        String msg = microService.createJob(ScheduleConstants.getConfigMapName(request.getRequest().getSceneId(), request.getRequest().getTaskId(),
            request.getRequest().getCustomerId()), PressureInstanceRedisKey.getEngineInstanceRedisKey(request.getRequest().getSceneId(),
                request.getRequest().getTaskId(), request.getRequest().getCustomerId()));
        if (StringUtils.isEmpty(msg)) {
            // 是空的
            log.info("场景{},任务{},顾客{}开始创建压测引擎Job，压测引擎job创建成功", request.getRequest().getSceneId(),
                request.getRequest().getTaskId(),
                request.getRequest().getCustomerId());
        } else {
            // 创建失败
            log.info("场景{},任务{},顾客{}开始创建压测引擎Job，压测引擎job创建失败", request.getRequest().getSceneId(),
                request.getRequest().getTaskId(),
                request.getRequest().getCustomerId());
            sceneManageService.reportRecord(SceneManageStartRecordVO.build(request.getRequest().getSceneId(),
                request.getRequest().getTaskId(),
                request.getRequest().getCustomerId()).success(false)
                .errorMsg(String.format("压测引擎job创建失败，失败原因：" + msg)).build());
        }
    }

    private void notifyTaskResult(ScheduleRunRequest request) {
        SceneTaskNotifyParam notify = new SceneTaskNotifyParam();
        notify.setSceneId(request.getRequest().getSceneId());
        notify.setTaskId(request.getRequest().getTaskId());
        notify.setCustomerId(request.getRequest().getCustomerId());
        notify.setCustomerId(request.getRequest().getCustomerId());
        notify.setStatus("started");
        sceneTaskService.taskResultNotify(notify);
    }

    @Override
    public void initScheduleCallback(ScheduleInitParam param) {
        // 场景生命周期更新：文件拆分中 ---->文件拆分完成
        sceneManageService.updateSceneLifeCycle(UpdateStatusBean.build(param.getSceneId(), param.getTaskId(),
            param.getCustomerId())
            .checkEnum(SceneManageStatusEnum.FILESPLIT_RUNNING).updateEnum(SceneManageStatusEnum.FILESPLIT_END)
            .build());
        log.info("场景{},任务{},文件拆分完成, 准备调度压测引擎", param.getSceneId(), param.getTaskId());

        //消息队列
        push(param);

        //删除文件分割pod
        microService.deleteJob(
            ScheduleConstants.getFileSplitScheduleName(param.getSceneId(), param.getTaskId(), param.getCustomerId()),
            PressureInstanceRedisKey.getEngineInstanceRedisKey(param.getSceneId(), param.getTaskId(), param.getCustomerId()));

        //通知调度run
        String dataKey = ScheduleConstants.getScheduleName(param.getSceneId(), param.getTaskId(),
            param.getCustomerId());
        String scheduleRunRequestData = redisClientUtils.getString(dataKey);
        if (StringUtils.isNotEmpty(scheduleRunRequestData)) {
            ScheduleRunRequest scheduleRunRequest = JSON.parseObject(scheduleRunRequestData, ScheduleRunRequest.class);
            //开始调度压测引擎
            runSchedule(scheduleRunRequest);
            //清空缓存数据
            redisClientUtils.delete(dataKey);
        }
    }

    /**
     * 创建引擎配置文件
     */
    public void createEngineConfigMap(ScheduleRunRequest request) {
        Map<String, Object> configMap = Maps.newHashMap();
        ScheduleStartRequest scheduleStartRequest = request.getRequest();
        configMap.put("name",ScheduleConstants.getConfigMapName(scheduleStartRequest.getSceneId(), scheduleStartRequest.getTaskId(),
            scheduleStartRequest.getCustomerId()));
        JSONObject param = new JSONObject();
        param.put("scriptPath", scheduleStartRequest.getScriptPath());
        param.put("extJarPath", "");
        param.put("isLocal", true);
        param.put("taskDir", taskDir);
        param.put("pressureMode", scheduleStartRequest.getPressureMode());
        param.put("continuedTime", scheduleStartRequest.getContinuedTime());
        if (scheduleStartRequest.getExpectThroughput() != null){
            param.put("expectThroughput", scheduleStartRequest.getExpectThroughput() / scheduleStartRequest.getTotalIp());
        }
        //将jar包放入引擎目录中，打包后会放入ext目录
        if (CollectionUtils.isNotEmpty(scheduleStartRequest.getDataFile())){
            List<String> jarFilePaths = scheduleStartRequest.getDataFile().stream().filter(o -> o.getName().endsWith(".jar"))
                .map(ScheduleStartRequest.DataFile::getPath).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(jarFilePaths)){
                jarFilePaths.forEach(scheduleStartRequest::addEnginePluginsFilePath);
            }
        }
        param.put("rampUp", scheduleStartRequest.getRampUp());
        param.put("steps", scheduleStartRequest.getSteps());
        // add start by lipeng 添加压测引擎插件文件夹目录 enginePluginFolderPath
        param.put("enginePluginsFilePath", scheduleStartRequest.getEnginePluginsFilePath());
        // add end
        JSONObject enginePressureParams = new JSONObject();
        enginePressureParams.put("engineRedisAddress",engineRedisAddress);
        enginePressureParams.put("engineRedisPort",engineRedisPort);
        String engineRedisPasswordDecrypt = null;
        try {
            engineRedisPasswordDecrypt = DesUtil.encrypt(engineRedisPassword, "DBMEETYOURMAKERSMANDKEY");
        } catch (Exception e) {
            log.error("redis密码加密失败",e);
        }
        BigDecimal podTpsNum = new BigDecimal(scheduleStartRequest.getTps()).divide(new BigDecimal(scheduleStartRequest.getTotalIp()), 0, BigDecimal.ROUND_UP);
        enginePressureParams.put("tpsTargetLevel",podTpsNum.longValue());
        enginePressureParams.put("engineRedisPassword",engineRedisPasswordDecrypt);
        enginePressureParams.put("enginePressureMode",scheduleStartRequest.getPressureType() == null ? "" : scheduleStartRequest.getPressureType().toString());
        if (scheduleStartRequest.getBusinessTpsData() != null){
            List<Map<String,String>> businessActivities = new ArrayList<>();
            scheduleStartRequest.getBusinessTpsData().forEach((k,v) ->{
                Map<String,String> businessActivity = new HashMap<>();
                businessActivity.put("elementTestName",k);
                businessActivity.put("throughputPercent",new BigDecimal(v).multiply(new BigDecimal(100))
                    .divide(new BigDecimal(scheduleStartRequest.getTps()),0, BigDecimal.ROUND_UP).toString());
                businessActivities.add(businessActivity);
            });
            enginePressureParams.put("businessActivities",businessActivities);
        }
        param.put("enginePressureParams",enginePressureParams);

        String engineInstanceRedisKey = PressureInstanceRedisKey.getEngineInstanceRedisKey(scheduleStartRequest.getSceneId(), scheduleStartRequest.getTaskId(),
            scheduleStartRequest.getCustomerId());
        redisTemplate.opsForHash().put(engineInstanceRedisKey,PressureInstanceRedisKey.SecondRedisKey.REDIS_TPS_ALL_LIMIT,scheduleStartRequest.getTps()+"");
        redisTemplate.opsForHash().put(engineInstanceRedisKey,PressureInstanceRedisKey.SecondRedisKey.REDIS_TPS_LIMIT,podTpsNum+"");
        redisTemplate.opsForHash().put(engineInstanceRedisKey,PressureInstanceRedisKey.SecondRedisKey.REDIS_TPS_POD_NUM,scheduleStartRequest.getTotalIp()+"");
        redisTemplate.expire(engineInstanceRedisKey,10, TimeUnit.DAYS);
        param.put(TroRequestConstant.CLUSTER_TEST_SCENE_HEADER_VALUE, scheduleStartRequest.getSceneId());
        param.put(TroRequestConstant.CLUSTER_TEST_TASK_HEADER_VALUE, scheduleStartRequest.getTaskId());
        //  客户id
        param.put(TroRequestConstant.CLUSTER_TEST_CUSTOMER_HEADER_VALUE, scheduleStartRequest.getCustomerId());

        param.put("consoleUrl",
            console + ScheduleConstants.getConsoleUrl(request.getRequest().getSceneId(),
                request.getRequest().getTaskId(),
                request.getRequest().getCustomerId()));
        param.put("troCloudCallbackUrl", console + "/api/engine/callback");
        // 解决 单个pod ,但文件处于需要切割分类状态的bug
        param.put("podCount", scheduleStartRequest.getTotalIp());
        param.put("fileSets", scheduleStartRequest.getDataFile());
        param.put("businessMap", GsonUtil.gsonToString(scheduleStartRequest.getBusinessData()));
        param.put("memSetting", pressureEngineMemSetting);
        configMap.put("engine.conf",param.toJSONString());
        microService.createConfigMap(configMap, PressureInstanceRedisKey.getEngineInstanceRedisKey(request.getRequest().getSceneId(),
            request.getRequest().getTaskId(), request.getRequest().getCustomerId()));
    }


    /**
     * 临时方案：
     * 拆分文件的索引都存入到redis队列, 避免控制台集群环境下索引获取不正确
     */
    private void push(ScheduleInitParam param) {
        //把数据放入队列
        String key = ScheduleConstants.getFileSplitQueue(param.getSceneId(), param.getTaskId(), param.getCustomerId());

        List<String> numList = Lists.newArrayList();
        for (int i = 1; i <= param.getTotal(); i++) {
            numList.add(i + "");
        }

        redisClientUtils.leftPushAll(key, numList);
    }

    /**
     * 压测结束，删除 pod job configMap
     */
    @IntrestFor(event = "finished")
    public void doDeleteJob(Event event) {
        log.info("通知deleteJob模块， 监听到完成事件.....");
        try {
            Object object = event.getExt();
            TaskResult taskResult = (TaskResult)object;
            // 删除 job pod
            String jobName = ScheduleConstants.getScheduleName(taskResult.getSceneId(), taskResult.getTaskId(),
                taskResult.getCustomerId());
            microService.deleteJob(jobName,PressureInstanceRedisKey.getEngineInstanceRedisKey(taskResult.getSceneId(), taskResult.getTaskId(),
                taskResult.getCustomerId()));
            // 删除 configMap
            String engineInstanceRedisKey = PressureInstanceRedisKey.getEngineInstanceRedisKey(taskResult.getSceneId(), taskResult.getTaskId(),
                taskResult.getCustomerId());
            String configMapName = ScheduleConstants.getConfigMapName(taskResult.getSceneId(), taskResult.getTaskId(), taskResult.getCustomerId());
            microService.deleteConfigMap(configMapName);
            redisTemplate.expire(engineInstanceRedisKey,10, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("【deleteJob】处理finished事件异常={}", e.getMessage(), e);
        }

    }

}
