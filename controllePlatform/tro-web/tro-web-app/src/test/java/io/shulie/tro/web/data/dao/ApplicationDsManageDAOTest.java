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

package io.shulie.tro.web.data.dao;

import io.shulie.tro.web.app.Application;
import io.shulie.tro.web.data.dao.application.ApplicationDsManageDAO;
import io.shulie.tro.web.data.model.mysql.ApplicationDsManageEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author loseself
 * @date 2021/3/27 7:27 下午
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class ApplicationDsManageDAOTest {

    @Autowired
    private ApplicationDsManageDAO applicationDsManageDao;

    @Test
    public void testListByApplicationId() {
        List<ApplicationDsManageEntity> applicationDsManageEntities = applicationDsManageDao.listByApplicationId(1002L);
        System.out.println(applicationDsManageEntities);
    }
}
