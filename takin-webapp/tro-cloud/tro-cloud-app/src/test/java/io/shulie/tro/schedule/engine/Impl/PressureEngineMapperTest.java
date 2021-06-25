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

package io.shulie.tro.schedule.engine.Impl;

import com.google.common.collect.Maps;
import io.shulie.tro.cloud.app.Application;
import io.shulie.tro.cloud.biz.input.engine.EnginePluginWrapperInput;
import io.shulie.tro.cloud.biz.output.engine.EnginePluginSimpleInfoOutput;
import io.shulie.tro.cloud.biz.service.engine.EnginePluginFilesService;
import io.shulie.tro.cloud.biz.service.engine.EnginePluginService;
import io.shulie.tro.cloud.data.mapper.mysql.EnginePluginMapper;
import io.shulie.tro.cloud.open.api.engine.CloudEngineApi;
import io.shulie.tro.cloud.open.req.engine.EnginePluginFetchWrapperReq;
import io.shulie.tro.cloud.open.resp.engine.EnginePluginSimpleInfoResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.json.JsonHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 引擎测试
 *
 * @author lipeng
 * @date 2021-01-06 4:08 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class PressureEngineMapperTest {

    @Resource
    private EnginePluginMapper pressureEngineMapper;

    @Resource
    private EnginePluginService enginePluginService;

    @Resource
    private EnginePluginFilesService enginePluginFilesService;

    @Resource
    private CloudEngineApi cloudEngineApi;

    @Test
    public void testFindPluginFilesPathByPluginIds() {
        List<Long> ids = Lists.newArrayList();
        ids.add(6L);
        ids.add(7L);
        List<String> result = enginePluginFilesService.findPluginFilesPathByPluginIds(ids);
        System.out.println(result);
    }

    @Test
    public void testQueryAvailablePluginsByType() {
        List<String> queryParams = Lists.newArrayList();
        queryParams.add("dubbo");
        queryParams.add("kafka");

        Map<String, List<EnginePluginSimpleInfoOutput>> res = enginePluginService.findEngineAvailablePluginsByType(queryParams);
        System.out.println(JsonHelper.bean2Json(res));
    }

    @Test
    public void testQueryEnginePluginSupportedVersions() {
        List<Map> listSupportedVersions = pressureEngineMapper.selectEnginePluginSupportedVersions(1L);
        System.out.println(listSupportedVersions);
    }

    @Test
    public void testSaveEnginePlugin() {
        EnginePluginWrapperInput input = new EnginePluginWrapperInput();
        input.setPluginName("redis-all");
        input.setPluginType("redis");
        input.setPluginUploadPath("/test/aa/f");
        List<String> versions = Lists.newArrayList();
        versions.add("1.0");
        versions.add("2.0");
        input.setSupportedVersions(versions);
        enginePluginService.saveEnginePlugin(input);
    }

    @Test
    public void testSdk() {
        EnginePluginFetchWrapperReq request = new EnginePluginFetchWrapperReq();
        List<String> req = Lists.newArrayList();
        req.add("dubbo");
        request.setPluginTypes(req);
        ResponseResult<Map<String, List<EnginePluginSimpleInfoResp>>> res = cloudEngineApi.listEnginePlugins(request);
        if(res != null) {
            List<EnginePluginSimpleInfoResp> resp = res.getData().get("dubbo");
            resp.forEach(item -> {
                System.out.println(item.getPluginName());
            });
        }
    }

}
