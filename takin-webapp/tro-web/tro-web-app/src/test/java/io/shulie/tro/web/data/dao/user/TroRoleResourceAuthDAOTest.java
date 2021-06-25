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

package io.shulie.tro.web.data.dao.user;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import io.shulie.tro.web.app.Application;
import io.shulie.tro.web.data.param.user.RoleResourceAuthQueryParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthUpdateParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: fanxx
 * @Date: 2020/11/4 9:34 上午
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class TroRoleResourceAuthDAOTest {

    @Autowired
    private TroRoleResourceAuthDAO dao;

    @Test
    public void selectList() {
        RoleResourceAuthQueryParam queryParam = new RoleResourceAuthQueryParam();
        queryParam.setRoleIdList(Arrays.asList(7L));
        dao.selectList(queryParam);
    }

    @Test
    public void insertOrUpdate() {
        List<RoleResourceAuthUpdateParam> updateParamList = Lists.newArrayList();
        RoleResourceAuthUpdateParam updateParam = new RoleResourceAuthUpdateParam();
//        updateParam.setRoleId(7L);
//        updateParam.setResoureId(6L);
//        updateParam.setActionList(Arrays.asList(2,3,4,6));
//        updateParamList.add(updateParam);

        RoleResourceAuthUpdateParam insertParam = new RoleResourceAuthUpdateParam();
        insertParam.setRoleId(7L);
        insertParam.setResoureId(16L);
        insertParam.setActionList(Lists.newArrayList());
        updateParamList.add(insertParam);
        dao.updateRoleAuth(updateParamList);
    }
}
