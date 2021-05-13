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

package io.shulie.tro.cloud.biz.service.machine;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import com.pamirs.tro.entity.dao.machine.TMachineSpecMapper;
import com.pamirs.tro.entity.dao.machine.TMachineTaskLogMapper;
import com.pamirs.tro.entity.dao.machine.TMachineTaskMapper;
import com.pamirs.tro.entity.dao.machine.TPressureMachineMapper;
import com.pamirs.tro.entity.domain.entity.machine.MachineTask;
import com.pamirs.tro.entity.domain.entity.machine.MachineTaskExample;
import com.pamirs.tro.entity.domain.entity.machine.MachineTaskLog;
import com.pamirs.tro.entity.domain.entity.machine.MachineTaskLogExample;
import com.pamirs.tro.entity.domain.entity.machine.PressureMachine;
import com.pamirs.tro.entity.domain.entity.machine.PressureMachineExample;
import com.pamirs.tro.entity.domain.vo.cloudserver.CloudAccountVO;
import com.pamirs.tro.entity.domain.vo.cloudserver.CloudPlatformVO;
import com.pamirs.tro.entity.domain.vo.machine.MachineTaskVO;
import io.shulie.flpt.core.plugin.BaseCreateRequest;
import io.shulie.flpt.core.plugin.BaseDeleteRequest;
import io.shulie.flpt.core.plugin.BaseInitRequest;
import io.shulie.flpt.core.plugin.BaseQueryRequest;
import io.shulie.flpt.core.plugin.BaseStartRequest;
import io.shulie.flpt.core.plugin.CreateResponse;
import io.shulie.flpt.core.plugin.DeleteResponse;
import io.shulie.flpt.core.plugin.EsInstance;
import io.shulie.flpt.core.plugin.InitResponse;
import io.shulie.flpt.core.plugin.QueryResponse;
import io.shulie.flpt.core.plugin.StartResponse;
import io.shulie.tro.cloud.biz.service.cloudServer.CloudAccountService;
import io.shulie.tro.cloud.biz.service.cloudServer.CloudPlatformService;
import io.shulie.tro.cloud.biz.service.cloudServer.CloudPluginServerService;
import io.shulie.tro.cloud.common.enums.OpenTypeEnum;
import io.shulie.tro.cloud.common.enums.machine.MachineStatusEnum;
import io.shulie.tro.cloud.common.enums.machine.MachineTaskStatusEnum;
import io.shulie.tro.cloud.common.utils.DateUtil;
import io.shulie.tro.common.beans.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: fanxx
 * @Date: 2020/5/18 下午3:43
 * @Description:
 */
@Component
@Slf4j
public class MachineFlowService {

    public static String FALSE_CORE = "0";
    @Autowired
    TMachineTaskMapper TMachineTaskMapper;
    @Autowired
    TMachineTaskLogMapper TMachineTaskLogMapper;
    @Autowired
    TMachineSpecMapper TMachineSpecMapper;
    @Autowired
    TPressureMachineMapper TPressureMachineMapper;
    @Autowired
    CloudAccountService cloudAccountService;
    @Autowired
    CloudPluginServerService cloudPluginServerService;
    @Autowired
    CloudPlatformService cloudPlatformService;
    @Autowired
    MachineEventPublishService machineEventPublishService;
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public void openMachine(MachineTask machineTask) {
        cachedThreadPool.execute(() -> asyncOpen(machineTask));
    }

    @Transactional(rollbackFor = Exception.class)
    public void asyncOpen(MachineTask machineTask) {
        CloudAccountVO cloudAccountVO = (CloudAccountVO)cloudAccountService.queryById(machineTask.getAccountId())
            .getData();
        CloudPlatformVO cloudPlatformVO = (CloudPlatformVO)cloudPlatformService.queryById(machineTask.getPlatformId())
            .getData();
        String authorizeParam = cloudAccountVO.getAuthorizeParam();
        HashMap<String, Object> extendsMap = (HashMap<String, Object>)JSON.parseObject(authorizeParam, HashMap.class);
        if (!StringUtils.isBlank(machineTask.getExtendConfig())) {
            MachineUtil.parseXMLToMap(machineTask.getExtendConfig(), extendsMap);
        }
        BaseCreateRequest baseCreateRequest = new BaseCreateRequest();
        baseCreateRequest.setAmount(machineTask.getMachineNum());
        baseCreateRequest.setOpenType(machineTask.getOpenType());
        baseCreateRequest.setExtendsMap(extendsMap);
        baseCreateRequest.setSpec(machineTask.getSpec());
        baseCreateRequest.setOpenTime(String.valueOf(machineTask.getOpenTime()));
        //调用SDK，开通机器
        ResponseResult createInstanceResponse = cloudPluginServerService.createInstance(cloudPlatformVO.getName(),
            baseCreateRequest);
        CreateResponse createResponse = (CreateResponse)createInstanceResponse.getData();
        MachineTaskExample machineTaskExample = new MachineTaskExample();
        MachineTaskExample.Criteria criteria = machineTaskExample.createCriteria();
        criteria.andSerialNoEqualTo(machineTask.getSerialNo());
        List<MachineTask> machineTasks = TMachineTaskMapper.selectByExample(machineTaskExample);
        Long taskId = machineTasks.get(0).getId();
        MachineTaskExample taskExample = new MachineTaskExample();
        MachineTaskExample.Criteria statusCriteria = taskExample.createCriteria();
        statusCriteria.andSerialNoEqualTo(machineTask.getSerialNo());
        if (!createResponse.isSuccess()) {
            //开通失败，更新开通任务状态
            machineTask.setStatus(MachineTaskStatusEnum.open_failed.getCode());
            TMachineTaskMapper.updateByExampleSelective(machineTask, taskExample);

            MachineTaskLog machineTaskLog = new MachineTaskLog();
            machineTaskLog.setTaskId(taskId);
            machineTaskLog.setSerialNo(machineTask.getSerialNo());
            machineTaskLog.setStatus(MachineStatusEnum.open_failed.getCode().byteValue());
            machineTaskLog.setLog(createResponse.getMessage());
            TMachineTaskLogMapper.insertSelective(machineTaskLog);
        } else {
            //开通成功，更新开通任务状态
            machineTask.setStatus(MachineTaskStatusEnum.open_success.getCode());
            TMachineTaskMapper.updateByExampleSelective(machineTask, taskExample);

            BaseQueryRequest baseQueryRequest = new BaseQueryRequest();
            List<String> instanceIds = createResponse.getInstanceIds();
            baseQueryRequest.setInstanceIds(instanceIds);
            baseQueryRequest.setExtendsMap(extendsMap);
            //调用SDK，查询机器信息
            ResponseResult queryInstanceResponse = cloudPluginServerService.queryInstance(cloudPlatformVO.getName(),
                baseQueryRequest);
            QueryResponse queryResponse = (QueryResponse)queryInstanceResponse.getData();
            List<EsInstance> esInstances = queryResponse.getInstanceInfos();
            ResponseResult mapResponse = cloudPluginServerService.queryInstanceMap(cloudPlatformVO.getName());
            Map<String, String> specMap = (Map)mapResponse.getData();
            for (EsInstance esInstance : esInstances) {
                PressureMachine pressureMachine = new PressureMachine();
                pressureMachine.setTaskId(taskId);
                pressureMachine.setAccountId(cloudAccountVO.getId());
                pressureMachine.setAccountName(cloudAccountVO.getAccount());
                pressureMachine.setInstanceId(esInstance.getInstanceId());
                pressureMachine.setInstanceName(esInstance.getInstanceName());
                pressureMachine.setPlatformId(cloudPlatformVO.getId());
                pressureMachine.setPlatformName(cloudPlatformVO.getName());
                pressureMachine.setOpenTime(machineTask.getOpenTime());
                pressureMachine.setOpenType(machineTask.getOpenType());
                pressureMachine.setPublicIp(esInstance.getPublicIp());
                pressureMachine.setPrivateIp(esInstance.getInnerIp());
                if (specMap.containsKey(esInstance.getSpec())) {
                    pressureMachine.setSpec(specMap.get(esInstance.getSpec()));
                    pressureMachine.setRefSpec(esInstance.getSpec());
                } else {
                    pressureMachine.setSpec(esInstance.getSpec());
                }
                pressureMachine.setRegionId(esInstance.getRegionId());
                pressureMachine.setRegionName(esInstance.getRegionName());
                pressureMachine.setStatus(MachineStatusEnum.open_success.getCode());
                if (OpenTypeEnum.fix.getCode().equals(machineTask.getOpenType())) {
                    //单位：月
                    int openTime = machineTask.getOpenTime();
                    Date expireDate = DateUtil.addMonth(new Date(), openTime);
                    pressureMachine.setExpireDate(expireDate);
                } else if (OpenTypeEnum.temporary.getCode().equals(machineTask.getOpenType())) {
                    //单位：小时
                    int openTime = machineTask.getOpenTime();
                    Date expireDate = DateUtil.addHour(new Date(), openTime);
                    pressureMachine.setExpireDate(expireDate);
                }
                TPressureMachineMapper.insertSelective(pressureMachine);

                MachineTaskLog machineTaskLog = new MachineTaskLog();
                machineTaskLog.setTaskId(taskId);
                machineTaskLog.setSerialNo(machineTask.getSerialNo());
                machineTaskLog.setIp(esInstance.getPublicIp());
                machineTaskLog.setHostname(esInstance.getInstanceName());
                machineTaskLog.setStatus(MachineStatusEnum.open_success.getCode().byteValue());
                machineTaskLog.setLog("开通成功");
                TMachineTaskLogMapper.insertSelective(machineTaskLog);
            }
            //发送启动机器事件
            machineTask.setId(taskId);
            machineTask.setInstanceIds(instanceIds);
            machineEventPublishService.start(machineTask);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void startMachine(MachineTask machineTask) {
        Long taskId = machineTask.getId();
        String serialNo = machineTask.getSerialNo();
        CloudAccountVO cloudAccountVO = (CloudAccountVO)cloudAccountService.queryById(machineTask.getAccountId())
            .getData();
        CloudPlatformVO cloudPlatformVO = (CloudPlatformVO)cloudPlatformService.queryById(machineTask.getPlatformId())
            .getData();
        String authorizeParam = cloudAccountVO.getAuthorizeParam();
        HashMap<String, Object> extendsMap = (HashMap<String, Object>)JSON.parseObject(authorizeParam, HashMap.class);
        if (!StringUtils.isBlank(machineTask.getExtendConfig())) {
            MachineUtil.parseXMLToMap(machineTask.getExtendConfig(), extendsMap);
        }
        List<String> instanceIds = machineTask.getInstanceIds();
        List<PressureMachine> pressureMachines = queryPressureMachineByInstanceIds(instanceIds);
        if (CollectionUtils.isEmpty(pressureMachines)) {
            log.error("压力机不存在: " + instanceIds.toString());
            return;
        }

        Map<String, PressureMachine> pressureMachineMap = getPressureMachineMap(pressureMachines);
        for (String instanceId : instanceIds) {
            if (!pressureMachineMap.containsKey(instanceId)) {
                log.error("该压测机不存在: " + instanceId);
                continue;
            }
            String ip = pressureMachineMap.get(instanceId).getPublicIp();
            updateTaskLog(taskId, serialNo, ip, MachineStatusEnum.do_start, "启动中 ...");
            updatePressureMachine(instanceId, MachineStatusEnum.do_start);
        }

        BaseStartRequest baseStartRequest = new BaseStartRequest();
        baseStartRequest.setInstanceIds(machineTask.getInstanceIds());
        baseStartRequest.setExtendsMap(extendsMap);
        //调用SDK，启动机器
        ResponseResult startInstanceResponse = cloudPluginServerService.startInstance(cloudPlatformVO.getName(),
            baseStartRequest);
        List<StartResponse> startResponseList = (List)startInstanceResponse.getData();
        for (StartResponse startResponse : startResponseList) {
            String instanceId = startResponse.getInstanceId();
            if (!pressureMachineMap.containsKey(instanceId)) {
                log.error("该压测机不存在: " + instanceId);
                continue;
            }
            String ip = pressureMachineMap.get(instanceId).getPublicIp();
            if (startResponse.isSuccess()) {
                updateTaskLog(taskId, serialNo, ip, MachineStatusEnum.start_success, "启动成功");
                updatePressureMachine(instanceId, MachineStatusEnum.start_success);
                //发送机器初始化事件
                MachineTask initMachineTask = new MachineTask();
                BeanUtils.copyProperties(machineTask, initMachineTask);
                initMachineTask.setInstanceIds(Arrays.asList(startResponse.getInstanceId()));
                machineEventPublishService.init(initMachineTask);
            } else {
                updateTaskLog(taskId, serialNo, ip, MachineStatusEnum.start_failed, startResponse.getMessage());
                updatePressureMachine(instanceId, MachineStatusEnum.start_failed);
            }
        }
    }

    public void initMachine(MachineTask machineTask) {
        cachedThreadPool.execute(() -> asyncInit(machineTask));
    }

    @Transactional(rollbackFor = Exception.class)
    public void asyncInit(MachineTask machineTask) {
        Long taskId = machineTask.getId();
        String serialNo = machineTask.getSerialNo();
        CloudAccountVO cloudAccountVO = (CloudAccountVO)cloudAccountService.queryById(machineTask.getAccountId())
            .getData();
        CloudPlatformVO cloudPlatformVO = (CloudPlatformVO)cloudPlatformService.queryById(machineTask.getPlatformId())
            .getData();
        String authorizeParam = cloudAccountVO.getAuthorizeParam();
        HashMap<String, Object> extendsMap = (HashMap<String, Object>)JSON.parseObject(authorizeParam, HashMap.class);
        if (!StringUtils.isBlank(machineTask.getExtendConfig())) {
            MachineUtil.parseXMLToMap(machineTask.getExtendConfig(), extendsMap);
        }
        List<String> instanceIds = machineTask.getInstanceIds();
        List<PressureMachine> pressureMachines = queryPressureMachineByInstanceIds(instanceIds);
        if (CollectionUtils.isEmpty(pressureMachines)) {
            log.error("压力机不存在: " + instanceIds.toString());
            return;
        }

        Map<String, PressureMachine> pressureMachineMap = getPressureMachineMap(pressureMachines);
        for (String instanceId : instanceIds) {
            if (!pressureMachineMap.containsKey(instanceId)) {
                log.error("该压测机不存在: " + instanceId);
                continue;
            }
            String ip = pressureMachineMap.get(instanceId).getPublicIp();
            updateTaskLog(taskId, serialNo, ip, MachineStatusEnum.do_init, "初始化中 ...");
            updatePressureMachine(instanceId, MachineStatusEnum.do_init);
        }

        BaseInitRequest baseInitRequest = new BaseInitRequest();
        baseInitRequest.setInstanceIds(instanceIds);
        baseInitRequest.setExtendsMap(extendsMap);
        //调用SDK，初始化机器环境
        ResponseResult initInstanceResponse = cloudPluginServerService.initialize(cloudPlatformVO.getName(),
            baseInitRequest);
        List<InitResponse> initResponseList = (List)initInstanceResponse.getData();
        for (InitResponse initResponse : initResponseList) {
            String instanceId = initResponse.getInstanceId();
            if (!pressureMachineMap.containsKey(instanceId)) {
                log.error("该压测机不存在: " + instanceId);
                continue;
            }
            String ip = pressureMachineMap.get(instanceId).getPublicIp();
            if (initResponse.isSuccess()) {
                updateTaskLog(taskId, serialNo, ip, MachineStatusEnum.do_run, "初始化成功");
                updatePressureMachine(instanceId, MachineStatusEnum.do_run);
            } else {
                updateTaskLog(taskId, serialNo, ip, MachineStatusEnum.init_failed, initResponse.getMessage());
                updatePressureMachine(instanceId, MachineStatusEnum.init_failed);
            }
        }
    }

    public void destoryMachine(MachineTaskVO machineTaskVO) {
        cachedThreadPool.execute(() -> asyncDestory(machineTaskVO));
    }

    @Transactional(rollbackFor = Exception.class)
    public void asyncDestory(MachineTaskVO machineTaskVO) {
        String serialNo = machineTaskVO.getSerialNo();
        MachineTask machineTask = queryMachineTaskBySerialNo(serialNo);
        if (null == machineTask) {
            return;
        }
        Long taskId = machineTask.getId();
        List<PressureMachine> pressureMachines = queryPressureMachineByIds(machineTaskVO.getMachineIdList());
        if (CollectionUtils.isEmpty(pressureMachines)) {
            log.error("压力机不存在: " + machineTaskVO.getMachineIdList());
            return;
        }
        CloudAccountVO cloudAccountVO = (CloudAccountVO)cloudAccountService.queryById(machineTaskVO.getAccountId())
            .getData();
        CloudPlatformVO cloudPlatformVO = (CloudPlatformVO)cloudPlatformService.queryById(machineTaskVO.getPlatformId())
            .getData();
        String authorizeParam = cloudAccountVO.getAuthorizeParam();
        HashMap<String, Object> extendsMap = (HashMap<String, Object>)JSON.parseObject(authorizeParam, HashMap.class);

        List<String> instanceIds = pressureMachines.stream().map(PressureMachine::getInstanceId).collect(
            Collectors.toList());
        Map<String, PressureMachine> pressureMachineMap = getPressureMachineMap(pressureMachines);
        for (String instanceId : instanceIds) {
            if (!pressureMachineMap.containsKey(instanceId)) {
                log.error("该压测机不存在: " + instanceId);
                continue;
            }
            String ip = pressureMachineMap.get(instanceId).getPublicIp();
            updateTaskLog(taskId, serialNo, ip, MachineStatusEnum.do_destory, "销毁中 ...");
            updatePressureMachine(instanceId, MachineStatusEnum.do_destory);
        }
        BaseDeleteRequest baseDeleteRequest = new BaseDeleteRequest();
        baseDeleteRequest.setInstanceIds(instanceIds);
        baseDeleteRequest.setExtendsMap(extendsMap);
        //调用SDK，销毁机器
        ResponseResult deleteInstanceResponse = cloudPluginServerService.deleteInstance(cloudPlatformVO.getName(),
            baseDeleteRequest);
        List<DeleteResponse> deleteResponseList = (List)deleteInstanceResponse.getData();
        for (DeleteResponse deleteResponse : deleteResponseList) {
            String instanceId = deleteResponse.getInstanceId();
            if (deleteResponse.isSuccess()) {
                //更新压测机状态
                updatePressureMachine(instanceId, MachineStatusEnum.destoryed);

                //更新销毁任务状态
                MachineTask oldMachineTask = new MachineTask();
                MachineTaskExample machineTaskExample = new MachineTaskExample();
                MachineTaskExample.Criteria criteria1 = machineTaskExample.createCriteria();
                criteria1.andSerialNoEqualTo(machineTaskVO.getSerialNo());
                oldMachineTask.setStatus(MachineTaskStatusEnum.destory_success.getCode());
                TMachineTaskMapper.updateByExampleSelective(oldMachineTask, machineTaskExample);

                //更新销毁日志
                MachineTaskLog machineTaskLog = new MachineTaskLog();
                machineTaskLog.setTaskId(taskId);
                machineTaskLog.setSerialNo(serialNo);
                PressureMachine pressureMachine = pressureMachineMap.get(instanceId);
                if (pressureMachine != null) {
                    machineTaskLog.setIp(pressureMachine.getPublicIp());
                    machineTaskLog.setHostname(pressureMachine.getInstanceName());
                }
                machineTaskLog.setStatus(MachineStatusEnum.destoryed.getCode().byteValue());
                machineTaskLog.setLog("销毁成功");
                TMachineTaskLogMapper.insertSelective(machineTaskLog);
            } else {
                //更新压测机状态
                updatePressureMachine(instanceId, MachineStatusEnum.destory_failed);

                //更新销毁任务状态
                MachineTask oldMachineTask = new MachineTask();
                MachineTaskExample machineTaskExample = new MachineTaskExample();
                MachineTaskExample.Criteria criteria1 = machineTaskExample.createCriteria();
                criteria1.andSerialNoEqualTo(machineTaskVO.getSerialNo());
                oldMachineTask.setStatus(MachineTaskStatusEnum.destory_failed.getCode());
                TMachineTaskMapper.updateByExampleSelective(oldMachineTask, machineTaskExample);

                //更新销毁日志
                MachineTaskLog machineTaskLog = new MachineTaskLog();
                machineTaskLog.setTaskId(taskId);
                machineTaskLog.setSerialNo(serialNo);
                PressureMachine pressureMachine = pressureMachineMap.get(instanceId);
                if (pressureMachine != null) {
                    machineTaskLog.setIp(pressureMachine.getPublicIp());
                    machineTaskLog.setHostname(pressureMachine.getInstanceName());
                }
                machineTaskLog.setStatus(MachineStatusEnum.destory_failed.getCode().byteValue());
                machineTaskLog.setLog("销毁失败");
                TMachineTaskLogMapper.insertSelective(machineTaskLog);
            }
        }
    }

    private void updateTaskLog(Long taskId, String serialNo, String ip, MachineStatusEnum machineStatusEnum,
        String msg) {
        MachineTaskLog machineTaskLog = new MachineTaskLog();
        machineTaskLog.setTaskId(taskId);
        MachineTaskLogExample machineTaskLogExample = new MachineTaskLogExample();
        MachineTaskLogExample.Criteria taskLogExampleCriteria = machineTaskLogExample.createCriteria();
        taskLogExampleCriteria.andSerialNoEqualTo(serialNo);
        taskLogExampleCriteria.andIpEqualTo(ip);
        taskLogExampleCriteria.andIsDeleteEqualTo(false);
        List<MachineTaskLog> machineTaskLogs = TMachineTaskLogMapper.selectByExampleWithBLOBs(machineTaskLogExample);
        if (CollectionUtils.isNotEmpty(machineTaskLogs)) {
            MachineTaskLog oldTaskLog = machineTaskLogs.get(0);
            machineTaskLog.setStatus(machineStatusEnum.getCode().byteValue());
            if (StringUtils.isBlank(oldTaskLog.getLog())) {
                machineTaskLog.setLog(msg);
            } else {
                StringBuilder oldMsg = new StringBuilder(oldTaskLog.getLog());
                oldMsg.append("\n\n" + "--------------------------------------------" + "\n\n");
                oldMsg.append(msg);
                machineTaskLog.setLog(oldMsg.toString());
            }
            TMachineTaskLogMapper.updateByExampleSelective(machineTaskLog, machineTaskLogExample);
        }
    }

    private void updatePressureMachine(String instanceId, MachineStatusEnum machineStatusEnum) {
        PressureMachine pressureMachine = new PressureMachine();
        PressureMachineExample pressureMachineExample = new PressureMachineExample();
        PressureMachineExample.Criteria pressureMachineExampleCriteria = pressureMachineExample.createCriteria();
        pressureMachineExampleCriteria.andInstanceIdEqualTo(instanceId);
        pressureMachineExampleCriteria.andIsDeleteEqualTo(false);
        pressureMachine.setStatus(machineStatusEnum.getCode());
        TPressureMachineMapper.updateByExampleSelective(pressureMachine, pressureMachineExample);
    }

    private PressureMachine queryPressureMachineByInstanceId(String instanceId) {
        PressureMachineExample pressureMachineExample = new PressureMachineExample();
        PressureMachineExample.Criteria pressureMachineExampleCriteria = pressureMachineExample.createCriteria();
        pressureMachineExampleCriteria.andInstanceIdEqualTo(instanceId);
        pressureMachineExampleCriteria.andIsDeleteEqualTo(false);
        List<PressureMachine> pressureMachineList = TPressureMachineMapper.selectByExample(pressureMachineExample);
        PressureMachine pressureMachine = null;
        if (CollectionUtils.isNotEmpty(pressureMachineList)) {
            pressureMachine = pressureMachineList.get(0);
            return pressureMachine;
        }
        return pressureMachine;
    }

    private List<PressureMachine> queryPressureMachineByInstanceIds(List<String> instanceIds) {
        PressureMachineExample pressureMachineExample = new PressureMachineExample();
        PressureMachineExample.Criteria pressureMachineExampleCriteria = pressureMachineExample.createCriteria();
        pressureMachineExampleCriteria.andInstanceIdIn(instanceIds);
        pressureMachineExampleCriteria.andIsDeleteEqualTo(false);
        List<PressureMachine> pressureMachineList = TPressureMachineMapper.selectByExample(pressureMachineExample);
        return pressureMachineList;
    }

    private List<PressureMachine> queryPressureMachineByIds(List<Long> ids) {
        PressureMachineExample pressureMachineExample = new PressureMachineExample();
        PressureMachineExample.Criteria pressureMachineExampleCriteria = pressureMachineExample.createCriteria();
        pressureMachineExampleCriteria.andIdIn(ids);
        pressureMachineExampleCriteria.andIsDeleteEqualTo(false);
        List<PressureMachine> pressureMachineList = TPressureMachineMapper.selectByExample(pressureMachineExample);
        return pressureMachineList;
    }

    private Map<String, PressureMachine> getPressureMachineMap(List<PressureMachine> pressureMachines) {
        Map<String, PressureMachine> pressureMachineMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(pressureMachines)) {
            for (PressureMachine p : pressureMachines) {
                pressureMachineMap.put(p.getInstanceId(), p);
            }
        }
        return pressureMachineMap;
    }

    private MachineTask queryMachineTaskBySerialNo(String serialNo) {
        MachineTaskExample machineTaskExample = new MachineTaskExample();
        MachineTaskExample.Criteria criteria = machineTaskExample.createCriteria();
        criteria.andSerialNoEqualTo(serialNo);
        List<MachineTask> machineTasks = TMachineTaskMapper.selectByExample(machineTaskExample);
        MachineTask machineTask = null;
        if (CollectionUtils.isNotEmpty(machineTasks)) {
            machineTask = machineTasks.get(0);
        }
        return machineTask;
    }
}
