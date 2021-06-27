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
//package io.shulie.tro.web.amdb.api.impl;
//
//import java.util.List;
//
//import io.shulie.amdb.common.dto.link.entrance.ServiceInfoDTO;
//import io.shulie.tro.web.amdb.api.ApplicationClient;
//import io.shulie.tro.web.amdb.api.ApplicationEntranceClient;
//import io.shulie.tro.web.amdb.bean.query.application.ApplicationNodeQueryDTO;
//import io.shulie.tro.web.amdb.bean.query.application.ApplicationQueryDTO;
//import io.shulie.tro.web.app.Application;
//import junit.framework.TestCase;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * @Author: fanxx
// * @Date: 2020/10/19 10:07 上午
// * @Description:
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(classes = Application.class)
//public class ApplicationClientImplTest extends TestCase {
//
//    @Autowired
//    ApplicationClient applicationClient;
//
//    @Autowired
//    private ApplicationEntranceClient applicationEntranceClient;
//
//    @Test
//    public void testPageApplications() {
//        ApplicationQueryDTO queryDTO = new ApplicationQueryDTO();
//        queryDTO.setAppNames("1");
//        queryDTO.setCurrentPage(0);
//        queryDTO.setPageSize(1);
//        System.out.println(applicationClient.pageApplications(queryDTO));
//    }
//
//    @Test
//    public void testPageApplicationNodes() {
//        ApplicationNodeQueryDTO queryDTO = new ApplicationNodeQueryDTO();
//        queryDTO.setAppNames("demo-fire-app");
//        queryDTO.setCurrentPage(0);
//        queryDTO.setPageSize(1);
//        applicationClient.pageApplicationNodes(queryDTO);
//    }
//
//    @Test
//    public void testMqs() {
//        List<ServiceInfoDTO> mqTopicGroups = applicationEntranceClient.getMqTopicGroups("spring-kafka-24-consumer");
//        System.out.println();
//    }
//}
