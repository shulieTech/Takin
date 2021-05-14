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

package io.shulie.tro.web.amdb.api.impl;

import java.util.Arrays;

import io.shulie.tro.web.amdb.api.TraceClient;
import io.shulie.tro.web.amdb.bean.query.trace.TraceInfoQueryDTO;
import io.shulie.tro.web.app.Application;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: fanxx
 * @Date: 2020/10/28 7:31 下午
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class TraceClientImplTest extends TestCase {

    @Autowired
    private TraceClient traceClient;

    @Test
    public void testPageApplications() {
        TraceInfoQueryDTO query = new TraceInfoQueryDTO();
        query.setEntranceList(Arrays.asList("/one/testmq#GET"));
        query.setStartTime(1610000279656L);
        traceClient.listEntryTraceInfo(query);
    }

    @Test
    public void testPageApplications2() {
        System.out.println(traceClient.getTraceDetailById("e364a8c016099155326311017d541b").toString());
    }
}
