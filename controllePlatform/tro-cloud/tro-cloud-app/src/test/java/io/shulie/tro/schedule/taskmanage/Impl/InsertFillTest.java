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

import io.shulie.tro.cloud.app.Application;
import io.shulie.tro.cloud.data.mapper.mysql.ReportMapper;
import io.shulie.tro.cloud.data.mapper.mysql.SceneManageMapper;
import io.shulie.tro.cloud.data.model.mysql.ReportEntity;
import io.shulie.tro.cloud.data.model.mysql.SceneManageEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName InsertFillTest
 * @Description
 * @Author qianshui
 * @Date 2020/10/27 下午5:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class InsertFillTest {

    @Autowired
    SceneManageMapper sceneManageMapper;

    @Autowired
    ReportMapper reportMapper;

    @Test
    public void insertSceneManage() {
        SceneManageEntity entity = new SceneManageEntity();
        entity.setSceneName("Test_scene_name");
        entity.setStatus(0);
        entity.setIsDeleted(1);
        sceneManageMapper.insert(entity);
    }

    @Test
    public void insertReport() {
        ReportEntity entity = new ReportEntity();
        entity.setSceneId(1L);
        entity.setSceneName("Test_scene_name");
        entity.setIsDeleted(1);
        reportMapper.insert(entity);
    }
}
