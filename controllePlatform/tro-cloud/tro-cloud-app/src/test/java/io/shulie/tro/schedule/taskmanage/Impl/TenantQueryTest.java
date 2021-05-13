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

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import io.shulie.tro.cloud.app.Application;
import io.shulie.tro.cloud.data.mapper.mysql.ReportMapper;
import io.shulie.tro.cloud.data.mapper.mysql.SceneManageMapper;
import io.shulie.tro.cloud.data.model.mysql.ReportEntity;
import io.shulie.tro.cloud.data.model.mysql.SceneManageEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName TenantQueryTest
 * @Description
 * @Author qianshui
 * @Date 2020/10/27 下午8:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class TenantQueryTest {
    @Autowired
    SceneManageMapper sceneManageMapper;

    @Autowired
    ReportMapper reportMapper;

    @Test
    public void querySceneManage (){
        Map<String, Object> columnMap = Maps.newHashMap();
        columnMap.put("scene_name", "Test_scene_name");
        List<SceneManageEntity> entities = sceneManageMapper.selectByMap(columnMap);
        Assert.assertTrue(entities != null && entities.size() == 1);
    }

    @Test
    public void queryReport (){
        Map<String, Object> columnMap = Maps.newHashMap();
        columnMap.put("scene_name", "Test_scene_name");
        List<ReportEntity> entities = reportMapper.selectByMap(columnMap);
        Assert.assertTrue(entities != null && entities.size() == 1);
    }
}
