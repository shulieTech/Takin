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

package io.shulie.tro.web.app.service.impl;

import java.util.Map;

import com.google.common.collect.Maps;
import com.pamirs.tro.entity.domain.dto.NodeUploadDataDTO;
import com.pamirs.tro.entity.domain.entity.ExceptionInfo;
import io.shulie.tro.web.app.Application;
import io.shulie.tro.web.app.service.ApplicationService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: fanxx
 * @Date: 2020/10/27 11:02 下午
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class ApplicationServiceImplTest extends TestCase {

    @Autowired
    private ApplicationService applicationService;

    @Test
    public void testUploadAccessStatus() {
        NodeUploadDataDTO param = new NodeUploadDataDTO();
        param.setAgentId("123");
        param.setApplicationName("test-demo-1028");
        param.setNodeKey("456");
        Map<String, Object> errorMap = Maps.newHashMap();
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setErrorCode("001");
        exceptionInfo.setMessage("数据库异常");
        exceptionInfo.setDetail("url:eroor");
        errorMap.put("agent异常", exceptionInfo);
        param.setSwitchErrorMap(errorMap);
        applicationService.uploadAccessStatus(param);
    }

    @Test
    public void testGetApplicationInfo() {
        applicationService.getApplicationInfo("6727065154824966144");
    }
}
