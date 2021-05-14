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

package io.shulie.tro.schedule.taskmanage.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.vo.schedule.ScheduleRunRequest;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleStartRequest;
import io.shulie.tro.cloud.app.Application;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import io.shulie.tro.cloud.biz.service.schedule.ScheduleService;

/**
 * @Author 莫问
 * @Date 2020-05-12
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class ScheduleTest {

    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void testScchedule() {
        ScheduleRunRequest runRequest = new ScheduleRunRequest();
        Long scheduleId = Long.parseLong(RandomUtils.nextInt() + "");
        runRequest.setScheduleId(scheduleId);
        ScheduleStartRequest scheduleStartRequest = new ScheduleStartRequest();
        scheduleStartRequest.setContinuedTime(100L);
        scheduleStartRequest.setPressureMode("fixed");
        scheduleStartRequest.setTaskId(scheduleId + 1);
        //        scheduleStartRequest.setScriptPath("/etc/engine/script/66/test.jmx");
        scheduleStartRequest.setScriptPath("/etc/engine/script/99/file-split.jmx");
        scheduleStartRequest.setSceneId(scheduleId + 2);
        scheduleStartRequest.setTotalIp(2);
        scheduleStartRequest.setExpectThroughput(50);
        scheduleStartRequest.setRampUp(2L);
        scheduleStartRequest.setSteps(3);

        List<ScheduleStartRequest.DataFile> list = new ArrayList<>();

        ScheduleStartRequest.DataFile dataFile = new ScheduleStartRequest.DataFile();
        dataFile.setName("abc.csv");
        dataFile.setPath("/etc/engine/script/99/abc.csv");
        dataFile.setSplit(true);

        ScheduleStartRequest.DataFile dataFile1 = new ScheduleStartRequest.DataFile();
        dataFile1.setName("aaaa.csv");
        dataFile1.setPath("/etc/engine/script/99/aaaa.csv");
        dataFile1.setSplit(true);
        //
        ScheduleStartRequest.DataFile dataFile2 = new ScheduleStartRequest.DataFile();
        dataFile2.setName("bbbb.csv");
        dataFile2.setPath("/etc/engine/script/99/bbbb.csv");
        dataFile2.setSplit(false);
        list.add(dataFile);
        list.add(dataFile1);
        list.add(dataFile2);

        scheduleStartRequest.setDataFile(list);
        Map<String, String> map = new HashMap<>();
        map.put("abc", "123456");
        scheduleStartRequest.setBusinessData(map);
        runRequest.setRequest(scheduleStartRequest);

        scheduleService.startSchedule(scheduleStartRequest);

    }

}
