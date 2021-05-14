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

import java.util.Arrays;
import java.util.List;

import com.github.pagehelper.PageInfo;
import io.shulie.tro.cloud.app.Application;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneManageQueryInput;
import io.shulie.tro.cloud.biz.output.scenemanage.SceneManageListOutput;
import io.shulie.tro.cloud.biz.service.scene.SceneManageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: mubai
 * @Date: 2020-12-03 20:05
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SceneManageServiceTest {

    @Autowired
    private SceneManageService sceneManageService;

    @Test
    public void testQuery(){
        SceneManageQueryInput queryInput = new SceneManageQueryInput();
       // List<Long> list = Arrays.asList(113l, 114l, 115l, 116l, 117l, 118l);
       // queryInput.setSceneIds(list);


        queryInput.setLastPtStartTime("2020-12-03 09:11:00");
        queryInput.setLastPtEndTime("2020-12-03 20:08:00");
        PageInfo<SceneManageListOutput> sceneManageListOutputPageInfo =
            sceneManageService.queryPageList(queryInput);
        System.out.println(sceneManageListOutputPageInfo);
    }
}
