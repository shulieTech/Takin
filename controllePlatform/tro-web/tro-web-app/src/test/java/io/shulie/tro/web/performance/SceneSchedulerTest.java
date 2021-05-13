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

package io.shulie.tro.web.performance;

import java.util.Optional;

import com.pamirs.tro.entity.domain.vo.report.SceneActionParam;
import io.shulie.tro.web.app.Application;
import io.shulie.tro.web.app.request.scenemanage.SceneSchedulerTaskCreateRequest;
import io.shulie.tro.web.app.service.scenemanage.SceneSchedulerTaskService;
import io.shulie.tro.web.app.service.scenemanage.SceneTaskService;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.common.http.HttpWebClient;
import io.shulie.tro.web.config.sync.zk.impl.client.ZkClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: mubai
 * @Date: 2020-12-03 14:31
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SceneSchedulerTest {

    @Autowired
    private SceneSchedulerTaskService sceneSchedulerTaskService;

    @Autowired
    private SceneTaskService  sceneTaskService;

    @Autowired
    private ZkClient zkClient;


    @Autowired
    private HttpWebClient httpWebClient;

    @Test
    public void testAdd(){

        SceneSchedulerTaskCreateRequest request = new SceneSchedulerTaskCreateRequest();
        request.setSceneId(100L);
        request.setUserId(1L);
        //request.setExecuteTime("2020-12-11 11:11");

        sceneSchedulerTaskService.insert(request) ;
    }



    @Test
    public void testDel(){

        sceneSchedulerTaskService.deleteBySceneId(25L);
    }

    @Test
    public void testZk(){
        String path = "/tro/config" ;
        zkClient.addNode(path,"cjc");
    }

    @Test
    public void testStartScene(){

        SceneActionParam param = new SceneActionParam();
        param.setUid(1L);
        param.setSceneId(127L);
        param.setUid(1l);
        param.setRequestUrl(RemoteConstant.SCENE_TASK_START_URL);
        param.setHttpMethod(HttpMethod.POST);
        httpWebClient.request(param);

    }

}
