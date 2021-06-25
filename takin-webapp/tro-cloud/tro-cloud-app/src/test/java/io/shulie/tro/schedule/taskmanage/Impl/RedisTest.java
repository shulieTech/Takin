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
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName RedisTest
 * @Description
 * @Author qianshui
 * @Date 2020/11/20 下午12:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class RedisTest {

    @Autowired
    private RedisClientUtils redisClientUtils;

    @Test
    public void testRedis() {
        System.out.println(redisClientUtils.zsetAdd("set1", "1"));
        System.out.println(redisClientUtils.zsetAdd("set1", "2"));
        System.out.println(redisClientUtils.zsetAdd("set1", "1"));
        System.out.println(redisClientUtils.zsetAdd("set1", "3"));
    }
}
