///*
// * Copyright 2021 Shulie Technology, Co.Ltd
// * Email: shulie@shulie.io
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package io.shulie.tro.cloud.biz.service.machine;
//
//import com.alibaba.fastjson.JSON;
//
//import com.pamirs.tro.entity.domain.entity.machine.MachineTask;
//import com.pamirs.tro.entity.domain.vo.machine.MachineTaskVO;
//import io.shulie.tro.eventcenter.Event;
//import io.shulie.tro.eventcenter.annotation.IntrestFor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @Author: fanxx
// * @Date: 2020/5/18 下午2:46
// * @Description:
// */
//@Component
//public class MachineEventSubscribeService {
//    private static final Logger logger = LoggerFactory.getLogger(MachineEventSubscribeService.class);
//
//    @Autowired
//    MachineFlowService machineFlowService;
//
//    @IntrestFor(event = "machineOpen")
//    public void doOpenEvent(Event event) {
//        logger.info("MachineEventSubscribeService event received: machineOpen,{}", JSON.toJSONString(event));
//        Object object = event.getExt();
//        MachineTask machineTask = (MachineTask)object;
//        machineFlowService.openMachine(machineTask);
//    }
//
//    @IntrestFor(event = "machineStart")
//    public void doStartEvent(Event event) {
//        logger.info("MachineEventSubscribeService event received: machineStart,{}", JSON.toJSONString(event));
//        Object object = event.getExt();
//        MachineTask machineTask = (MachineTask)object;
//        machineFlowService.startMachine(machineTask);
//    }
//
//    @IntrestFor(event = "machineInit")
//    public void doInitEvent(Event event) {
//        logger.info("MachineEventSubscribeService event received: machineInit,{}", JSON.toJSONString(event));
//        Object object = event.getExt();
//        MachineTask machineTask = (MachineTask)object;
//        machineFlowService.initMachine(machineTask);
//    }
//
//    @IntrestFor(event = "machineDestroy")
//    public void doDestroyEvent(Event event) {
//        logger.info("MachineEventSubscribeService event received: machineDestroy,{}", JSON.toJSONString(event));
//        Object object = event.getExt();
//        MachineTaskVO machineTaskVO = (MachineTaskVO)object;
//        machineFlowService.destoryMachine(machineTaskVO);
//    }
//}
