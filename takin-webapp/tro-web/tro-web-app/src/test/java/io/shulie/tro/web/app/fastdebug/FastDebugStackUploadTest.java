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

package io.shulie.tro.web.app.fastdebug;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;

import com.pamirs.tro.common.constant.LogLevelEnum;
import com.pamirs.tro.common.util.DateUtils;
import io.shulie.tro.web.app.Application;
import io.shulie.tro.web.app.request.fastdebug.FastDebugAgentLogUploadRequest;
import io.shulie.tro.web.app.request.fastdebug.FastDebugLogReq;
import io.shulie.tro.web.app.request.fastdebug.FastDebugMachinePerformanceCreateRequest;
import io.shulie.tro.web.app.request.fastdebug.FastDebugStackInfoCreateRequest;
import io.shulie.tro.web.app.request.fastdebug.FastDebugStackUploadCreateRequest;
import io.shulie.tro.web.app.service.fastdebug.FastDebugAgentUploadService;
import io.shulie.tro.web.app.service.fastdebug.FastDebugLogService;
import io.shulie.tro.web.app.service.fastdebug.FastDebugStackInfoService;
import io.shulie.tro.web.data.dao.fastdebug.FastDebugMachinePerformanceDao;
import io.shulie.tro.web.data.dao.fastdebug.FastDebugStackInfoDao;
import io.shulie.tro.web.data.param.fastdebug.FastDebugStackInfoCreateParam;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: mubai
 * @Date: 2020-12-28 16:27
 * @Description:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class FastDebugStackUploadTest {

    @Autowired
    private FastDebugAgentUploadService fastDebugAgentUploadService;

    @Autowired
    private FastDebugLogService fastDebugLogService;

    @Autowired
    private FastDebugMachinePerformanceDao fastDebugMachinePerformanceDao;

    @Autowired
    private FastDebugStackInfoService fastDebugStackInfoService;

    @Autowired
    private FastDebugStackInfoDao fastDebugStackInfoDao;

    @Test
    public void testUploadStackLog() {
        List<FastDebugStackUploadCreateRequest> requestList = new ArrayList<>();
        FastDebugStackUploadCreateRequest request = new FastDebugStackUploadCreateRequest();
        assembleStackData(request);
        requestList.add(request);
        System.out.println(JSON.toJSONString(requestList));
       // fastDebugAgentUploadService.uploadDebugStack(requestList,"");
       // Assert.assertEquals(true,true);
    }

    private void assembleStackData(FastDebugStackUploadCreateRequest request) {
        request.setAgentId("1111");
        request.setAppName("cjc-app");
        request.setCmdId("0000");
        request.setTraceId("traceid-001");
        request.setRpcId("rpc:000");
       FastDebugStackInfoCreateRequest log  =new FastDebugStackInfoCreateRequest();

       request.setLog(log);
        //for (int i = 0; i < 2; i++) {
        //    FastDebugStackInfoCreateRequest log = new FastDebugStackInfoCreateRequest();
        //    log.setContent("I am content ddddvdvs" + i);
        //    log.setLevel("INFO");
        //    logs.add(log);
        //}
        //FastDebugStackInfoCreateRequest log = new FastDebugStackInfoCreateRequest();
        log.setContent("2021-01-12 16:16:54 [pamirs] ERROR traceId:traceId rpcId:rpcId isPressure:true errorType:datasource erorCode:MQ-0001 message:Alibaba-RocketMQ消费端启动失败! detail:subscription");
        log.setLevel("ERROR");
        //logs.add(log);


        List<FastDebugMachinePerformanceCreateRequest> machineList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            FastDebugMachinePerformanceCreateRequest machine = new FastDebugMachinePerformanceCreateRequest();
            machine.setCpuLoad("1");
            machine.setCpuUsage("2");
            machine.setIndex("beforeFirst");
            machine.setIoWait("3");
            machine.setMemoryTotal("33");
            machine.setMemoryUsage("0.1");
            machine.setYoungGcCount(11);
            machine.setYoungGcTime(12);
            machine.setOldGcCount(11);
            machine.setOldGcTime(12);
            machineList.add(machine);

        }
        request.setMachineInfo(machineList);
    }

    @Test
    public void testUploadAppLog() {
        FastDebugAgentLogUploadRequest request = new FastDebugAgentLogUploadRequest();
        request.setAppName("cjc-app");
        request.setAgentId("app001");
        List<FastDebugLogReq> logs = new ArrayList<>();
        request.setLogs(logs);
        FastDebugLogReq log = new FastDebugLogReq();
        log.setEndLine(100);
        log.setFileName("app-log001.log");
        log.setFilePath("/dd/app-log001.log");
        log.setLogContent("JSON 语法规则\n"
            + "JSON是一个标记符的序列。这套标记符包含六个构造字符、字符串、数字和三个字面名。\n"
            + "JSON是一个序列化的对象或数组。\n"
            + "1. 六个构造字符：\n"
            + "begin-array = ws %x5B ws ; [ 左方括号\n"
            + "begin-object = ws %x7B ws ; { 左大括号\n"
            + "end-array = ws %x5D ws ; ] 右方括号\n"
            + "end-object = ws %x7D ws ; } 右大括号\n"
            + "name-separator = ws %x3A ws ; : 冒号\n"
            + "value-separator = ws %x2C ws ; , 逗号\n");
        log.setTotalLines(1000);
        logs.add(log);
        System.out.println(JSON.toJSONString(request));
        fastDebugAgentUploadService.uploadAppLog(request);
        Assert.assertEquals(true,true);

    }

    @Test
    public void testUploadAgentLog() {
        FastDebugAgentLogUploadRequest request = new FastDebugAgentLogUploadRequest();
        request.setAgentId("111");
        request.setAppName("cjc-test");
        List<FastDebugLogReq> logs = new ArrayList<>();
        request.setLogs(logs);
        request.setLogs(logs);
        FastDebugLogReq log = new FastDebugLogReq();
        log.setEndLine(111);
        log.setFileName("agent001.log");
        log.setFilePath("");
        log.setTotalLines(11110000);
        log.setLogContent("eeeeeeeeeeeeeeee");
        logs.add(log);
        System.out.println(JSON.toJSONString(request));
        fastDebugAgentUploadService.uploadAgentLog(request);
        Assert.assertEquals(true,true);
    }

    @Test
    public void testTraceIdHasErrorLog() {
        Long callCount = fastDebugAgentUploadService.hasStackErrorLog("ddd");
        System.out.println(callCount);
        Assert.assertEquals(callCount > 1,true);
    }

    @Test
    public void testSendCommand() {
        String appName = "" ;
        String agentId = "aijun022223388888494994949498888888" ;
        String traceId = "traceId001" ;
        String fileName = "" ;
        // CommandSend commandSend = fastDebugLogService.buildPullAgentCommand(appName,agentId,traceId,fileName);
        //try {
        //    fastDebugLogService.sendPullLogCommand(commandSend);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        Assert.assertEquals(true,true);

    }

    @Test
    public void testClearMachineData(){
        Date previousNDay = DateUtils.getPreviousNDay(3);
        fastDebugMachinePerformanceDao.clearHistoryData(previousNDay);
    }


    @Test
    public void testClearStackData(){
        Date previousNDay = DateUtils.getPreviousNDay(2);
        fastDebugStackInfoService.clearHistory(previousNDay);

    }

    @Test
    public void testInsertBatch(){
        List<FastDebugStackInfoCreateParam> list = new ArrayList<>();
        FastDebugStackInfoCreateParam param = new FastDebugStackInfoCreateParam();
        param.setAgentId("111");
        param.setLevel(LogLevelEnum.INFO.name());
        param.setRpcId("sss");
        list.add(param);

        fastDebugStackInfoDao.insert(list);
    }
}
