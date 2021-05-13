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

package io.shulie.tro.web.app.cache.webimpl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.google.common.collect.Lists;
import com.pamirs.pradar.ext.commons.lang3.StringUtils;
import io.shulie.tro.web.data.dao.user.TroUserDAO;
import io.shulie.tro.web.data.result.user.UserCacheResult;
import io.shulie.tro.web.data.result.user.UserDetailResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2021-01-29
 */
@Component
@Slf4j
public class AllUserCache {

    public static final String CACHE_NAME = "t:a:c:users:all";
    @Autowired
    private TroUserDAO troUserDAO;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    private void init() {
        redisTemplate.delete(CACHE_NAME);
        log.info("清除key:{}对应的缓存成功", CACHE_NAME);
        setUserCacheFromDB();
    }

    private synchronized void setUserCacheFromDB() {
        Object cacheData = redisTemplate.opsForValue().get(CACHE_NAME);
        if (null != cacheData) {
            return;
        }
        List<UserDetailResult> userDetailResults = troUserDAO.selectAllUser();
        if (CollectionUtils.isEmpty(userDetailResults)) {
            redisTemplate.opsForValue().set(CACHE_NAME, Lists.newArrayList());
            redisTemplate.expire(CACHE_NAME, 3, TimeUnit.MINUTES);
            return;
        }
        Map<Long, UserDetailResult> userMap = userDetailResults.stream().collect(
            Collectors.toMap(UserDetailResult::getId, e -> e));

        List<UserCacheResult> userCaches = userDetailResults.stream().map(user -> {
            UserCacheResult userCacheResult = new UserCacheResult();
            userCacheResult.setId(user.getId());
            userCacheResult.setName(user.getName());
            userCacheResult.setKey(user.getKey());
            userCacheResult.setUserType(user.getUserType());
            userCacheResult.setCustomerId(user.getCustomerId());
            UserDetailResult customerUser = userMap.get(user.getCustomerId());
            if (customerUser != null) {
                userCacheResult.setCustomerKey(customerUser.getKey());
            }
            return userCacheResult;
        }).collect(Collectors.toList());
        redisTemplate.opsForValue().set(CACHE_NAME, userCaches);
        redisTemplate.expire(CACHE_NAME, 3, TimeUnit.MINUTES);
    }

    public UserCacheResult getCachedUserByKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException(" getCachedUserByKey , key must not null ");
        }
        List<UserCacheResult> userDetailResults = (List<UserCacheResult>)redisTemplate.opsForValue().get(CACHE_NAME);
        if (userDetailResults == null) {
            setUserCacheFromDB();
            userDetailResults = (List<UserCacheResult>)redisTemplate.opsForValue().get(CACHE_NAME);
        }
        if (CollectionUtils.isEmpty(userDetailResults)) {
            return null;
        }
        // 多个key相同，取id最小的
        userDetailResults.sort(Comparator.comparing(UserCacheResult::getId));
        for (UserCacheResult userDetailResult : userDetailResults) {
            if (key.equals(userDetailResult.getKey())) {
                return userDetailResult;
            }
        }
        return null;
    }

    public UserCacheResult getCachedUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(" getCachedUserById , id must not null ");
        }
        List<UserCacheResult> userDetailResults = (List<UserCacheResult>)redisTemplate.opsForValue().get(CACHE_NAME);
        if (userDetailResults == null) {
            setUserCacheFromDB();
            userDetailResults = (List<UserCacheResult>)redisTemplate.opsForValue().get(CACHE_NAME);
        }
        if (CollectionUtils.isEmpty(userDetailResults)) {
            return null;
        }
        return userDetailResults.stream().filter(user -> id.equals(user.getId())).findFirst().orElse(null);
    }

    public UserCacheResult getCachedUserByName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException(" getCachedUserByName , name must not empty ");
        }
        List<UserCacheResult> userDetailResults = (List<UserCacheResult>)redisTemplate.opsForValue().get(CACHE_NAME);
        if (userDetailResults == null) {
            setUserCacheFromDB();
            userDetailResults = (List<UserCacheResult>)redisTemplate.opsForValue().get(CACHE_NAME);
        }
        if (CollectionUtils.isEmpty(userDetailResults)) {
            return null;
        }
        return userDetailResults.stream().filter(user -> name.equals(user.getName())).findFirst().orElse(null);
    }
}
