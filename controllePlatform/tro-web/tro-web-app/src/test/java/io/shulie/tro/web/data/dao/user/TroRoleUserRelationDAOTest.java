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

import io.shulie.tro.web.app.Application;
import io.shulie.tro.web.data.param.user.UserRoleQueryParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchDeleteParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchQueryParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationQueryParam;
import io.shulie.tro.web.data.result.user.UserRoleRelationResult;
import io.shulie.tro.web.data.result.user.UserRoleResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: fanxx
 * @Date: 2020/11/3 9:33 下午
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class TroRoleUserRelationDAOTest {

    @Autowired
    private TroRoleUserRelationDAO dao;

    @Test
    public void selectList() {
        UserRoleQueryParam param = new UserRoleQueryParam();
        param.setUserIdList(Arrays.asList("9755"));
        List<UserRoleResult> userRoleResultList = dao.selectList(param);
        System.out.println(userRoleResultList);
    }

    @Test
    public void insertRoleToUser() {

//        UserRoleRelationCreateParam param = new UserRoleRelationCreateParam();
//        param.setUserId("9755");
//        param.setRoleId("9");
//        int count = dao.insertRoleToUser(param);
//        System.out.println(count);
    }

    @Test
    public void selectUserRoleRelation() {
        UserRoleRelationQueryParam param = new UserRoleRelationQueryParam();
        param.setUserId("9755");
        param.setRoleId("9");
        UserRoleRelationResult relationResult = dao.selectUserRoleRelation(param);
        System.out.println(relationResult);
    }

    @Test
    public void selectUserRoleRelationBatch() {
        UserRoleRelationBatchQueryParam param = new UserRoleRelationBatchQueryParam();
        param.setUserIdList(Arrays.asList("9755"));
        List<UserRoleRelationResult> relationResultList = dao.selectUserRoleRelationBatch(param);
        System.out.println(relationResultList);
    }

    @Test
    public void deleteUserRoleRelationBatch() {
        UserRoleRelationBatchDeleteParam param = new UserRoleRelationBatchDeleteParam();
        param.setUserId(Arrays.asList("9755"));
        int count = dao.deleteUserRoleRelationBatch(param);
        System.out.println(count);
    }
}
