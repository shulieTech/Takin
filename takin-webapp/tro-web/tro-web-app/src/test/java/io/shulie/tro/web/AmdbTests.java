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
//package io.shulie.tro.web;
//
//import java.util.List;
//
//import io.shulie.tro.web.amdb.api.ApplicationEntranceClient;
//import io.shulie.tro.web.amdb.api.NotifyClient;
//import io.shulie.tro.web.app.Application;
//import io.shulie.tro.web.app.controller.application.ApplicationEntranceController;
//import io.shulie.tro.web.data.dao.application.ApplicationDAO;
//import io.shulie.tro.web.data.param.application.ApplicationQueryParam;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(classes = Application.class)
//public class AmdbTests {
//
//    @Autowired
//    private ApplicationEntranceClient applicationEntranceClient;
//
//    @Autowired
//    private ApplicationDAO applicationDAO;
//
//    @Autowired
//    private NotifyClient notifyClient;
//
//    @Autowired
//    private ApplicationEntranceController applicationEntranceController;
//
//    @Test
//    public void test_01() {
//        System.out.println(applicationEntranceClient.getApplicationEntrances("wsdemo-http-210104", "HTTP"));
//    }
//
//    @Test
//    public void test_02(){
//    }
//
//    @Test
//    public void test_03() {
//        //System.out.println(
//        //    JSON.toJSON(applicationEntranceClient
//        //        .getApplicationEntrancesTopology("canace-local-test-1209-Virtual", "a90c4d478b98110a281dd2156b584148")
//        //    )
//        //);
//    }
//
//    @Test
//    public void test_05() {
//        List<String> allApplication = applicationDAO.getAllApplicationName(new ApplicationQueryParam());
//        System.out.println(allApplication);
//    }
//
//    @Test
//    public void test_04(){
//        //notifyClient.startApplicationEntrancesCalculate("canace-local-test-1209","");
//    }
//
//    @Test
//    public void test_06(){
//        //notifyClient.stopApplicationEntrancesCalculate();
//    }
//

//}
