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

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.Application;
import io.shulie.tro.web.data.param.user.UserQueryParam;
import io.shulie.tro.web.data.result.user.UserCommonResult;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: fanxx
 * @Date: 2020/11/3 10:42 上午
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class TroUserDAOImplTest extends TestCase {

    @Autowired
    private TroUserDAO troUserDAO;

    @Test
    public void selectUser() {
        UserQueryParam param = new UserQueryParam();
        param.setName("taco");
//        param.setRoleIds(Arrays.asList("8"));
        param.setDeptIds(Arrays.asList("8"));
        PagingList<UserCommonResult> pagingList = troUserDAO.selectUserByCondition(param);
        System.out.println(pagingList.getList());
        System.out.println(pagingList.getTotal());
    }
}
