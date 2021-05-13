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

package io.shulie.tro.web.app.service.dsmanage.impl;

import io.shulie.tro.web.app.request.application.ApplicationDsCreateRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsUpdateRequest;
import io.shulie.tro.web.app.service.dsManage.impl.ShadowKafkaServiceImpl;
import io.shulie.tro.web.data.param.application.ApplicationDsCreateParam;
import io.shulie.tro.web.data.param.application.ApplicationDsUpdateParam;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.service.dsmanage.impl
 * @date 2021/4/22 2:37 下午
 */
public class ServiceImplTest {
    @Test
    public void KafkaJsonTest() {
        ShadowKafkaServiceImpl shadowKafkaService = new ShadowKafkaServiceImpl();
        ApplicationDsCreateRequest createRequest = new ApplicationDsCreateRequest();
        createRequest.setConfig("[\n"
            + "            {\n"
            + "                \"key\":\"PT_业务主题\",\n"
            + "                \"topic\":\"PT_业务主题\",\n"
            + "                \"topicTokens\":\"PT_业务主题:影子主题token\",\n"
            + "                \"group\":\"\",\n"
            + "                \"systemIdToken\":\"\"\n"
            + "            }\n"
            + "        ]");
        ApplicationDsCreateParam createParam = new ApplicationDsCreateParam();
        shadowKafkaService.addParserConfig(createRequest,createParam);
        Assert.assertEquals("PT_业务主题",createParam.getUrl());
    }

    @Test
    public void KafkaJsonUpdateTest() {
        ShadowKafkaServiceImpl shadowKafkaService = new ShadowKafkaServiceImpl();
        ApplicationDsUpdateRequest createRequest = new ApplicationDsUpdateRequest();
        createRequest.setConfig("{\"key\": \"PT_业务主题\",\"topic\": \"PT_业务主题\",\"topicTokens\": \"PT_业务主题:影子主题token\",\"group\": \"\","
            + "\"systemIdToken\": \"\"}");
        ApplicationDsUpdateParam createParam = new ApplicationDsUpdateParam();
        shadowKafkaService.updateParserConfig(createRequest,createParam);
        Assert.assertEquals("PT_业务主题",createParam.getUrl());
    }
}
